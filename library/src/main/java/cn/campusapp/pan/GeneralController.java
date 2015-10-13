package cn.campusapp.pan;

import android.app.Activity;


/**
 * Created by nius on 7/20/15.
 */
public abstract class GeneralController<T extends FactoryViewModel> implements Controller {

    protected T $vm;

//    protected EventBus mBus; TODO 插件化

    public GeneralController() {
    }


    /**
     * 预留用于做一些别的事情
     *
     * @param viewModel
     */
    void bindViewModel(T viewModel) {
        $vm = viewModel;
        onBindViewModel();
        bindEvents();
    }

    protected void onBindViewModel() {

    }

    /**
     * 绑定的事件
     * 也可以在此方法中进行一部分controller的初始化操作
     * <p/>
     * 最好直接使用$vm对象，在内部类中获取$vm的状态
     * <p/>
     * 因为对象的状态可能随时改变，被controller，或者后台数据有所变化
     * 因此，在匿名内部事件类中，直接使用mViewModel以获得最新的状态
     */
    protected abstract void bindEvents();


    public Activity getActivity() {
        return $vm.getActivity();
    }

    public FactoryViewModel getViewModel() {
        return $vm;
    }

}