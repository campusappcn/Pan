package cn.campusapp.pan.lifecycle;

/**
 * <p>
 * 表明这个Activity或者Fragment的生命周期被观察了，常见的实现是{@link cn.campusapp.pan.PanActivity}和{@link cn.campusapp.pan.PanFragment}
 * </p><p>
 * 注意！这个接口的实现者，必须完全拷贝{@link cn.campusapp.pan.PanActivity} 或者 {@link cn.campusapp.pan.PanFragment}里的代码，否则Pan将无法正常工作
 * 例如{@link cn.campusapp.pan.PanAppCompatActivity}
 * </p><p>
 * Created by nius on 7/22/15.
 * </p>
 * @see LifecycleObserver
 */
public interface LifecycleObserved {
}
