package cn.campusapp.pan;

import android.app.Activity;


/**
 * 通用控制器，用户通过继承此类编写控制器。
 *
 * @param <T> 绑定的ViewModel泛型
 */
public abstract class GeneralController<T extends FactoryViewModel> implements Controller {

    protected T $vm;

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

    /**
     * 此时ViewModel已经绑定，可以获取到界面元素，可用于初始化
     */
    protected void onBindViewModel() {

    }

    /**
     * 绑定的事件
     * 也可以在此方法中进行一部分controller的初始化操作
     * <p/>
     * 可直接使用$vm对象，在内部类中获取$vm的状态
     * <p/>
     * 如果存在异步代码，且在Adapter/ViewHolder模式中使用，请注意绑定的$vm是否存在被更换的可能。如果$vm
     * 可变，那么需要在异步回调中，直接引用需要修改的数据对象，而不是通过$vm获得。可以参考sample-github-users的UserListController
     */
    protected abstract void bindEvents();

    public Activity getActivity() {
        return $vm.getActivity();
    }

    public T getViewModel() {
        return $vm;
    }

}