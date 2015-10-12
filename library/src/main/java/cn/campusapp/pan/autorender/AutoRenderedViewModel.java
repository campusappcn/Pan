package cn.campusapp.pan.autorender;

/**
 * 标记某个ViewModel需要自动刷新
 *
 * 所谓的“自动刷新”，即在Activity或者Fragment的特定生命周期位置，自动刷新
 *
 * 1. OnStartActivity
 * 2. OnVisibleFragment
 * 3. OnViewCreatedFragment
 * Created by nius on 7/29/15.
 */
public interface AutoRenderedViewModel {

    /**
     * 获取最新的数据，以用来自动更新
     * 注意，该方法必须同步返回，不可发起异步请求
     *
     * render方法紧随其后被调用
     */
    void loadDataQuickly();
}
