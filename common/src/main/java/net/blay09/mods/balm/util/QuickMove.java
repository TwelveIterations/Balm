package net.blay09.mods.balm.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class QuickMove {

    public static final String CONTAINER = "container";
    public static final String PLAYER = "player";
    private static final String PLAYER_INVENTORY = "inventory";
    private static final String PLAYER_HOTBAR = "hotbar";

    @FunctionalInterface
    public interface MoveItemStackTo {
        boolean moveItemStackTo(ItemStack itemStack, int start, int end, boolean reverse);
    }

    protected record NamedRange(String name, int start, int end) {
        boolean contains(int index) {
            return index >= start && index < end;
        }
    }

    protected record Route(Predicate<ItemStack> predicate, String sourceName, String targetName, boolean reverse) {
    }

    private QuickMove() {
    }

    public static Builder create(MoveItemStackTo moveItemStackTo) {
        return new Builder(moveItemStackTo);
    }

    public static Builder create(AbstractContainerMenu menu, MoveItemStackTo moveItemStackTo) {
        final var containerSlotCount = menu.slots.size() - 36;
        return new Builder(moveItemStackTo)
                .slotRange(CONTAINER, 0, containerSlotCount)
                .slotRange(PLAYER, containerSlotCount, containerSlotCount + 36)
                .slotRange(PLAYER_INVENTORY, containerSlotCount, containerSlotCount + 27)
                .slotRange(PLAYER_HOTBAR, containerSlotCount + 27, containerSlotCount + 36);
    }

    public static final class Builder {
        private final MoveItemStackTo moveItemStackTo;
        private final List<NamedRange> ranges = new ArrayList<>();
        private final List<Route> routes = new ArrayList<>();
        private boolean includeDefaultRoutes = true;

        private Builder(MoveItemStackTo moveItemStackTo) {
            this.moveItemStackTo = moveItemStackTo;
        }

        public Builder slot(String name, int slot) {
            ranges.add(new NamedRange(name, slot, slot + 1));
            return this;
        }

        public Builder slotRange(String name, int startInclusive, int endExclusive) {
            ranges.add(new NamedRange(name, startInclusive, endExclusive));
            return this;
        }

        public Builder route(String sourceRangeName, String targetRangeName) {
            return route(sourceRangeName, targetRangeName, false);
        }

        public Builder route(String sourceRangeName, String targetRangeName, boolean reverse) {
            return route(it -> true, sourceRangeName, targetRangeName, reverse);
        }

        public Builder route(Predicate<ItemStack> predicate, String sourceRangeName, String targetRangeName) {
            return route(predicate, sourceRangeName, targetRangeName, false);
        }

        public Builder route(Predicate<ItemStack> predicate, String sourceRangeName, String targetRangeName, boolean reverse) {
            routes.add(new Route(predicate, sourceRangeName, targetRangeName, reverse));
            return this;
        }

        public Builder disableDefaultRoutes() {
            includeDefaultRoutes = false;
            return this;
        }

        public Routing build() {
            if (includeDefaultRoutes) {
                route(CONTAINER, PLAYER);
                route(PLAYER, PLAYER);
            }
            return new Routing(List.copyOf(ranges), List.copyOf(routes), moveItemStackTo);
        }
    }

    public record Routing(List<NamedRange> ranges, List<Route> routes, MoveItemStackTo moveItemStackTo) {

        public ItemStack transfer(AbstractContainerMenu menu, Player player, int index) {
            var itemStack = ItemStack.EMPTY;
            final var slot = menu.slots.get(index);
            if (!slot.hasItem()) {
                return ItemStack.EMPTY;
            }

            final var slotStack = slot.getItem();
            itemStack = slotStack.copy();

            final var sourceRange = findRangeByIndex(index);
            var moved = false;
            if (sourceRange != null) {
                for (final var route : routes) {
                    if (route.sourceName.equals(sourceRange.name) && route.predicate.test(slotStack)) {
                        final var targetRange = findRangeByName(route.targetName);
                        if (targetRange != null) {
                            if (targetRange.name.equals(PLAYER)) {
                                final var hotbarRange = findRangeByName(PLAYER_HOTBAR);
                                final var inventoryRange = findRangeByName(PLAYER_INVENTORY);
                                if (hotbarRange != null && !hotbarRange.contains(index)) {
                                    if (moveItemStackTo.moveItemStackTo(slotStack, hotbarRange.start, hotbarRange.end, !route.reverse)) {
                                        moved = true;
                                        break;
                                    }
                                }
                                if (inventoryRange != null && !inventoryRange.contains(index)) {
                                    if (moveItemStackTo.moveItemStackTo(slotStack, inventoryRange.start, inventoryRange.end, route.reverse)) {
                                        moved = true;
                                        break;
                                    }
                                }
                            } else if (moveItemStackTo.moveItemStackTo(slotStack, targetRange.start, targetRange.end, route.reverse)) {
                                moved = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (!moved) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
            return itemStack;
        }

        private NamedRange findRangeByIndex(int index) {
            for (final var range : ranges) {
                if (range.contains(index)) {
                    return range;
                }
            }
            return null;
        }

        private NamedRange findRangeByName(String name) {
            for (final var range : ranges) {
                if (range.name.equals(name)) {
                    return range;
                }
            }
            return null;
        }
    }
}
