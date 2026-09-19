package net.mcreator.sharks.procedures;

import javax.annotation.Nullable;
import net.mcreator.sharks.entity.AxodileEntity;
import net.mcreator.sharks.entity.BlacktipReefSharkEntity;
import net.mcreator.sharks.entity.BlueSharkEntity;
import net.mcreator.sharks.entity.BonnetheadSharkEntity;
import net.mcreator.sharks.entity.BullSharkEntity;
import net.mcreator.sharks.entity.GreaterAxodileEntity;
import net.mcreator.sharks.entity.GreenlandSharkEntity;
import net.mcreator.sharks.entity.LandSharkEntity;
import net.mcreator.sharks.entity.LemonSharkEntity;
import net.mcreator.sharks.entity.MakoSharkEntity;
import net.mcreator.sharks.entity.MegalodonEntity;
import net.mcreator.sharks.entity.ShrakEntity;
import net.mcreator.sharks.entity.TigerSharkEntity;
import net.mcreator.sharks.entity.WhitetipSharkEntity;
import net.mcreator.sharks.init.BenssharksModItems;
import net.mcreator.sharks.init.BenssharksModMobEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class BleedingGlobalProcedure {
   @SubscribeEvent
   public static void onEntityAttacked(LivingIncomingDamageEvent event) {
      if (event.getEntity() != null) {
         execute(event, event.getEntity().level(), event.getSource(), event.getEntity(), event.getSource().getEntity());
      }
   }

   public static void execute(LevelAccessor world, DamageSource damagesource, Entity entity, Entity sourceentity) {
      execute(null, world, damagesource, entity, sourceentity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, DamageSource damagesource, Entity entity, Entity sourceentity) {
      if (damagesource != null && entity != null && sourceentity != null) {
         if ((
               sourceentity instanceof ShrakEntity
                  || sourceentity instanceof TigerSharkEntity
                  || sourceentity instanceof BlueSharkEntity
                  || sourceentity instanceof MakoSharkEntity
                  || sourceentity instanceof BonnetheadSharkEntity
                  || sourceentity instanceof BlacktipReefSharkEntity
                  || sourceentity instanceof BullSharkEntity
                  || sourceentity instanceof MegalodonEntity
                  || sourceentity instanceof LemonSharkEntity
                  || sourceentity instanceof GreenlandSharkEntity
                  || sourceentity instanceof WhitetipSharkEntity
                  || sourceentity instanceof LandSharkEntity
            )
            && entity instanceof LivingEntity _entity
            && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(BenssharksModMobEffects.BLEEDING, 200, 0, true, false));
         }

         if ((sourceentity instanceof LivingEntity _livEntxxxxxxxxx ? _livEntxxxxxxxxx.getMainHandItem() : ItemStack.EMPTY)
                  .getEnchantmentLevel(
                     world.registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("dwurdysharks:serrated")))
                  )
               != 0
            && (
               (sourceentity instanceof LivingEntity _livEntxxxxxxxx ? _livEntxxxxxxxx.getMainHandItem() : ItemStack.EMPTY)
                     .is(ItemTags.create(ResourceLocation.parse("minecraft:tools/swords")))
                  || (sourceentity instanceof LivingEntity _livEntxxxxxxx ? _livEntxxxxxxx.getMainHandItem() : ItemStack.EMPTY)
                     .is(ItemTags.create(ResourceLocation.parse("minecraft:swords")))
                  || (sourceentity instanceof LivingEntity _livEntxxxxxx ? _livEntxxxxxx.getMainHandItem() : ItemStack.EMPTY)
                     .is(ItemTags.create(ResourceLocation.parse("minecraft:tools/tridents")))
                  || (sourceentity instanceof LivingEntity _livEntxxxxx ? _livEntxxxxx.getOffhandItem() : ItemStack.EMPTY)
                     .is(ItemTags.create(ResourceLocation.parse("minecraft:tools/tridents")))
                  || (sourceentity instanceof LivingEntity _livEntxxxx ? _livEntxxxx.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof TridentItem
                  || (sourceentity instanceof LivingEntity _livEntxxx ? _livEntxxx.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof SwordItem
                  || (sourceentity instanceof LivingEntity _livEntxx ? _livEntxx.getMainHandItem() : ItemStack.EMPTY)
                     .is(ItemTags.create(ResourceLocation.parse("minecraft:tools/axes")))
                  || (sourceentity instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY)
                     .is(ItemTags.create(ResourceLocation.parse("minecraft:axes")))
                  || (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof AxeItem
            )
            && entity instanceof LivingEntity _entity
            && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(BenssharksModMobEffects.BLEEDING, 200, 0, true, false));
         }

         if (damagesource.isDirect()
            && (
               entity instanceof AxodileEntity
                  || entity instanceof GreaterAxodileEntity
                  || (entity instanceof LivingEntity _entGetArmorxxx ? _entGetArmorxxx.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem()
                        == BenssharksModItems.JAGGED_HELMET.get()
                     && (entity instanceof LivingEntity _entGetArmorxx ? _entGetArmorxx.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem()
                        == BenssharksModItems.JAGGED_CHESTPLATE.get()
                     && (entity instanceof LivingEntity _entGetArmorx ? _entGetArmorx.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem()
                        == BenssharksModItems.JAGGED_LEGGINGS.get()
                     && (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem()
                        == BenssharksModItems.JAGGED_BOOTS.get()
            )
            && sourceentity instanceof LivingEntity _entity
            && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(BenssharksModMobEffects.BLEEDING, 100, 0, true, false));
         }
      }
   }
}
