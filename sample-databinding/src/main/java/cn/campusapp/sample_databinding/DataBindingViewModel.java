package cn.campusapp.sample_databinding;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

import cn.campusapp.pan.GeneralViewModel;

/**
 * <p>
 * support for data binding
 * </p>
 * Created by nius on 4/11/16.
 */
public abstract class DataBindingViewModel<D extends BaseObservable, B extends ViewDataBinding> extends GeneralViewModel {

    protected D mData;
    protected B mViewDataBinding;

    public DataBindingViewModel<D, B> setData(D data){
        mData = data;
        bindData(data);
        return this;
    }

    public B getViewDataBinding(){
        return mViewDataBinding;
    }

    public D getData(){
        return mData;
    }

    protected abstract void bindData(D data);

    @Override
    protected void onInit() {
        super.onInit();
        mViewDataBinding = DataBindingUtil.bind(getRootView());
    }

}
