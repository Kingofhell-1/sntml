package com.sntml.core.init;

import com.sntml.entity.ObserverEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    // Явно указываем строковый ID мода "sntml"
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "sntml");

    // Регистрируем через классический метод Forge без лишних лямбда-оберток внутри билдера
    public static final RegistryObject<EntityType<ObserverEntity>> OBSERVER = ENTITIES.register("observer",
            () -> EntityType.Builder.<ObserverEntity>of(ObserverEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build("sntml:observer")); // Здесь тоже жестко пишем строковый ID вместо динамического
}