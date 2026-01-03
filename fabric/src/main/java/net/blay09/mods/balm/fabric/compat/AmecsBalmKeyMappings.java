package net.blay09.mods.balm.fabric.compat;

import com.mojang.blaze3d.platform.InputConstants;
import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyBindingUtils;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.api.client.keymappings.KeyModifiers;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.common.client.keymappings.CommonBalmKeyMappings;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AmecsBalmKeyMappings extends CommonBalmKeyMappings {

    private static final Map<KeyMapping, KeyConflictContext> contextAwareKeyMappings = new ConcurrentHashMap<>();

    public AmecsBalmKeyMappings() {
        super(new LegacyNamespaceResolver(() -> {
            throw new UnsupportedOperationException("No default namespace available");
        }));
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyBinding = new AmecsKeyBinding(name, type, keyCode, category, toAmecs(modifier));
        contextAwareKeyMappings.put(keyBinding, conflictContext);
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyMapping = new AmecsKeyBinding(name, type, keyCode, category, toAmecs(modifiers));
        contextAwareKeyMappings.put(keyMapping, conflictContext);
        keyMapping = KeyBindingHelper.registerKeyBinding(keyMapping);
        if (modifiers.hasCustomModifiers()) {
            registerCustomModifierKeyMappings(keyMapping, conflictContext, modifiers.getCustomModifiers());
        }
        return keyMapping;
    }

    @Override
    public BalmKeyMappings scoped(String modId) {
        return this;
    }

    @Override
    protected boolean isContextActive(KeyMapping keyMapping) {
        KeyConflictContext conflictContext = contextAwareKeyMappings.getOrDefault(keyMapping, KeyConflictContext.UNIVERSAL);
        return isContextActive(conflictContext);
    }

    @Override
    protected boolean areModifiersActive(KeyMapping keyMapping) {
        de.siphalor.amecs.api.KeyModifiers boundModifiers = KeyBindingUtils.getBoundModifiers(keyMapping);
        if (boundModifiers.getControl() && !Screen.hasControlDown()) {
            return false;
        }
        if (boundModifiers.getShift() && !Screen.hasShiftDown()) {
            return false;
        }
        if (boundModifiers.getAlt() && !Screen.hasAltDown()) {
            return false;
        }
        return super.areModifiersActive(keyMapping);
    }

    private de.siphalor.amecs.api.KeyModifiers toAmecs(KeyModifier modifier) {
        final var modifiers = new de.siphalor.amecs.api.KeyModifiers();
        switch (modifier) {
            case ALT:
                modifiers.setAlt(true);
                break;
            case CONTROL:
                modifiers.setControl(true);
                break;
            case SHIFT:
                modifiers.setShift(true);
                break;
            case NONE:
                break;
        }
        return modifiers;
    }

    private de.siphalor.amecs.api.KeyModifiers toAmecs(KeyModifiers modifiers) {
        final var amecsModifiers = new de.siphalor.amecs.api.KeyModifiers();
        if (modifiers.contains(KeyModifier.ALT)) {
            amecsModifiers.setAlt(true);
        }
        if (modifiers.contains(KeyModifier.CONTROL)) {
            amecsModifiers.setControl(true);
        }
        if (modifiers.contains(KeyModifier.SHIFT)) {
            amecsModifiers.setShift(true);
        }
        return amecsModifiers;
    }
}
