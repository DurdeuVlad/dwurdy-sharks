package net.mcreator.sharks.item;

import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.mcreator.sharks.client.model.ModelJaggedArmorJava;
import net.mcreator.sharks.init.BenssharksModItems;
import net.mcreator.sharks.procedures.JaggedHelmetTickEventProcedure;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial.Layer;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(
   bus = Bus.MOD
)
public abstract class JaggedItem extends ArmorItem {
   public static Holder<ArmorMaterial> ARMOR_MATERIAL = null;

   @SubscribeEvent
   public static void registerArmorMaterial(RegisterEvent event) {
      event.register(
         Registries.ARMOR_MATERIAL,
         registerHelper -> {
            ArmorMaterial armorMaterial = new ArmorMaterial(
               (Map)Util.make(new EnumMap(Type.class), map -> {
                  map.put(Type.BOOTS, 3);
                  map.put(Type.LEGGINGS, 6);
                  map.put(Type.CHESTPLATE, 8);
                  map.put(Type.HELMET, 3);
                  map.put(Type.BODY, 8);
               }),
               50,
               DeferredHolder.create(Registries.SOUND_EVENT, ResourceLocation.parse("item.armor.equip_turtle")),
               () -> Ingredient.of(new ItemStack[]{new ItemStack((ItemLike)BenssharksModItems.AXOSCUTE.get())}),
               List.of(new Layer(ResourceLocation.parse("benssharks:jaggedarmor_"))),
               2.0F,
               0.1F
            );
            registerHelper.register(ResourceLocation.parse("benssharks:jagged"), armorMaterial);
            ARMOR_MATERIAL = BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(armorMaterial);
         }
      );
   }

   @SubscribeEvent
   public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
      event.registerItem(
         new IClientItemExtensions() {
            public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
               HumanoidModel armorModel = new HumanoidModel(
                  new ModelPart(
                     Collections.emptyList(),
                     Map.of(
                        "head",
                        (new ModelJaggedArmorJava(Minecraft.getInstance().getEntityModels().bakeLayer(ModelJaggedArmorJava.LAYER_LOCATION))).bipedHead,
                        "hat",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "body",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_arm",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_arm",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_leg",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_leg",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap())
                     )
                  )
               );
               armorModel.crouching = living.isShiftKeyDown();
               armorModel.riding = defaultModel.riding;
               armorModel.young = living.isBaby();
               return armorModel;
            }
         },
         new Item[]{(Item)BenssharksModItems.JAGGED_HELMET.get()}
      );
      event.registerItem(
         new IClientItemExtensions() {
            @OnlyIn(Dist.CLIENT)
            public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
               HumanoidModel armorModel = new HumanoidModel(
                  new ModelPart(
                     Collections.emptyList(),
                     Map.of(
                        "body",
                        (new ModelJaggedArmorJava(Minecraft.getInstance().getEntityModels().bakeLayer(ModelJaggedArmorJava.LAYER_LOCATION))).bipedBody,
                        "left_arm",
                        (new ModelJaggedArmorJava(Minecraft.getInstance().getEntityModels().bakeLayer(ModelJaggedArmorJava.LAYER_LOCATION))).bipedLeftArm,
                        "right_arm",
                        (new ModelJaggedArmorJava(Minecraft.getInstance().getEntityModels().bakeLayer(ModelJaggedArmorJava.LAYER_LOCATION))).bipedRightArm,
                        "head",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "hat",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_leg",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_leg",
                        new ModelPart(Collections.emptyList(), Collections.emptyMap())
                     )
                  )
               );
               armorModel.crouching = living.isShiftKeyDown();
               armorModel.riding = defaultModel.riding;
               armorModel.young = living.isBaby();
               return armorModel;
            }
         },
         new Item[]{(Item)BenssharksModItems.JAGGED_CHESTPLATE.get()}
      );
   }

   public JaggedItem(Type type, Properties properties) {
      super(ARMOR_MATERIAL, type, properties);
   }

   public static class Boots extends JaggedItem {
      public Boots() {
         super(Type.BOOTS, new Properties().durability(Type.BOOTS.getDurability(33)));
      }
   }

   public static class Chestplate extends JaggedItem {
      public Chestplate() {
         super(Type.CHESTPLATE, new Properties().durability(Type.CHESTPLATE.getDurability(33)));
      }
   }

   public static class Helmet extends JaggedItem {
      public Helmet() {
         super(Type.HELMET, new Properties().durability(Type.HELMET.getDurability(33)));
      }

      public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
         super.inventoryTick(itemstack, world, entity, slot, selected);
         if (entity instanceof Player player && Iterables.contains(player.getArmorSlots(), itemstack)) {
            JaggedHelmetTickEventProcedure.execute(entity);
         }
      }
   }

   public static class Leggings extends JaggedItem {
      public Leggings() {
         super(Type.LEGGINGS, new Properties().durability(Type.LEGGINGS.getDurability(33)));
      }
   }
}
