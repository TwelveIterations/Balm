package net.blay09.mods.balm.client.platform.config.screen.list;

import net.blay09.mods.balm.client.platform.config.screen.list.internal.BalmConfigListDragController;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.Collection;

public interface BalmConfigListEditorContext<T> {
    Font font();

    ConfiguredProperty<? extends Collection<T>> property();

    BalmConfigListDragController dragController();

    boolean canReorderValues();

    void focusEntry(BalmConfigListEditorEntry<T> entry);

    void setValidationError(BalmConfigListEditorEntry<T> entry, Component error);

    void revalidate();

    void delete(BalmConfigListEditorEntry<T> entry);

    boolean commit();
}
