package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenLabeledEntry;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class BalmConfigScreenButtonEntry extends BalmConfigScreenLabeledEntry {
    public BalmConfigScreenButtonEntry(BalmConfigScreen screen, BalmConfigScreenButtonRow row) {
        super(screen, row.label(), row.tooltip(), Button.builder(row.buttonLabel(), _ -> row.onPress(screen)).build());
    }

    @Override
    protected @Nullable Component getValidationError() {
        return null;
    }
}
