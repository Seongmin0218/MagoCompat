package kr.mago.compat.farm;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.api.Protection;
import medi.makiba.farmers_attributes.attribute.EasyHarvest;
import medi.makiba.farmers_attributes.registry.FAAttributes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Mago Farmer Area Harvest
 *
 * Hoe + Right Click
 *
 * 3x3: ps_farm_harvest_3x3
 * 5x5: ps_farm_harvest_5x5
 *
 * Reuses Farmer's Attributes EasyHarvest:
 * - Crop drops
 * - Automatic replanting
 * - Green Thumb
 * - Puffish Skills experience
 */
@EventBusSubscriber(modid = "mago_compat")
public final class AreaHarvestEvents {

    private static final String TAG_HARVEST_3X3 =
            "ps_farm_harvest_3x3";

    private static final String TAG_HARVEST_5X5 =
            "ps_farm_harvest_5x5";

    private static final TagKey<Block> AREA_HARVEST_CROPS =
            TagKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(
                            "mago_skill",
                            "farm_area_harvest"
                    )
            );

    private AreaHarvestEvents() {
    }

    /**
     * Handle area harvesting before Farmer's Attributes
     * processes its normal single-crop harvest.
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(
            PlayerInteractEvent.RightClickBlock event
    ) {

        // Server-side only.
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        // Exclude automated players and invalid game modes.
        if (player instanceof FakePlayer
                || player.isCreative()
                || player.isSpectator()
                || !player.getAbilities().mayBuild) {
            return;
        }

        // Respect previously denied interactions.
        if (event.isCanceled()
                || event.getUseBlock() == TriState.FALSE
                || event.getUseItem() == TriState.FALSE) {
            return;
        }

        // Prevent duplicate processing from the offhand.
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        /*
         * Area harvesting does not require crouching.
         *
         * Preserve the original EasyHarvest restriction
         * while the player is crouching.
         */
        if (player.isCrouching()) {
            return;
        }

        ItemStack hoe = event.getItemStack();

        // Only hoes can activate area harvesting.
        if (!(hoe.getItem() instanceof HoeItem)) {
            return;
        }

        if (player.getCooldowns().isOnCooldown(hoe.getItem())) {
            return;
        }

        // Requires the original Easy Harvest skill.
        if (player.getAttributeValue(
                FAAttributes.EASY_HARVEST
        ) <= 0.0) {
            return;
        }

        if (!level.getGameRules().getBoolean(
                GameRules.RULE_DOBLOCKDROPS
        )) {
            return;
        }

        int radius = getHarvestRadius(player);

        // Neither area harvest skill is unlocked.
        if (radius == 0) {
            return;
        }

        BlockPos center = event.getPos();

        if (!level.hasChunkAt(center)) {
            return;
        }

        BlockState centerState = level.getBlockState(center);

        /*
         * Activate only when targeting a supported crop.
         *
         * Clicking dirt, grass or farmland continues to
         * use the normal hoe interaction.
         */
        if (!isSupportedCrop(centerState)) {
            return;
        }

        /*
         * Cancel the interaction if the center crop
         * cannot be accessed.
         */
        if (!canHarvestAt(level, player, center)) {
            event.setCanceled(true);
            event.setCancellationResult(
                    InteractionResult.FAIL
            );
            return;
        }

        /*
         * Consume the original interaction before
         * harvesting to prevent duplicate single harvesting.
         */
        event.setCanceled(true);
        event.setCancellationResult(
                InteractionResult.CONSUME
        );

        int harvested = 0;

        /*
         * Harvest the targeted crop first.
         * It has priority when the hoe has low durability.
         */
        if (harvestCrop(level, player, center, hoe)) {
            harvested++;
        }

        /*
         * Scan the remaining crops at the same Y level.
         */
        harvestLoop:
        for (int dz = -radius; dz <= radius; dz++) {

            for (int dx = -radius; dx <= radius; dx++) {

                // The center has already been processed.
                if (dx == 0 && dz == 0) {
                    continue;
                }

                // Stop immediately when the hoe breaks.
                if (hoe.isEmpty()) {
                    break harvestLoop;
                }

                BlockPos target = center.offset(dx, 0, dz);

                if (harvestCrop(
                        level,
                        player,
                        target,
                        hoe
                )) {
                    harvested++;
                }
            }
        }

        if (harvested > 0) {
            player.swing(InteractionHand.MAIN_HAND);

            player.displayClientMessage(
                    Component.literal(
                            "범위 수확: " + harvested + "개 작물"
                    ),
                    true
            );
        }
    }

    /**
     * Harvest a single crop using the original EasyHarvest.
     */
    private static boolean harvestCrop(
            ServerLevel level,
            ServerPlayer player,
            BlockPos pos,
            ItemStack hoe
    ) {

        if (hoe.isEmpty()) {
            return false;
        }

        // Do not force-load neighboring chunks.
        if (!level.hasChunkAt(pos)) {
            return false;
        }

        BlockState before = level.getBlockState(pos);

        // Skip immature and unsupported crops.
        if (!isMatureCrop(before)) {
            return false;
        }

        // Check the original EasyHarvest restrictions.
        if (!EasyHarvest.isEasyHarvestable(
                level,
                pos,
                before,
                Direction.UP,
                hoe
        )) {
            return false;
        }

        // Check permissions individually for every crop.
        if (!canHarvestAt(level, player, pos)) {
            return false;
        }

        /*
         * Preserve the existing harvest implementation:
         *
         * - Loot generation
         * - Green Thumb multiplier
         * - Seed consumption
         * - Automatic replanting
         * - Inventory insertion
         * - Puffish Skills experience
         */
        boolean result = EasyHarvest.tryHarvest(
                level,
                pos,
                player,
                Direction.UP,
                hoe
        );

        if (!result) {
            return false;
        }

        BlockState after = level.getBlockState(pos);

        // Only count crops whose block state changed.
        if (before.equals(after)) {
            return false;
        }

        /*
         * Consume one durability point per
         * successfully harvested crop.
         */
        hoe.hurtAndBreak(
                1,
                player,
                EquipmentSlot.MAINHAND
        );

        return true;
    }

    /**
     * 5x5 takes priority over 3x3.
     */
    private static int getHarvestRadius(
            ServerPlayer player
    ) {

        if (player.getTags().contains(TAG_HARVEST_5X5)) {
            return 2;
        }

        if (player.getTags().contains(TAG_HARVEST_3X3)) {
            return 1;
        }

        return 0;
    }

    /**
     * Only explicitly supported CropBlock crops.
     */
    private static boolean isSupportedCrop(
            BlockState state
    ) {

        return state.is(AREA_HARVEST_CROPS)
                && state.getBlock() instanceof CropBlock
                && !state.hasBlockEntity();
    }

    /**
     * Check the actual maximum age of each crop.
     */
    private static boolean isMatureCrop(
            BlockState state
    ) {

        if (!isSupportedCrop(state)) {
            return false;
        }

        if (!(state.getBlock() instanceof CropBlock crop)) {
            return false;
        }

        return crop.getAge(state) == crop.getMaxAge();
    }

    /**
     * Check vanilla and FTB Chunks protection.
     */
    private static boolean canHarvestAt(
            ServerLevel level,
            ServerPlayer player,
            BlockPos pos
    ) {

        if (!level.getWorldBorder().isWithinBounds(pos)) {
            return false;
        }

        if (!level.mayInteract(player, pos)) {
            return false;
        }

        if (ModList.get().isLoaded("ftbchunks")) {

            var api = FTBChunksAPI.api();

            if (!api.isManagerLoaded()) {
                return false;
            }

            var manager = api.getManager();

            // Check block interaction permission.
            if (manager.shouldPreventInteraction(
                    player,
                    InteractionHand.MAIN_HAND,
                    pos,
                    Protection.INTERACT_BLOCK,
                    null
            )) {
                return false;
            }

            // Check block editing permission.
            if (manager.shouldPreventInteraction(
                    player,
                    InteractionHand.MAIN_HAND,
                    pos,
                    Protection.EDIT_BLOCK,
                    null
            )) {
                return false;
            }
        }

        return true;
    }
}