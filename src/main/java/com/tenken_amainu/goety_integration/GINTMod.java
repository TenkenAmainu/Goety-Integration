package com.tenken_amainu.goety_integration;

import com.Polarice3.Goety.api.entities.ally.illager.IllagerType;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.api.ritual.RitualType;
import com.Polarice3.Goety.common.blocks.DarkAltarBlock;
import com.Polarice3.Goety.common.blocks.ModBlocks;
import com.Polarice3.Goety.common.blocks.PedestalBlock;
import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.blocks.entities.PedestalBlockEntity;
import com.Polarice3.Goety.common.entities.neutral.Minion;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.items.ModSpawnEggItem;
import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import com.Polarice3.Goety.common.items.research.Scroll;
import com.Polarice3.Goety.config.AttributesConfig;
import com.Polarice3.goety_spillage.common.items.GSItems;
import com.tenken_amainu.goety_integration.client.gui.PatchouliFocusMenu;
import com.tenken_amainu.goety_integration.common.block.ForgeSpawnerBlock;
import com.tenken_amainu.goety_integration.Handler.TargetSelectionHandler;
import com.tenken_amainu.goety_integration.common.GINTResearchList;
import com.tenken_amainu.goety_integration.common.block.AllyMagicFireBlock;
import com.tenken_amainu.goety_integration.common.block.ForgeTrialDoorBlock;
import com.tenken_amainu.goety_integration.common.block.MagiBookshelfBlock;
import com.tenken_amainu.goety_integration.common.blockentity.*;
import com.tenken_amainu.goety_integration.common.effect.BetrayalHazeEffect;
import com.tenken_amainu.goety_integration.common.effect.DeceivedEffect;
import com.tenken_amainu.goety_integration.common.entities.ally.AllyCrashagerEntity;
import com.tenken_amainu.goety_integration.common.entities.ally.AllyKaboomerEntity;
import com.tenken_amainu.goety_integration.common.entities.ally.illager.*;
import com.tenken_amainu.goety_integration.common.entities.ally.illager.train.GINTIllagerType;
import com.tenken_amainu.goety_integration.common.entities.ally.undead.AllySurrendered;
import com.tenken_amainu.goety_integration.common.entities.ally.undead.bound.BoundInvoker;
import com.tenken_amainu.goety_integration.common.entities.monster.AncientHoglin;
import com.tenken_amainu.goety_integration.common.entities.monster.miniboss.EntityBurningHoglinLord;
import com.tenken_amainu.goety_integration.common.entities.projectiles.*;
import com.tenken_amainu.goety_integration.common.event.CurioAttributeEvents;
import com.tenken_amainu.goety_integration.common.event.MagiBrainEvents;
import com.tenken_amainu.goety_integration.common.event.SpellerRobeEvents;
import com.tenken_amainu.goety_integration.common.item.*;
import com.tenken_amainu.goety_integration.common.recipe.WiccaRecipeLoader;
import com.tenken_amainu.goety_integration.compat.patchouli.PageWiccaRecipe;
import com.tenken_amainu.goety_integration.magic.spells.*;
import com.tenken_amainu.goety_integration.magic.spells.ill.*;
import com.tenken_amainu.goety_integration.magic.spells.integration.*;
import com.tenken_amainu.goety_integration.magic.spells.necromancy.ConjureSkullSpell;
import com.tenken_amainu.goety_integration.magic.spells.soulgather.ShotgunAttackSpell;
import com.tenken_amainu.goety_integration.magic.spells.wicca.MagicalFlamesSpell;
import com.tenken_amainu.goety_integration.magic.spells.wild.NagaPoisonBallSpell;
import com.tenken_amainu.goety_integration.network.OracleNetwork;
import fuzs.illagerinvasion.init.ModRegistry;
import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.Polarice3.Goety.common.items.ServantSpawnEggs.egg;

@Mod(GINTMod.MODID)
public class GINTMod {
    public static final String MODID = "goety_integration";
    public static final Logger LOGGER = LogUtils.getLogger();

    // ========== 注册器 ==========

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GINTMod.MODID);
    public static final Map<ResourceLocation, ModBlocks.BlockLootSetting> BLOCK_LOOT = new HashMap<>();
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GINTMod.MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, GINTMod.MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    // ========== 工具方法 ==========
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }

    //法术类型
    public static SpellType SOULGATHER;
    public static SpellType WICCA;
    public static SpellType INTEGRATION;

    // 注册音效
    public static final RegistryObject<SoundEvent> BURNING_HOGLIN_SOUND = SOUND_EVENTS.register(
            "burning_hoglin_disc",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "burning_hoglin_disc"))
    );
    public static final RegistryObject<SoundEvent> BURNING_HOGLIN_LORD_BOSSMUSIC = SOUND_EVENTS.register(
            "burning_hoglin_lord_bossmusic",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "burning_hoglin_lord_bossmusic"))
    );
    public static final RegistryObject<SoundEvent> BURNING_HOGLIN_LORD_PRELOOPMUSIC = SOUND_EVENTS.register(
            "burning_hoglin_lord_preloopmusic",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "burning_hoglin_lord_preloopmusic"))
    );

    public static final RegistryObject<SoundEvent> INVOKER_SOUND = SOUND_EVENTS.register(
            "invoker_disc",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "invoker_disc"))
    );
    public static final RegistryObject<SoundEvent> INVOKER_BOSSMUSIC = SOUND_EVENTS.register(
            "invoker_bossmusic",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "invoker_bossmusic"))
    );
    public static final RegistryObject<SoundEvent> INVOKER_POSTMUSIC = SOUND_EVENTS.register(
            "invoker_postmusic",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "invoker_postmusic"))
    );

    public static final RegistryObject<SoundEvent> FORGE_SPAWNER_AMBIENT =
            SOUND_EVENTS.register("forge_spawner_ambient",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "forge_spawner_ambient")));

    public static final RegistryObject<SoundEvent> FORGE_SPAWNER_DETECT_PLAYER =
            SOUND_EVENTS.register("forge_spawner_detect_player",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "forge_spawner_detect_player")));

    public static final RegistryObject<SoundEvent> FORGE_SPAWNER_OPEN_SHUTTER =
            SOUND_EVENTS.register("forge_spawner_open_shutter",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "forge_spawner_open_shutter")));

    public static final RegistryObject<SoundEvent> FORGE_SPAWNER_CLOSE_SHUTTER =
            SOUND_EVENTS.register("forge_spawner_close_shutter",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "forge_spawner_close_shutter")));

    public static final RegistryObject<SoundEvent> FORGE_SPAWNER_EJECT_ITEM =
            SOUND_EVENTS.register("forge_spawner_eject_item",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "forge_spawner_eject_item")));

    public static final RegistryObject<SoundEvent> SUMMON_SPELL_FIERY =
            SOUND_EVENTS.register("summon_spell_fiery",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "summon_spell_fiery")));

    public static final RegistryObject<SoundEvent> FORGE_TRIAL_DOOR_OPEN = SOUND_EVENTS.register("forge_trial_door_open",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GINTMod.MODID, "forge_trial_door_open")));

    //方块状态
    public static final EnumProperty<ForgeSpawnerState> FORGE_SPAWNER_STATE =
            EnumProperty.create("state", ForgeSpawnerState.class);
    //方块
    public static final RegistryObject<Block> MAGI_BOOKSHELF = BLOCKS.register("magi_bookshelf",
            MagiBookshelfBlock::new);
    public static final RegistryObject<Item> MAGI_BOOKSHELF_ITEM = ITEMS.register("magi_bookshelf",
            () -> new BlockItem(MAGI_BOOKSHELF.get(), new Item.Properties()));

    public static final RegistryObject<Block> FORGE_SPAWNER = BLOCKS.register("forge_spawner",
            ForgeSpawnerBlock::new);
    public static final RegistryObject<BlockEntityType<ForgeSpawnerBlockEntity>> FORGE_SPAWNER_BE =
            BLOCK_ENTITY.register("forge_spawner",
                    () -> BlockEntityType.Builder.of(ForgeSpawnerBlockEntity::new,
                            FORGE_SPAWNER.get()).build(null));
    public static final RegistryObject<Item> FORGE_SPAWNER_ITEM = ITEMS.register("forge_spawner",
            () -> new BlockItem(FORGE_SPAWNER.get(), new Item.Properties()));

    public static final RegistryObject<Block> ALLY_MAGIC_FIRE = BLOCKS.register("ally_magic_fire",
            () -> new AllyMagicFireBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .instabreak()
                    .lightLevel(state -> 15)
                    .noOcclusion()
                    .sound(SoundType.WOOL)));
    public static final RegistryObject<BlockEntityType<AllyMagicFireBlockEntity>> ALLY_MAGIC_FIRE_BLOCKENTITY = BLOCK_ENTITY.register("ally_magic_fire",
            () -> BlockEntityType.Builder.of(AllyMagicFireBlockEntity::new, ALLY_MAGIC_FIRE.get()).build(null));

    public static final RegistryObject<Block> FORGE_TRIAL_DOOR = BLOCKS.register("forge_trial_door",
            () -> new ForgeTrialDoorBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F) // 不可破坏
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));
    public static final RegistryObject<BlockEntityType<ForgeTrialDoorBlockEntity>> FORGE_TRIAL_DOOR_BE =
            BLOCK_ENTITY.register("forge_trial_door",
                    () -> BlockEntityType.Builder.of(ForgeTrialDoorBlockEntity::new, FORGE_TRIAL_DOOR.get()).build(null));

    //祭坛块
    public static final RegistryObject<Block> DARK_ALTAR_POLISHED_BASALT =
            BLOCKS.register("dark_altar_polished_basalt",
                    () -> new DarkAltarBlock(BlockBehaviour.Properties.copy(JNEBlocks.POLISHED_BASALT_BRICKS.get()).noOcclusion()));
    public static final RegistryObject<Block> DARK_ALTAR_SOUL_SLATE =
            BLOCKS.register("dark_altar_soul_slate",
                    () -> new DarkAltarBlock(BlockBehaviour.Properties.copy(JNEBlocks.SOUL_SLATE_BRICKS.get()).noOcclusion()));
    public static final RegistryObject<Block> DARK_ALTAR_RED_NETHER_BRICK =
            BLOCKS.register("dark_altar_red_nether_brick",
                    () -> new DarkAltarBlock(BlockBehaviour.Properties.copy(Blocks.RED_NETHER_BRICKS).noOcclusion()));
    public static final RegistryObject<Block> DARK_ALTAR_BLUE_NETHER_BRICK =
            BLOCKS.register("dark_altar_blue_nether_brick",
                    () -> new DarkAltarBlock(BlockBehaviour.Properties.copy(JNEBlocks.BLUE_NETHER_BRICKS.get()).noOcclusion()));
    public static final RegistryObject<Block> DARK_ALTAR_NETHER_RACK =
            BLOCKS.register("dark_altar_nether_rack",
                    () -> new DarkAltarBlock(BlockBehaviour.Properties.copy(JNEBlocks.NETHERRACK_BRICKS.get()).noOcclusion()));
    //基座块
    public static final RegistryObject<Block> PEDESTAL_POLISHED_BASALT = BLOCKS.register("pedestal_polished_basalt",
            () -> new PedestalBlock(BlockBehaviour.Properties.copy(JNEBlocks.POLISHED_BASALT_BRICKS.get()).noOcclusion()));
    public static final RegistryObject<Block> PEDESTAL_SOUL_SLATE = BLOCKS.register("pedestal_soul_slate",
            () -> new PedestalBlock(BlockBehaviour.Properties.copy(JNEBlocks.SOUL_SLATE_BRICKS.get()).noOcclusion()));
    public static final RegistryObject<Block> PEDESTAL_RED_NETHER_BRICK = BLOCKS.register("pedestal_red_nether_brick",
            () -> new PedestalBlock(BlockBehaviour.Properties.copy(Blocks.RED_NETHER_BRICKS).noOcclusion()));
    public static final RegistryObject<Block> PEDESTAL_BLUE_NETHER_BRICK = BLOCKS.register("pedestal_blue_nether_brick",
            () -> new PedestalBlock(BlockBehaviour.Properties.copy(JNEBlocks.BLUE_NETHER_BRICKS.get()).noOcclusion()));
    public static final RegistryObject<Block> PEDESTAL_NETHER_RACK = BLOCKS.register("pedestal_nether_rack",
            () -> new PedestalBlock(BlockBehaviour.Properties.copy(JNEBlocks.NETHERRACK_BRICKS.get()).noOcclusion()));
    //块实体
    public static final RegistryObject<BlockEntityType<DarkAltarBlockEntity>> DARK_ALTAR = BLOCK_ENTITY.register("dark_altar",
            () -> BlockEntityType.Builder.of(DarkAltarBlockEntity::new,
                    GINTMod.DARK_ALTAR_POLISHED_BASALT.get(),
                    GINTMod.DARK_ALTAR_SOUL_SLATE.get(), GINTMod.DARK_ALTAR_RED_NETHER_BRICK.get(), GINTMod.DARK_ALTAR_BLUE_NETHER_BRICK.get(),
                    GINTMod.DARK_ALTAR_NETHER_RACK.get()
            ).build(null));
    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITY.register("pedestal",
            () -> BlockEntityType.Builder.of(PedestalBlockEntity::new,
                    GINTMod.PEDESTAL_POLISHED_BASALT.get(),
                    GINTMod.PEDESTAL_SOUL_SLATE.get(), GINTMod.PEDESTAL_RED_NETHER_BRICK.get(), GINTMod.PEDESTAL_BLUE_NETHER_BRICK.get(),
                    GINTMod.PEDESTAL_NETHER_RACK.get()
            ).build(null));
    // ========== 方块物品 (BlockItem) ==========
    // 祭坛块物品
    public static final RegistryObject<Item> DARK_ALTAR_POLISHED_BASALT_ITEM = ITEMS.register(
            "dark_altar_polished_basalt",
            () -> new net.minecraft.world.item.BlockItem(DARK_ALTAR_POLISHED_BASALT.get(), new Item.Properties()));

    public static final RegistryObject<Item> DARK_ALTAR_SOUL_SLATE_ITEM = ITEMS.register(
            "dark_altar_soul_slate",
            () -> new net.minecraft.world.item.BlockItem(DARK_ALTAR_SOUL_SLATE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DARK_ALTAR_RED_NETHER_BRICK_ITEM = ITEMS.register(
            "dark_altar_red_nether_brick",
            () -> new net.minecraft.world.item.BlockItem(DARK_ALTAR_RED_NETHER_BRICK.get(), new Item.Properties()));

    public static final RegistryObject<Item> DARK_ALTAR_BLUE_NETHER_BRICK_ITEM = ITEMS.register(
            "dark_altar_blue_nether_brick",
            () -> new net.minecraft.world.item.BlockItem(DARK_ALTAR_BLUE_NETHER_BRICK.get(), new Item.Properties()));

    public static final RegistryObject<Item> DARK_ALTAR_NETHER_RACK_ITEM = ITEMS.register(
            "dark_altar_nether_rack",
            () -> new net.minecraft.world.item.BlockItem(DARK_ALTAR_NETHER_RACK.get(), new Item.Properties()));

    // 基座块物品
    public static final RegistryObject<Item> PEDESTAL_POLISHED_BASALT_ITEM = ITEMS.register(
            "pedestal_polished_basalt",
            () -> new net.minecraft.world.item.BlockItem(PEDESTAL_POLISHED_BASALT.get(), new Item.Properties()));

    public static final RegistryObject<Item> PEDESTAL_SOUL_SLATE_ITEM = ITEMS.register(
            "pedestal_soul_slate",
            () -> new net.minecraft.world.item.BlockItem(PEDESTAL_SOUL_SLATE.get(), new Item.Properties()));

    public static final RegistryObject<Item> PEDESTAL_RED_NETHER_BRICK_ITEM = ITEMS.register(
            "pedestal_red_nether_brick",
            () -> new net.minecraft.world.item.BlockItem(PEDESTAL_RED_NETHER_BRICK.get(), new Item.Properties()));

    public static final RegistryObject<Item> PEDESTAL_BLUE_NETHER_BRICK_ITEM = ITEMS.register(
            "pedestal_blue_nether_brick",
            () -> new net.minecraft.world.item.BlockItem(PEDESTAL_BLUE_NETHER_BRICK.get(), new Item.Properties()));

    public static final RegistryObject<Item> PEDESTAL_NETHER_RACK_ITEM = ITEMS.register(
            "pedestal_nether_rack",
            () -> new net.minecraft.world.item.BlockItem(PEDESTAL_NETHER_RACK.get(), new Item.Properties()));

    // ========== 物品 ==========
    public static final RegistryObject<Item> LOGO_ITEM = ITEMS.register("logo_item", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGI_RESIDUE = ITEMS.register("magi_residue", () -> new MagiResidue(new Item.Properties()));
    public static final RegistryObject<Item> TREACHEROUS_CANDLE_STAFF = ITEMS.register("treacherous_candle_staff", TreacherousCandleStaff::new);
    public static final RegistryObject<Item> SOULGATHER_STAFF = ITEMS.register("soulgather_staff", SoulgatherStaff::new);
    public static final RegistryObject<Item> INVOKE_SCROLL = ITEMS.register("invoke_scroll", InvokeScrollItem::new);
    public static final RegistryObject<Item> WARPED_SPEAR = ITEMS.register("warped_spear", WarpedSpearItem::new);
    public static final RegistryObject<Item> BURNING_TUSK = ITEMS.register("burning_tusk", () -> new BurningTusk(new Item.Properties()));
    public static final RegistryObject<Item> SOULGATHER_SCROLL = ITEMS.register("soulgather_scroll", () -> new Scroll(GINTResearchList.SOULGATHER));
    public static final RegistryObject<Item> BAT_WING = ITEMS.register("bat_wing", () -> new BatWing(new Item.Properties()));
    public static final RegistryObject<Item> ORACLE_OF_TOTEM = ITEMS.register("oracle_of_totem", () -> new OracleOfTotemItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FORGEFORSAKEN_SCROLL = ITEMS.register("forgeforsaken_scroll", () -> new Scroll(GINTResearchList.FORGEFORSAKEN));
    public static final RegistryObject<Item> DISPENSER_PACKAGE = ITEMS.register("dispenser_package", () -> new DispenserPackageItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> WROUGHT_HATCHET_ITEM = ITEMS.register("wrought_hatchet", () -> new WroughtHatchetItem(new Item.Properties().stacksTo(1).durability(5000)));
    public static final RegistryObject<Item> WROUGHT_INGOT = ITEMS.register("wrought_ingot", () -> new WroughtIngot(new Item.Properties()));
    public static final RegistryObject<Item> FORGE_TRIAL_KEY = ITEMS.register("forge_trial_key", ForgeTrialKey::new); // 钥匙只能堆叠1个
    public static final RegistryObject<Item> SPELLER_ROBE = ITEMS.register("speller_robe", () -> new SpellerRobeItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MAGI_BRAIN = ITEMS.register("magi_brain", () -> new MagiBrainItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BURNING_DISC = ITEMS.register("burning_disc", () -> new RecordItem(15, GINTMod.BURNING_HOGLIN_SOUND, new Item.Properties().stacksTo(1), 3780) );
    public static final RegistryObject<Item> INVOKER_DISC = ITEMS.register("invoker_disc", () -> new RecordItem(15, GINTMod.INVOKER_SOUND, new Item.Properties().stacksTo(1), 2940) );

// 聚晶
    public static final RegistryObject<Item> MAGIFIREBALL_FOCUS = ITEMS.register("magifireball_focus", () -> new MagicFocus(new MagiFireBallSpell()));
    public static final RegistryObject<Item> LIFESTEALATTACK_FOCUS = ITEMS.register("lifestealattack_focus", () -> new MagicFocus(new LifeStealAttackSpell()));
    public static final RegistryObject<Item> FIREARROW_FOCUS = ITEMS.register("firearrow_focus", () -> new MagicFocus(new FireArrowSpell()));
    public static final RegistryObject<Item> CRASHAGER_FOCUS = ITEMS.register("crashager_focus", () -> new MagicFocus(new CrashagerSpell()));
    public static final RegistryObject<Item> PATCHOULI_FOCUS = ITEMS.register("patchouli_focus", () -> new MagicFocus(new PatchouliSpell()));
    public static final RegistryObject<Item> SHOTGUNATTACK_FOCUS = ITEMS.register("shotgunattack_focus", () -> new MagicFocus(new ShotgunAttackSpell()));
    public static final RegistryObject<Item> MAGICALFLAMES_FOCUS = ITEMS.register("magicalflames_focus", () -> new MagicFocus(new MagicalFlamesSpell()));
    public static final RegistryObject<Item> CONJUREFANGS_FOCUS = ITEMS.register("conjurefangs_focus", () -> new MagicFocus(new ConjureFangsSpell()));
    public static final RegistryObject<Item> SPITATTACK_FOCUS = ITEMS.register("spitattack_focus", () -> new MagicFocus(new NagaPoisonBallSpell()));
    public static final RegistryObject<Item> FIREWORK_FOCUS = ITEMS.register("firework_focus", () -> new MagicFocus(new FireWorkSpell()));
    public static final RegistryObject<Item> SURRENDERED_FOCUS = ITEMS.register("surrendered_focus", () -> new MagicFocus(new SurrenderedSpell()));
    public static final RegistryObject<Item> GATLINFIREWORK_FOCUS = ITEMS.register("gatlinfirework_focus", () -> new MagicFocus(new GatlinFireworkSpell()));
    public static final RegistryObject<Item> KABOOMER_FOCUS = ITEMS.register("kaboomer_focus", () -> new MagicFocus(new KaboomerSpell()));
    public static final RegistryObject<Item> CONJURESKELL_FOCUS = ITEMS.register("conjureskull_focus", () -> new MagicFocus(new ConjureSkullSpell()));

    // ========== 实体类型 ==========
    public static final RegistryObject<EntityType<MarauderServant>> MARAUDER_SERVANT = ENTITY_TYPES.register(
            "marauder_servant",
            () -> EntityType.Builder.of(MarauderServant::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F).clientTrackingRange(10).updateInterval(3)
                    .build(location("marauder_servant").toString()));

    public static final RegistryObject<EntityType<AllySurrendered>> ALLY_SURRENDERED = ENTITY_TYPES.register(
            "ally_surrendered",
            () -> EntityType.Builder.of(AllySurrendered::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.99F).clientTrackingRange(10).updateInterval(3)
                    .build(location("ally_surrendered").toString()));

    public static final RegistryObject<EntityType<InquisitorServant>> INQUISITOR_SERVANT = ENTITY_TYPES.register(
            "inquisitor_servant",
            () -> EntityType.Builder.of(InquisitorServant::new, MobCategory.CREATURE)
                    .sized(0.6F, 2.25F).clientTrackingRange(10).updateInterval(3)
                    .build(location("inquisitor_servant").toString()));

    public static final RegistryObject<EntityType<AllyCrashagerEntity>> ALLY_CRASHAGER = ENTITY_TYPES.register(
            "ally_crashager",
            () -> EntityType.Builder.of(AllyCrashagerEntity::new, MobCategory.MISC)
                    .sized(1.5f, 2.5f).build(location("ally_crashager").toString()));

    public static final RegistryObject<EntityType<AllyKaboomerEntity>> ALLY_KABOOMER = ENTITY_TYPES.register(
            "ally_kaboomer",
            () -> EntityType.Builder.of(AllyKaboomerEntity::new, MobCategory.MISC)
                    .sized(2.0F, 2.5F).clientTrackingRange(10).build(location("ally_kaboomer").toString()));

    public static final RegistryObject<EntityType<ProvokerServant>> PROVOKER_SERVANT = ENTITY_TYPES.register(
            "provoker_servant",
            () -> EntityType.Builder.of(ProvokerServant::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F).clientTrackingRange(10).updateInterval(3)
                    .build(location("provoker_servant").toString()));

    public static final RegistryObject<EntityType<BasherServant>> BASHER_SERVANT = ENTITY_TYPES.register(
            "basher_servant",
            () -> EntityType.Builder.of(BasherServant::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F).clientTrackingRange(10).updateInterval(3)
                    .build(location("basher_servant").toString()));

    public static final RegistryObject<EntityType<WarpedSpearEntity>> WARPED_SPEAR_ENTITY = ENTITY_TYPES.register(
            "warped_spear",
            () -> EntityType.Builder.<WarpedSpearEntity>of(WarpedSpearEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                    .build(location("warped_spear").toString()));

    public static final RegistryObject<EntityType<BoundInvoker>> BOUND_INVOKER = ENTITY_TYPES.register(
            "bound_invoker",
            () -> EntityType.Builder.of(BoundInvoker::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F).clientTrackingRange(10).updateInterval(3)
                    .build(location("bound_invoker").toString()));

    public static final RegistryObject<EntityType<AlchemistServant>> ALCHEMIST_SERVANT =
            ENTITY_TYPES.register("alchemist_servant",
                    () -> EntityType.Builder.<AlchemistServant>of(AlchemistServant::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build(location("alchemist_servant").toString()));

    public static final Supplier<EntityType<TreacherousShotgunPellet>> TREACHEROUS_SHOTGUN_PELLET = ENTITY_TYPES.register("treacherous_shotgun_pellet", () ->
            EntityType.Builder.<TreacherousShotgunPellet>of(TreacherousShotgunPellet::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("treacherous_shotgun_pellet"));

    public static final RegistryObject<EntityType<AncientHoglin>> ANCIENT_HOGLIN = ENTITY_TYPES.register(
            "ancient_hoglin",
            () -> EntityType.Builder.of(AncientHoglin::new, MobCategory.MONSTER)
                    .sized(1.41f, 1.68f)
                    .clientTrackingRange(10)
                    .build(location("ancient_hoglin").toString()));

    public static final RegistryObject<EntityType<IllashooterEntityServant>> ILLASHOOTER_SERVANT =
            ENTITY_TYPES.register("illashooter_servant",
                    () -> EntityType.Builder.<IllashooterEntityServant>of(IllashooterEntityServant::new, MobCategory.MISC)
                            .sized(0.5F, 1.0F)  // 与原 Illashooter 尺寸一致
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(GINTMod.MODID, "illashooter_servant").toString()));

    public static final RegistryObject<EntityType<DispenserServant>> DISPENSER_SERVANT =
            ENTITY_TYPES.register("dispenser_servant",
                    () -> EntityType.Builder.<DispenserServant>of(DispenserServant::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F) // 与原 Dispenser 尺寸一致
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(GINTMod.MODID, "dispenser_servant").toString()));

    public static final Supplier<EntityType<WroughtHatchet>> WROUGHT_HATCHET_ENTITY_TYPE = ENTITY_TYPES.register("wrought_hatchet",
            () -> EntityType.Builder.<WroughtHatchet>of(WroughtHatchet::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)  // 碰撞箱大小
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("wrought_hatchet")
    );

    public static final RegistryObject<EntityType<AllyPoisonBallEntity>> SPIT_POISON_BALL =
            ENTITY_TYPES.register("spit_poison_ball",
                    () -> EntityType.Builder.<AllyPoisonBallEntity>of(AllyPoisonBallEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(MODID, "spit_poison_ball").toString())
            );

    public static final RegistryObject<EntityType<CustomEarthQuakeEntity>> CUSTOM_EARTHQUAKE =
            ENTITY_TYPES.register("custom_earthquake",
                    () -> EntityType.Builder.<CustomEarthQuakeEntity>of(CustomEarthQuakeEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("custom_earthquake")
            );

    public static final RegistryObject<EntityType<EntityBurningHoglinLord>> BURNING_HOGLIN_LORD =
            ENTITY_TYPES.register("burning_hoglin_lord",
                    () -> EntityType.Builder.of(EntityBurningHoglinLord::new, MobCategory.MONSTER)
                            .sized(2.25F, 2.10F)
                            .fireImmune()
                            .build("burning_hoglin_lord"));

    public static final RegistryObject<EntityType<AllySkullBolt>> ALLY_SKULL_BOLT =
            ENTITY_TYPES.register("ally_skull_bolt",
                    () -> EntityType.Builder.<AllySkullBolt>of(AllySkullBolt::new, MobCategory.MISC)
                            .sized(0.3125F, 0.3125F)
                            .setTrackingRange(64)
                            .setUpdateInterval(1)
                            .build("ally_skull_bolt"));

    public static final RegistryObject<EntityType<SorcererServant>> SORCERER_SERVANT =
            ENTITY_TYPES.register("sorcerer_servant",
                    () -> EntityType.Builder.of(SorcererServant::new, MobCategory.MISC)
                            .sized(0.5F, 1.92F)
                            .build(new ResourceLocation(GINTMod.MODID, "sorcerer_servant").toString()));


    //粒子
    //buff
    // ========== 状态效果 ==========
    public static final RegistryObject<MobEffect> BETRAYAL_HAZE = MOB_EFFECTS.register("betrayal_haze",
            () -> new BetrayalHazeEffect(MobEffectCategory.HARMFUL, 0x8B0000)); // 血红色
    public static final RegistryObject<MobEffect> DECEIVED = MOB_EFFECTS.register("deceived",
            () -> new DeceivedEffect(MobEffectCategory.HARMFUL, 0x8B0000));

    // ========== 仆从刷怪蛋 ==========
    public static final RegistryObject<ServantSpawnEggItem> MARAUDER_SERVANT_SPAWN_EGG = ITEMS.register(
            "marauder_servant_spawn_egg", () -> new ServantSpawnEggItem(MARAUDER_SERVANT, 9541270, 5441030, egg()));
    public static final RegistryObject<ServantSpawnEggItem> ALLY_SURRENDERED_SPAWN_EGG = ITEMS.register(
            "ally_surrendered_spawn_egg", () -> new ServantSpawnEggItem(ALLY_SURRENDERED, 11260369, 11858160, egg()));
    public static final RegistryObject<ServantSpawnEggItem> INQUISITOR_SERVANT_SPAWN_EGG = ITEMS.register(
            "inquisitor_servant_spawn_egg", () -> new ServantSpawnEggItem(INQUISITOR_SERVANT, 9541270, 4934471, egg()));
    public static final RegistryObject<ServantSpawnEggItem> PROVOKER_SERVANT_SPAWN_EGG = ITEMS.register(
            "provoker_servant_spawn_egg", () -> new ServantSpawnEggItem(PROVOKER_SERVANT, 9541270, 9399876, egg()));
    public static final RegistryObject<ServantSpawnEggItem> BASHER_SERVANT_SPAWN_EGG = ITEMS.register(
            "basher_servant_spawn_egg", () -> new ServantSpawnEggItem(BASHER_SERVANT, 9541270, 5985087, egg()));
    public static final RegistryObject<ServantSpawnEggItem> BOUND_INVOKER_SPAWN_EGG = ITEMS.register(
            "bound_invoker_spawn_egg", () -> new ServantSpawnEggItem(BOUND_INVOKER, 9541270, 0xCEC987, egg()));
    public static final RegistryObject<ServantSpawnEggItem> ALCHEMIST_SERVANT_SPAWN_EGG = ITEMS.register(
            "alchemist_servant_spawn_egg", () -> new ServantSpawnEggItem(ALCHEMIST_SERVANT, 9541270, 7550099, egg()));
    public static final RegistryObject<ServantSpawnEggItem> ILLASHOOTER_SERVANT_SPAWN_EGG = ITEMS.register(
            "illashooter_servant_spawn_egg", () -> new ServantSpawnEggItem(ILLASHOOTER_SERVANT, 16775294, 16775294, egg()));
    public static final RegistryObject<ServantSpawnEggItem> DISPENSER_SERVANT_SERVANT_SPAWN_EGG = ITEMS.register(
            "dispenser_servant_spawn_egg", () -> new ServantSpawnEggItem(DISPENSER_SERVANT, 16775294, 16775340, egg()));
    public static final RegistryObject<ModSpawnEggItem> ANCIENT_HOGLIN_SPAWN_EGG = ITEMS.register(
            "ancient_hoglin_spawn_egg", () -> new ModSpawnEggItem(ANCIENT_HOGLIN, 0x8F6E44, 0xCEC987, new Item.Properties()));
    public static final RegistryObject<Item> BURNING_HOGLIN_LORD_SPAWN_EGG = ITEMS.register(
            "burning_hoglin_lord_spawn_egg", () -> new ForgeSpawnEggItem(BURNING_HOGLIN_LORD, 0x8B0000, 0xFFA500, new Item.Properties()));
    public static final RegistryObject<ServantSpawnEggItem> SORCERER_SERVANT_SPAWN_EGG = ITEMS.register(
            "sorcerer_servant_spawn_egg", () -> new ServantSpawnEggItem(SORCERER_SERVANT, 9541270, 10899592, egg()));

    // ========== 菜单 ==========
    public static final RegistryObject<MenuType<PatchouliFocusMenu>> PATCHOULI_FOCUS_MENU =
            MENU_TYPES.register("patchouli_focus", () -> IForgeMenuType.create(PatchouliFocusMenu::new));

    // ========== 创造页 ==========
    public static final RegistryObject<CreativeModeTab> GINT_TAB = TABS.register("goety_integration_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Goety Integration"))
                    .icon(() -> LOGO_ITEM.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(WARPED_SPEAR.get());
                        output.accept(SOULGATHER_STAFF.get());
                        output.accept(TREACHEROUS_CANDLE_STAFF.get());
                        output.accept(MAGI_BRAIN.get());
                        output.accept(SPELLER_ROBE.get());
                        output.accept(ModRegistry.PLATINUM_INFUSED_HATCHET_ITEM.get());
                        output.accept(WROUGHT_HATCHET_ITEM.get());
                        output.accept(ORACLE_OF_TOTEM.get());

                        output.accept(DISPENSER_PACKAGE.get());

                        output.accept(FORGEFORSAKEN_SCROLL.get());
                        output.accept(SOULGATHER_SCROLL.get());
                        output.accept(INVOKE_SCROLL.get());

                        output.accept(FORGE_TRIAL_KEY.get());
                        output.accept(WROUGHT_INGOT.get());
                        output.accept(MAGI_RESIDUE.get());
                        output.accept(ModRegistry.PRIMAL_ESSENCE_ITEM.get());
                        output.accept(BURNING_TUSK.get());
                        output.accept(ModRegistry.PLATINUM_SHEET_ITEM.get());
                        output.accept(BAT_WING.get());

                        output.accept(INVOKER_DISC.get());
                        output.accept(BURNING_DISC.get());

                        output.accept(CRASHAGER_FOCUS.get());
                        output.accept(KABOOMER_FOCUS.get());
                        output.accept(MAGIFIREBALL_FOCUS.get());
                        output.accept(LIFESTEALATTACK_FOCUS.get());
                        output.accept(FIREARROW_FOCUS.get());
                        output.accept(ModItems.SOUL_BOLT_FOCUS.get());
                        output.accept(ModItems.SOUL_LIGHT_FOCUS.get());
                        output.accept(ModItems.SHOCKWAVE_FOCUS.get());
                        output.accept(GSItems.IMPISH_FOCUS.get());
                        output.accept(GSItems.SPIRIT_HAND_FOCUS.get());
                        output.accept(GSItems.REQUIEM_FOCUS.get());
                        output.accept(GSItems.SOUL_BEAM_FOCUS.get());
                        output.accept(SHOTGUNATTACK_FOCUS.get());
                        output.accept(CONJURESKELL_FOCUS.get());
                        output.accept(MAGICALFLAMES_FOCUS.get());
                        output.accept(CONJUREFANGS_FOCUS.get());
                        output.accept(SURRENDERED_FOCUS.get());
                        output.accept(SPITATTACK_FOCUS.get());
                        output.accept(FIREWORK_FOCUS.get());
                        output.accept(GATLINFIREWORK_FOCUS.get());
                        output.accept(PATCHOULI_FOCUS.get());

                        output.accept(ANCIENT_HOGLIN_SPAWN_EGG.get());
                        output.accept(BURNING_HOGLIN_LORD_SPAWN_EGG.get());
                        output.accept(ModRegistry.INVOKER_SPAWN_EGG_ITEM.get());

                        output.accept(ALLY_SURRENDERED_SPAWN_EGG.get());
                        output.accept(BOUND_INVOKER_SPAWN_EGG.get());
                        output.accept(MARAUDER_SERVANT_SPAWN_EGG.get());
                        output.accept(INQUISITOR_SERVANT_SPAWN_EGG.get());
                        output.accept(PROVOKER_SERVANT_SPAWN_EGG.get());
                        output.accept(BASHER_SERVANT_SPAWN_EGG.get());
                        output.accept(ALCHEMIST_SERVANT_SPAWN_EGG.get());
                        output.accept(SORCERER_SERVANT_SPAWN_EGG.get());
                        output.accept(ILLASHOOTER_SERVANT_SPAWN_EGG.get());
                        output.accept(DISPENSER_SERVANT_SERVANT_SPAWN_EGG.get());

                        output.accept(MAGI_BOOKSHELF_ITEM.get());
                        output.accept(FORGE_SPAWNER_ITEM.get());
                        output.accept(DARK_ALTAR_POLISHED_BASALT_ITEM.get());
                        output.accept(DARK_ALTAR_SOUL_SLATE_ITEM.get());
                        output.accept(DARK_ALTAR_RED_NETHER_BRICK_ITEM.get());
                        output.accept(DARK_ALTAR_BLUE_NETHER_BRICK_ITEM.get());
                        output.accept(DARK_ALTAR_NETHER_RACK_ITEM.get());
                        output.accept(PEDESTAL_POLISHED_BASALT_ITEM.get());
                        output.accept(PEDESTAL_SOUL_SLATE_ITEM.get());
                        output.accept(PEDESTAL_RED_NETHER_BRICK_ITEM.get());
                        output.accept(PEDESTAL_BLUE_NETHER_BRICK_ITEM.get());
                        output.accept(PEDESTAL_NETHER_RACK_ITEM.get());
                    })
                    .build());

    // ========== 构造函数 ==========
    public GINTMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        LOGGER.info("=== Goety Integration Mod 初始化 ===");
        ENTITY_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        TABS.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        MOB_EFFECTS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY.register(modEventBus);
        PARTICLE_TYPES.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerAttributes);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TargetSelectionHandler());
        MinecraftForge.EVENT_BUS.register(SpellerRobeEvents.class);
        MinecraftForge.EVENT_BUS.register(MagiBrainEvents.class);
        MinecraftForge.EVENT_BUS.register(CurioAttributeEvents.class);
        // 在 mod 构造时立即扩展枚举
        SOULGATHER = SpellType.create("soulgather", "spell.goety_integration.soulgather");
        WICCA = SpellType.create("wicca", "spell.goety_integration.wicca");
        INTEGRATION = SpellType.create("integration", "spell.goety_integration.integration");

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(BuiltinPacksRegistry::register);

        LOGGER.info("=== 初始化完成 ===");
    }

    // ========== 通用设置 ==========
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("通用设置完成");
            OracleNetwork.register();
            IllagerType.create("MARAUDER", new GINTIllagerType());
            LOGGER.info("已注册 Marauder 训练类型");
            IllagerType.create("INQUISITOR", new GINTIllagerType());
            LOGGER.info("已注册 Inquisitor 训练类型");
            IllagerType.create("BASHER", new GINTIllagerType());
            LOGGER.info("已注册 Basher 训练类型");
            IllagerType.create("PROVOKER", new GINTIllagerType());
            LOGGER.info("已注册 Provoker 训练类型");
            IllagerType.create("ALCHEMIST", new GINTIllagerType());
            LOGGER.info("已注册 ALCHEMIST 训练类型");
            RitualType.addRitualType("soulgather", new com.tenken_amainu.goety_integration.common.ritual.Type.SoulgatherRitualType());
            event.enqueueWork(() -> {
                // 发送 IMC 给 Patchouli 注册自定义页面
                InterModComms.sendTo("patchouli", "register", () -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("pageClass", PageWiccaRecipe.class.getName());
                    map.put("type", new ResourceLocation(GINTMod.MODID, "wicca_recipe").toString());
                    return map;
                });
            });
        });
    }

    // ========== 实体属性注册 ==========
    private void registerAttributes(EntityAttributeCreationEvent event) {
        LOGGER.info("注册实体属性...");
        if (MARAUDER_SERVANT.isPresent()) {
            AttributeSupplier.Builder attributes = Monster.createMonsterAttributes()
                    .add(Attributes.MAX_HEALTH, 24.0D)
                    .add(Attributes.MOVEMENT_SPEED, 0.35D)
                    .add(Attributes.ATTACK_DAMAGE, 5.0D)
                    .add(Attributes.FOLLOW_RANGE, 12.0D);
            event.put(MARAUDER_SERVANT.get(), attributes.build());
            LOGGER.info("MarauderServant 属性注册成功");
        }
        if (ALLY_SURRENDERED.isPresent()) {
            AttributeSupplier.Builder attributes = Minion.createMobAttributes()
                    .add(Attributes.MAX_HEALTH, AttributesConfig.SummonedVexHealth.get())
                    .add(Attributes.ATTACK_DAMAGE, AttributesConfig.SummonedVexDamage.get())
                    .add(Attributes.MOVEMENT_SPEED, 0.25D)
                    .add(Attributes.FOLLOW_RANGE, 16.0D);
            event.put(ALLY_SURRENDERED.get(), attributes.build());
            LOGGER.info("AllySurrendered 属性注册成功");
        }
        if (INQUISITOR_SERVANT.isPresent()) {
            AttributeSupplier.Builder attributes = InquisitorServant.setCustomAttributes();
            event.put(INQUISITOR_SERVANT.get(), attributes.build());
            LOGGER.info("InquisitorServant 属性注册成功");
        }
        if (ALLY_CRASHAGER.isPresent()) {
            event.put(ALLY_CRASHAGER.get(), AllyCrashagerEntity.createAttributes().build());
            LOGGER.info("AllyCrashagerEntity 属性注册成功");
        }
        if (ALLY_KABOOMER.isPresent()) {
            event.put(ALLY_KABOOMER.get(), AllyKaboomerEntity.createAttributes().build());
            LOGGER.info("AllyKaboomerEntity 属性注册成功");
        }
        if (PROVOKER_SERVANT.isPresent()) {
            AttributeSupplier.Builder attributes = ProvokerServant.setCustomAttributes();
            event.put(PROVOKER_SERVANT.get(), attributes.build());
            LOGGER.info("ProvokerServant 属性注册成功");
        }
        if (BASHER_SERVANT.isPresent()) {
            AttributeSupplier.Builder attributes = BasherServant.setCustomAttributes();
            event.put(BASHER_SERVANT.get(), attributes.build());
            LOGGER.info("BasherServant 属性注册成功");
        }
        if (BOUND_INVOKER.isPresent()) {
            AttributeSupplier.Builder attributes = BoundInvoker.setCustomAttributes();
            event.put(BOUND_INVOKER.get(), attributes.build());
            LOGGER.info("BoundInvoker 属性注册成功");
        }
        if (ALCHEMIST_SERVANT.isPresent()) {
            AttributeSupplier.Builder attributes = AlchemistServant.setCustomAttributes();
            event.put(ALCHEMIST_SERVANT.get(), attributes.build());
            LOGGER.info("AlchemistServant 属性注册成功");
        }
        event.put(SORCERER_SERVANT.get(), SorcererServant.createAttributes().build());
        AttributeSupplier.Builder attributes = AncientHoglin.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.2D);
        event.put(ANCIENT_HOGLIN.get(), attributes.build());
        event.put(ILLASHOOTER_SERVANT.get(), IllashooterEntityServant.setCustomAttributes().build());
        event.put(DISPENSER_SERVANT.get(), DispenserServant.setCustomAttributes().build());
        event.put(BURNING_HOGLIN_LORD.get(), EntityBurningHoglinLord.createAttributes().build());
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        WarpedSpearEntity.PLAYER_LOGOUT_POSITIONS.put(event.getEntity().getUUID(), event.getEntity().position());
    }

    // ========== 其他方法 ==========
    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new WiccaRecipeLoader());
    }
}
