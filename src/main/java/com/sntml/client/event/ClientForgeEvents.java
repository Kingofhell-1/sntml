package com.sntml.client.event;

import com.sntml.SNTMLMod;
import com.sntml.client.screen.DebugMenuScreen;
import com.sntml.core.init.KeyBindingInit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SNTMLMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Проверяем, что тик завершился, игра запущена, и игрок находится в мире
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            
            // Проверяем, нажата ли наша клавиша "O" и не открыто ли сейчас какое-то другое меню
            if (KeyBindingInit.DEBUG_MENU_KEY.consumeClick() && Minecraft.getInstance().screen == null) {
                // Открываем твое кастомное дебаг-меню!
                Minecraft.getInstance().setScreen(new DebugMenuScreen());
            }
        }
    }
}