package com.example.tms.model.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PersonalResource {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("executeTime")
    private Integer executeTime;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("id")
        private Integer id;
        @SerializedName("person_img")
        private String personImg;
    }
}
