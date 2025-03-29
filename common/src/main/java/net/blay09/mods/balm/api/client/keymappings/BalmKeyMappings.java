package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import java.util.Optional;

public interface BalmKeyMappings {
    default KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        return registerKeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category);

    @Deprecated
    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category);

    @Deprecated
    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, int keyCode, String category);

    @Deprecated
    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category);

    @Deprecated
    KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, InputConstants.Type type, int keyCode, String category);

    @Deprecated
    default boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return isActiveAndMatches(keyMapping, InputConstants.getKey(keyCode, scanCode));
    }

    @Deprecated
    default boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        return isActiveAndMatches(keyMapping, type.getOrCreate(type == InputConstants.Type.SCANCODE ? scanCode : keyCode));
    }

    @Deprecated
    boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Key input);

    @Deprecated
    boolean isActiveAndWasPressed(KeyMapping keyMapping);

    @Deprecated
    boolean isKeyDownIgnoreContext(KeyMapping keyMapping);

    @Deprecated
    boolean isActiveAndKeyDown(KeyMapping keyMapping);

    /**
     * @deprecated No longer in use.
     */
    @Deprecated
    Optional<Boolean> conflictsWith(KeyMapping first, KeyMapping second);

    /**
     * @deprecated No longer in use. Has no effect.
     */
    @Deprecated
    void ignoreConflicts(KeyMapping keyMapping);

    /**
     * @deprecated No longer in use.
     */
    @Deprecated
    boolean shouldIgnoreConflicts(KeyMapping keyMapping);
}
