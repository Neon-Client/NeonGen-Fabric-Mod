package com.neonclient.entry;

import com.neonclient.SharedVars;
import com.neonclient.generator.NeonAccountGenerator;
import com.neonclient.util.MinecraftProvider;
import net.fabricmc.api.ModInitializer;

public class ModEntryPoint implements ModInitializer, MinecraftProvider {

    @Override
    public void onInitialize() {
        SharedVars.startSession = MC.getUser();
        NeonAccountGenerator.getInstance().loadLicenseKey();
        NeonAccountGenerator.getInstance().startStockUpdater();
    }
}
