package net.acetheeldritchking.secrets_of_forging_revelations;

import net.acetheeldritchking.secrets_of_forging_revelations.effects.BlizzardEffect;
import net.acetheeldritchking.secrets_of_forging_revelations.effects.FlameEffect;
import net.acetheeldritchking.secrets_of_forging_revelations.effects.FreezingEffect;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Adds this mod's stat bars to Tetra's workbench and holosphere.
 */
@EventBusSubscriber(modid = SecretsOfForgingRevelations.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            FreezingEffect.init();
            FlameEffect.init();
            BlizzardEffect.init();
        });
    }
}
