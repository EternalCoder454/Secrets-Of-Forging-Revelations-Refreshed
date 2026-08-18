package net.acetheeldritchking.secrets_of_forging_revelations;

import net.acetheeldritchking.secrets_of_forging_revelations.effects.BlizzardEffect;
import net.acetheeldritchking.secrets_of_forging_revelations.effects.FlameEffect;
import net.acetheeldritchking.secrets_of_forging_revelations.effects.FreezingEffect;
import net.acetheeldritchking.secrets_of_forging_revelations.effects.potion.PotionEffects;
import net.acetheeldritchking.secrets_of_forging_revelations.item.ModularPolearm;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * Secrets of Forging: Revelations, by AceTheEldritchKing, updated by GamerK_2.
 *
 * Ported to 26.1.2 NeoForge. The mod loading context is gone, so the buses arrive as constructor
 * arguments, and the effect handlers register against the game bus rather than a static singleton.
 */
@Mod(SecretsOfForgingRevelations.MOD_ID)
public class SecretsOfForgingRevelations {
    public static final String MOD_ID = "secrets_of_forging_revelations";

    public SecretsOfForgingRevelations(IEventBus modEventBus) {
        SoFrRegistry.init(modEventBus);
        PotionEffects.register(modEventBus);

        modEventBus.addListener(this::onBuildContents);

        NeoForge.EVENT_BUS.register(new FreezingEffect());
        NeoForge.EVENT_BUS.register(new FlameEffect());
        NeoForge.EVENT_BUS.register(new BlizzardEffect());
    }

    private void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModularPolearm.setupPolearm());
        }
    }
}
