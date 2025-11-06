package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.BalmClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#keyMappings(String, Consumer)} instead.
 */
@Deprecated
public interface BalmKeyMappings {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#keyMappings(String, Consumer)} instead.
     */
    @Deprecated
    default KeyMapping registerKeyMapping(ResourceLocation id, int keyCode, KeyMapping.Category category) {
        return registerKeyMapping(id, InputConstants.Type.KEYSYM, keyCode, category);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#keyMappings(String, Consumer)} instead.
     */
    @Deprecated
    default KeyMapping registerKeyMapping(ResourceLocation id, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
        final var keyMapping = new KeyMapping(id.getPath(), type, keyCode, category);
        BalmClient.getRuntime().keyMappings(id.getNamespace(), registrar -> registrar.register(keyMapping));
        return keyMapping;
    }

    @Deprecated
    default BalmKeyMappings scoped(String modId) {
        return this;
    }

    BalmKeyMappings LEGACY = new BalmKeyMappings() {
    };
}
