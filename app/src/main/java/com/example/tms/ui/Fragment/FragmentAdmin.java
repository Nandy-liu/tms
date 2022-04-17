package com.example.tms.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tms.R;
import com.example.tms.base.BaseFragment;
import com.example.tms.model.IFragmentAdmin;
import com.example.tms.presenter.FragmentAdminPresentImpl;
import com.example.tms.ui.activity.sub.admin.AddAdmin;
import com.example.tms.ui.activity.sub.admin.ChangeAccount;
import com.example.tms.ui.activity.sub.admin.ModifiedCourse;
import com.example.tms.utils.DBOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
@SuppressLint("NonConstantResourceId")
public class FragmentAdmin extends BaseFragment<IFragmentAdmin.IView,FragmentAdminPresentImpl> implements IFragmentAdmin.IView {
    static final int QUERY_COURSE = 1;
    static final int QUERY_MESSAGE = 2;
    static final int QUERY_ACCOUNT = 3;
    private int listview_state = 1;
    SQLiteDatabase db;
    @BindView(R.id.toolbar_fragment_common)
    public Toolbar toolbar;
    @BindView(R.id.f_c_listview)
    public ListView listView;
    @BindView(R.id.f_look_sumcourse)
    public Button mButton_query_course;
    @BindView(R.id.f_query_liuyan)
    public Button mButton_look_message;
    @BindView(R.id.look_admin)
    public Button mButton_look_admin;
    private ArrayList<Map<String, String>> mAccountsList;
    private ArrayList<Map<String, String>> mCourseList;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_admin_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guanliyuan_backup_add:
                startActivity(new Intent(getActivity(), AddAdmin.class));
                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, null);
        ButterKnife.bind(FragmentAdmin.this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Toolbar设置
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        db = new DBOpenHelper(getContext(), "test_db").getWritableDatabase();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listview_state==QUERY_COURSE) {
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);
                    Intent intent_delete = new Intent(getActivity(), ModifiedCourse.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("course_id",mCourseList.get(position).get("course_id"));
                    intent_delete.putExtra("lecturer_name", map_item.get("lecturer_name") + "");
                    intent_delete.putExtra("course_name", map_item.get("course_name") + "");
                    intent_delete.putExtra("course_time", map_item.get("course_time") + "");
                    intent_delete.putExtra("course_period", map_item.get("course_period") + "");
                    startActivity(intent_delete);
                } else if (listview_state==QUERY_MESSAGE) {

                } else if (listview_state==QUERY_ACCOUNT) {
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), ChangeAccount.class);
                    intent.putExtra("account", map_item.get("account") + "");
                    intent.putExtra("password", map_item.get("password") + "");
                    intent.putExtra("role",mAccountsList.get(position).get("role"));
                    startActivity(intent);
                }
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    //*查看课程设置情况//
                    case R.id.f_look_sumcourse:
                        listview_state = QUERY_COURSE;
                        getMVPPresenter().searchCourse();
                        break;
                    //查看留言*
                    case R.id.f_query_liuyan:
                        listview_state = QUERY_MESSAGE;
                        getMVPPresenter().searchFeedback();
                        break;
                    case R.id.look_admin:
                        listview_state = QUERY_ACCOUNT;
                        getMVPPresenter().searchAccount();
                        break;
                    default:
                        break;
                }
            }
        };
        mButton_query_course.setOnClickListener(listener);
        mButton_look_message.setOnClickListener(listener);
        mButton_look_admin.setOnClickListener(listener);
    }

    @Override
    public void onSearchedFeedback(ArrayList<Map<String, String>> messages) {
        //设置适配器
        SimpleAdapter simpleAdapter_look_message = new SimpleAdapter(getActivity(), messages, R.layout.item_message,
                new String[]{"account", "name", "company", "message"}, new int[]{R.id.message_id, R.id.message_name, R.id.message_department, R.id.message_content});
        listView.setAdapter(simpleAdapter_look_message);
    }

    @Override
    public void onSearchedAccount(ArrayList<Map<String, String>> accounts) {
        mAccountsList = accounts;
        SimpleAdapter simpleAdapter_look_admin = new SimpleAdapter(getActivity(), accounts, R.layout.item_account,
                new String[]{"account", "password"}, new int[]{R.id.account_t, R.id.account_tv});
        listView.setAdapter(simpleAdapter_look_admin);
    }

    @Override
    public void onSearchedCourse(ArrayList<Map<String, String>> courses) {
        mCourseList = courses;
        //设置适配器
        SimpleAdapter simpleAdapter_look_course = new SimpleAdapter(getActivity(), courses, R.layout.item_course,
                new String[]{"lecturer_name", "course_name", "course_weight", "course_time", "course_period"}, new int[]{R.id.text_lecturer_name, R.id.text_course_name, R.id.text_course_weight, R.id.text_course_time, R.id.text_course_period});
        listView.setAdapter(simpleAdapter_look_course);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public FragmentAdminPresentImpl bindPresenter() {
        return new FragmentAdminPresentImpl(getContext());
    }

    @Override
    public IFragmentAdmin.IView bindView() {
        return this;
    }
}
