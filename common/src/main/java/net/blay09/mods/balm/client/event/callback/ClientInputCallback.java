package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;

public interface ClientInputCallback {

    @FunctionalInterface
    interface Keyboard {
        void handle(int key, int scanCode, int action, int modifiers);

        /**
         * Fired on the client when a key input event occurs, such as pressing, releasing, or repeating a key while held.
         *
         * <li>Fabric: via supplemental event</li>
         * <li>NeoForge: {@code net.minecraftforge.client.event.InputEvent.KeyInputEvent}</li>
         * <li>Forge: {@code net.minecraftforge.client.event.InputEvent.KeyInputEvent}</li>
         */
        EventMapper<Keyboard> EVENT = EventMapper.createUnbound("ClientInputCallback.Keyboard");
    }

}
