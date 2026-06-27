package net.blay09.mods.balm.platform.compatibility.vr;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public interface BalmModSupportVR {
    boolean isInVR(Player player);
}
