package cn.campusapp.pan;

import android.view.View;

import butterknife.ButterKnife;

/**
 * 使用Butterknife，使用代码生成而不是反射
 * Created by nius on 7/24/15.
 */
public abstract class ButterViewHolder extends GeneralViewHolder{


    @Override
    protected void injectViews(View root) {
        ButterKnife.bind(this, root);
    }
}
