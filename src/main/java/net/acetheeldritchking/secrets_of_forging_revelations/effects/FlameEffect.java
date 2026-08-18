package net.acetheeldritchking.secrets_of_forging_revelations.effects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

/**
 * Sets the target alight when a modular item carrying the infernal effect hits it.
 *
 * The damage event is split into a pre and post pair now. This hangs off Post, because it reacts to
 * a hit having landed rather than changing the damage itself.
 */
public class FlameEffect {
    private static final ItemEffect infernal = ItemEffect.get("secrets_of_forging_revelations:infernal");

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent.Post event) {
        LivingEntity defender = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (!(source instanceof LivingEntity attacker) || attacker.level().isClientSide()) {
            return;
        }

        ItemStack heldStack = attacker.getMainHandItem();
        if (!(heldStack.getItem() instanceof ModularItem item)) {
            return;
        }

        int level = item.getEffectLevel(heldStack, infernal);
        if (level > 0) {
            defender.igniteForSeconds(level);
        }
    }
}
