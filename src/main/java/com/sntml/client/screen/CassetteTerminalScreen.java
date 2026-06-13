package com.sntml.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CassetteTerminalScreen extends Screen {

    // Текст который будет печататься
    private static final String[] LINES = {
        "> ACCESSING FILE...",
        "> PROTOCOL #CVOF14",
        "> DATE: ??/??/??",
        "> OPERATOR: [REDACTED]",
        "> STATUS: [CORRUPTED]",
        "> WARNING: DATA INCOMPLETE",
        "> LOADING FOOTAGE...",
        ".",
        "..",
        "...",
        "> TRANSFERRING SUBJECT",
    };

    private final List<String> displayedLines = new ArrayList<>();
    private int currentLine = 0;       // текущая строка
    private int currentChar = 0;       // текущий символ в строке
    private int tickCounter = 0;
    private int blinkTick = 0;
    private boolean finished = false;
    private int finishDelay = 0;

    // Callback — что делать после анимации (телепортация)
    private final Runnable onFinish;

    public CassetteTerminalScreen(Runnable onFinish) {
        super(Component.empty());
        this.onFinish = onFinish;
    }

    @Override
    public boolean isPauseScreen() {
        return false; // не ставит игру на паузу
    }

    @Override
    public void tick() {
        tickCounter++;
        blinkTick++;

        if (finished) {
            finishDelay++;
            // Через 2 секунды после завершения — телепортируем
            if (finishDelay > 40) {
                this.onClose();
                if (onFinish != null) onFinish.run();
            }
            return;
        }

        // Каждые 2 тика печатаем новый символ (скорость печати)
        if (tickCounter % 2 == 0) {
            if (currentLine < LINES.length) {
                String line = LINES[currentLine];

                if (currentChar < line.length()) {
                    // Добавляем символ к текущей строке
                    if (displayedLines.size() <= currentLine) {
                        displayedLines.add("");
                    }
                    displayedLines.set(currentLine,
                        displayedLines.get(currentLine) + line.charAt(currentChar));
                    currentChar++;
                } else {
                    // Строка закончилась — переходим к следующей
                    currentLine++;
                    currentChar = 0;

                    // Пауза между строками (20 тиков = 1 секунда)
                    tickCounter = -18;
                }
            } else {
                finished = true;
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Чёрный фон на весь экран
        graphics.fill(0, 0, this.width, this.height, 0xFF000000);

        int x = 20;
        int y = 20;
        int lineHeight = 12;

        // Рисуем все напечатанные строки
        for (int i = 0; i < displayedLines.size(); i++) {
            String line = displayedLines.get(i);
            // Зелёный цвет как в старых терминалах
            graphics.drawString(this.font, line, x, y + i * lineHeight, 0xFF00FF41);
        }

        // Мигающий курсор на последней строке
        if (!finished && blinkTick % 20 < 10) {
            int cursorY = y + displayedLines.size() * lineHeight;
            int cursorX = x;
            if (!displayedLines.isEmpty()) {
                cursorX = x + this.font.width(displayedLines.get(displayedLines.size() - 1));
            }
            graphics.drawString(this.font, "█", cursorX, cursorY, 0xFF00FF41);
        }

        // После завершения — мигающее сообщение
        if (finished && blinkTick % 20 < 10) {
            int finalY = y + (displayedLines.size() + 2) * lineHeight;
            graphics.drawString(this.font, "> INITIALIZING TRANSFER...", x, finalY, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Блокируем все клавиши во время анимации
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Блокируем клик мышью
        return true;
    }
}
