package com.example.tms.ui.activity.sub.lecturer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tms.R;
import com.example.tms.model.Api;
import com.example.tms.model.domain.Ask;
import com.example.tms.model.domain.ResponseOther;
import com.example.tms.utils.DBOpenHelper;
import com.example.tms.utils.HttpUtil;
import com.example.tms.utils.ImageStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class Answer extends AppCompatActivity {
    @BindView(R.id.answer_list)
    ListView mListView;
    @BindView(R.id.answer_et)
    EditText mEditText;
    @BindView(R.id.answer_submit)
    ImageButton mImageButton;
    @BindView(R.id.l_answer)
    LinearLayout mLayout;
    private ArrayList<Map<String, Object>> mArrayList;
    private Api mApi;
    private String mAsk_id;
    private InputMethodManager mImm;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.bind(this);
        mImm = (InputMethodManager) Answer.this.getSystemService(Context.INPUT_METHOD_SERVICE);

        mDb = new DBOpenHelper(getApplicationContext(),"test_db").getWritableDatabase();
        loadData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAsk_id = String.valueOf(mArrayList.get(position).get("ask_id"));
                mEditText.setText("");
                mLayout.setVisibility(View.VISIBLE);
                mImm.showSoftInput(mEditText, 0);
                mEditText.requestFocus();
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setVisibility(View.INVISIBLE);
                mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                Map<String, String> map = new HashMap<>();
                String s = mEditText.getText().toString();
                map.put("ask_id", mAsk_id);
                map.put("answer", s);
                Call<ResponseOther> task = mApi.ASK_PUT(map);
                task.enqueue(new Callback<ResponseOther>() {
                    @Override
                    public void onResponse(Call<ResponseOther> call, Response<ResponseOther> response) {
                        Toast.makeText(Answer.this, "回复成功", Toast.LENGTH_SHORT).show();
                        loadData();
                    }

                    @Override
                    public void onFailure(Call<ResponseOther> call, Throwable t) {
                        Toast.makeText(Answer.this, "回复失败", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                });

            }
        });
    }

    private void updateView() {
        SimpleAdapter adapter = new SimpleAdapter(Answer.this, mArrayList, R.layout.item_ask,
                new String[]{"name", "ask", "answer", "person_img"},
                new int[]{R.id.ask_name, R.id.ask_tv, R.id.ask_answer, R.id.ask_img});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        mListView.setAdapter(adapter);
    }

    private void loadData() {
        mApi = HttpUtil.getInstance().getApi();
        Call<Ask> task = mApi.ASK_GET(null, getIntent().getStringExtra("account"));
        task.enqueue(new Callback<Ask>() {
            @Override
            public void onResponse(Call<Ask> call, Response<Ask> response) {
                List<Ask.DataDTO> data = response.body().getData();
                mArrayList = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < data.size(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ask_id", data.get(i).getAskId());
                    map.put("name", data.get(i).getName());
                    map.put("ask", data.get(i).getAsk());
                    map.put("answer", data.get(i).getAnswer());
                    Bitmap bitmap = null;
                    try {
                        Bitmap bmp = ImageStore.getBmp(mDb, String.valueOf(data.get(i).getStudentId()));
                        if (bmp==null) {
                            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.person);
                        } else {
                            bitmap = ImageStore.getBmp(mDb,String.valueOf(data.get(i).getStudentId()));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    map.put("person_img", bitmap);
                    mArrayList.add(map);
                }
                updateView();
            }

            @Override
            public void onFailure(Call<Ask> call, Throwable t) {

            }
        });
    }
}
