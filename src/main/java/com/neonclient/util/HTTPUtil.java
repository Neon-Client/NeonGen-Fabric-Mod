package com.neonclient.util;

import com.google.gson.JsonObject;
import com.neonclient.SharedVars;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@UtilityClass
public class HTTPUtil {
    public void sendAuthenticationRequest(String accessToken, String uuid, String digest) {
        if (SharedVars.neonGenLicenseKey == null || SharedVars.endpointUrl == null) {
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("accessToken", accessToken);
        jsonObject.addProperty("selectedProfile", uuid);
        jsonObject.addProperty("serverId", digest);
        jsonObject.addProperty("licenseKey", SharedVars.neonGenLicenseKey);

        try (HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SharedVars.endpointUrl + "forwardGameRequest"))
                    .header("Content-Type", "application/json; utf-8")
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString(), StandardCharsets.UTF_8))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getResponse(String callPoint) {
        if (SharedVars.neonGenLicenseKey == null || SharedVars.endpointUrl == null) {
            return null;
        }

        try (HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SharedVars.endpointUrl + callPoint))
                    .header("User-Agent", "Neon Gen/1.0")
                    .header("license", SharedVars.neonGenLicenseKey)
                    .header("unban-type", SharedVars.unbanType)
                    .timeout(Duration.ofSeconds(SharedVars.unbanType.equalsIgnoreCase("None") ? 5 : 125))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }
}
