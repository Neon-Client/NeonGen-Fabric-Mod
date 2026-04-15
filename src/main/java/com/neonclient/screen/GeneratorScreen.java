package com.neonclient.screen;

import com.neonclient.SharedVars;
import com.neonclient.generator.NeonAccountGenerator;
import com.neonclient.generator.object.StockInfo;
import com.neonclient.util.StringUtil;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Getter
public class GeneratorScreen extends Screen {
    protected GeneratorScreen() {
        super(Component.literal("Neon Account Generator"));
    }

    public static final GeneratorScreen DEFAULT = new GeneratorScreen();
    private Button generateButton;
    private Button resetButton;
    private StringWidget status;
    private StringWidget stockText;

    @Override
    protected void init() {
        this.generateButton = Button
                .builder(Component.literal("Generate Account"), _ ->
                        NeonAccountGenerator.getInstance().generateAccount(null))
                .bounds(0, 0, 150, 20)
                .build();

        this.resetButton = Button
                .builder(Component.literal("Reset"), _ -> {
                    SharedVars.useNeonAuthServers = false;
                    this.updateText("§aSwapped back to Microsoft auth servers");
                    NeonAccountGenerator.getInstance().resetSession();
                })
                .bounds(0, 0, 150, 20)
                .build();

        boolean hasKey = SharedVars.neonGenLicenseKey != null && SharedVars.endpointUrl != null;
        this.stockText = this.addRenderableWidget(new StringWidget(Component.literal(""),
                this.minecraft.font));
        this.status = this.addRenderableWidget(new StringWidget(
                Component.literal(hasKey ? "§aWaiting..." : "§c§lNo License Key"),
                this.minecraft.font));
        this.addRenderableWidget(this.generateButton);
        this.addRenderableWidget(this.resetButton);

        if (!SharedVars.firstInit) {
            SharedVars.firstInit = true;
            this.updateText("§aWaiting...");
        } else {
            this.updateText(SharedVars.lastStatusMessage);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor guiGraphics,
                                  int mouseX, int mouseY, float partialTick) {
        this.extractPanorama(guiGraphics, partialTick);

        this.generateButton.setPosition(width / 2 - this.generateButton.getWidth() / 2,
                (height / 2 - this.generateButton.getHeight() / 2) - 30);
        this.resetButton.setPosition(width / 2 - this.resetButton.getWidth() / 2,
                (height / 2 - this.resetButton.getHeight() / 2) - 5);
        this.status.setPosition(
                width / 2 - (this.minecraft.font.width(this.status.getMessage()) / 2),
                (height / 2 - this.status.getHeight() / 2) - 50);

        this.generateButton.active = SharedVars.lastAccountGenerate == -1L
                || (System.currentTimeMillis() - SharedVars.lastAccountGenerate) >= 3000L;

        StockInfo stockInfo = NeonAccountGenerator.getInstance().getStockInfo();

        if (stockInfo != null) {
            this.stockText.visible = true;
            this.stockText.setPosition(
                    width / 2 - (this.minecraft.font.width(this.stockText.getMessage()) / 2),
                    (height / 2 - this.status.getHeight() / 2) - 60);

            int accounts = stockInfo.getStock();
            long lastRestock = stockInfo.getLastRestock();
            long stockDelta = System.currentTimeMillis() - lastRestock;
            int toSeconds = (int) (stockDelta / 1000.0);

            String accountLiteral = accounts != 1 ? "accounts" : "account";
            this.stockText.setMessage(Component.literal("§7§lStock: §a" + accounts + " "
                    + accountLiteral + " §r§l| §7§lLast Restock: §a"
                    + StringUtil.formatTimeFromSeconds(toSeconds)));

            int width = this.minecraft.font.width(this.stockText.getMessage());
            this.stockText.setWidth(width);
            this.stockText.setMaxWidth(Integer.MAX_VALUE);
        } else {
            this.stockText.visible = false;
        }
    }

    public void updateText(String text) {
        SharedVars.lastStatusMessage = text;
        if (this.status != null) {
            this.status.setMessage(Component.literal(text));

            int width = this.minecraft.font.width(this.status.getMessage());
            this.status.setWidth(width);
            this.status.setMaxWidth(Integer.MAX_VALUE);
        }
    }
}