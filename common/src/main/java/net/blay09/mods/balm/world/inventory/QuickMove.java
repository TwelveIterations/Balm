package net.blay09.mods.balm.world.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Utility to declaratively implement shift-click (quick move) item transfers in container menus.
 * <p>
 * Typical usage from a custom {@link AbstractContainerMenu}:
 * <pre>{@code
 * private final QuickMove.Routing quickMove = QuickMove.create(this, this::moveItemStackTo)
 *     // define slot ranges and routes here
 *     .build();
 *
 * @Override
 * public ItemStack quickMoveStack(Player player, int index) {
 *     return quickMove.transfer(this, player, index);
 * }
 * }</pre>
 */
public final class QuickMove {

    /**
     * Name used for the container's own slots.
     */
    public static final String CONTAINER = "container";

    /**
     * Name used for the combined player slots (inventory + hotbar). When used as a target, it will
     * first try the hotbar and then the inventory.
     */
    public static final String PLAYER = "player";

    private static final String PLAYER_INVENTORY = "inventory";
    private static final String PLAYER_HOTBAR = "hotbar";

    /**
     * Functional interface matching the signature of {@code AbstractContainerMenu#moveItemStackTo(ItemStack, int, int, boolean)}.
     * To be passed as a method reference from your container menu since <code>moveItemStackTo</code> is protected.
     */
    @FunctionalInterface
    public interface MoveItemStackTo {
        /**
         * Matches the signature of {@code AbstractContainerMenu#moveItemStackTo(ItemStack, int, int, boolean)}.
         *
         * @param itemStack the stack to move/merge (will be mutated)
         * @param start     start slot index (inclusive)
         * @param end       end slot index (exclusive)
         * @param reverse   whether to iterate the target range in reverse
         * @return {@code true} if any items were moved, otherwise {@code false}
         */
        boolean moveItemStackTo(ItemStack itemStack, int start, int end, boolean reverse);
    }

    /**
     * Named half-open slot range [start, end).
     */
    protected record NamedRange(String name, int start, int end) {
        /**
         * @return {@code true} if {@code index} lies within this range.
         */
        boolean contains(int index) {
            return index >= start && index < end;
        }
    }

    /**
     * Route definition that maps items from a source range to a target range if the {@link #predicate}
     * returns {@code true} for the stack being moved.
     */
    protected record Route(Predicate<ItemStack> predicate, String sourceName, String targetName, boolean reverse) {
    }

    private QuickMove() {
    }

    /**
     * Creates a new builder with no predefined ranges.
     * Use {@link #create(AbstractContainerMenu, MoveItemStackTo)} for a ready-made player/container layout.
     *
     * @param moveItemStackTo delegate that performs the actual move/merge
     */
    public static Builder create(MoveItemStackTo moveItemStackTo) {
        return new Builder(moveItemStackTo);
    }

    /**
     * Creates a new builder and registers the common ranges for the given menu:
     * <ul>
     *   <li>{@link #CONTAINER} covering all non-player slots</li>
     *   <li>{@link #PLAYER} covering all player slots (36)</li>
     *   <li>{@link #PLAYER_INVENTORY} covering the main inventory (27)</li>
     *   <li>{@link #PLAYER_HOTBAR} covering the hotbar (9)</li>
     * </ul>
     * The method assumes that the last 36 slots of the menu are the player's inventory and hotbar.
     *
     * @param menu            the container menu to derive slot counts from
     * @param moveItemStackTo delegate that performs the actual move/merge
     */
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

        /**
         * Registers a single-slot range with the given name.
         *
         * @param name a unique range identifier
         * @param slot the slot index
         * @return this builder
         */
        public Builder slot(String name, int slot) {
            ranges.add(new NamedRange(name, slot, slot + 1));
            return this;
        }

        /**
         * Registers a named range for slots in {@code [startInclusive, endExclusive)}.
         *
         * @param name           a unique range identifier
         * @param startInclusive start index (inclusive)
         * @param endExclusive   end index (exclusive)
         * @return this builder
         */
        public Builder slotRange(String name, int startInclusive, int endExclusive) {
            ranges.add(new NamedRange(name, startInclusive, endExclusive));
            return this;
        }

        /**
         * Adds an unconditional route from {@code sourceRangeName} to {@code targetRangeName}.
         *
         * @see #route(String, String, boolean)
         */
        public Builder route(String sourceRangeName, String targetRangeName) {
            return route(sourceRangeName, targetRangeName, false);
        }

        /**
         * Adds an unconditional route from {@code sourceRangeName} to {@code targetRangeName}.
         *
         * @param reverse whether to iterate the target range in reverse when merging
         * @see #route(Predicate, String, String, boolean)
         */
        public Builder route(String sourceRangeName, String targetRangeName, boolean reverse) {
            return route(it -> true, sourceRangeName, targetRangeName, reverse);
        }

        /**
         * Adds a conditional route that only applies if {@code predicate} returns {@code true}
         * for the item being moved.
         *
         * @see #route(Predicate, String, String, boolean)
         */
        public Builder route(Predicate<ItemStack> predicate, String sourceRangeName, String targetRangeName) {
            return route(predicate, sourceRangeName, targetRangeName, false);
        }

        /**
         * Adds a conditional route from {@code sourceRangeName} to {@code targetRangeName}.
         * If multiple routes match, they are evaluated in registration order; the first successful
         * move wins.
         *
         * @param predicate       condition that must match the current {@link ItemStack}
         * @param sourceRangeName named source range
         * @param targetRangeName named target range
         * @param reverse         whether to iterate the target range in reverse when merging
         * @return this builder
         */
        public Builder route(Predicate<ItemStack> predicate, String sourceRangeName, String targetRangeName, boolean reverse) {
            routes.add(new Route(predicate, sourceRangeName, targetRangeName, reverse));
            return this;
        }

        /**
         * Disables the default routes that are added during {@link #build()}:
         * <ul>
         *   <li>{@code container -> player}</li>
         *   <li>{@code player -> player} (move between hotbar and inventory)</li>
         * </ul>
         */
        public Builder disableDefaultRoutes() {
            includeDefaultRoutes = false;
            return this;
        }

        /**
         * Builds an immutable {@link Routing} instance. If default routes are enabled, this will also add fallback routes for moving items from the container to the player, and between the hotbar and inventory.
         */
        public Routing build() {
            if (includeDefaultRoutes) {
                route(CONTAINER, PLAYER);
                route(PLAYER, PLAYER);
            }
            return new Routing(List.copyOf(ranges), List.copyOf(routes), moveItemStackTo);
        }
    }

    public record Routing(List<NamedRange> ranges, List<Route> routes, MoveItemStackTo moveItemStackTo) {

        /**
         * Executes a quick-move operation for the given slot index.
         * <p>
         * The method determines the source named range for {@code index}, iterates all configured
         * routes in registration order, and attempts to move the stack using the first matching route.
         * When the target range is {@link QuickMove#PLAYER}, it will try hotbar and inventory in a sensible
         * order while avoiding moving within the same sub-range.
         *
         * @param menu   the container menu
         * @param player the player performing the action
         * @param index  the clicked slot index
         * @return the original stack copy if any items were moved; {@link ItemStack#EMPTY} otherwise
         */
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

