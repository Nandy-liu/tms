package com.example.tms.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tms.R;
import com.example.tms.base.BaseActivity;
import com.example.tms.model.IActivityLecturer;
import com.example.tms.presenter.ActivityLecturerPresenterImpl;
import com.example.tms.ui.activity.sub.lecturer.AddCourse;
import com.example.tms.ui.activity.sub.lecturer.Answer;
import com.example.tms.ui.activity.sub.lecturer.LecturerAboutMe;
import com.example.tms.ui.activity.sub.lecturer.LecturerCourseInfo;
import com.example.tms.utils.CommonMethod;
import com.example.tms.utils.DBOpenHelper;
import com.example.tms.utils.DialogSwitch;
import com.example.tms.utils.ImageStore;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class ActivityLecturer extends BaseActivity<IActivityLecturer.view, ActivityLecturerPresenterImpl> implements IActivityLecturer.view {
    @BindView(R.id.listview_lecturer)
    public ListView listView;
    @BindView(R.id.drawerlayout_lecturer)
    public DrawerLayout drawerLayout;
    @BindView(R.id.lecturer_SwipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar_lecturer)
    public Toolbar toolbar;
    @BindView(R.id.button_lookmycourse)
    public Button button_look_mycourse;
    @BindView(R.id.navigation_view_t)
    public NavigationView navigationView;
    private SQLiteDatabase db;
    private CircleImageView circleImageView;
    private AlertDialog.Builder builder;
    private Uri imageUri;
    private static final int TAKE_PHOTO = 1;
    private ImageStore imageStore;
    private String mAccount;
    @BindView(R.id.lecturer_coordinatorlayout)
    public CoordinatorLayout mCoordinatorLayout;
    private ArrayList<Map<String, Object>> mData;
    private Bitmap mBitmap_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer);
        ButterKnife.bind(this);
        db = new DBOpenHelper(getApplicationContext(), "test_db").getWritableDatabase();
        Intent intentParent = getIntent();
        mAccount = intentParent.getStringExtra("account");
        init();
        initListener();
    }

    @Override
    public ActivityLecturerPresenterImpl bindPresenter() {
        return new ActivityLecturerPresenterImpl(getApplicationContext());
    }

    @Override
    public IActivityLecturer.view bindView() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.lecturer_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.lecturer_message:
                //todo
                Intent intent = new Intent(ActivityLecturer.this, Answer.class);
                intent.putExtra("account", mAccount);
                startActivity(intent);
                break;
            case R.id.lecturer_add_course:
                Intent intent_submit = new Intent(ActivityLecturer.this, AddCourse.class);
                intent_submit.putExtra("account", mAccount);
                startActivity(intent_submit);
                break;
            default:
                break;
        }
        return true;
    }
    //初始化组件

    public void init() {
        //设置标题栏与状态栏颜色保持一致
        CommonMethod.setToolbarColor(ActivityLecturer.this);
        //设置toolbar
        setSupportActionBar(toolbar);
        //actionbar设置
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_right);
        }

        //NavigationView绑定及监听子项
        View headview = navigationView.inflateHeaderView(R.layout.headlayout);
        TextView textViewWelcome = headview.findViewById(R.id.head_tv);
        circleImageView = headview.findViewById(R.id.head_img);
        //表示欢迎的textview

        textViewWelcome.setText(mAccount);
        //头像初始化
        imageStore = new ImageStore();
        //为SwipeRefreshLayout设置颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        Handler myHandler = new MyHandler(this);
        imageStore.getBmp(db, mAccount, myHandler);
    }

    private class MyHandler extends Handler {
        private WeakReference<AppCompatActivity> mReference;

        public MyHandler(AppCompatActivity activity) {
            mReference = new WeakReference<AppCompatActivity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                Bitmap img = msg.getData().getParcelable("img");
                circleImageView.setImageBitmap(img);
            }
        }
    }

    private void initListener() {
        //监听navigation
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_menu_myinfo_t:
                    Intent intent_about = new Intent(ActivityLecturer.this, LecturerAboutMe.class);
                    intent_about.putExtra("account", mAccount);
                    startActivity(intent_about);
                    break;
                //修改账户
                case R.id.nav_menu_changeacc_t:
                    builder = new DialogSwitch(ActivityLecturer.this).build();
                    builder.show();
                    break;
                case R.id.nav_menu_feedback_l:
                    Intent intent_submit = new Intent(ActivityLecturer.this, AddFeedback.class);
                    intent_submit.putExtra("account", mAccount);
                    startActivity(intent_submit);
                default:
                    break;
            }
            return true;
        });
        //为listview设定监听器
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String course_id = String.valueOf(mData.get(position).get("course_id"));
            Intent intent = new Intent(ActivityLecturer.this, LecturerCourseInfo.class);
            intent.putExtra("course_id", course_id);
            intent.putExtra("account", mAccount);
            startActivity(intent);
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String course_id = String.valueOf(mData.get(position).get("course_id"));
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ActivityLecturer.this);
                builder.setIcon(R.drawable.ic_launcher_foreground);
                builder.setTitle("弹出警告框");
                builder.setMessage("确定删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getMVPPresenter().deleteCourse(course_id);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return true;
            }
        });
        //为SwipeRefreshLayout设置监听器
        swipeRefreshLayout.setOnRefreshListener(() -> new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    getMVPPresenter().loadCourse(mAccount);
                }
            });
        }).start());
        //按钮监听器
        View.OnClickListener listener = v -> {
            switch (v.getId()) {
                case R.id.button_lookmycourse:
                    getMVPPresenter().loadCourse(mAccount);
                    break;
                case R.id.head_img:
                    // 创建File对象，用于存储拍照后的图片
                    File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                    try {
                        if (outputImage.exists()) {
                            boolean delete = outputImage.delete();
                        }
                        boolean newFile = outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUri = FileProvider.getUriForFile(ActivityLecturer.this, "com.example.tms.fileprovider", outputImage);
                    // 启动相机程序
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                    break;
            }
        };
        button_look_mycourse.setOnClickListener(listener);
        circleImageView.setOnClickListener(listener);
    }
    //点击头像进行拍照的回调函数

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示到头像中
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        circleImageView.setImageBitmap(bitmap);
                        //更新本人资源表
                        Bitmap bitmap1 = CommonMethod.compressBoundsBitmap(ActivityLecturer.this, imageUri, 200, 200);
                        imageStore.update(bitmap1, db, mAccount);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }


    public void updateList(ArrayList<Map<String, Object>> arrayList) {
        mData = arrayList;
        SimpleAdapter simpleAdapter = new SimpleAdapter(ActivityLecturer.this, arrayList, R.layout.item_lecturer_course,
                new String[]{"course_name", "course_time", "course_period"}, new int[]{R.id.t_mycourse_name, R.id.t_mycourse_time, R.id.t_mycourse_period});
        listView.setAdapter(simpleAdapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(ArrayList<Map<String, Object>> arrayList) {
        Snackbar.make(mCoordinatorLayout, "您共有" + arrayList.size() + "门课程", Snackbar.LENGTH_LONG)
                .setAction("好的", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();
    }
}
