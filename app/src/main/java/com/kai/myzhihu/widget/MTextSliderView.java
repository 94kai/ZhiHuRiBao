package com.kai.myzhihu.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.kai.myzhihu.R;

/**
 * Created by 凯 on 2016/3/25 23:53
 */
public class MTextSliderView extends BaseSliderView {
    public MTextSliderView(Context context) {
        super(context);
    }
    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_msliderview,null);
        ImageView target = (ImageView)v.findViewById(R.id.iv_mslider_view);
        TextView description = (TextView)v.findViewById(R.id.tv_mslider_view);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
