package net.blay09.mods.balm.world.item;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public interface BalmItemRegistration extends BalmHolderRegistration<Item> {
    default ItemLike asItemLike() {
        return asDeferredItem();
    }

    DeferredItem asDeferredItem();
}
