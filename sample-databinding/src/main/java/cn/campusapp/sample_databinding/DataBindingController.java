package cn.campusapp.sample_databinding;

import cn.campusapp.pan.GeneralController;

/**
 * 接入DataBinding的控制器
 * Created by nius on 4/11/16.
 */
public abstract class DataBindingController<T extends DataBindingViewModel> extends GeneralController<T> {

    @Override
    protected void onBindViewModel() {
        super.onBindViewModel();

        bindHandlers();
    }

    protected abstract void bindHandlers();

}
