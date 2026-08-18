package net.acetheeldritchking.secrets_of_forging_revelations;

import net.acetheeldritchking.secrets_of_forging_revelations.item.ModularPolearm;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import se.mickelus.tetra.TetraMod;

/**
 * The polearm registers into Tetra's namespace rather than this mod's, which is how the original
 * was written and why all of its data lives under data/tetra.
 *
 * DeferredRegister.Items rather than a plain register, because an item's properties carry its
 * registry id now and only the item specific register sets it.
 */
public class SoFrRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TetraMod.MOD_ID);

    public static DeferredHolder<Item, ModularPolearm> polearm;

    public static void init(IEventBus bus) {
        polearm = ITEMS.registerItem(ModularPolearm.identifier, properties -> {
            ModularPolearm item = new ModularPolearm(properties);
            ModularPolearm.instance = item;
            return item;
        });

        ITEMS.register(bus);
    }
}
