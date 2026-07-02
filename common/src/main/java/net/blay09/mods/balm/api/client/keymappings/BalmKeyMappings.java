package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.minecraft.client.KeyMapping;

import java.util.Optional;

public interface BalmKeyMappings {
    default KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        return registerKeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category);

    /**
     * @deprecated Use Kuma instead.
     */
    @Deprecated(since = "1.21.5")
    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category);

    /**
     * @deprecated Use Kuma instead.
     */
    @Deprecated(since = "1.21.5")
    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, int keyCode, String category);

    /**
     * @deprecated Use Kuma instead.
     */
    @Deprecated(since = "1.21.5")
    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category);

    /**
     * @deprecated Use Kuma instead.
     */
    @Deprecated(since = "1.21.5")
    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, InputConstants.Type type, int keyCode, String category);

    /**
     * @deprecated Use Kuma or KeyMapping's native methods instead.
     */
    @Deprecated(since = "1.21.5")
    default boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return isActiveAndMatches(keyMapping, InputConstants.getKey(keyCode, scanCode));
    }

    /**
     * @deprecated Use Kuma or KeyMapping's native methods instead.
     */
    @Deprecated(since = "1.21.5")
    default boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        return isActiveAndMatches(keyMapping, type.getOrCreate(type == InputConstants.Type.SCANCODE ? scanCode : keyCode));
    }

    /**
     * @deprecated Use Kuma or KeyMapping's native methods instead.
     */
    @Deprecated(since = "1.21.5")
    boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Key input);

    /**
     * @deprecated Use Kuma or KeyMapping's native methods instead.
     */
    @Deprecated(since = "1.21.5")
    boolean isActiveAndWasPressed(KeyMapping keyMapping);

    /**
     * @deprecated Use Kuma or KeyMapping's native methods instead.
     */
    @Deprecated(since = "1.21.5")
    boolean isKeyDownIgnoreContext(KeyMapping keyMapping);

    /**
     * @deprecated Use Kuma or KeyMapping's native methods instead.
     */
    @Deprecated(since = "1.21.5")
    boolean isActiveAndKeyDown(KeyMapping keyMapping);

    /**
     * @deprecated No longer in use.
     */
    @Deprecated(since = "1.21.5")
    Optional<Boolean> conflictsWith(KeyMapping first, KeyMapping second);

    /**
     * @deprecated No longer in use. Has no effect.
     */
    @Deprecated(since = "1.21.5")
    void ignoreConflicts(KeyMapping keyMapping);

    /**
     * @deprecated No longer in use.
     */
    @Deprecated(since = "1.21.5")
    boolean shouldIgnoreConflicts(KeyMapping keyMapping);

    BalmKeyMappings scoped(String modId);
}
