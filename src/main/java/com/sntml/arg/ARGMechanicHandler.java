package com.sntml.arg;

import com.sntml.SNTMLMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

@Mod.EventBusSubscriber(modid = SNTMLMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ARGMechanicHandler {

    private static boolean warningFileCreated = false;

    public static void checkAndCreateWarningFile(ServerPlayer player, int paranoiaLevel) {
        if (paranoiaLevel >= 70 && !warningFileCreated) {
            try {
                String mcDir = player.getServer().getServerDirectory().getAbsolutePath();
                File warningFile = new File(mcDir, "WHTTAT_WARNING.txt");

                try (FileWriter writer = new FileWriter(warningFile)) {
                    writer.write("=== WHTTAT FILES ===\n\n");
                    writer.write("Оно проникло в код.\n");
                    writer.write("Перезапуск клиента не поможет.\n\n");
                    writer.write("WE HAVE TO TALK ABOUT THAT.\n");
                    writer.write("INDEX-17\n");
                    writer.write("MCN-ENTITY-OVERRIDE ACTIVE\n");
                }

                warningFileCreated = true;
                System.out.println("[WHTTAT] Created corrupted log file: " + warningFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
