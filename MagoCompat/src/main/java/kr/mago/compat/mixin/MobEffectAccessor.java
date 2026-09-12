package kr.mago.compat.mixin;

import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MobEffect.class)
public interface MobEffectAccessor {

    /**
     * Accesses MobEffect's internal attribute modifier template map.
     *
     * Value type is intentionally opaque because
     * MobEffect.AttributeTemplate is not part of the public API.
     */
    @Accessor("attributeModifiers")
    Map<?, ?> mago$getAttributeModifiers();
}