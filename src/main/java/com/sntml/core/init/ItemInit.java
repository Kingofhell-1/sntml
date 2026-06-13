package com.sntml.core.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
        public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, "sntml");

    public static final RegistryObject<Item> MILKI_TAPE = ITEMS.register("vhs_tape_milki",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> EXPERIMENT_TAPE = ITEMS.register("vhs_tape_experiment",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
}