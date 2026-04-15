package com.neonclient.mixin.network;

import com.neonclient.SharedVars;
import com.neonclient.generator.NeonAccountGenerator;
import com.neonclient.util.MinecraftProvider;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientHandshakePacketListenerImpl.class)
public class ClientLoginNetworkHandlerMixin implements MinecraftProvider {

    @Inject(method = {"authenticateServer"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void joinServerSession(String digest, CallbackInfoReturnable<Component> cir) {
        if (SharedVars.useNeonAuthServers) {
            NeonAccountGenerator.getInstance().sendServerAuth(digest);
            cir.cancel();
        }
    }
}
