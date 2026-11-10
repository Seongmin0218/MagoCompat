package kr.mago.compat.celestisynth;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Set;

/**
 * Celestisynth 히든 전사 무기 사용 제한.
 *
 * 사용 조건:
 * 1. 공격계 전사 전문화가 현재 활성 상태일 것.
 * 2. 히든 전사 영구 자격 태그를 보유할 것.
 *
 * Puffish Skills Item Gating에는 의존하지 않는다.
 * 마법사 전문화의 주문 제한과 마찬가지로
 * 실제 사용 이벤트에서 직접 서버 권한을 검사한다.
 */
public final class MagoCelestisynthGate {

    public static final String ACTIVE_TAG =
            "mago_warrior_attack_active";

    public static final String QUALIFICATION_TAG =
            "mago_hidden_warrior_qualified";

    private static final Set<String> CELESTISYNTH_WEAPONS =
            Set.of(
                    "celestisynth:aquaflora",
                    "celestisynth:keres",
                    "celestisynth:solaris",
                    "celestisynth:crescentia",
                    "celestisynth:breezebreaker",
                    "celestisynth:poltergeist",
                    "celestisynth:frostbound"
            );


    private MagoCelestisynthGate() {
    }


    public static void register() {

        /*
         * 일반 좌클릭 평타 차단.
         */
        NeoForge.EVENT_BUS.addListener(
                EventPriority.HIGHEST,
                MagoCelestisynthGate::onAttackEntity
        );

        /*
         * Celestisynth의 SkilledSwordItem / SkilledAxeItem은
         * Item#use()에서 우클릭 스킬을 시작한다.
         *
         * Item#use() 실행 직전인 RightClickItem에서 차단한다.
         */
        NeoForge.EVENT_BUS.addListener(
                EventPriority.HIGHEST,
                MagoCelestisynthGate::onRightClickItem
        );
    }


    private static void onAttackEntity(
            AttackEntityEvent event
    ) {

        Player player =
                event.getEntity();

        ItemStack stack =
                player.getMainHandItem();


        if (!isCelestisynthWeapon(stack)) {
            return;
        }


        if (canUse(player)) {
            return;
        }


        event.setCanceled(true);

        sendDeniedMessage(player);
    }


    private static void onRightClickItem(
            PlayerInteractEvent.RightClickItem event
    ) {

        Player player =
                event.getEntity();

        ItemStack stack =
                event.getItemStack();


        if (!isCelestisynthWeapon(stack)) {
            return;
        }


        if (canUse(player)) {
            return;
        }


        /*
         * 이벤트 자체를 취소하고 FAIL을 반환해서
         * Celestisynth의 Item#use()까지 진행되지 않도록 한다.
         */
        event.setCanceled(true);

        event.setCancellationResult(
                InteractionResult.FAIL
        );

        sendDeniedMessage(player);
    }


    private static boolean canUse(
            Player player
    ) {

        return player.getTags().contains(
                ACTIVE_TAG
        )
                &&
                player.getTags().contains(
                        QUALIFICATION_TAG
                );
    }


    private static boolean isCelestisynthWeapon(
            ItemStack stack
    ) {

        if (stack == null || stack.isEmpty()) {
            return false;
        }


        String itemId =
                BuiltInRegistries.ITEM
                        .getKey(
                                stack.getItem()
                        )
                        .toString();


        return CELESTISYNTH_WEAPONS.contains(
                itemId
        );
    }


    private static void sendDeniedMessage(
            Player player
    ) {

        /*
         * 이벤트 취소는 양쪽에서 수행하지만,
         * 안내 메시지는 서버에서 한 번만 전송한다.
         */
        if (
                player.level()
                        .isClientSide()
        ) {
            return;
        }


        if (
                !player.getTags().contains(
                        ACTIVE_TAG
                )
        ) {

            player.displayClientMessage(
                    Component.literal(
                            "현재 공격계 전사 전문화가 활성화되어 있지 않습니다."
                    ),
                    true
            );

            return;
        }


        player.displayClientMessage(
                Component.literal(
                        "Celestisynth 히든 무기 사용 자격이 없습니다."
                ),
                true
        );
    }
}