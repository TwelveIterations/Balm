package net.blay09.mods.balm.client.platform.config.screen.list.internal;

public interface BalmConfigListDragController {
    void startDragging(Object entry, double mouseY);

    void dragTo(double mouseY);

    void stopDragging();

    void moveUp(Object entry);

    void moveDown(Object entry);

    void moveToTop(Object entry);

    void moveToBottom(Object entry);

    boolean isDragging(Object entry);
}
