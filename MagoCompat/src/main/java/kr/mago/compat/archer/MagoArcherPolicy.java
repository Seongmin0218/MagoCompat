package kr.mago.compat.archer;

import kr.mago.compat.MagoCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

import java.util.Set;
import java.util.function.Predicate;

public final class MagoArcherPolicy {

    // =========================================================
    // Retired Twilight Forest weapon
    // =========================================================

    private static final ResourceLocation TRIPLE_BOW =
            id(
                    "twilightforest",
                    "triple_bow"
            );


    // =========================================================
    // Ice and Fire
    // =========================================================

    private static final ResourceLocation DRAGONBONE_ARROW =
            id(
                    "iceandfire",
                    "dragonbone_arrow"
            );

    private static final ResourceLocation DRAGONBONE_BOW =
            id(
                    "iceandfire",
                    "dragonbone_bow"
            );


    /*
     * Dragon Bone Arrow를 사용할 수 있는 무기 목록.
     *
     * 현재 Mago에서는 Ice and Fire의 정식 Dragonbone Bow만 허용한다.
     * 추후 별도 Dragonbone Bow 계열 무기를 허용해야 한다면
     * 이 Set에 ResourceLocation만 추가하면 된다.
     */
    private static final Set<ResourceLocation> DRAGONBONE_ARROW_WEAPONS =
            Set.of(
                    DRAGONBONE_BOW
            );


    private static boolean loggedTripleBowRemoval = false;
    private static boolean loggedDragonboneRestriction = false;


    private MagoArcherPolicy() {
    }


    // =========================================================
    // Registration
    // =========================================================

    public static void register(
            IEventBus modEventBus
    ) {

        /*
         * Creative tab은 MOD bus 이벤트.
         */
        modEventBus.addListener(
                EventPriority.LOWEST,
                MagoArcherPolicy::onBuildCreativeContents
        );


        /*
         * 실제 게임 이벤트.
         */
        NeoForge.EVENT_BUS.addListener(
                MagoArcherPolicy::onLivingDrops
        );

        NeoForge.EVENT_BUS.addListener(
                MagoArcherPolicy::onLivingGetProjectile
        );
    }


    // =========================================================
    // Triple Bow retirement
    // =========================================================

    /**
     * Triple Bow를 일반 Creative tab과 검색 탭에서 숨긴다.
     */
    private static void onBuildCreativeContents(
            BuildCreativeModeTabContentsEvent event
    ) {

        Item tripleBow =
                BuiltInRegistries.ITEM.get(
                        TRIPLE_BOW
                );

        if (tripleBow == null) {
            return;
        }

        event.remove(
                new ItemStack(tripleBow),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
        );
    }


    /**
     * 다른 데이터팩/모드가 Snow Queen 등의 드롭에
     * Triple Bow를 다시 삽입하더라도 마지막 안전장치로 제거한다.
     */
    private static void onLivingDrops(
            LivingDropsEvent event
    ) {

        boolean removed =
                event.getDrops().removeIf(
                        itemEntity ->
                                isItem(
                                        itemEntity.getItem(),
                                        TRIPLE_BOW
                                )
                );

        if (removed && !loggedTripleBowRemoval) {
            loggedTripleBowRemoval = true;

            MagoCompat.LOGGER.info(
                    "[MagoCompat] Retired Twilight Forest Triple Bow removed from entity drops."
            );
        }
    }


    // =========================================================
    // Dragon Bone Arrow restriction
    // =========================================================

    /**
     * Ice and Fire Dragon Bone Arrow를
     * Mago에서 허용된 Dragonbone Bow 계열만 사용할 수 있게 한다.
     *
     * NeoForge의 LivingGetProjectileEvent는
     * 실제로 선택된 투사체와 그 투사체를 요구한 무기를 모두 제공한다.
     */
    private static void onLivingGetProjectile(
            LivingGetProjectileEvent event
    ) {

        ItemStack projectile =
                event.getProjectileItemStack();

        if (!isItem(
                projectile,
                DRAGONBONE_ARROW
        )) {
            return;
        }


        ItemStack weapon =
                event.getProjectileWeaponItemStack();

        ResourceLocation weaponId =
                BuiltInRegistries.ITEM.getKey(
                        weapon.getItem()
                );


        /*
         * Dragonbone Bow는 그대로 허용.
         */
        if (DRAGONBONE_ARROW_WEAPONS.contains(
                weaponId
        )) {
            return;
        }


        /*
         * 일반 활이 Dragon Bone Arrow를 선택했다면
         * 가능한 경우 다른 정상 탄약으로 교체한다.
         *
         * 단순히 EMPTY 처리만 하면
         * Dragon Bone Arrow가 인벤토리 앞쪽에 있다는 이유로
         * 일반 화살까지 못 쏘는 상황이 생길 수 있기 때문이다.
         */
        ItemStack replacement =
                ItemStack.EMPTY;

        if (event.getEntity() instanceof Player player) {
            replacement =
                    findAlternativeProjectile(
                            player,
                            weapon
                    );
        }


        event.setProjectileItemStack(
                replacement
        );


        if (!loggedDragonboneRestriction) {
            loggedDragonboneRestriction = true;

            MagoCompat.LOGGER.info(
                    "[MagoCompat] Dragon Bone Arrow restriction active. "
                            + "Ammo: {} | Allowed weapons: {}",
                    DRAGONBONE_ARROW,
                    DRAGONBONE_ARROW_WEAPONS
            );
        }
    }


    /**
     * 현재 무기가 사용할 수 있는 탄약 중
     * Dragon Bone Arrow가 아닌 다른 탄약을 찾는다.
     *
     * 손 -> 인벤토리 순서.
     */
    private static ItemStack findAlternativeProjectile(
            Player player,
            ItemStack weaponStack
    ) {

        if (!(weaponStack.getItem()
                instanceof ProjectileWeaponItem projectileWeaponItem)) {

            return ItemStack.EMPTY;
        }


        Predicate<ItemStack> notDragonboneArrow =
                stack ->
                        !isItem(
                                stack,
                                DRAGONBONE_ARROW
                        );


        // -----------------------------------------------------
        // 1. Main/Off Hand
        // -----------------------------------------------------

        Predicate<ItemStack> supportedHeldProjectiles =
                projectileWeaponItem
                        .getSupportedHeldProjectiles(
                                weaponStack
                        )
                        .and(
                                notDragonboneArrow
                        );


        ItemStack heldProjectile =
                ProjectileWeaponItem.getHeldProjectile(
                        player,
                        supportedHeldProjectiles
                );


        if (!heldProjectile.isEmpty()) {
            return heldProjectile;
        }


        // -----------------------------------------------------
        // 2. Inventory
        // -----------------------------------------------------

        Predicate<ItemStack> supportedProjectiles =
                projectileWeaponItem
                        .getAllSupportedProjectiles(
                                weaponStack
                        )
                        .and(
                                notDragonboneArrow
                        );


        for (
                int slot = 0;
                slot < player.getInventory().getContainerSize();
                slot++
        ) {

            ItemStack candidate =
                    player.getInventory().getItem(
                            slot
                    );


            if (
                    !candidate.isEmpty()
                            && supportedProjectiles.test(
                                    candidate
                            )
            ) {

                return candidate;
            }
        }


        return ItemStack.EMPTY;
    }


    // =========================================================
    // Utilities
    // =========================================================

    private static boolean isItem(
            ItemStack stack,
            ResourceLocation expectedId
    ) {

        if (stack == null || stack.isEmpty()) {
            return false;
        }


        ResourceLocation actualId =
                BuiltInRegistries.ITEM.getKey(
                        stack.getItem()
                );


        return expectedId.equals(
                actualId
        );
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