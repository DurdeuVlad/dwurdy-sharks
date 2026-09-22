package net.mcreator.sharks.init;

import net.mcreator.sharks.entity.AxodileEntity;
import net.mcreator.sharks.entity.BarracudaEntity;
import net.mcreator.sharks.entity.BaskingSharkEntity;
import net.mcreator.sharks.entity.BlacktipReefSharkEntity;
import net.mcreator.sharks.entity.BlueSharkEntity;
import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.mcreator.sharks.entity.CookiecutterSharkEntity;
import net.mcreator.sharks.entity.GreaterAxodileEntity;
import net.mcreator.sharks.entity.GreenlandSharkEntity;
import net.mcreator.sharks.entity.KrillEntity;
import net.mcreator.sharks.entity.LandSharkEntity;
import net.mcreator.sharks.entity.LemonSharkEntity;
import net.mcreator.sharks.entity.MakoSharkEntity;
import net.mcreator.sharks.entity.MegalodonEntity;
import net.mcreator.sharks.entity.NurseSharkEntity;
import net.mcreator.sharks.entity.PilotFishEntity;
import net.mcreator.sharks.entity.RemoraEntity;
import net.mcreator.sharks.entity.RollParticleEntity;
import net.mcreator.sharks.entity.SeekerSharkProjectileEntity;
import net.mcreator.sharks.entity.SeekingArrowEntity;
import net.mcreator.sharks.entity.ShrakEntity;
import net.mcreator.sharks.entity.ThalassogerEntity;
import net.mcreator.sharks.entity.TigerSharkEntity;
import net.mcreator.sharks.entity.WhaleSharkEntity;
import net.mcreator.sharks.entity.WhitetipSharkEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class DwurdySharksModEntities {
   public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, "dwurdysharks");
   public static final DeferredHolder<EntityType<?>, EntityType<ShrakEntity>> GREATWHITESHARK = register(
      "greatwhiteshark",
      Builder.of(ShrakEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(1.0F, 0.6F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<NurseSharkEntity>> NURSE_SHARK = register(
      "nurse_shark",
      Builder.of(NurseSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(1.15F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<RemoraEntity>> REMORA = register(
      "remora",
      Builder.of(RemoraEntity::new, MobCategory.WATER_AMBIENT)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(0.5F, 0.25F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<TigerSharkEntity>> TIGER_SHARK = register(
      "tiger_shark",
      Builder.of(TigerSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(1.0F, 0.5F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<AxodileEntity>> AXODILE = register(
      "axodile",
      Builder.of(AxodileEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .sized(1.0F, 0.6F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<BlueSharkEntity>> BLUE_SHARK = register(
      "blue_shark",
      Builder.of(BlueSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(1.0F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<BonnetheadSharkEntity>> BONNETHEAD_SHARK = register(
      "bonnethead_shark",
      Builder.of(BonnetheadSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(1.0F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<MakoSharkEntity>> MAKO_SHARK = register(
      "mako_shark",
      Builder.of(MakoSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(1.0F, 0.6F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<CookiecutterSharkEntity>> COOKIECUTTER_SHARK = register(
      "cookiecutter_shark",
      Builder.of(CookiecutterSharkEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(0.5F, 0.25F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<BlacktipReefSharkEntity>> BLACKTIP_REEF_SHARK = register(
      "blacktip_reef_shark",
      Builder.of(BlacktipReefSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(1.0F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<BaskingSharkEntity>> BASKING_SHARK = register(
      "basking_shark",
      Builder.of(BaskingSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(128)
         .setUpdateInterval(3)
         .sized(1.0F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<BullSharkEntity>> BULL_SHARK = register(
      "bull_shark",
      Builder.of(BullSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .sized(1.15F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<MegalodonEntity>> MEGALODON = register(
      "megalodon",
      Builder.of(MegalodonEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .sized(1.55F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<LandSharkEntity>> LAND_SHARK = register(
      "land_shark",
      Builder.of(LandSharkEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.6F, 0.85F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<LemonSharkEntity>> LEMON_SHARK = register(
      "lemon_shark",
      Builder.of(LemonSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .sized(1.15F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<ThalassogerEntity>> THALASSOGER = register(
      "thalassoger",
      Builder.of(ThalassogerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.6F, 1.8F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<SeekerSharkProjectileEntity>> SEEKER_SHARK_PROJECTILE = register(
      "seeker_shark_projectile",
      Builder.<SeekerSharkProjectileEntity>of(SeekerSharkProjectileEntity::new, MobCategory.MISC)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(1)
         .sized(1.0F, 1.0F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<KrillEntity>> KRILL = register(
      "krill",
      Builder.of(KrillEntity::new, MobCategory.WATER_AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.5F, 0.4F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<WhaleSharkEntity>> WHALE_SHARK = register(
      "whale_shark",
      Builder.of(WhaleSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(128)
         .setUpdateInterval(3)
         .sized(1.5F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<SeekingArrowEntity>> SEEKING_ARROW = register(
      "seeking_arrow",
      Builder.<SeekingArrowEntity>of(SeekingArrowEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.25F, 0.25F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<PilotFishEntity>> PILOT_FISH = register(
      "pilot_fish",
      Builder.of(PilotFishEntity::new, MobCategory.WATER_AMBIENT)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(32)
         .setUpdateInterval(3)
         .sized(0.5F, 0.3F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<GreaterAxodileEntity>> GREATER_AXODILE = register(
      "greater_axodile",
      Builder.of(GreaterAxodileEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .sized(1.0F, 0.6F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<RollParticleEntity>> ROLL_PARTICLE = register(
      "roll_particle",
      Builder.of(RollParticleEntity::new, MobCategory.MISC)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .fireImmune()
         .sized(0.1F, 0.1F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<GreenlandSharkEntity>> GREENLAND_SHARK = register(
      "greenland_shark",
      Builder.of(GreenlandSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .sized(1.0F, 0.6F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<WhitetipSharkEntity>> WHITETIP_SHARK = register(
      "whitetip_shark",
      Builder.of(WhitetipSharkEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .sized(1.15F, 0.7F)
   );
   public static final DeferredHolder<EntityType<?>, EntityType<BarracudaEntity>> BARRACUDA = register(
      "barracuda",
      Builder.of(BarracudaEntity::new, MobCategory.WATER_CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .sized(0.7F, 0.4F)
   );

   private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, Builder<T> entityTypeBuilder) {
      return REGISTRY.register(registryname, () -> entityTypeBuilder.build(registryname));
   }

   @SubscribeEvent
   public static void init(RegisterSpawnPlacementsEvent event) {
      ShrakEntity.init(event);
      NurseSharkEntity.init(event);
      RemoraEntity.init(event);
      TigerSharkEntity.init(event);
      AxodileEntity.init(event);
      BlueSharkEntity.init(event);
      BonnetheadSharkEntity.init(event);
      MakoSharkEntity.init(event);
      CookiecutterSharkEntity.init(event);
      BlacktipReefSharkEntity.init(event);
      BaskingSharkEntity.init(event);
      BullSharkEntity.init(event);
      MegalodonEntity.init(event);
      LandSharkEntity.init(event);
      LemonSharkEntity.init(event);
      ThalassogerEntity.init(event);
      KrillEntity.init(event);
      WhaleSharkEntity.init(event);
      PilotFishEntity.init(event);
      GreaterAxodileEntity.init(event);
      RollParticleEntity.init(event);
      GreenlandSharkEntity.init(event);
      WhitetipSharkEntity.init(event);
      BarracudaEntity.init(event);
   }

   @SubscribeEvent
   public static void registerAttributes(EntityAttributeCreationEvent event) {
      event.put((EntityType)GREATWHITESHARK.get(), ShrakEntity.createAttributes().build());
      event.put((EntityType)NURSE_SHARK.get(), NurseSharkEntity.createAttributes().build());
      event.put((EntityType)REMORA.get(), RemoraEntity.createAttributes().build());
      event.put((EntityType)TIGER_SHARK.get(), TigerSharkEntity.createAttributes().build());
      event.put((EntityType)AXODILE.get(), AxodileEntity.createAttributes().build());
      event.put((EntityType)BLUE_SHARK.get(), BlueSharkEntity.createAttributes().build());
      event.put((EntityType)BONNETHEAD_SHARK.get(), BonnetheadSharkEntity.createAttributes().build());
      event.put((EntityType)MAKO_SHARK.get(), MakoSharkEntity.createAttributes().build());
      event.put((EntityType)COOKIECUTTER_SHARK.get(), CookiecutterSharkEntity.createAttributes().build());
      event.put((EntityType)BLACKTIP_REEF_SHARK.get(), BlacktipReefSharkEntity.createAttributes().build());
      event.put((EntityType)BASKING_SHARK.get(), BaskingSharkEntity.createAttributes().build());
      event.put((EntityType)BULL_SHARK.get(), BullSharkEntity.createAttributes().build());
      event.put((EntityType)MEGALODON.get(), MegalodonEntity.createAttributes().build());
      event.put((EntityType)LAND_SHARK.get(), LandSharkEntity.createAttributes().build());
      event.put((EntityType)LEMON_SHARK.get(), LemonSharkEntity.createAttributes().build());
      event.put((EntityType)THALASSOGER.get(), ThalassogerEntity.createAttributes().build());
      event.put((EntityType)KRILL.get(), KrillEntity.createAttributes().build());
      event.put((EntityType)WHALE_SHARK.get(), WhaleSharkEntity.createAttributes().build());
      event.put((EntityType)PILOT_FISH.get(), PilotFishEntity.createAttributes().build());
      event.put((EntityType)GREATER_AXODILE.get(), GreaterAxodileEntity.createAttributes().build());
      event.put((EntityType)ROLL_PARTICLE.get(), RollParticleEntity.createAttributes().build());
      event.put((EntityType)GREENLAND_SHARK.get(), GreenlandSharkEntity.createAttributes().build());
      event.put((EntityType)WHITETIP_SHARK.get(), WhitetipSharkEntity.createAttributes().build());
      event.put((EntityType)BARRACUDA.get(), BarracudaEntity.createAttributes().build());
   }
}
