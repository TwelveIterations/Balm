package net.blay09.mods.balm.fabric.gametest;

import net.blay09.mods.balm.api.block.entity.OnLoadHandler;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityOnLoadGameTest {

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void blockEntityOnLoadIsCalled(GameTestHelper helper) {
        final var pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos, Blocks.CHEST);

        final var blockEntity = new OnLoadTestBlockEntity(helper.absolutePos(pos), Blocks.CHEST.defaultBlockState());
        helper.getLevel().setBlockEntity(blockEntity);

        helper.assertTrue(blockEntity.onLoadCalls == 0, "Block entity onLoad did not wait for the first tick");
        helper.runAtTickTime(5, () -> {
            helper.assertTrue(blockEntity.onLoadCalls == 1, "Block entity onLoad should be called exactly once");
            helper.succeed();
        });
    }

    private static class OnLoadTestBlockEntity extends ChestBlockEntity implements OnLoadHandler {

        private int onLoadCalls;

        private OnLoadTestBlockEntity(BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public void onLoad() {
            onLoadCalls++;
        }
    }
}
