package cn.campusapp.pan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import cn.campusapp.pan.autorender.AutoRenderedController;
import cn.campusapp.pan.autorender.AutoRenderedViewModel;


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


    //region 提前为子类实现AutoRenderedController以及 EventBusController

    /**
     * 一般在Activity的OnCreate中绑定Controller，此方法可以被调用
     */
    public void onStart() {
        if ($vm instanceof AutoRenderedViewModel && this instanceof AutoRenderedController) {
            ((AutoRenderedViewModel) $vm).loadDataQuickly();
            $vm.render();
        } else if ($vm instanceof AutoRenderedViewModel) {
            throw new RuntimeException("必须要使用一个AutoRenderedViewModel啊");
        }

        //TODO 插件化
//        if (this instanceof EventBusActivityController) {
//            if (!mBus.isRegistered(this)) {
//                mBus.register(this);
//            }
//        }
    }

    public void onStop() {
        //TODO 插件化
        //注册的时候是在Pan实例化时就注册了，见Pan.bindController
//        if (this instanceof EventBusActivityController) {
//            if (mBus.isRegistered(this)) {
//                mBus.unregister(this);
//            }
//        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        //TODO 插件化
//        if (this instanceof EventBusFragmentController) {
//            if (!mBus.isRegistered(this)) {
//                mBus.register(this);
//            }
//        }

        if ($vm == null) {
            return;
        }

        //刚刚创建完毕，自动刷新一下
        if ($vm instanceof AutoRenderedViewModel && this instanceof AutoRenderedController) {
            ((AutoRenderedViewModel) $vm).loadDataQuickly();
            $vm.render();
        } else if ($vm instanceof AutoRenderedViewModel) {
            throw new RuntimeException("必须要使用一个AutoRenderedViewModel啊");
        }
    }

    public void onDestroyView() {
        //TODO 插件化
        //注册的时候是在Pan实例化时就注册了，见Pan.bindController
//        if (this instanceof EventBusFragmentController) {
//            if (mBus.isRegistered(this)) {
//                mBus.unregister(this);
//            }
//        }
    }

    public void onVisible(boolean isVisible) {
        if ($vm == null || !isVisible) {
            return;
        }

        //当可见时，刷新一下
        if ($vm instanceof AutoRenderedViewModel && this instanceof AutoRenderedController) {
            ((AutoRenderedViewModel) $vm).loadDataQuickly();
            $vm.render();
        } else if ($vm instanceof AutoRenderedViewModel) {
            throw new RuntimeException("必须要使用一个AutoRenderedViewModel啊");
        }
    }

    //endregion

    public Activity getActivity() {
        return $vm.getActivity();
    }
}
