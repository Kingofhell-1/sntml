package com.sntml.client;

import com.sntml.entity.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "sntml", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(FMLClientSetupEvent event) {
        // NoopRenderer — невидимый рендерер, entity существует но не отрисовывается
        // Это намеренно — entity будет невидимым, только хитбокс
        EntityRenderers.register(ModEntities.OBSERVER.get(), NoopRenderer::new);
        EntityRenderers.register(ModEntities.PURSUER.get(), NoopRenderer::new);
    }
}
