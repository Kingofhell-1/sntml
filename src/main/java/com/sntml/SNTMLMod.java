package com.sntml;

import com.sntml.arg.ARGMechanicHandler;
import com.sntml.entity.ModEntities;
import com.sntml.entity.ObserverEntity;
import com.sntml.entity.PursuerEntity;
import com.sntml.item.ModItems;
import com.sntml.world.ModStructures;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(SNTMLMod.MOD_ID)
public class SNTMLMod {
    public static final String MOD_ID = "sntml";

    public SNTMLMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        com.sntml.core.init.ItemInit.ITEMS.register(modEventBus);
        com.sntml.core.init.BlockInit.BLOCKS.register(modEventBus);
        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModStructures.register(modEventBus);

        // ARG Mechanics
        MinecraftForge.EVENT_BUS.register(ARGMechanicHandler.class);
        modEventBus.addListener(this::registerAttributes);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.OBSERVER.get(), ObserverEntity.createAttributes().build());
        event.put(ModEntities.PURSUER.get(), PursuerEntity.createAttributes().build());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModStructures::setupPieceTypes);
    }
}
