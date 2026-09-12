package net.mcreator.sharks.init;

import net.mcreator.sharks.client.renderer.AxodileRenderer;
import net.mcreator.sharks.client.renderer.BarracudaRenderer;
import net.mcreator.sharks.client.renderer.BaskingSharkRenderer;
import net.mcreator.sharks.client.renderer.BlacktipReefSharkRenderer;
import net.mcreator.sharks.client.renderer.BlueSharkRenderer;
import net.mcreator.sharks.client.renderer.BonnetheadSharkRenderer;
import net.mcreator.sharks.client.renderer.BullSharkRenderer;
import net.mcreator.sharks.client.renderer.CookiecutterSharkRenderer;
import net.mcreator.sharks.client.renderer.GreaterAxodileRenderer;
import net.mcreator.sharks.client.renderer.GreenlandSharkRenderer;
import net.mcreator.sharks.client.renderer.KrillRenderer;
import net.mcreator.sharks.client.renderer.LandSharkRenderer;
import net.mcreator.sharks.client.renderer.LemonSharkRenderer;
import net.mcreator.sharks.client.renderer.MakoSharkRenderer;
import net.mcreator.sharks.client.renderer.MegalodonRenderer;
import net.mcreator.sharks.client.renderer.NurseSharkRenderer;
import net.mcreator.sharks.client.renderer.PilotFishRenderer;
import net.mcreator.sharks.client.renderer.RemoraRenderer;
import net.mcreator.sharks.client.renderer.RollParticleRenderer;
import net.mcreator.sharks.client.renderer.SeekerSharkProjectileRenderer;
import net.mcreator.sharks.client.renderer.SeekingArrowRenderer;
import net.mcreator.sharks.client.renderer.ShrakRenderer;
import net.mcreator.sharks.client.renderer.ThalassogerRenderer;
import net.mcreator.sharks.client.renderer.TigerSharkRenderer;
import net.mcreator.sharks.client.renderer.WhaleSharkRenderer;
import net.mcreator.sharks.client.renderer.WhitetipSharkRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;

@EventBusSubscriber(
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class BenssharksModEntityRenderers {
   @SubscribeEvent
   public static void registerEntityRenderers(RegisterRenderers event) {
      event.registerEntityRenderer((EntityType)BenssharksModEntities.GREATWHITESHARK.get(), ShrakRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.NURSE_SHARK.get(), NurseSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.REMORA.get(), RemoraRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.TIGER_SHARK.get(), TigerSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.AXODILE.get(), AxodileRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.BLUE_SHARK.get(), BlueSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.BONNETHEAD_SHARK.get(), BonnetheadSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.MAKO_SHARK.get(), MakoSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.COOKIECUTTER_SHARK.get(), CookiecutterSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.BLACKTIP_REEF_SHARK.get(), BlacktipReefSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.BASKING_SHARK.get(), BaskingSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.BULL_SHARK.get(), BullSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.MEGALODON.get(), MegalodonRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.LAND_SHARK.get(), LandSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.LEMON_SHARK.get(), LemonSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.THALASSOGER.get(), ThalassogerRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.SEEKER_SHARK_PROJECTILE.get(), SeekerSharkProjectileRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.KRILL.get(), KrillRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.WHALE_SHARK.get(), WhaleSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.SEEKING_ARROW.get(), SeekingArrowRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.PILOT_FISH.get(), PilotFishRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.GREATER_AXODILE.get(), GreaterAxodileRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.ROLL_PARTICLE.get(), RollParticleRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.GREENLAND_SHARK.get(), GreenlandSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.WHITETIP_SHARK.get(), WhitetipSharkRenderer::new);
      event.registerEntityRenderer((EntityType)BenssharksModEntities.BARRACUDA.get(), BarracudaRenderer::new);
   }
}
