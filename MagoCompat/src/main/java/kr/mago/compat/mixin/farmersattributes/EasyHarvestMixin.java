package kr.mago.compat.mixin.farmersattributes;

import medi.makiba.farmers_attributes.FAConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.attachment.AttachmentType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Fixes Green Thumb drop multiplication in Farmer's Attributes 1.9.0.
 *
 * The original EasyHarvest implementation checks whether a chunk
 * contains Green Thumb attachment data, but does not check whether
 * the harvested crop position is actually registered.
 */
@Pseudo
@Mixin(
        targets = "medi.makiba.farmers_attributes.attribute.EasyHarvest",
        remap = false
)
public abstract class EasyHarvestMixin {

    /**
     * Only apply Green Thumb when the harvested crop position
     * is registered in the chunk attachment.
     *
     * Also respect the Green Thumb blacklist, matching the
     * normal block-breaking harvest behavior.
     */
    @Redirect(
            method = "tryHarvest",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/ChunkAccess;"
                            + "hasData(Ljava/util/function/Supplier;)Z"
            ),
            require = 1,
            allow = 1
    )
    private static boolean magoCompat$checkGreenThumbPosition(
            ChunkAccess chunk,
            Supplier<AttachmentType<Set<BlockPos>>> attachmentType,
            Level level,
            BlockPos pos,
            Player player,
            Direction face,
            ItemStack heldItem
    ) {
        // No Green Thumb data in this chunk.
        if (!chunk.hasData(attachmentType)) {
            return false;
        }

        // The current crop must be individually registered.
        Set<BlockPos> greenThumbPositions =
                chunk.getData(attachmentType);

        if (!greenThumbPositions.contains(pos)) {
            return false;
        }

        // Respect the configured Green Thumb blacklist.
        BlockState state = level.getBlockState(pos);

        String blockId = BuiltInRegistries.BLOCK
                .getKey(state.getBlock())
                .toString();

        return !FAConfig.GREEN_THUMB_BLACKLIST
                .get()
                .contains(blockId);
    }

    /**
     * The original method creates extraDrops using List.of().
     * That list is immutable and throws an exception when
     * oversized multiplied drops need to be split.
     */
    @Redirect(
            method = "tryHarvest",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;of()Ljava/util/List;"
            ),
            require = 1,
            allow = 1
    )
    private static List<ItemStack> magoCompat$createMutableExtraDrops() {
        return new ArrayList<>();
    }
}