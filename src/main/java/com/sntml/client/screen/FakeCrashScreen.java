package com.sntml.client.screen;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class FakeCrashScreen extends Screen {

    private int tickCounter = 0;
    private final Random random = new Random();
    private final Runnable onCloseCallback;

    public FakeCrashScreen(Runnable onCloseCallback) {
        super(Component.literal("CRITICAL ERROR"));
        this.onCloseCallback = onCloseCallback;
    }

    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter > 100) { // ~5 seconds
            if (onCloseCallback != null) onCloseCallback.run();
            Minecraft.getInstance().setScreen(null);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        PoseStack pose = graphics.pose();
        int w = this.width;
        int h = this.height;

        // Blue screen / glitch background
        graphics.fill(0, 0, w, h, 0xFF0000AA);

        // VHS glitch lines
        for (int i = 0; i < 25; i++) {
            if (random.nextFloat() < 0.6f) {
                int y = random.nextInt(h);
                graphics.fill(0, y, w, y + 3, 0x88FFFFFF);
            }
        }

        // Main error text
        String[] lines = {
            "CRITICAL ERROR: Connection to Subject lost",
            "Anti-Parasite protocol failed",
            "ENTITY OVERRIDE DETECTED",
            "WHTTAT-INDEX-17",
            "",
            "WE HAVE TO TALK ABOUT THAT."
        };

        int y = h / 2 - 60;
        for (String line : lines) {
            graphics.drawCenteredString(font, line, w / 2, y, 0xFFFFFFFF);
            y += 18;
        }

        if (tickCounter % 4 < 2) {
            graphics.drawCenteredString(font, "SYSTEM HALTED", w / 2, y + 30, 0xFFFF0000);
        }

        // Random binary garbage
        if (random.nextFloat() < 0.3f) {
            graphics.drawString(font, "01 10 11 00 01 10", random.nextInt(w-100), random.nextInt(h-50), 0xAA00FF00);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
