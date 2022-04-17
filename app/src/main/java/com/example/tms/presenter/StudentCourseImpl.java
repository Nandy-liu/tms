package com.example.tms.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.tms.base.BasePresenter;
import com.example.tms.model.Api;
import com.example.tms.model.IStudentCourse;
import com.example.tms.model.domain.Ask;
import com.example.tms.model.domain.CheckIn;
import com.example.tms.model.domain.Disscuss;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.HttpUtil;
import com.example.tms.utils.ImageStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentCourseImpl extends BasePresenter<IStudentCourse.IView> implements IStudentCourse.IPresenter {
    private static Api sApi = HttpUtil.getInstance().getApi();
    private List<CheckIn.DataDTO> mCheckinData;

    public StudentCourseImpl(Context context) {
        super(context);
    }

    @Override
    public void queryCheckInfo(SQLiteDatabase db, String student_id, String course_id) {
        Call<CheckIn> checkInCall = sApi.CHECK_IN_GET(student_id, course_id);
        checkInCall.enqueue(new Callback<CheckIn>() {
            @Override
            public void onResponse(Call<CheckIn> call, Response<CheckIn> response) {
                if (response.body() != null && response.body().getData() != null) {
                    mCheckinData = response.body().getData();
                    ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.SIMPLIFIED_CHINESE);
                    SimpleDateFormat ft2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.SIMPLIFIED_CHINESE);
                    for (int i = 0; i < mCheckinData.size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", mCheckinData.get(i).getName());
                        try {
                            String checkinDate = mCheckinData.get(i).getCheckinDate();
                            map.put("checkin_date", ft2.format(ft.parse(checkinDate)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = ImageStore.getBmp(db,student_id);
                        map.put("person_img", bitmap);
                        arrayList.add(map);
                    }
                    getView().onCheckInLoaded(arrayList);
                }
            }

            @Override
            public void onFailure(Call<CheckIn> call, Throwable t) {

            }
        });
    }

    @Override
    public void queryAskInfo( SQLiteDatabase db,String student_id, String course_id) {
        Call<Ask> askCall = sApi.ASK_GET(course_id,null);
        askCall.enqueue(new Callback<Ask>() {
            @Override
            public void onResponse(Call<Ask> call, Response<Ask> response) {
                if (response.body() != null && response.body().getData() != null) {
                    List<Ask.DataDTO> data = response.body().getData();
                    ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < data.size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", data.get(i).getName());
                        map.put("ask", data.get(i).getAsk());
                        map.put("answer", data.get(i).getAnswer());
                        Bitmap bitmap = ImageStore.getBmp(db, String.valueOf(data.get(i).getStudentId()));
                        map.put("person_img", bitmap);
                        arrayList.add(map);
                    }
                    getView().onAskLoaded(arrayList);
                }
            }

            @Override
            public void onFailure(Call<Ask> call, Throwable t) {

            }
        });
    }

    @Override
    public void queryDiscussInfo( SQLiteDatabase db,String student_id, String course_id) {
        Call<Disscuss> disscussCall = sApi.DISCUSS_GET(course_id);
        disscussCall.enqueue(new Callback<Disscuss>() {
            @Override
            public void onResponse(Call<Disscuss> call, Response<Disscuss> response) {
                if (response.body() != null && response.body().getData() != null) {
                    List<Disscuss.DataDTO> data = response.body().getData();
                    ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < data.size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", data.get(i).getName());
                        map.put("discuss_content", data.get(i).getDiscussContent());
                        Bitmap bitmap = ImageStore.getBmp(db, String.valueOf(data.get(i).getId()));
                        map.put("person_img", bitmap);
                        arrayList.add(map);
                    }
                    getView().onDiscussLoaded(arrayList);
                }
            }

            @Override
            public void onFailure(Call<Disscuss> call, Throwable t) {

            }
        });
    }

    @Override
    public void checkIn(SQLiteDatabase db,String student_id, String course_id) {
        try {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
            if (mCheckinData.size() == 0) {
                doCheck(db,student_id,course_id);
            } else {
                Date temp = ft.parse(mCheckinData.get(0).getCheckinDate());
                String checkinLastest = ft.format(temp);
                String nowDate = ft.format(new Date());
                if (checkinLastest.equals(nowDate)) {
                    getView().showMessage("今天已经签到");
                } else {
                    doCheck(db,student_id,course_id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doCheck(SQLiteDatabase db,String mStudentId, String mCourseId) {
        CheckIn.DataDTO dataDTO = new CheckIn.DataDTO();
        dataDTO.setStudentId(Integer.valueOf(mStudentId));
        dataDTO.setCourseId(Integer.valueOf(mCourseId));
        Call<ResponseOther> dataDTOCall = sApi.CHECK_IN_POST(dataDTO);
        dataDTOCall.enqueue(new Callback<ResponseOther>() {
            @Override
            public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    getView().showMessage("签到成功");
                    queryCheckInfo(db,mStudentId, mCourseId);
                } else {
                    getView().showMessage("签到失败，code：" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseOther> call, Throwable t) {

            }
        });
    }

    @Override
    public void askSubmit(SQLiteDatabase db,String student_id, String course_id, String text) {
        Ask.DataDTO dataDTO = new Ask.DataDTO();
        dataDTO.setStudentId(Integer.valueOf(student_id));
        dataDTO.setCourseId(Integer.valueOf(course_id));
        dataDTO.setAsk(text);
        Call<ResponseOther> dataDTOCall = sApi.ASK_POST(dataDTO);
        dataDTOCall.enqueue(new Callback<ResponseOther>() {
            @Override
            public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    getView().showMessage("提交成功");
                    queryAskInfo(db,student_id, course_id);
                } else {
                    getView().showMessage("提交失败,code:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseOther> call, Throwable t) {
                getView().showMessage("提交失败");
            }
        });
    }

    @Override
    public void discussSubmit(SQLiteDatabase db,String student_id, String course_id, String text) {
        Disscuss.DataDTO dataDTO = new Disscuss.DataDTO();
        dataDTO.setStudentId(Integer.valueOf(student_id));
        dataDTO.setCourseId(Integer.valueOf(course_id));
        dataDTO.setDiscussContent(text);
        Call<ResponseOther> dataDTOCall = sApi.DISCUSS_POST(dataDTO);
        dataDTOCall.enqueue(new Callback<ResponseOther>() {
            @Override
            public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    getView().showMessage("提交成功");
                    queryDiscussInfo( db,student_id, course_id);
                } else {
                    getView().showMessage("提交失败,code:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseOther> call, Throwable t) {
                getView().showMessage("提交失败");
            }
        });
    }
}
