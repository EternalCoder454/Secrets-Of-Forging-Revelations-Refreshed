package net.acetheeldritchking.secrets_of_forging_revelations;

import net.acetheeldritchking.secrets_of_forging_revelations.effects.BlizzardEffect;
import net.acetheeldritchking.secrets_of_forging_revelations.effects.FlameEffect;
import net.acetheeldritchking.secrets_of_forging_revelations.effects.FreezingEffect;
import net.acetheeldritchking.secrets_of_forging_revelations.effects.potion.PotionEffects;
import net.acetheeldritchking.secrets_of_forging_revelations.item.ModularPolearm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import se.mickelus.tetra.TetraMod;

/**
 * Secrets of Forging: Revelations, by AceTheEldritchKing, updated by GamerK_2.
 *
 * Ported to 26.1.2 NeoForge. The mod loading context is gone, so the buses arrive as constructor
 * arguments, and the effect handlers register against the game bus rather than a static singleton.
 *
 * Bundled inside Tetra Refreshed with jarJar rather than folded into it, so this stays its own mod
 * with its own id. Everything it adds to Tetra's own screens is reached from this side: the polearm
 * joins Tetra's creative tab, and the holosphere entry and the effect stat bars ship as data under
 * assets/tetra, which is where Tetra's stores look.
 */
@Mod(SecretsOfForgingRevelations.MOD_ID)
public class SecretsOfForgingRevelations {
    public static final String MOD_ID = "secrets_of_forging_revelations";

    /**
     * Tetra's own tab, which its registry names "default". Built by id rather than read off
     * TetraRegistries, whose holder for it is private.
     */
    private static final ResourceKey<CreativeModeTab> tetraTab = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(TetraMod.MOD_ID, "default"));

    public SecretsOfForgingRevelations(IEventBus modEventBus) {
        SoFrRegistry.init(modEventBus);
        PotionEffects.register(modEventBus);

        modEventBus.addListener(this::onBuildContents);

        NeoForge.EVENT_BUS.register(new FreezingEffect());
        NeoForge.EVENT_BUS.register(new FlameEffect());
        NeoForge.EVENT_BUS.register(new BlizzardEffect());
    }

    /**
     * The polearm used to land in vanilla's Combat tab, where nothing named it and nothing listed it
     * as craftable, which is why players did not find it. It belongs with the other modular items.
     */
    private void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (tetraTab.equals(event.getTabKey())) {
            event.accept(ModularPolearm.setupPolearm());
        }
    }
}
