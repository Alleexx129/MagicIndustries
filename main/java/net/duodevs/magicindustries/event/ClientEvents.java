package net.duodevs.magicindustries.event;

import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.init.ModContent;
import net.duodevs.magicindustries.network.ClientManaData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ClientEvents {
    private static final ResourceLocation MANA_EMPTY = new ResourceLocation(MagicIndustries.MODID, "textures/gui/mana_bar_empty.png");
    private static final ResourceLocation MANA_FULL = new ResourceLocation(MagicIndustries.MODID, "textures/gui/mana_bar_full.png");
    private static final ResourceLocation MANA_OVERLAY = new ResourceLocation(MagicIndustries.MODID, "textures/misc/in_mana_water.png");

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        for (Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
            if (item.getRegistryName() != null && MagicIndustries.MODID.equals(item.getRegistryName().getNamespace())) {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        }
        if (ModContent.MANA_WATER_BLOCK != null) {
            ModelLoader.setCustomStateMapper(ModContent.MANA_WATER_BLOCK, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return new ModelResourceLocation(new ResourceLocation(MagicIndustries.MODID, "mana_water_block"), "normal");
                }
            });
        }
    }

    @SubscribeEvent
    public void renderMana(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.gameSettings.hideGUI) return;
        int mana = ClientManaData.getMana();
        if (mana <= 0) return;

        int x = event.getResolution().getScaledWidth() / 2;
        int y = event.getResolution().getScaledHeight();
        int offset = -48;
        if (mc.player.getTotalArmorValue() > 0) offset -= 10;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableDepth();
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(MANA_EMPTY);
        Gui.drawModalRectWithCustomSizedTexture(x - 91, y + offset, 0, 0, 81, 8, 81, 11);
        int width = (int) (81F * (mana / 100F));
        mc.getTextureManager().bindTexture(MANA_FULL);
        Gui.drawModalRectWithCustomSizedTexture(x - 91, y + offset, 0, 0, width, 8, 81, 11);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
    }

    private boolean inMana(Entity entity) {
        if (entity == null || entity.world == null || ModContent.MANA_WATER_BLOCK == null) return false;
        BlockPos eye = new BlockPos(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        return entity.world.getBlockState(eye).getBlock() == ModContent.MANA_WATER_BLOCK;
    }

    @SubscribeEvent
    public void fogColors(EntityViewRenderEvent.FogColors event) {
        if (inMana(event.getEntity())) {
            event.setRed(0.8784314F);
            event.setGreen(0.21960784F);
            event.setBlue(0.8156863F);
        }
    }

    @SubscribeEvent
    public void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (inMana(event.getEntity())) {
            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            GlStateManager.setFogStart(1F);
            GlStateManager.setFogEnd(6F);
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public void manaOverlay(RenderBlockOverlayEvent event) {
        if (event.getOverlayType() != RenderBlockOverlayEvent.OverlayType.WATER || !inMana(event.getPlayer())) return;
        event.setCanceled(true);
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(MANA_OVERLAY);

        // Vanilla 1.12 underwater overlay geometry, retaining the source mod's custom texture.
        float brightness = mc.player.getBrightness();
        GlStateManager.color(brightness, brightness, brightness, 0.5F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        float scale = 4.0F;
        float u = -mc.player.rotationYaw / 64.0F;
        float v = mc.player.rotationPitch / 64.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-1.0D, -1.0D, -0.5D).tex(scale + u, scale + v).endVertex();
        buffer.pos( 1.0D, -1.0D, -0.5D).tex(0.0F + u, scale + v).endVertex();
        buffer.pos( 1.0D,  1.0D, -0.5D).tex(0.0F + u, 0.0F + v).endVertex();
        buffer.pos(-1.0D,  1.0D, -0.5D).tex(scale + u, 0.0F + v).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}
