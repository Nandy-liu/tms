package com.example.tms.model.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Ask {

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
        @SerializedName("ask_id")
        private Integer askId;
        @SerializedName("course_id")
        private Integer courseId;
        @SerializedName("student_id")
        private Integer studentId;
        @SerializedName("ask")
        private String ask;
        @SerializedName("answer")
        private String answer;
        @SerializedName("id")
        private Integer id;
        @SerializedName("name")
        private String name;
        @SerializedName("sex")
        private String sex;
        @SerializedName("age")
        private Integer age;
        @SerializedName("department")
        private String department;
        @SerializedName("phone")
        private Integer phone;
        @SerializedName("company")
        private String company;
        @SerializedName("person_img")
        private String personImg;
    }
}
