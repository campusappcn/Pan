package cn.campusapp.pan.lifecycle;

/**
 * 对应Fragment的setUserVisibleHint方法，换个名字更明确一点
 *
 * 大部分情况下Fragment的onPause和onResume都不太好用，直接使用这个比较明确
 *
 * Created by nius on 7/22/15.
 */
public interface OnVisible extends LifecycleObserver, LifecycleObserver.FragmentOnly{

    /**
     * 注意，该方法调用时，有可能还没有onCreateView，所以此时很有可能会出异常
     *
     * 不过考虑到，绑定都在onCreateView中进行(初始化ViewModel,Controller等)，
     * 绑定之前反正不会调用，所以稍微注意一下就好
     *
     * @param isVisible
     */
    void onVisible(boolean isVisible);
}
