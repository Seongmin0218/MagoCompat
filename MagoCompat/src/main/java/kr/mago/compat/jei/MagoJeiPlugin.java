package kr.mago.compat.jei;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import kr.mago.compat.MagoCompat;
import kr.mago.compat.spell.MagoSpellPolicy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Final JEI visibility safety net for Mago-retired spells.
 *
 * Iron's Spells already filters disabled spells from its own JEI recipes,
 * creative scroll tab, Scroll Forge, Arcane Anvil, Alchemist Cauldron,
 * and normal random spell loot.
 *
 * This plugin additionally removes every disabled spell-scroll subtype
 * after all JEI plugins have registered, protecting against addon plugins
 * that register disabled spell scrolls directly.
 */
@JeiPlugin
public final class MagoJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID =
            ResourceLocation.fromNamespaceAndPath(
                    MagoCompat.MOD_ID,
                    "spell_visibility"
            );

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        List<ItemStack> hiddenScrollVariants = new ArrayList<>();
        int disabledSpellCount = 0;

        for (var spell : SpellRegistry.REGISTRY) {
            if (spell == SpellRegistry.none()) {
                continue;
            }

            String spellId = spell.getSpellId();

            if (!MagoSpellPolicy.isDisabled(spellId)) {
                continue;
            }

            disabledSpellCount++;

            int minLevel = Math.max(1, spell.getMinLevel());
            int maxLevel = spell.getMaxLevel();

            for (int level = minLevel; level <= maxLevel; level++) {
                ItemStack scrollStack =
                        new ItemStack(ItemRegistry.SCROLL.get());

                ISpellContainer.createScrollContainer(
                        spell,
                        level,
                        scrollStack
                );

                hiddenScrollVariants.add(scrollStack);
            }
        }

        if (!hiddenScrollVariants.isEmpty()) {
            jeiRuntime.getIngredientManager()
                    .removeIngredientsAtRuntime(
                            VanillaTypes.ITEM_STACK,
                            hiddenScrollVariants
                    );
        }

        MagoCompat.LOGGER.info(
                "[MagoCompat] JEI disabled-spell visibility filter active. "
                        + "Disabled spells: {} | Scroll variants removed: {}",
                disabledSpellCount,
                hiddenScrollVariants.size()
        );
    }
}
