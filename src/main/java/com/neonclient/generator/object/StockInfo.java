package com.neonclient.generator.object;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public class StockInfo {
    @SerializedName("stock")
    private final int stock;

    @SerializedName("lastRestock")
    private final long lastRestock;

    @SerializedName("unbanTypes")
    private final List<String> unbanTypes;
}
