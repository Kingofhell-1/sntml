package com.sntml.client;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = "sntml", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ResourcePackHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Запускаем после загрузки клиента
        event.enqueueWork(() -> {
            enableProgrammerArt();
        });
    }

    private static void enableProgrammerArt() {
        Minecraft mc = Minecraft.getInstance();
        PackRepository packRepository = mc.getResourcePackRepository();

        // Обновляем список доступных паков
        packRepository.reload();

        // Ищем Programmer Art пак
        Pack programmerArt = packRepository.getPack("programer_art"); // именно так пишется в коде Minecraft

        if (programmerArt == null) {
            // Попробуем альтернативное название
            programmerArt = packRepository.getPack("programmer_art");
        }

        if (programmerArt != null) {
            // Получаем текущий список включённых паков
            Collection<String> currentPacks = packRepository.getSelectedIds();
            List<String> newPacks = new ArrayList<>(currentPacks);

            // Добавляем Programmer Art если ещё не включён
            if (!newPacks.contains(programmerArt.getId())) {
                newPacks.add(programmerArt.getId());

                // Применяем новый список паков
                packRepository.setSelected(newPacks);
                mc.reloadResourcePacks();

                System.out.println("[SNTML.files] Programmer Art enabled.");
            }
        } else {
            System.out.println("[SNTML.files] Programmer Art pack not found.");
        }
    }
}
