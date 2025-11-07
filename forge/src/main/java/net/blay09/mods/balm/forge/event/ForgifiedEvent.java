package net.blay09.mods.balm.forge.event;

import net.minecraftforge.eventbus.api.event.RecordEvent;

public record ForgifiedEvent<T>(T data) implements RecordEvent {
}
