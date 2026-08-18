package net.acetheeldritchking.secrets_of_forging_revelations.effects;

import net.acetheeldritchking.secrets_of_forging_revelations.effects.potion.PotionEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterDecimal;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class BlizzardEffect {
    private static final ItemEffect blizzardEffect = ItemEffect.get("secrets_of_forging_revelations:blizzard");

    @OnlyIn(Dist.CLIENT)
    public static void init()
    {
        final IStatGetter effectStatGetter = new StatGetterEffectLevel(blizzardEffect, 1);
        final GuiStatBar effectBar = new GuiStatBar
                (0, 0, barLength, "secrets_of_forging_revelations.effect.blizzard.name", 0, 30, false, effectStatGetter,
                        LabelGetterBasic.decimalLabel, new TooltipGetterDecimal
                        ("secrets_of_forging_revelations.effect.blizzard.tooltip", effectStatGetter));

        WorkbenchStatsGui.addBar(effectBar);
        HoloStatsGui.addBar(effectBar);
    }

    @SubscribeEvent
    public void onPlayerTickEvent(PlayerTickEvent.Post event)
    {
        Player player = event.getEntity();

        ItemStack heldStack = player.getMainHandItem();

        // Every second
        if (player.tickCount % 20 == 0 && heldStack.getItem() instanceof ModularItem item)
        {
            // Potency of effects
            int level = item.getEffectLevel(heldStack, blizzardEffect);

            // Duration of effects
            int eff = (int) item.getEffectEfficiency(heldStack, blizzardEffect);

            // Block pos
            BlockPos pos = player.blockPosition();

            // Biome temperature
            boolean coldEnoughToSnow = player.level().getBiome(pos).value().coldEnoughToSnow(pos, player.level().getSeaLevel());
            boolean tooWarm = player.level().getBiome(pos).value().warmEnoughToRain(pos, player.level().getSeaLevel());

            if (level > 0 && !player.level().isClientSide())
            {
                if (coldEnoughToSnow)
                {
                    grantBuffs(player, eff, level);
                }
                else if (tooWarm)
                {
                    grantDebuffs(player, eff, level);
                }

                // Immune to freezing
                if (player.hasEffect(PotionEffects.FREEZING))
                {
                    player.removeEffect(PotionEffects.FREEZING);
                }
            }
        }
    }

    private void grantBuffs(Player player, int duration, int level)
    {
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration*20,
                level - 1, true, true, true));
        player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, duration*20,
                level - 1, true, true, true));
        player.addEffect(new MobEffectInstance(MobEffects.SPEED, duration*20,
                level - 1, true, true, true));
    }

    private void grantDebuffs(Player player, int duration, int level)
    {
        player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration*20,
                level, true, true, true));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration*20,
                level, true, true, true));
        player.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, duration*20,
                level, true, true, true));
    }
}
