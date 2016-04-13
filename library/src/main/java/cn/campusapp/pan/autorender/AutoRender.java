package cn.campusapp.pan.autorender;

import cn.campusapp.pan.ViewModel;

/**
 *
 * Trigger render on shown, which means these cases:
 * <ol>
 * <li>Activity.onResume</li>
 * <li>Fragment.onResume</li>
 * <li>Fragment.setUserVisibleHint(true)</li>
 * </ol>
 * Created by nius on 7/29/15.
 */
public interface AutoRender extends ViewModel {

    /**
     * 获取最新的数据，以用来自动更新
     * 注意，该方法必须同步返回，不可发起异步请求
     *
     * render方法紧随其后被调用
     */
    void loadDataQuickly();

    /**
     *
     * @return false if DO NOT render in any cases
     */
    boolean shouldRenderOnTrigger();
}
