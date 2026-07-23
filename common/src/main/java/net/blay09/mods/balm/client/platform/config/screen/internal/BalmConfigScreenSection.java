package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.minecraft.network.chat.Component;

import java.util.List;

public record BalmConfigScreenSection(Component title, List<BalmConfigScreenRow> rows) {
}
