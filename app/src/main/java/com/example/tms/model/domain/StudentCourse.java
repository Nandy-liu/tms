package com.example.tms.model.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StudentCourse {
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
        @SerializedName("student_course_id")
        private Integer studentCourseId;
        @SerializedName("student_id")
        private Integer studentId;
        @SerializedName("course_id")
        private Integer courseId;
        @SerializedName("lecturer_id")
        private Integer lecturerId;
        @SerializedName("score")
        private Object score;
        @SerializedName("id")
        private Integer id;
        @SerializedName("name")
        private String name;
        @SerializedName("sex")
        private String sex;
        @SerializedName("age")
        private Integer age;
        @SerializedName("level")
        private String level;
        @SerializedName("phone")
        private Integer phone;
        @SerializedName("company")
        private String company;
        @SerializedName("course_name")
        private String courseName;
        @SerializedName("course_weight")
        private Integer courseWeight;
        @SerializedName("course_time")
        private String courseTime;
        @SerializedName("course_period")
        private String coursePeriod;
        @SerializedName("course_img")
        private String courseImg;
    }
}
