package net.acetheeldritchking.secrets_of_forging_revelations.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;

/**
 * The modular polearm.
 *
 * Ported to 26.1.2. Three things changed shape: properties are handed in by the registry rather
 * than built here, the config values are constants rather than config entries read at construction,
 * and the pose it holds while thrown is declared here rather than mixed into Tetra's renderer.
 */
public class ModularPolearm extends ItemModularHandheld {
    public final static String headKey = "polearm/head";
    public final static String handleKey = "polearm/handle";
    public final static String bindingKey = "polearm/binding";
    public static final String identifier = "modular_polearm";

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(1, -3, -11, 21);
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(-14, 0);

    public static ModularPolearm instance;

    public ModularPolearm(Properties properties) {
        super(properties.stacksTo(1).fireResistant());

        entityHitDamage = 1;

        majorModuleKeys = new String[] { headKey, handleKey };
        minorModuleKeys = new String[] { bindingKey };

        requiredModules = new String[] { headKey, handleKey };

        updateConfig(ConfigHandler.HONE_SINGLE_BASE_DEFAULT, ConfigHandler.HONE_SINGLE_INTEGRITY_MULTIPLIER_DEFAULT);

        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> synergies = DataManager.instance.synergyData.getOrdered("polearm/"));
    }

    @Override
    public String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        if (isThrowing(itemStack, entity)) {
            return super.getModelCacheKey(itemStack, entity) + ":throwing";
        }

        return super.getModelCacheKey(itemStack, entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTransformVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        return isThrowing(itemStack, entity) ? "throwing" : null;
    }

    /**
     * Flies point first and level, rather than tumbling. This used to be a mixin into Tetra's thrown
     * renderer, because the renderer chose the pose itself and there was no way to ask an item for
     * one. Tetra asks the item now, so this is an override.
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyThrownPose(PoseStack poseStack, float yaw, float pitch, float spin, boolean dealtDamage, boolean onGround) {
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch + 135.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        poseStack.translate(0.3, -0.5, 0.0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }

    public static ItemStack setupPolearm() {
        ItemStack itemStack = new ItemStack(instance);

        IModularItem.putModuleInSlot(itemStack, headKey, "polearm/spearhead", "polearm/spearhead_material", "spearhead/iron");
        IModularItem.putModuleInSlot(itemStack, handleKey, "polearm/basic_handle", "polearm/basic_handle_material", "basic_handle/oak");
        IModularItem.updateIdentifier(itemStack);

        return itemStack;
    }
}
