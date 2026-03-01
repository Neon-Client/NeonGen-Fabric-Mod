package com.neonclient.generator.object;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class NeonAccount {
    @SerializedName("uuid")
    private final String uuid;

    @SerializedName("accessToken")
    private final String accessToken;

    @SerializedName("username")
    private final String username;

    @SerializedName("lastUpdate")
    private final long lastUpdate;

    @SerializedName("accountType")
    private final String accountType;
}
