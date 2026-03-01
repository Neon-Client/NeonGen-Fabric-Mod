package com.neonclient.generator.object;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class NeonResult {
    @SerializedName("message")
    private final String message;

    @SerializedName("neonAccount")
    private NeonAccount neonAccount;
}
