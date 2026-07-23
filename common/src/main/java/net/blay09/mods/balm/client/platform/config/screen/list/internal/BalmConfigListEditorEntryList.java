package net.blay09.mods.balm.client.platform.config.screen.list.internal;

import net.blay09.mods.balm.client.platform.config.screen.internal.BalmConfigScreenList;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorEntry;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BalmConfigListEditorEntryList<T> extends ContainerObjectSelectionList<BalmConfigListEditorEntry<T>> implements BalmConfigListDragController {
    private final BalmConfigListEditorScreen<T> screen;
    private @Nullable BalmConfigListEditorEntry<T> draggedEntry;
    private double dragMouseY;
    private double dragGrabOffsetY;
    private int draggedEntryStartIndex = -1;
    private boolean extractingDraggedEntry;

    public BalmConfigListEditorEntryList(BalmConfigListEditorScreen<T> screen) {
        super(Minecraft.getInstance(),
                screen.width,
                screen.contentHeight(),
                screen.headerHeight(),
                BalmConfigScreenList.ROW_HEIGHT);
        this.screen = screen;
    }

    @Override
    public int addEntry(BalmConfigListEditorEntry<T> entry) {
        return super.addEntry(entry);
    }

    @Override
    public int getRowWidth() {
        return Math.min(BalmConfigScreenList.MAX_ROW_WIDTH, screen.width - BalmConfigScreenList.MIN_SCREEN_PADDING_X * 2);
    }

    @Override
    public void startDragging(Object entry, double mouseY) {
        if (!screen.canReorderValues()) {
            return;
        }

        final var entries = children();
        if (!(entry instanceof BalmConfigListEditorEntry<?> configEntry) || !entries.contains(configEntry)) {
            return;
        }

        @SuppressWarnings("unchecked") final var typedEntry = (BalmConfigListEditorEntry<T>) configEntry;
        draggedEntry = typedEntry;
        dragMouseY = mouseY;
        dragGrabOffsetY = mouseY - typedEntry.getY();
        draggedEntryStartIndex = entries.indexOf(typedEntry);
    }

    @Override
    public void dragTo(double mouseY) {
        if (draggedEntry == null || !screen.canReorderValues()) {
            return;
        }

        dragMouseY = mouseY;

        final int edgeScrollArea = 12;
        if (mouseY < getY() + edgeScrollArea) {
            setScrollAmount(scrollAmount() - 4);
        } else if (mouseY > getBottom() - edgeScrollArea) {
            setScrollAmount(scrollAmount() + 4);
        }

        final var targetEntry = getEntryAtPosition(getRowLeft(), mouseY);
        if (targetEntry == null || targetEntry == draggedEntry) {
            return;
        }

        final var entries = new ArrayList<>(children());
        final int draggedIndex = entries.indexOf(draggedEntry);
        final int targetIndex = entries.indexOf(targetEntry);
        if (draggedIndex == -1 || targetIndex == -1) {
            return;
        }

        entries.remove(draggedIndex);
        entries.add(targetIndex, draggedEntry);
        replaceEntries(entries);
    }

    @Override
    public void stopDragging() {
        if (draggedEntry != null && draggedEntryStartIndex != -1) {
            final var entries = new ArrayList<>(children());
            final int targetIndex = entries.indexOf(draggedEntry);
            if (targetIndex != -1 && targetIndex != draggedEntryStartIndex && !screen.moveValue(draggedEntry, targetIndex)) {
                entries.remove(targetIndex);
                entries.add(draggedEntryStartIndex, draggedEntry);
                replaceEntries(entries);
            }
        }
        draggedEntry = null;
        draggedEntryStartIndex = -1;
    }

    @Override
    public void moveUp(Object entry) {
        moveBy(entry, -1);
    }

    @Override
    public void moveDown(Object entry) {
        moveBy(entry, 1);
    }

    @Override
    public void moveToTop(Object entry) {
        final var entries = new ArrayList<>(children());
        if (!(entry instanceof BalmConfigListEditorEntry<?> configEntry) || !entries.contains(configEntry)) {
            return;
        }

        @SuppressWarnings("unchecked") final var typedEntry = (BalmConfigListEditorEntry<T>) configEntry;
        final int currentIndex = entries.indexOf(typedEntry);
        if (currentIndex <= 0) {
            screen.moveValueToTop(typedEntry);
            return;
        }

        if (!screen.moveValueToTop(typedEntry)) {
            return;
        }
        entries.remove(currentIndex);
        entries.addFirst(typedEntry);
        replaceEntries(entries);
    }

    @Override
    public void moveToBottom(Object entry) {
        final var entries = new ArrayList<>(children());
        if (!(entry instanceof BalmConfigListEditorEntry<?> configEntry) || !entries.contains(configEntry)) {
            return;
        }

        @SuppressWarnings("unchecked") final var typedEntry = (BalmConfigListEditorEntry<T>) configEntry;
        final int currentIndex = entries.indexOf(typedEntry);
        final int targetIndex = entries.size() - 1;
        if (currentIndex == -1 || currentIndex == targetIndex) {
            screen.moveValueToBottom(typedEntry);
            return;
        }

        if (!screen.moveValueToBottom(typedEntry)) {
            return;
        }
        entries.remove(currentIndex);
        entries.add(targetIndex, typedEntry);
        replaceEntries(entries);
    }

    @Override
    public boolean isDragging(Object entry) {
        return draggedEntry == entry;
    }

    private void moveBy(Object entry, int offset) {
        if (!screen.canReorderValues()) {
            return;
        }

        final var entries = new ArrayList<>(children());
        if (!(entry instanceof BalmConfigListEditorEntry<?> configEntry) || !entries.contains(configEntry)) {
            return;
        }

        @SuppressWarnings("unchecked") final var typedEntry = (BalmConfigListEditorEntry<T>) configEntry;
        final int currentIndex = entries.indexOf(typedEntry);
        final int targetIndex = currentIndex + offset;
        if (currentIndex == -1 || targetIndex < 0 || targetIndex >= entries.size()) {
            return;
        }

        if (!screen.moveValue(typedEntry, targetIndex)) {
            return;
        }
        entries.remove(currentIndex);
        entries.add(targetIndex, typedEntry);
        replaceEntries(entries);
    }

    public boolean isExtractingDraggedEntry() {
        return extractingDraggedEntry;
    }

    public double draggedEntryYOffset(BalmConfigListEditorEntry<T> entry) {
        return dragMouseY - dragGrabOffsetY - entry.getY();
    }

    @Override
    protected void extractListItems(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        extractingDraggedEntry = false;
        super.extractListItems(graphics, mouseX, mouseY, partialTick);

        if (draggedEntry != null) {
            extractingDraggedEntry = true;
            extractItem(graphics, mouseX, mouseY, partialTick, draggedEntry);
            extractingDraggedEntry = false;
        }
    }

}
