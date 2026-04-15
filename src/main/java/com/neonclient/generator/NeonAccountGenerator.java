package com.neonclient.generator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neonclient.SharedVars;
import com.neonclient.generator.object.NeonAccount;
import com.neonclient.generator.object.NeonResult;
import com.neonclient.generator.object.StockInfo;
import com.neonclient.screen.GeneratorScreen;
import com.neonclient.util.GsonUtil;
import com.neonclient.util.HTTPUtil;
import com.neonclient.util.MinecraftProvider;
import com.neonclient.util.ThreadUtil;
import lombok.Getter;
import net.minecraft.client.User;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public class NeonAccountGenerator implements MinecraftProvider {
    @Getter
    private static final NeonAccountGenerator instance = new NeonAccountGenerator();
    private static volatile String uuid, accessToken;
    private StockInfo stockInfo;

    public void startStockUpdater() {
        ThreadUtil.task(() -> this.stockInfo = this.getStockInformation(), 0L, 30L, TimeUnit.SECONDS);
    }

    public void loadLicenseKey() {
        File folder = MC.gameDirectory.toPath().resolve("NeonGen").toFile();
        if (!folder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            folder.mkdir();
        }

        File configFile = new File(folder, "neon-gen-license.dat");

        if (configFile.exists()) {
            try {
                String data = Files.readString(configFile.toPath());
                data = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
                JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();

                if (jsonObject.has("licenseKey") && jsonObject.has("endpoint")) {
                    SharedVars.neonGenLicenseKey = jsonObject.get("licenseKey").getAsString();
                    SharedVars.endpointUrl = jsonObject.get("endpoint").getAsString();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private StockInfo getStockInformation() {
        if (SharedVars.neonGenLicenseKey == null) {
            return null;
        }

        String response = HTTPUtil.getResponse("stock");

        if (response != null) {
            return GsonUtil.GSON.fromJson(response, StockInfo.class);
        }

        return null;
    }

    public void reconnectUsingGenerator(Consumer<Boolean> onComplete) {
        SharedVars.useNeonAuthServers = true;
        this.generateAccount(onComplete);
    }

    public void generateAccount(Consumer<Boolean> callback) {
        this.loadLicenseKey();

        if (SharedVars.neonGenLicenseKey == null
                || SharedVars.endpointUrl == null
                || SharedVars.neonGenLicenseKey.length() <= 10) {
            GeneratorScreen.DEFAULT.updateText("§c§lNo License Key");
            return;
        }

        SharedVars.lastAccountGenerate = System.currentTimeMillis();
        GeneratorScreen.DEFAULT.updateText("§8Getting Account...");

        ThreadUtil.execute(() -> {
            String response = HTTPUtil.getResponse("generate");

            if (response != null) {
                NeonResult neonResult = GsonUtil.GSON.fromJson(response, NeonResult.class);

                if (neonResult != null) {
                    NeonAccount neonAccount = neonResult.getNeonAccount();

                    if (neonAccount != null) {
                        boolean hasError = !neonResult.getMessage().isEmpty();

                        if (hasError) {
                            GeneratorScreen.DEFAULT.updateText("§c§lERROR: §7" + neonResult.getMessage());
                        } else {
                            String username = neonAccount.getUsername();
                            String accessTokenLocal = neonAccount.getAccessToken();
                            String uuidLocal = neonAccount.getUuid();

                            UUID parsedUuid;
                            try {
                                parsedUuid = UUID.fromString(uuidLocal);
                            } catch (IllegalArgumentException e) {
                                GeneratorScreen.DEFAULT.updateText("§c§lERROR: §7Invalid UUID");
                                return;
                            }

                            SharedVars.useNeonAuthServers = true;
                            GeneratorScreen.DEFAULT.updateText("§aLogged into §7" + username);

                            synchronized (MC) {
                                accessToken = accessTokenLocal;
                                uuid = uuidLocal;
                                MC.user = new User(
                                        username,
                                        parsedUuid,
                                        accessTokenLocal,
                                        Optional.empty(),
                                        Optional.empty()
                                );
                                SharedVars.startSession = MC.getUser();
                            }

                            callback.accept(true);
                            return;
                        }
                    } else {
                        GeneratorScreen.DEFAULT.updateText("§c§lERROR: §7No accounts in stock");
                    }
                } else {
                    GeneratorScreen.DEFAULT.updateText("§c§lERROR: §7No accounts in stock");
                }
            } else {
                GeneratorScreen.DEFAULT.updateText("§c§lERROR: §7Timeout");
            }

            callback.accept(false);
        });
    }

    public void sendServerAuth(String digest) {
        if (accessToken == null || uuid == null) {
            return;
        }

        HTTPUtil.sendAuthenticationRequest(accessToken, uuid, digest);
    }

    public void resetSession() {
        User newUser = Objects.requireNonNullElseGet(SharedVars.startSession, () -> new User(
                "Player",
                UUID.randomUUID(),
                "",
                Optional.empty(),
                Optional.empty()
        ));
        synchronized (MC) {
            MC.user = newUser;
        }
    }
}
