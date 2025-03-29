package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.multiplayer.ClientRecipeContainer;

/**
 * @deprecated This event is no longer fired.
 */
@Deprecated(forRemoval = true, since = "1.21.5")
public class RecipesUpdatedEvent extends BalmEvent {
    private final ClientRecipeContainer clientRecipeContainer;

    public RecipesUpdatedEvent(ClientRecipeContainer clientRecipeContainer) {
        this.clientRecipeContainer = clientRecipeContainer;
    }

    public ClientRecipeContainer getClientRecipeContainer() {
        return clientRecipeContainer;
    }
}
