package net.mcreator.sharks.procedures;

import java.util.Comparator;
import net.mcreator.sharks.entity.GreenlandSharkEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GreenlandSharkOnInitialEntitySpawnProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity instanceof GreenlandSharkEntity) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
               .stream()
               .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
               .toList()) {
               if (entityiterator instanceof ElderGuardian || entityiterator instanceof Guardian) {
                  if (!((entityiterator instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) instanceof LivingEntity)) {
                     if (entityiterator instanceof Mob _entity && entity instanceof LivingEntity _ent) {
                        _entity.setTarget(_ent);
                     }
                  } else if (Math.sqrt(
                           Math.pow(entityiterator.getX() - entity.getX(), 2.0)
                              + Math.pow(entityiterator.getY() - entity.getY(), 2.0)
                              + Math.pow(entityiterator.getZ() - entity.getZ(), 2.0)
                        )
                        < Math.sqrt(
                           Math.pow(entityiterator.getX() - (entityiterator instanceof Mob _mobEntxxx ? _mobEntxxx.getTarget() : null).getX(), 2.0)
                              + Math.pow(entityiterator.getY() - (entityiterator instanceof Mob _mobEntxx ? _mobEntxx.getTarget() : null).getY(), 2.0)
                              + Math.pow(entityiterator.getZ() - (entityiterator instanceof Mob _mobEntx ? _mobEntx.getTarget() : null).getZ(), 2.0)
                        )
                     && entityiterator instanceof Mob _entity
                     && entity instanceof LivingEntity _ent) {
                     _entity.setTarget(_ent);
                  }
               }
            }
         }
      }
   }
}
