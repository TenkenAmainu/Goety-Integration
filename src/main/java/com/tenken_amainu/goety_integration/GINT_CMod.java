package com.tenken_amainu.goety_integration;

import com.tenken_amainu.goety_integration.client.gui.PatchouliFocusScreen;
import com.tenken_amainu.goety_integration.client.model.*;
import com.tenken_amainu.goety_integration.client.renderer.InvokerBossBarRenderer;
import com.tenken_amainu.goety_integration.client.renderer.MagiBrainRenderer;
import com.tenken_amainu.goety_integration.client.renderer.SpellerRobeRenderer;
import com.tenken_amainu.goety_integration.client.renderer.EmptyProjectileRenderer;
import com.tenken_amainu.goety_integration.client.renderer.entity.*;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderPoisonBall;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = GINTMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GINT_CMod {

    public static final DeferredRegister<ParticleType<?>> PARTICLE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, GINTMod.MODID);


    // ========== 模型层定义 ==========
    public static final ModelLayerLocation WARPED_SPEAR_LAYER = location("warped_spear");
    public static final ModelLayerLocation BOUND_INVOKER_LAYER = location("bound_invoker");
    public static final ModelLayerLocation ALCHEMIST_SERVANT_LAYER = location("alchemist_servant");
    public static final ModelLayerLocation SPELLER_ROBE_LAYER = new ModelLayerLocation(
            new ResourceLocation(GINTMod.MODID, "speller_robe"), "main");
    public static final ModelLayerLocation MAGI_BRAIN_LAYER = new ModelLayerLocation(
            new ResourceLocation(GINTMod.MODID, "magi_brain"), "main");

    public static final ModelLayerLocation SORCERER_SERVANT_LAYER =
            new ModelLayerLocation(new ResourceLocation("goety_integration", "sorcerer_servant"), "main");

    private static ModelLayerLocation location(String path) {
        return new ModelLayerLocation(new ResourceLocation(GINTMod.MODID, path), "main");
    }

    // ========== 客户端初始化 ==========
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            GINTMod.LOGGER.info("[客户端] 注册 Patchouli 屏幕...");
            event.enqueueWork(() -> {
                MenuScreens.register(GINTMod.PATCHOULI_FOCUS_MENU.get(), PatchouliFocusScreen::new);
            });
            CuriosRendererRegistry.register(GINTMod.SPELLER_ROBE.get(), () -> new SpellerRobeRenderer());
            CuriosRendererRegistry.register(GINTMod.MAGI_BRAIN.get(), () -> new MagiBrainRenderer());
            EntityRenderers.register(GINTMod.CUSTOM_EARTHQUAKE.get(), EmptyProjectileRenderer::new);
        });
        MinecraftForge.EVENT_BUS.register(InvokerBossBarRenderer.class);
    }

    // ========== 模型层定义注册 ==========
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        GINTMod.LOGGER.info("[客户端] 注册模型层定义...");
        event.registerLayerDefinition(WARPED_SPEAR_LAYER, WarpedSpearEntityModel::createBodyLayer);
        event.registerLayerDefinition(BOUND_INVOKER_LAYER, BoundInvokerModel::createBodyLayer);
        event.registerLayerDefinition(IllashooterServantModel.LAYER_LOCATION, IllashooterServantModel::createBodyLayer);
        event.registerLayerDefinition(DispenserServantModel.LAYER_LOCATION, DispenserServantModel::createBodyLayer);
        event.registerLayerDefinition(SPELLER_ROBE_LAYER, SpellerRobeModel::createBodyLayer);
        event.registerLayerDefinition(MAGI_BRAIN_LAYER, MagiBrainModel::createBodyLayer);
        event.registerLayerDefinition(ALCHEMIST_SERVANT_LAYER, AlchemistServantModel::createBodyLayer);
        event.registerLayerDefinition(SORCERER_SERVANT_LAYER, HatIllagerServantModel::createBodyLayer);
    }

    // 粒子
    // ========== 方块实体渲染器注册 ==========
    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        GINTMod.LOGGER.info("[客户端] 注册方块实体渲染器...");
        event.registerBlockEntityRenderer(GINTMod.FORGE_TRIAL_DOOR_BE.get(), context -> new GeoBlockRenderer<>(new ForgeTrialDoorModel()));
    }
    // ========== 实体渲染器注册 ==========
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        GINTMod.LOGGER.info("[客户端] 注册实体渲染器...");
        event.registerEntityRenderer(GINTMod.MARAUDER_SERVANT.get(), MarauderServantRenderer::new);
        event.registerEntityRenderer(GINTMod.INQUISITOR_SERVANT.get(), InquisitorServantRenderer::new);
        event.registerEntityRenderer(GINTMod.ALLY_CRASHAGER.get(), AllyCrashagerRenderer::new);
        event.registerEntityRenderer(GINTMod.PROVOKER_SERVANT.get(), ProvokerServantRenderer::new);
        event.registerEntityRenderer(GINTMod.BASHER_SERVANT.get(), BasherServantRenderer::new);
        event.registerEntityRenderer(GINTMod.WARPED_SPEAR_ENTITY.get(), WarpedSpearRenderer::new);
        event.registerEntityRenderer(GINTMod.BOUND_INVOKER.get(), BoundInvokerRenderer::new);
        event.registerEntityRenderer(GINTMod.ALCHEMIST_SERVANT.get(), AlchemistServantRender::new);
        event.registerEntityRenderer(GINTMod.TREACHEROUS_SHOTGUN_PELLET.get(), TreacherousShotgunPelletRenderer::new);
        event.registerEntityRenderer(GINTMod.ANCIENT_HOGLIN.get(), AncientHoglinRenderer::new);
        event.registerEntityRenderer(GINTMod.ILLASHOOTER_SERVANT.get(), IllashooterServantRenderer::new);
        event.registerEntityRenderer(GINTMod.DISPENSER_SERVANT.get(), DispenserServantRenderer::new);
        event.registerEntityRenderer(GINTMod.WROUGHT_HATCHET_ENTITY_TYPE.get(), WroughtHatchetRenderer::new);
        event.registerEntityRenderer(GINTMod.SPIT_POISON_BALL.get(), RenderPoisonBall::new);
        event.registerEntityRenderer(GINTMod.BURNING_HOGLIN_LORD.get(), RenderBurningHoglinLord::new);
        event.registerEntityRenderer(GINTMod.ALLY_SKULL_BOLT.get(), AllySkullBoltRender::new);
        event.registerEntityRenderer(GINTMod.SORCERER_SERVANT.get(), SorcererServantRenderer::new);
    }
}