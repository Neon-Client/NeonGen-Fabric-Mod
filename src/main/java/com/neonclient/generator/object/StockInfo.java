package com.neonclient.generator.object;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class StockInfo {
    @SerializedName("stock")
    private final int stock;

    @SerializedName("lastRestock")
    private final long lastRestock;
}
