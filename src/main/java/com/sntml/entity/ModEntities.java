package com.sntml.entity;

import com.sntml.SNTMLMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SNTMLMod.MOD_ID);

    // Наблюдатель — просто стоит и смотрит
    public static final RegistryObject<EntityType<ObserverEntity>> OBSERVER =
            ENTITY_TYPES.register("observer", () ->
                    EntityType.Builder.<ObserverEntity>of(ObserverEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.9f) // размер хитбокса как у игрока
                            .build(new ResourceLocation(SNTMLMod.MOD_ID, "observer").toString()));

    // Преследователь — следует за игроком
    public static final RegistryObject<EntityType<PursuerEntity>> PURSUER =
            ENTITY_TYPES.register("pursuer", () ->
                    EntityType.Builder.<PursuerEntity>of(PursuerEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.9f)
                            .build(new ResourceLocation(SNTMLMod.MOD_ID, "pursuer").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
