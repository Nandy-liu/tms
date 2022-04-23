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
import com.example.tms.model.IFragmentStudent;
import com.example.tms.presenter.FragmentStudentPresentImpl;
import com.example.tms.ui.activity.sub.admin.AddStudent;
import com.example.tms.ui.activity.sub.admin.ChangeAccount;
import com.example.tms.ui.activity.sub.admin.ModifiedStudent;
import com.example.tms.utils.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

@SuppressLint("NonConstantResourceId")
public class FragmentStudent extends BaseFragment<IFragmentStudent.IView, FragmentStudentPresentImpl> implements IFragmentStudent.IView {
    @BindView(R.id.s_et_byid)
    public EditText mEditById;
    @BindView(R.id.s_et_byname)
    public EditText mEditTextByName;
    @BindView(R.id.s_et_bydepartment)
    public EditText mEditTextByDepartment;
    @BindView(R.id.fs_listview)
    public ListView listView;
    @BindView(R.id.fs_toolbar)
    public Toolbar toolbar;
    @BindView(R.id.s_query)
    public Button mButtonQuery;
    @BindView(R.id.s_query_account)
    public Button mButtonQueryAccount;
    @BindView(R.id.s_query_all)
    public Button mButtonQueryAll;

    private int listview_state = 1;
    static final int QUERY_ALL = 1;
    static final int QUERY_ACCOUNT = 2;
    static final int QUERY = 3;
    private ArrayList<Map<String, String>> mAccountsList;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_student_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.student_backup_qingchu:
                listView.setAdapter(null);
                mEditById.setText("");
                mEditTextByDepartment.setText("");
                mEditTextByName.setText("");
                break;
            case R.id.student_backup_add:
                Intent intent = new Intent(getActivity(), AddStudent.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        ButterKnife.bind(FragmentStudent.this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //状态栏颜色设置
        CommonMethod.setToolbarColor(getActivity());
        setHasOptionsMenu(true);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.student_swipe_refresh);
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
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // refresh ui 的操作代码
                                swipeRefreshLayout.setRefreshing(false);
                                listviewLoad(listview_state);
                            }
                        });
                    }
                }).start();
            }
        });
    }
    @OnItemClick(R.id.fs_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listview_state == QUERY || listview_state == QUERY_ALL) {
            HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), ModifiedStudent.class);
            //获取map中的三项数据，并放入intent
            intent.putExtra("id", map.get("id") + "");
            intent.putExtra("name", map.get("name") + "");
            intent.putExtra("sex", map.get("sex") + "");
            intent.putExtra("age", map.get("age") + "");
            intent.putExtra("department", map.get("department") + "");
            intent.putExtra("phone", map.get("phone") + "");
            intent.putExtra("company", map.get("company") + "");
            startActivity(intent);
        } else if (listview_state == QUERY_ACCOUNT) {
            HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), ChangeAccount.class);
            intent.putExtra("account", map.get("account") + "");
            intent.putExtra("password", map.get("password") + "");
            intent.putExtra("role", mAccountsList.get(position).get("role"));
            startActivity(intent);
        }
    }
    @OnClick({R.id.s_query, R.id.s_query_all, R.id.s_query_account})
    public void onClick(View v) {
        //设置按钮点击监听器
        switch (v.getId()) {
            case R.id.s_query:
                listview_state = QUERY;
                listviewLoad(listview_state);
                break;
            //查询所有学员信息*
            case R.id.s_query_all:
                //查询的是学员表,所以
                listview_state = QUERY_ALL;
                listviewLoad(listview_state);
                break;
            //*查询所有学员用户账号密码*
            case R.id.s_query_account:
                //查询的是账户，所以设置listview为查询账户状态
                listview_state = QUERY_ACCOUNT;
                listviewLoad(listview_state);
                break;
            default:
                break;
        }
    }

    public void listviewLoad(int state) {
        switch (state) {
            case QUERY_ALL:
                getMVPPresenter().searchAll(null, null, null);
                break;
            case QUERY:
                String e_id = mEditById.getText().toString();
                String e_name = mEditTextByName.getText().toString();
                String e_department = mEditTextByDepartment.getText().toString();
                getMVPPresenter().search(e_id, e_name, e_department);
                break;
            case QUERY_ACCOUNT:
                getMVPPresenter().searchAccount();
                break;
        }
    }

    @Override
    public void onSearchedAll(ArrayList<Map<String, String>> students) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getActivity(), students, R.layout.item
                , new String[]{"name", "id", "sex", "age", "department", "phone", "company"}
                , new int[]{R.id.iname, R.id.iid, R.id.isex, R.id.iage
                , R.id.idepartment, R.id.iphone, R.id.icompany});
        listView.setAdapter(simpleAdapter);
    }

    @Override
    public void onSearchedAccount(ArrayList<Map<String, String>> accounts) {
        mAccountsList = accounts;
        SimpleAdapter simpleAdapter_account = new SimpleAdapter(
                getActivity(), accounts, R.layout.item_account
                , new String[]{"account", "password"}
                , new int[]{R.id.account_t, R.id.account_tv});
        listView.setAdapter(simpleAdapter_account);
    }

    @Override
    public void onSearched(ArrayList<Map<String, String>> students) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getActivity(), students, R.layout.item
                , new String[]{"name", "id", "sex", "age", "department", "phone", "company"}
                , new int[]{R.id.iname, R.id.iid, R.id.isex, R.id.iage, R.id.idepartment, R.id.iphone, R.id.icompany});
        listView.setAdapter(simpleAdapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public FragmentStudentPresentImpl bindPresenter() {
        return new FragmentStudentPresentImpl(getContext());
    }

    @Override
    public IFragmentStudent.IView bindView() {
        return this;
    }
}
