package kr.mago.compat.effect;

import kr.mago.compat.MagoCompat;
import kr.mago.compat.mixin.MobEffectAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Map;

public final class MagoMobEffectPatches {

    // =========================================================
    // Effects
    // =========================================================

    private static final ResourceLocation SUBMERGED_EFFECT =
            id(
                    "somakespells",
                    "submerged"
            );


    // =========================================================
    // Attributes
    // =========================================================

    private static final ResourceLocation AQUA_SPELL_POWER =
            id(
                    "somakespells",
                    "aqua_spell_power"
            );

    private static final ResourceLocation HYDRO_SPELL_POWER =
            id(
                    "aces_spell_utils",
                    "hydro_spell_power"
            );


    private MagoMobEffectPatches() {
    }


    public static void register(
            IEventBus modEventBus
    ) {
        modEventBus.addListener(
                MagoMobEffectPatches::onCommonSetup
        );
    }


    private static void onCommonSetup(
            FMLCommonSetupEvent event
    ) {
        event.enqueueWork(
                MagoMobEffectPatches::patchSubmerged
        );
    }


    /**
     * Somake Submerged
     *
     * Original:
     * Aqua Spell Power bonus
     *
     * Mago:
     * Same exact modifier template
     * Aqua Spell Power -> Hydro Spell Power
     *
     * The original:
     * - amount
     * - operation
     * - amplifier scaling
     * - modifier ID
     *
     * are preserved.
     */
    @SuppressWarnings("unchecked")
    private static void patchSubmerged() {
        MobEffect submerged =
                BuiltInRegistries.MOB_EFFECT.get(
                        SUBMERGED_EFFECT
                );

        if (submerged == null) {
            MagoCompat.LOGGER.warn(
                    "[MagoCompat] Submerged effect not found: {}",
                    SUBMERGED_EFFECT
            );

            return;
        }


        Attribute aquaAttribute =
                BuiltInRegistries.ATTRIBUTE.get(
                        AQUA_SPELL_POWER
                );

        if (aquaAttribute == null) {
            MagoCompat.LOGGER.warn(
                    "[MagoCompat] Aqua Spell Power attribute not found: {}",
                    AQUA_SPELL_POWER
            );

            return;
        }


        Attribute hydroAttribute =
                BuiltInRegistries.ATTRIBUTE.get(
                        HYDRO_SPELL_POWER
                );

        if (hydroAttribute == null) {
            throw new IllegalStateException(
                    "[MagoCompat] Missing required Hydro attribute: "
                            + HYDRO_SPELL_POWER
            );
        }


        Holder<Attribute> aquaHolder =
                BuiltInRegistries.ATTRIBUTE.wrapAsHolder(
                        aquaAttribute
                );

        Holder<Attribute> hydroHolder =
                BuiltInRegistries.ATTRIBUTE.wrapAsHolder(
                        hydroAttribute
                );


        Map<?, ?> rawModifiers =
                ((MobEffectAccessor) (Object) submerged)
                        .mago$getAttributeModifiers();

        Map<Holder<Attribute>, Object> modifiers =
                (Map<Holder<Attribute>, Object>)
                        (Map<?, ?>) rawModifiers;


        /*
         * If Hydro is already present, do not overwrite its
         * template. Remove the legacy Aqua entry only.
         */
        if (modifiers.containsKey(hydroHolder)) {

            Object removed =
                    modifiers.remove(
                            aquaHolder
                    );

            MagoCompat.LOGGER.info(
                    "[MagoCompat] Submerged already contains Hydro Spell Power. "
                            + "Legacy Aqua modifier removed: {}",
                    removed != null
            );

            return;
        }


        /*
         * Move the actual modifier template.
         *
         * We deliberately do not recreate it because the template
         * may contain amplifier-dependent scaling.
         */
        Object aquaTemplate =
                modifiers.remove(
                        aquaHolder
                );

        if (aquaTemplate == null) {
            MagoCompat.LOGGER.warn(
                    "[MagoCompat] Submerged effect was found, "
                            + "but it has no Aqua Spell Power modifier to migrate."
            );

            return;
        }


        modifiers.put(
                hydroHolder,
                aquaTemplate
        );


        MagoCompat.LOGGER.info(
                "[MagoCompat] Submerged effect patched. "
                        + "Aqua Spell Power -> Hydro Spell Power | "
                        + "Original modifier template preserved."
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