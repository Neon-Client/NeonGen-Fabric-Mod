package com.neonclient.mixin.screen;

import com.neonclient.SharedVars;
import com.neonclient.generator.NeonAccountGenerator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {
    @Final
    @Shadow
    private LinearLayout layout;

    @Final
    @Shadow
    private Screen parent;

    protected DisconnectedScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/layouts/LinearLayout;arrangeElements()V"))
    private void addButtons(CallbackInfo ci) {
        if (SharedVars.neonGenLicenseKey == null
                || SharedVars.endpointUrl == null
                || SharedVars.lastKnownServerAddress == null
                || SharedVars.lastKnownServerData == null) return;

        this.layout.addChild(
                Button.builder(Component.literal("Reconnect with NeonGen"), button -> {
                    button.active = false;
                    NeonAccountGenerator.getInstance().reconnectUsingGenerator(loggedIn -> {
                        if (loggedIn) {
                            this.minecraft.execute(() ->
                                    ConnectScreen.startConnecting(
                                            parent,
                                            this.minecraft,
                                            SharedVars.lastKnownServerAddress,
                                            SharedVars.lastKnownServerData,
                                            false,
                                            null
                                    )
                            );
                            return;
                        }
                        this.minecraft.execute(() -> button.active = true);
                    });
                }).width(200).build()
        );
    }
}
