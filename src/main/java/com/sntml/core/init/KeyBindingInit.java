package com.sntml.core.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.sntml.SNTMLMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SNTMLMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindingInit {
    
    // Создаем саму кнопку. "O" — клавиша по умолчанию, "category.sntml" — раздел в настройках управления
    public static final KeyMapping DEBUG_MENU_KEY = new KeyMapping(
            "key.sntml.debug_menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O, 
            "category.sntml.horror"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        // Регистрируем клавишу в системе игры
        event.register(DEBUG_MENU_KEY);
    }
}