package kr.mago.compat.spell;

import io.redspace.ironsspellbooks.api.config.ModifyDefaultConfigValuesEvent;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.network.casting.OnCastFinishedPacket;
import kr.mago.compat.MagoCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

public final class MagoSpellEvents {

    private static boolean registered = false;


    private MagoSpellEvents() {
    }


    public static void register() {

        if (registered) {
            return;
        }

        registered = true;


        NeoForge.EVENT_BUS.addListener(
                EventPriority.LOWEST,
                MagoSpellEvents::onModifyDefaultConfigValues
        );


        NeoForge.EVENT_BUS.addListener(
                EventPriority.HIGHEST,
                MagoSpellEvents::onSpellPreCast
        );


        MagoCompat.LOGGER.info(
                "[MagoCompat] Spell policy registered. "
                        + "School overrides: {} | Disabled spells: {}",
                MagoSpellPolicy.getSchoolOverrideCount(),
                MagoSpellPolicy.getDisabledSpellCount()
        );
    }


    private static void onModifyDefaultConfigValues(
            ModifyDefaultConfigValuesEvent event
    ) {

        String spellId =
                event.getSpell().getSpellId();


        ResourceLocation targetSchoolId =
                MagoSpellPolicy.getTargetSchool(
                        spellId
                );


        if (targetSchoolId != null) {

            SchoolType targetSchool =
                    SchoolRegistry.getSchool(
                            targetSchoolId
                    );


            if (targetSchool == null) {

                throw new IllegalStateException(
                        "[MagoCompat] Target spell school does not exist. "
                                + "Spell: "
                                + spellId
                                + " | Target: "
                                + targetSchoolId
                );
            }


            event.setDefaultValue(
                    SpellConfigParameter.SCHOOL,
                    targetSchool
            );
        }


        if (
                MagoSpellPolicy.isDisabled(
                        spellId
                )
        ) {

            event.setDefaultValue(
                    SpellConfigParameter.ENABLED,
                    false
            );

            event.setDefaultValue(
                    SpellConfigParameter.ALLOW_CRAFTING,
                    false
            );
        }
    }


    private static void onSpellPreCast(
            SpellPreCastEvent event
    ) {

        /*
         * Existing deleted-spell hard block.
         */
        if (
                MagoSpellPolicy.isDisabled(
                        event.getSpellId()
                )
        ) {

            cancelCastAndResetClient(event);
            return;
        }


        /*
         * Mago basic Mage school / rarity gate.
         */
        var gateResult =
                MagoMageGatePolicy.check(
                        event.getEntity(),
                        event.getSpellId(),
                        event.getSpellLevel(),
                        event.getSchoolType()
                );


        if (gateResult.allowed()) {
            return;
        }


        cancelCastAndResetClient(event);


        /*
         * Action-bar feedback only from the server side.
         */
        if (
                !event.getEntity()
                        .level()
                        .isClientSide()
        ) {

            event.getEntity()
                    .displayClientMessage(
                            gateResult.message(),
                            true
                    );
        }
    }


    /**
     * Cancel the actual server-side cast and also execute
     * Iron's normal client-side cancellation cleanup.
     *
     * Targeted spells may already have sent SyncTargetingDataPacket
     * before SpellPreCastEvent is denied. If we only cancel the event,
     * ClientMagicData.spellTargetingData remains and the target marker
     * can stay rendered indefinitely.
     *
     * OnCastFinishedPacket(cancelled=true) calls
     * ClientMagicData.resetClientCastState(), which also clears
     * targeting data and stops the item-use/cast state.
     */
    private static void cancelCastAndResetClient(
            SpellPreCastEvent event
    ) {

        event.setCanceled(true);


        if (
                event.getEntity()
                        instanceof ServerPlayer serverPlayer
        ) {

            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new OnCastFinishedPacket(
                            serverPlayer.getUUID(),
                            event.getSpellId(),
                            true
                    )
            );
        }
    }
}