package net.acetheeldritchking.secrets_of_forging_revelations.effects;

import net.acetheeldritchking.secrets_of_forging_revelations.effects.potion.PotionEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

/**
 * Applies the freezing effect on hit, and stacks it up to the item's efficiency.
 */
public class FreezingEffect {
    private static final ItemEffect freezing = ItemEffect.get("secrets_of_forging_revelations:freezing");

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent.Post event) {
        LivingEntity defender = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (!(source instanceof LivingEntity attacker)) {
            return;
        }

        ItemStack heldStack = attacker.getMainHandItem();
        if (!(heldStack.getItem() instanceof ModularItem item)) {
            return;
        }

        int level = item.getEffectLevel(heldStack, freezing);
        if (level <= 0) {
            return;
        }

        double efficiency = item.getEffectEfficiency(heldStack, freezing);
        MobEffectInstance current = PotionEffects.getPotionEffect(defender.getActiveEffects(), PotionEffects.FREEZING);

        if (current == null) {
            defender.addEffect(new MobEffectInstance(PotionEffects.FREEZING, level * 20, 0, false, false, false));
            return;
        }

        // Already frozen, so deepen it until the item's efficiency says to stop.
        if (efficiency > 0 && current.getAmplifier() < efficiency - 1) {
            current.update(new MobEffectInstance(PotionEffects.FREEZING, current.getDuration() + level * 10,
                    current.getAmplifier() + 1, false, false, false));
        }
    }
}
