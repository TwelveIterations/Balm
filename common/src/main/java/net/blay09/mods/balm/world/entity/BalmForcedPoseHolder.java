package net.blay09.mods.balm.world.entity;

import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

public interface BalmForcedPoseHolder {

    @Nullable
    Pose balm$getForcedPose();

    void balm$setForcedPose(@Nullable Pose pose);

}
