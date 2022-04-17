package com.example.tms.model.domain;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseOther {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Integer data;
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("executeTime")
    private Integer executeTime;
}
