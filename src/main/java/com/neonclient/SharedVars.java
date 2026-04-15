package com.neonclient;

import net.minecraft.client.User;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class SharedVars {
    public static String lastStatusMessage;
    public static boolean firstInit;
    public static long lastAccountGenerate = -1L;
    public static boolean useNeonAuthServers;
    public static String neonGenLicenseKey;
    public static String endpointUrl;
    public static User startSession;
    public static ServerAddress lastKnownServerAddress;
    public static ServerData lastKnownServerData;
}
