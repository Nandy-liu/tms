package com.example.tms.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tms.R;
import com.example.tms.base.BaseFragment;
import com.example.tms.model.IFragmentLecturer;
import com.example.tms.presenter.FragmentLecturerPresentImpl;
import com.example.tms.ui.activity.sub.admin.AddLecturer;
import com.example.tms.ui.activity.sub.admin.ChangeAccount;
import com.example.tms.ui.activity.sub.admin.ModifiedLecturer;
import com.example.tms.utils.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

@SuppressLint("NonConstantResourceId")
public class FragmentLecturer extends BaseFragment<IFragmentLecturer.IView, FragmentLecturerPresentImpl> implements IFragmentLecturer.IView {
    static final int QUERY_ALL = 1;
    static final int QUERY_ACCOUNT = 2;
    static final int QUERY = 3;
    private int listview_state = 1;

    @BindView(R.id.fl_toolbar)
    public Toolbar toolbar;
    @BindView(R.id.f_t_listview)
    public ListView listView;
    @BindView(R.id.flb_account)
    public Button mButtonQueryAccount;
    @BindView(R.id.flb_all)
    public Button mButtonQuery_All;
    @BindView(R.id.flb_query)
    public Button mButtonQuery;
    @BindView(R.id.fl_byid)
    public EditText mEtById;
    @BindView(R.id.fl_byname)
    public EditText mEditText;
    private ArrayList<Map<String, String>> mAccountsList;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_lecturer_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lecturer_backup_qingchu:
                listView.setAdapter(null);
                mEditText.setText("");
                mEtById.setText("");
                break;
            case R.id.lecturer_backup_add:
                Intent intent_add_lecturer = new Intent(getActivity(), AddLecturer.class);
                startActivity(intent_add_lecturer);
                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecturer, null);
        ButterKnife.bind(FragmentLecturer.this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Toolbar设置
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //
        setHasOptionsMenu(true);
        //状态栏颜色设置
        CommonMethod.setToolbarColor(getActivity());

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.fl_swipe_refresh);
        //进度条刷新旋钮的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //设置下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listview_set(listview_state);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }
    @OnClick({R.id.flb_all,R.id.flb_account,R.id.flb_query})
    public void onClick(View v) {
        //设置按钮点击监听器
        switch (v.getId()) {
            //查询讲师信息
            case R.id.flb_all:
                //设置listview状态为讲师
                listview_state = QUERY_ALL;
                listview_set(QUERY_ALL);
                break;
            //查询讲师登陆信息
            case R.id.flb_account:
                //设置listview为讲师账户的查询状态
                listview_state = QUERY_ACCOUNT;
                listview_set(QUERY_ACCOUNT);
                break;
            case R.id.flb_query:
                listview_state = QUERY;
                listview_set(QUERY);
                break;
            default:
                break;
        }
    }
    @OnItemClick(R.id.f_t_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //若是讲师状态
        if (listview_state == QUERY || listview_state == QUERY_ALL) {
            HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), ModifiedLecturer.class);
            //获取map中的三项数据，并放入intent
            intent.putExtra("lecturer_id", map_item.get("id") + "");
            intent.putExtra("name", map_item.get("name") + "");
            intent.putExtra("sex", map_item.get("sex") + "");
            intent.putExtra("age", map_item.get("age") + "");
            intent.putExtra("level", map_item.get("level") + "");
            intent.putExtra("phone", map_item.get("phone") + "");
            intent.putExtra("company", map_item.get("company") + "");
            startActivity(intent);
        } else if(listview_state == QUERY_ACCOUNT) {
            HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), ChangeAccount.class);
            intent.putExtra("account", map_item.get("account") + "");
            intent.putExtra("password", map_item.get("password") + "");
            intent.putExtra("role",mAccountsList.get(position).get("role"));
            startActivity(intent);
        }
    }

    public void listview_set(int state) {
        switch (state) {
            case QUERY_ALL:
                getMVPPresenter().searchAll();
                break;
            case QUERY:
                final String e_id = mEtById.getText().toString();
                final String e_name = mEditText.getText().toString();
                getMVPPresenter().search(e_id, e_name);
                break;
            case QUERY_ACCOUNT:
                getMVPPresenter().searchAccount();
                break;
        }
    }

    @Override
    public void onSearchedAll(ArrayList<Map<String, String>> lecturers) {
        //设置适配器
        SimpleAdapter simpleAdapter_lecturer = new SimpleAdapter(
                getActivity(), lecturers, R.layout.item_lecturer
                ,new String[]{"id", "name", "sex", "age", "level", "phone", "company"},
                new int[]{R.id.list_t_id, R.id.list_t_name, R.id.list_t_sex, R.id.list_t_age,
                        R.id.list_t_level, R.id.list_t_phone, R.id.list_t_company});
        listView.setAdapter(simpleAdapter_lecturer);
    }

    @Override
    public void onSearchedAccount(ArrayList<Map<String, String>> accounts) {
        mAccountsList = accounts;
        SimpleAdapter simpleAdapter_look = new SimpleAdapter(
                getActivity(), accounts, R.layout.item_account
                ,new String[]{"account", "password"}
                , new int[]{R.id.account_t, R.id.account_tv});
        listView.setAdapter(simpleAdapter_look);
    }

    @Override
    public void onSearched(ArrayList<Map<String, String>> lecturers) {
        //设置适配器
        SimpleAdapter simpleAdapter_lecturer = new SimpleAdapter(
                getActivity(), lecturers, R.layout.item_lecturer,
                new String[]{"lecturer_id", "name", "sex", "age", "level", "phone", "company"},
                new int[]{R.id.list_t_id, R.id.list_t_name, R.id.list_t_sex, R.id.list_t_age,
                        R.id.list_t_level, R.id.list_t_phone, R.id.list_t_company});
        listView.setAdapter(simpleAdapter_lecturer);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public FragmentLecturerPresentImpl bindPresenter() {
        return new FragmentLecturerPresentImpl(getContext());
    }

    @Override
    public IFragmentLecturer.IView bindView() {
        return this;
    }
}
