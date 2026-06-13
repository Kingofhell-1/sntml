package com.sntml.item;

import com.sntml.client.screen.CassetteTerminalScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CassetteItem extends Item {

    public CassetteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            net.minecraftforge.fml.loading.FMLEnvironment.dist.isClient();
            openTerminalClient();
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openTerminalClient() {
        Minecraft.getInstance().setScreen(
            new CassetteTerminalScreen(() -> {
                Minecraft.getInstance().setScreen(null);
            })
        );
    }
}
