package cn.campusapp.sample_databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.campusapp.pan.FactoryViewModel;

/**
 * 使用DataBindintUtil来初始化View
 * Created by nius on 4/11/16.
 */
public class DataBindingViewFactory extends FactoryViewModel.ViewFactory {

    public final static DataBindingViewFactory INSTANCE = new DataBindingViewFactory();

    @Override
    public View initWithoutView(Context context, ViewGroup parent, boolean attach, Class clazz) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayout(clazz), parent, attach);
        return binding.getRoot();
    }

}
