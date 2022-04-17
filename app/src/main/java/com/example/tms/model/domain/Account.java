package com.example.tms.model.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@lombok.NoArgsConstructor
@lombok.Data
public class Account {

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

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class DataDTO {
        @SerializedName("account")
        private Integer account;
        @SerializedName("role")
        private Integer role;
        @SerializedName("password")
        private String password;

        public DataDTO(Integer account, Integer role, String password) {
            this.account = account;
            this.role = role;
            this.password = password;
        }
    }
}
