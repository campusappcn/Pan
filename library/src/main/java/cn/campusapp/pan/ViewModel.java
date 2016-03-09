package cn.campusapp.pan;

import android.view.View;

public interface ViewModel {

    /**
     * 渲染界面
     *
     * 原先设计时，该方法会被在Activity的onResume时被调用，以及Fragment的setUserVisibleHint时调用
     * 如果手动调用，就显得很啰嗦
     *
     * 但如果自动调用，像ListView中的ItemView被调用似乎就不应该了，虽然调用了也没啥关系
     * 自动调用意味着要在GeneralController中增加默认的render位置。以及，要给没有Controller的ViewModel给一个默认的Controller
     *
     * 具体写到时，再体会该如何写吧 2015-07-28
     *
     * 或许可以加一个AutoRender注解，包含该注解的会被自动渲染 2015-07-29
     *
     *
     * 最终实现： 2015-07-29
     *
     * 使用AutoRenderedController和AutoRenderedViewModel这两个接口
     *
     * Controller实现类可以直接实现AutoRenderedFragmentController和AutoRenderedActivityController
     * 对应的自动刷新方法已经由GeneralController父类实现了
     *
     * 返回自己，用于chainable调用，返回值子类可以重写自己的类型
     */
    ViewModel render();

    View getRootView();
}
