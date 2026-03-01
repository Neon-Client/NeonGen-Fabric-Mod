package com.neonclient.mixin.screen;

import com.llamalad7.mixinextras.sugar.Local;
import com.neonclient.screen.GeneratorScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.components.Button;
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

    @Inject(method = "init", at =
    @At(value = "INVOKE", target =
            "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
            ordinal = 1))
    private void addGeneratorButton(CallbackInfo info, @Local(ordinal = 3) int l) {
        addRenderableWidget(
                Button.builder(
                                Component.literal("Neon Gen"),
                                a -> minecraft.setScreen(GeneratorScreen.DEFAULT))
                        .bounds(this.width / 2 - 100, l + 24, 200, 20)
                        .build());
    }
}