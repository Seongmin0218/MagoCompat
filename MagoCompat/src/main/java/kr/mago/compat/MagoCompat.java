package kr.mago.compat;

import com.mojang.logging.LogUtils;
import kr.mago.compat.archer.MagoArcherPolicy;
import kr.mago.compat.celestisynth.MagoCelestisynthGate;
import kr.mago.compat.effect.MagoMobEffectPatches;
import kr.mago.compat.item.MagoCurioAttributeEvents;
import kr.mago.compat.item.MagoItemAttributeEvents;
import kr.mago.compat.item.MagoRetiredItemPolicy;
import kr.mago.compat.spell.MagoSpellEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(MagoCompat.MOD_ID)
public final class MagoCompat {

    public static final String MOD_ID = "mago_compat";
    public static final Logger LOGGER = LogUtils.getLogger();


    public MagoCompat(IEventBus modEventBus) {

        MagoCurioAttributeEvents.register();

        MagoItemAttributeEvents.register();

        MagoRetiredItemPolicy.register(modEventBus);

        MagoArcherPolicy.register(modEventBus);

        MagoMobEffectPatches.register(modEventBus);

        MagoSpellEvents.register();

        MagoCelestisynthGate.register();

        LOGGER.info(
                "[MagoCompat] Mago compatibility patches loaded."
        );
    }
}