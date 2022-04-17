package com.example.tms.ui.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.tms.R;
import com.example.tms.model.domain.Course;

import java.util.List;

/*
主要用于设定listview的适配器
 */
public class ChooseCourseAdapter extends BaseAdapter {
    Context mContext;
    List<Course.DataDTO> mList;
    ViewHolder mViewHolder;

    public ChooseCourseAdapter(Context context, List<Course.DataDTO> list) {
        mContext = context;
        mList = list;
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setIsCheck(false);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Course.DataDTO it = mList.get(i);
        if (view == null) {
            //用LayouInflater加载布局，传给布局对象view
            // 用view找到三个控件，存放在viewHolder中，再把viewHolder储存到View中
            // 完成了把控件展示在ListView的步骤
            view = LayoutInflater.from(mContext).inflate(R.layout.item_course_choose, viewGroup, false);
            mViewHolder = new ViewHolder();
            mViewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox_1);
            mViewHolder.course_name = view.findViewById(R.id.t_course_name);
            mViewHolder.course_time = view.findViewById(R.id.t_course_time);
            mViewHolder.course_period = view.findViewById(R.id.t_course_period);
            mViewHolder.lecturer_name = view.findViewById(R.id.t_lecturer_name);
            mViewHolder.course_weight = view.findViewById(R.id.t_course_weight);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        mViewHolder.course_name.setText(it.getCourseName());
        mViewHolder.course_time.setText(it.getCourseTime());
        mViewHolder.course_period.setText(it.getCoursePeriod());
        mViewHolder.lecturer_name.setText(it.getName());
        mViewHolder.checkBox.setChecked(false);
        mViewHolder.course_weight.setText(it.getCourseWeight().toString());
        mViewHolder.checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        it.setIsCheck(b);
                    }
                });
        return view;
    }

    static class ViewHolder {
        TextView course_name;
        TextView course_time;
        CheckBox checkBox;
        TextView course_period;
        TextView lecturer_name;
        TextView course_weight;
    }
}
