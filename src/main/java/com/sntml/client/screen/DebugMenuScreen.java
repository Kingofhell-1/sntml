package com.sntml.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DebugMenuScreen extends Screen {

    private static boolean vhsEnabled = true;
    private Button vhsButton;

    public DebugMenuScreen() {
        super(Component.literal("§6SNTML DEBUG MENU"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 50;
        int buttonWidth = 280;

        this.addRenderableWidget(Button.builder(
                Component.literal("§c[1] Установить 100 Паранойи + ARG"),
                btn -> execute(1)
        ).pos(centerX - buttonWidth / 2, startY).width(buttonWidth).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("§a[2] Сбросить Паранойю до 0"),
                btn -> execute(2)
        ).pos(centerX - buttonWidth / 2, startY + 25).width(buttonWidth).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("§7[3] Заспавнить Наблюдателя"),
                btn -> execute(3)
        ).pos(centerX - buttonWidth / 2, startY + 55).width(buttonWidth).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("§c[4] Заспавнить Преследователя"),
                btn -> execute(4)
        ).pos(centerX - buttonWidth / 2, startY + 80).width(buttonWidth).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("§b[5] Алтарь / Монолит / Лагерь (locate)"),
                btn -> execute(5)
        ).pos(centerX - buttonWidth / 2, startY + 110).width(buttonWidth).build());

        this.vhsButton = Button.builder(
                Component.literal(vhsEnabled ? "§e[6] Выключить VHS эффект" : "§e[6] Включить VHS эффект"),
                btn -> execute(6)
        ).pos(centerX - buttonWidth / 2, startY + 140).width(buttonWidth).build();
        this.addRenderableWidget(this.vhsButton);

        this.addRenderableWidget(Button.builder(
                Component.literal("§6[7] Создать ARG Warning File"),
                btn -> execute(7)
        ).pos(centerX - buttonWidth / 2, startY + 170).width(buttonWidth).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("§d[8] Выдать Кассету"),
                btn -> execute(8)
        ).pos(centerX - buttonWidth / 2, startY + 200).width(buttonWidth).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("§fЗакрыть меню"),
                btn -> this.onClose()
        ).pos(centerX - 70, this.height - 40).width(140).build());
    }

    private void execute(int id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        switch (id) {
            case 1 -> sendServerCommand("/debugmenu maxparanoia");
            case 2 -> sendServerCommand("/debugmenu resetparanoia");
            case 3 -> sendServerCommand("/debugmenu spawnobserver");
            case 4 -> sendServerCommand("/debugmenu spawnpursuer");
            case 5 -> mc.player.sendSystemMessage(Component.literal("§bИспользуй /locate structure sntml:altar"));
            case 6 -> toggleVhs();
            case 7 -> sendServerCommand("/debugmenu createargwarning");
            case 8 -> sendServerCommand("/debugmenu givecassette");
        }
    }

private void sendServerCommand(String command) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player != null && mc.getConnection() != null) {
        // Безопасная отправка команды на сервер в Forge 1.20.1 через пакет подписи команд
        if (command.startsWith("/")) {
            String rawCommand = command.substring(1);
            mc.getConnection().sendChat(rawCommand);
        } else {
            mc.getConnection().sendChat(command);
        }
    }
}
    private void toggleVhs() {
        vhsEnabled = !vhsEnabled;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendSystemMessage(Component.literal(vhsEnabled ? "§eVHS включён" : "§eVHS выключен"));
        }
        if (this.vhsButton != null) {
            this.vhsButton.setMessage(Component.literal(vhsEnabled ? "§e[6] Выключить VHS эффект" : "§e[6] Включить VHS эффект"));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(font, "§6SNTML DEBUG MENU", this.width / 2, 20, 0xFFAA00);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}