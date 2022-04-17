package com.example.tms.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tms.R;
import com.example.tms.base.BaseActivity;
import com.example.tms.model.IActivityStudent;
import com.example.tms.presenter.ActivityStudentPresenteImpl;
import com.example.tms.ui.activity.sub.student.AboutMe;
import com.example.tms.ui.activity.sub.student.ChooseCourse;
import com.example.tms.utils.CommonMethod;
import com.example.tms.utils.DBOpenHelper;
import com.example.tms.utils.DialogSwitch;
import com.example.tms.utils.ImageStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class ActivityStudent extends BaseActivity<IActivityStudent.view, ActivityStudentPresenteImpl> implements IActivityStudent.view {
    private static final int TAKE_PHOTO = 1;
    private SQLiteDatabase db;
    private AlertDialog.Builder mDialog;
    private View headview;
    private TextView mTextViewWelcome;
    private CircleImageView circleImageView;
    private Uri imageUri;
    private ImageStore imageStore;
    private Intent mIntentParent;
    private ActionBar mActionBar;
    private Bitmap mBitmapTemp;
    @BindView(R.id.toolbar_student)
    public Toolbar toolbar;
    @BindView(R.id.listview_mycourse)
    public ListView listView_mycourse;
    @BindView(R.id.drawerlayout_student)
    public DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    public NavigationView mNavigationview;
    @BindView(R.id.floatingbutton_choose_course)
    public FloatingActionButton floatingActionButton;
    @BindView(R.id.student_swipe_refresh)
    public SwipeRefreshLayout mRefreshLayout;
    private String mAccount;
    private ArrayList<Map<String, Object>> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
        mIntentParent = getIntent();
        //获取登录信息，以锁定用户
        mAccount = mIntentParent.getStringExtra("account");

        db = new DBOpenHelper(getApplicationContext(), "test_db").getWritableDatabase();
        initView();
        getMVPPresenter().queryInfo(mAccount);
        initListener();
    }

    @Override
    public ActivityStudentPresenteImpl bindPresenter() {
        return new ActivityStudentPresenteImpl(getApplicationContext());
    }

    @Override
    public IActivityStudent.view bindView() {
        return this;
    }

    private void initView() {
        //设置标题栏与状态栏颜色保持一致
        CommonMethod.setToolbarColor(ActivityStudent.this);
        setSupportActionBar(toolbar);
        headview = mNavigationview.inflateHeaderView(R.layout.headlayout);
        mTextViewWelcome = headview.findViewById(R.id.head_tv);
        circleImageView = headview.findViewById(R.id.head_img);
        imageStore = new ImageStore();
        //进度条刷新旋钮的颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //菜单栏实现
        mNavigationview.setCheckedItem(R.id.nav_menu_myinfo);
        mNavigationview.setCheckedItem(R.id.nav_menu_changeacc);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            //设置左箭头图片
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_right);
        }
        //头像初始化
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
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.floatingbutton_choose_course:
                        //再将从登陆界面接受的学员账号传给选择课程的活动
                        Intent intent1 = new Intent(ActivityStudent.this, ChooseCourse.class);
                        intent1.putExtra("account", mAccount);
                        startActivity(intent1);
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
                        imageUri = FileProvider.getUriForFile(ActivityStudent.this, "com.example.tms.fileprovider", outputImage);
                        // 启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                        break;
                    default:
                        break;
                }
            }
        };
        floatingActionButton.setOnClickListener(listener);
        circleImageView.setOnClickListener(listener);
        mNavigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_myinfo:
                        Intent intent = new Intent(ActivityStudent.this, AboutMe.class);
                        intent.putExtra("account", mAccount);
                        startActivity(intent);
                        break;
                    case R.id.nav_menu_changeacc:
                        mDialog = new DialogSwitch(ActivityStudent.this).build();
                        mDialog.show();
                        break;
                    //留言
                    case R.id.nav_menu_feedback:
                        Intent intent_submit = new Intent(ActivityStudent.this, AddFeedback.class);
                        intent_submit.putExtra("account", mAccount);
                        startActivity(intent_submit);
                        break;
                    //查看选课结果
                    case R.id.nav_menu_look_hcourse:
                        getMVPPresenter().searchCourse(mAccount);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //设置下拉刷新的监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // refresh ui 的操作代码
                                getMVPPresenter().searchCourse(mAccount);
                                mRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @OnItemClick(R.id.listview_mycourse)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), ActivityStudentCourse.class);
        intent.putExtra("account", mAccount);
        intent.putExtra("course_id", String.valueOf(mData.get(position).get("course_id")));
        intent.putExtra("course_img", String.valueOf(mData.get(position).get("course_img")));
        startActivity(intent);
    }

    //点击头像拍照实现
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
                        Bitmap bitmap1 = CommonMethod.compressBoundsBitmap(ActivityStudent.this, imageUri, 200, 200);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onSearchedCourse(ArrayList<Map<String, Object>> courses) {
        mData = courses;
        //设置适配器，并绑定布局文件
        SimpleAdapter simpleAdapter = new SimpleAdapter(ActivityStudent.this, courses, R.layout.item_choose_result,
                new String[]{"course_id", "course_name", "lecturer_name", "course_time", "course_weight", "course_period", "course_img", "score"},
                new int[]{R.id.c_id, R.id.result_course_name, R.id.result_lecturer_name, R.id.result_time, R.id.result_weight, R.id.result_period, R.id.url, R.id.score});
        listView_mycourse.setAdapter(simpleAdapter);
    }

    @Override
    public void onSearchedName(String name) {
        mTextViewWelcome.setText(name);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
