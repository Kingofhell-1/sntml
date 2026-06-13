package com.sntml.core.init;

import com.sntml.entity.ModEntities;
import com.sntml.entity.ObserverEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.sntml.SNTMLMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SNTMLMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        // Регистрируем базовую команду /debugmenu
        dispatcher.register(Commands.literal("debugmenu")
            .requires(source -> source.hasPermission(2)) // Только для операторов (креатив/чит-коды)
            
            // Подкоманда: maxparanoia
            .then(Commands.literal("maxparanoia").executes(context -> {
                ServerPlayer player = context.getSource().getPlayer();
                if (player != null) {
                    player.sendSystemMessage(Component.literal("§c[SNTML] Паранойя выкручена на 100%!"));
                    // Здесь в будущем будет вызов: ClientForgeEvents.updatePlayerParanoia(player, 100, true);
                }
                return 1;
            }))
            
            // Подкоманда: resetparanoia
            .then(Commands.literal("resetparanoia").executes(context -> {
                ServerPlayer player = context.getSource().getPlayer();
                if (player != null) {
                    player.sendSystemMessage(Component.literal("§a[SNTML] Паранойя сброшена до нуля."));
                }
                return 1;
            }))
            
            // Подкоманда: givecassette
            .then(Commands.literal("givecassette").executes(context -> {
                ServerPlayer player = context.getSource().getPlayer();
                if (player != null) {
                    player.sendSystemMessage(Component.literal("§e[SNTML] Выдана поврежденная кассета."));
                }
                return 1;
            }))
            // Заспавнить Наблюдателя вдалеке
            .then(Commands.literal("spawnobserver").executes(context -> {
                ServerPlayer player = context.getSource().getPlayer();
                if (player != null) {
                    net.minecraft.server.level.ServerLevel world = player.serverLevel();
                    
                    // Вычисляем позицию для скрытого спавна вдалеке от игрока
                    // Берем направление взгляда игрока, отступаем на 25 блоков вперед и немного смещаем вбок
                    net.minecraft.world.phys.Vec3 lookVec = player.getLookAngle().normalize();
                    double spawnX = player.getX() + (lookVec.x * 25.0) + (player.getRandom().nextDouble() * 4.0 - 2.0);
                    double spawnZ = player.getZ() + (lookVec.z * 25.0) + (player.getRandom().nextDouble() * 4.0 - 2.0);
                    double spawnY = player.getY(); // На уровне ног игрока
                    
                    // Корректируем Y, чтобы моб не завис в воздухе (ищем твердую землю)
                    net.minecraft.core.BlockPos spawnPos = new net.minecraft.core.BlockPos((int)spawnX, (int)spawnY, (int)spawnZ);
                    
                    // Создаем экземпляр нашего Наблюдателя
                    ObserverEntity observer1 = ModEntities.OBSERVER.get().create(world);
                    
                    if (observer1 != null) {
                        observer1.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, player.getYRot() + 180.0F, 0.0F);
                        
                        // Спавним сущность в мир
                        world.addFreshEntity(observer1);

                        player.sendSystemMessage(Component.literal("§7[SNTML] Вы чувствуете на себе чей-то пристальный взгляд..."));
                    }
                }
                return 1;
            }))
        );
    }

}