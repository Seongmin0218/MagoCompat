package kr.mago.compat;

import com.mojang.logging.LogUtils;
import kr.mago.compat.item.MagoCurioAttributeEvents;
import kr.mago.compat.item.MagoItemAttributeEvents;
import kr.mago.compat.spell.MagoSpellEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import org.slf4j.Logger;
import kr.mago.compat.item.MagoRetiredItemPolicy;
import kr.mago.compat.effect.MagoMobEffectPatches;

@Mod(MagoCompat.MOD_ID)
public final class MagoCompat {

    public static final String MOD_ID = "mago_compat";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final String MECHANIZED_WRAITHBLADE_CLASS =
            "com.gametechbc.traveloptics.item.bossweapon.mechanized_wraithblade.MechanizedWraithbladeItem";

    private static final ResourceLocation TECHNOMANCY_SPELL_POWER_ID =
            ResourceLocation.fromNamespaceAndPath(
                    "cataclysm_spellbooks",
                    "technomancy_spell_power"
            );

    private static final ResourceLocation WRAITHBLADE_TECHNOMANCY_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(
                    MOD_ID,
                    "mechanized_wraithblade_technomancy_spell_power"
            );

    private static boolean loggedWraithbladeAttributePatch = false;


    public MagoCompat(IEventBus modEventBus) {

        NeoForge.EVENT_BUS.addListener(
                MagoCompat::onItemAttributeModifiers
        );

        MagoCurioAttributeEvents.register();

        MagoItemAttributeEvents.register();

        MagoRetiredItemPolicy.register(modEventBus);

        MagoMobEffectPatches.register(modEventBus);

        MagoSpellEvents.register();

        LOGGER.info(
                "[MagoCompat] Mago compatibility patches loaded."
        );
    }


    /**
     * Adds Mago's Technomancy bonus to the Mechanized Wraithblade.
     *
     * +20% Technomancy Spell Power while held in the main hand.
     */
    private static void onItemAttributeModifiers(
            ItemAttributeModifierEvent event
    ) {
        String itemClassName =
                event.getItemStack()
                        .getItem()
                        .getClass()
                        .getName();

        if (!MECHANIZED_WRAITHBLADE_CLASS.equals(itemClassName)) {
            return;
        }

        Attribute technomancySpellPower =
                BuiltInRegistries.ATTRIBUTE.get(
                        TECHNOMANCY_SPELL_POWER_ID
                );

        if (technomancySpellPower == null) {
            throw new IllegalStateException(
                    "[MagoCompat] Missing required attribute: "
                            + TECHNOMANCY_SPELL_POWER_ID
            );
        }

        Holder<Attribute> technomancySpellPowerHolder =
                BuiltInRegistries.ATTRIBUTE.wrapAsHolder(
                        technomancySpellPower
                );

        AttributeModifier modifier =
                new AttributeModifier(
                        WRAITHBLADE_TECHNOMANCY_MODIFIER_ID,
                        0.20D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                );

        boolean added =
                event.addModifier(
                        technomancySpellPowerHolder,
                        modifier,
                        EquipmentSlotGroup.MAINHAND
                );

        if (added && !loggedWraithbladeAttributePatch) {
            loggedWraithbladeAttributePatch = true;

            ResourceLocation itemId =
                    BuiltInRegistries.ITEM.getKey(
                            event.getItemStack().getItem()
                    );

            LOGGER.info(
                    "[MagoCompat] Mechanized Wraithblade Technomancy bonus active. "
                            + "Item: {} | Attribute: {} | Bonus: +20%",
                    itemId,
                    TECHNOMANCY_SPELL_POWER_ID
            );
        }
    }
}