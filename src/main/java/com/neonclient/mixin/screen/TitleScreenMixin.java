package com.neonclient.mixin.screen;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.neonclient.screen.GeneratorScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "CONSTANT", args = "intValue=3", ordinal = 0))
    private void addGeneratorButton(CallbackInfo info, @Local(ordinal = 3) LocalIntRef menuY) {
        LinearLayout row = LinearLayout.horizontal();
        Button generatorButton = row.addChild(
                Button.builder(
                                Component.literal("Neon Gen"),
                                _ -> minecraft.gui.setScreen(GeneratorScreen.DEFAULT))
                        .width(200)
                        .build());

        row.setX(this.width / 2 - 100);
        row.setY(menuY.get() + 24);
        row.arrangeElements();
        addRenderableWidget(generatorButton);

        menuY.set(menuY.get() + 24);
    }
}
