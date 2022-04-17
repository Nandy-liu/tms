package com.example.tms.model;

import com.example.tms.model.domain.Account;
import com.example.tms.model.domain.Ask;
import com.example.tms.model.domain.CheckIn;
import com.example.tms.model.domain.Course;
import com.example.tms.model.domain.Disscuss;
import com.example.tms.model.domain.Feedback;
import com.example.tms.model.domain.Lecturer;
import com.example.tms.model.domain.PersonalResource;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.model.domain.Student;
import com.example.tms.model.domain.StudentCourse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    /**
     * 查询
     */
    @GET("account")
    Call<Account> ACCOUNT_GET(@Query("account") String account, @Query("role") String role);

    @GET("course")
    Call<Course> COURSE_GET(@Query("account") String account);

    @GET("student_course")
    Call<StudentCourse> STUDENT_COURSE_GET(@Query("account") String account, @Query("course_id") String courseId);

    @GET("student_course_test")
    Call<StudentCourse> STUDENT_COURSE_TEST_GET(@Query("account") String account, @Query("course_id") String courseId);

    @GET("student")
    Call<Student> STUDENT_GET(@Query("account") String account, @Query("name") String name, @Query("department") String department);

    @GET("lecturer")
    Call<Lecturer> LECTURER_GET(@Query("account") String account, @Query("name") String name);

    @GET("checkin")
    Call<CheckIn> CHECK_IN_GET(@Query("account") String student_id, @Query("course_id") String course_id);

    @GET("ask")
    Call<Ask> ASK_GET(@Query("course_id") String course_id, @Query("lecturer_id") String lecturerId);

    @GET("discuss")
    Call<Disscuss> DISCUSS_GET(@Query("course_id") String course_id);

    @GET("feedback")
    Call<Feedback> FEEDBACK_GET();
    @GET("personal_resource")
    Call<PersonalResource> RESOURCE_GET(@Query("id") String id);

    /**
     * 添加
     */
    @POST("checkin")
    Call<ResponseOther> CHECK_IN_POST(@Body CheckIn.DataDTO dto);

    @POST("ask")
    Call<ResponseOther> ASK_POST(@Body Ask.DataDTO dto);

    @POST("course")
    Call<ResponseOther> COURSE_POST(@Body Course.DataDTO dto);

    @POST("feedback")
    Call<ResponseOther> FEEDBACK_POST(@Body Feedback.DataDTO dto);

    @POST("discuss")
    Call<ResponseOther> DISCUSS_POST(@Body Disscuss.DataDTO dto);

    @POST("student_course")
    Call<ResponseOther> STUDENT_COURSE_POST(@Body StudentCourse.DataDTO dto);

    @POST("student")
    Call<ResponseOther> STUDENT_POST(@Body Map<String, String> map);

    @POST("lecturer")
    Call<ResponseOther> LECTURER_POST(@Body Map<String, String> map);

    @POST("account")
    Call<ResponseOther> ACCOUNT_POST(@Body Map<String, String> map);


    /**
     * 修改
     */
    @PUT("student_course")
    Call<ResponseOther> STUDENT_COURSE_PUT(@Body Map<String, String> map);

    @PUT("ask")
    Call<ResponseOther> ASK_PUT(@Body Map<String, String> map);

    @PUT("account")
    Call<ResponseOther> ACCOUNT_PUT(@Body Map<String, String> map);

    @PUT("student")
    Call<ResponseOther> STUDENT_PUT(@Body Student.DataDTO dto);

    @PUT("lecturer")
    Call<ResponseOther> LECTURER_PUT(@Body Lecturer.DataDTO dto);

    @PUT("course")
    Call<ResponseOther> COURSE_PUT(@Body Map<String, String> map);

    /**
     * 删除
     */
    @DELETE("course/{course_id}")
    Call<ResponseOther> COURSE_DELETE(@Path("course_id") String courseId);

    @DELETE("student/{account}")
    Call<ResponseOther> STUDENT_DELETE(@Path("account") String account);

    @DELETE("lecturer/{account}")
    Call<ResponseOther> LECTURER_DELETE(@Path("account") String account);
}
