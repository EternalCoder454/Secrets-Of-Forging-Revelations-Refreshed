package net.acetheeldritchking.secrets_of_forging_revelations.effects.potion;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.acetheeldritchking.secrets_of_forging_revelations.SecretsOfForgingRevelations;

/**
 * Slows and chills whatever it is applied to.
 *
 * Attribute modifiers are keyed by an identifier rather than a uuid string now, and the operation
 * names changed, so the old ADDITION is ADD_VALUE and MULTIPLY_TOTAL is ADD_MULTIPLIED_TOTAL. The
 * tick method takes the level and reports whether the effect should continue.
 */
public class FreezingPotionEffect extends MobEffect {
    private static final Identifier speedModifier =
            Identifier.fromNamespaceAndPath(SecretsOfForgingRevelations.MOD_ID, "freezing_movement_speed");
    private static final Identifier attackModifier =
            Identifier.fromNamespaceAndPath(SecretsOfForgingRevelations.MOD_ID, "freezing_attack_speed");
    private static final Identifier knockbackModifier =
            Identifier.fromNamespaceAndPath(SecretsOfForgingRevelations.MOD_ID, "freezing_knockback_resistance");

    public FreezingPotionEffect() {
        super(MobEffectCategory.HARMFUL, 0xeeeeee);

        addAttributeModifier(Attributes.MOVEMENT_SPEED, speedModifier, -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED, attackModifier, -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, knockbackModifier, 0.1, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        entity.setIsInPowderSnow(true);
        entity.setTicksFrozen(140);

        int particles = Math.min(amplifier, 10) * 2;
        level.sendParticles(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getY(), entity.getZ(), particles, 0.1, 0.1, 0.1, 0.01);

        return super.applyEffectTick(level, entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return true;
    }
}
