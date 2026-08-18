package net.acetheeldritchking.secrets_of_forging_revelations.effects.potion;

import net.acetheeldritchking.secrets_of_forging_revelations.SecretsOfForgingRevelations;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.Collection;

public class PotionEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, SecretsOfForgingRevelations.MOD_ID);

    // Stole from Tetrutils with permission from Panda <3
    public static final DeferredHolder<MobEffect, FreezingPotionEffect> FREEZING =
            MOB_EFFECTS.register("freezing", FreezingPotionEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    /**
     * The instance of an effect an entity currently has, or null.
     *
     * An effect instance holds a Holder rather than the effect itself now, so this compares holders.
     * It also takes the live collection rather than a copy of it, since the caller only reads.
     */
    @Nullable
    public static MobEffectInstance getPotionEffect(Collection<MobEffectInstance> effects, Holder<MobEffect> effectType) {
        for (MobEffectInstance effect : effects) {
            if (effect.getEffect().equals(effectType)) {
                return effect;
            }
        }

        return null;
    }
}
