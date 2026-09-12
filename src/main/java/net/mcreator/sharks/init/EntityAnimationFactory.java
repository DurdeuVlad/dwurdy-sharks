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
import net.mcreator.sharks.entity.ShrakEntity;
import net.mcreator.sharks.entity.ThalassogerEntity;
import net.mcreator.sharks.entity.TigerSharkEntity;
import net.mcreator.sharks.entity.WhaleSharkEntity;
import net.mcreator.sharks.entity.WhitetipSharkEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;

@EventBusSubscriber
public class EntityAnimationFactory {
   @SubscribeEvent
   public static void onEntityTick(Pre event) {
      if (event != null && event.getEntity() != null) {
         if (event.getEntity() instanceof ShrakEntity syncable) {
            String animation = syncable.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncable.setAnimation("undefined");
               syncable.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof NurseSharkEntity syncablex) {
            String animation = syncablex.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablex.setAnimation("undefined");
               syncablex.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof RemoraEntity syncablexx) {
            String animation = syncablexx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexx.setAnimation("undefined");
               syncablexx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof TigerSharkEntity syncablexxx) {
            String animation = syncablexxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxx.setAnimation("undefined");
               syncablexxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof AxodileEntity syncablexxxx) {
            String animation = syncablexxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxx.setAnimation("undefined");
               syncablexxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof BlueSharkEntity syncablexxxxx) {
            String animation = syncablexxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxx.setAnimation("undefined");
               syncablexxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof BonnetheadSharkEntity syncablexxxxxx) {
            String animation = syncablexxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxx.setAnimation("undefined");
               syncablexxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof MakoSharkEntity syncablexxxxxxx) {
            String animation = syncablexxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxx.setAnimation("undefined");
               syncablexxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof CookiecutterSharkEntity syncablexxxxxxxx) {
            String animation = syncablexxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof BlacktipReefSharkEntity syncablexxxxxxxxx) {
            String animation = syncablexxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof BaskingSharkEntity syncablexxxxxxxxxx) {
            String animation = syncablexxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof BullSharkEntity syncablexxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof MegalodonEntity syncablexxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof LandSharkEntity syncablexxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof LemonSharkEntity syncablexxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof ThalassogerEntity syncablexxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof KrillEntity syncablexxxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof WhaleSharkEntity syncablexxxxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof PilotFishEntity syncablexxxxxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof GreaterAxodileEntity syncablexxxxxxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof RollParticleEntity syncablexxxxxxxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof GreenlandSharkEntity syncablexxxxxxxxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof WhitetipSharkEntity syncablexxxxxxxxxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }

         if (event.getEntity() instanceof BarracudaEntity syncablexxxxxxxxxxxxxxxxxxxxxxx) {
            String animation = syncablexxxxxxxxxxxxxxxxxxxxxxx.getSyncedAnimation();
            if (!animation.equals("undefined")) {
               syncablexxxxxxxxxxxxxxxxxxxxxxx.setAnimation("undefined");
               syncablexxxxxxxxxxxxxxxxxxxxxxx.animationprocedure = animation;
            }
         }
      }
   }
}
