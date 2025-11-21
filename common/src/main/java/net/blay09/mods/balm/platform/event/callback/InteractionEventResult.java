package net.blay09.mods.balm.platform.event.callback;

import net.minecraft.world.InteractionResult;

import java.util.Optional;

public interface InteractionEventResult {
    Optional<InteractionResult> interactionResult();

    InteractionEventResult DEFAULT = Optional::empty;
    InteractionEventResult SUCCESS = () -> Optional.of(InteractionResult.SUCCESS);
    InteractionEventResult SUCCESS_SERVER = () -> Optional.of(InteractionResult.SUCCESS_SERVER);
    InteractionEventResult CONSUME = () -> Optional.of(InteractionResult.CONSUME);
    InteractionEventResult FAIL = () -> Optional.of(InteractionResult.FAIL);

    /**
     * @deprecated On Fabric, this result has no effect. Consider using {@link InteractionEventResult#DEFAULT} instead.
     */
    @Deprecated
    InteractionEventResult PASS = () -> Optional.of(InteractionResult.PASS);

    InteractionEventResult TRY_WITH_EMPTY_HAND = () -> Optional.of(InteractionResult.TRY_WITH_EMPTY_HAND);
}
