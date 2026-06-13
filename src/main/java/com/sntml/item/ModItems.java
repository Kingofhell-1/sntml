package com.sntml.item;

import com.sntml.SNTMLMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SNTMLMod.MOD_ID);

    // Кассета — основной предмет для входа в found footage измерения
    public static final RegistryObject<Item> CASSETTE =
            ITEMS.register("cassette", () ->
                    new CassetteItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
