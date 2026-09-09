package kr.mago.compat.item;

import kr.mago.compat.MagoCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.Set;

public final class MagoRetiredItemPolicy {

    // =========================================================
    // ISS: Magic From The East
    // =========================================================

    public static final ResourceLocation SYMMETRY_RUNE =
            id(
                    "iss_magicfromtheeast",
                    "symmetry_rune"
            );

    public static final ResourceLocation SYMMETRY_UPGRADE_ORB =
            id(
                    "iss_magicfromtheeast",
                    "symmetry_upgrade_orb"
            );

    /*
     * The item shown as Taiji Sword is also the mod's staff item.
     *
     * iss_magicfromtheeast:staff tag:
     * - iss_magicfromtheeast:taiji_sword
     */
    public static final ResourceLocation TAIJI_SWORD =
            id(
                    "iss_magicfromtheeast",
                    "taiji_sword"
            );

    public static final ResourceLocation SPIRIT_RUNE =
            id(
                    "iss_magicfromtheeast",
                    "spirit_rune"
            );


    // =========================================================
    // Retired items
    // =========================================================

    private static final Set<ResourceLocation> RETIRED_ITEMS =
            Set.of(
                    SYMMETRY_RUNE,
                    SYMMETRY_UPGRADE_ORB,
                    TAIJI_SWORD
            );


    private static boolean loggedSymmetryDropMigration = false;


    private MagoRetiredItemPolicy() {
    }


    public static void register(
            IEventBus modEventBus
    ) {
        /*
         * Creative tab construction is a MOD bus event.
         * LOWEST ensures Mago removes entries after normal listeners
         * have populated their tabs.
         */
        modEventBus.addListener(
                EventPriority.LOWEST,
                MagoRetiredItemPolicy::onBuildCreativeContents
        );

        /*
         * Entity drops are NeoForge game bus events.
         */
        NeoForge.EVENT_BUS.addListener(
                MagoRetiredItemPolicy::onLivingDrops
        );
    }


    /**
     * Removes retired items from:
     *
     * - their normal creative tab
     * - creative search results
     */
    private static void onBuildCreativeContents(
            BuildCreativeModeTabContentsEvent event
    ) {
        for (ResourceLocation itemId : RETIRED_ITEMS) {
            Item item =
                    BuiltInRegistries.ITEM.get(
                            itemId
                    );

            if (item == null) {
                continue;
            }

            event.remove(
                    new ItemStack(item),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    /**
     * ISS Taoists normally drop a Symmetry Rune.
     *
     * Since Symmetry is retired in Mago, preserve the reward
     * by converting that drop into a Spirit Rune instead.
     */
    private static void onLivingDrops(
            LivingDropsEvent event
    ) {
        Item spiritRune =
                BuiltInRegistries.ITEM.get(
                        SPIRIT_RUNE
                );

        if (spiritRune == null) {
            throw new IllegalStateException(
                    "[MagoCompat] Missing required item: "
                            + SPIRIT_RUNE
            );
        }

        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack =
                    drop.getItem();

            ResourceLocation itemId =
                    BuiltInRegistries.ITEM.getKey(
                            stack.getItem()
                    );

            if (!SYMMETRY_RUNE.equals(itemId)) {
                continue;
            }

            int count =
                    stack.getCount();

            drop.setItem(
                    new ItemStack(
                            spiritRune,
                            count
                    )
            );

            if (!loggedSymmetryDropMigration) {
                loggedSymmetryDropMigration = true;

                MagoCompat.LOGGER.info(
                        "[MagoCompat] Retired Symmetry Rune drop migrated. "
                                + "{} -> {}",
                        SYMMETRY_RUNE,
                        SPIRIT_RUNE
                );
            }
        }
    }


    public static boolean isRetired(
            ItemStack stack
    ) {
        ResourceLocation itemId =
                BuiltInRegistries.ITEM.getKey(
                        stack.getItem()
                );

        return RETIRED_ITEMS.contains(
                itemId
        );
    }


    public static Set<ResourceLocation> retiredItemIds() {
        return RETIRED_ITEMS;
    }


    private static ResourceLocation id(
            String namespace,
            String path
    ) {
        return ResourceLocation.fromNamespaceAndPath(
                namespace,
                path
        );
    }
}