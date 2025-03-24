package net.blay09.mods.balm.fabric.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.api.client.keymappings.KeyModifiers;
import net.blay09.mods.balm.common.client.keymappings.CommonBalmKeyMappings;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricBalmKeyMappings extends CommonBalmKeyMappings {

    private final Map<KeyMapping, KeyConflictContext> contextAwareKeyMappings = new ConcurrentHashMap<>();

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyBinding = new KeyMapping(name, type, keyCode, category);
        contextAwareKeyMappings.put(keyBinding, conflictContext);
        KeyMapping keyMapping = KeyBindingHelper.registerKeyBinding(keyBinding);
        if (modifier != KeyModifier.NONE) {
            registerModifierKeyMappings(keyMapping, conflictContext, Collections.singletonList(modifier));
        }
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, InputConstants.Type type, int keyCode, String category) {
        var keyModifiers = modifiers.asList();
        KeyMapping keyMapping = new KeyMapping(name, type, keyCode, category);
        contextAwareKeyMappings.put(keyMapping, conflictContext);
        if (!keyModifiers.isEmpty()) {
            registerModifierKeyMappings(keyMapping, conflictContext, keyModifiers);
        }
        if (modifiers.hasCustomModifiers()) {
            registerCustomModifierKeyMappings(keyMapping, conflictContext, modifiers.getCustomModifiers());
        }
        return KeyBindingHelper.registerKeyBinding(keyMapping);
    }

    private boolean isActiveAndMatchesStrictModifier(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        return isActive(keyMapping) && keyMapping.matches(keyCode, scanCode);
    }

    @Override
    protected boolean isContextActive(KeyMapping keyMapping) {
        KeyConflictContext conflictContext = contextAwareKeyMappings.getOrDefault(keyMapping, KeyConflictContext.UNIVERSAL);
        return isContextActive(conflictContext);
    }

}
