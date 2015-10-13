package cn.campusapp.pan.lifecycle;

import cn.campusapp.pan.Controller;
import cn.campusapp.pan.FactoryViewModel;
import cn.campusapp.pan.GeneralController;

/**
 * Lifecycle plugin can be plugged into Pan, so it will observe the lifecycle and behave accordingly
 *
 * Created by nius on 10/13/15.
 */
public interface LifecyclePlugin{


    void call(Controller controller, Class<? extends LifecycleObserver> lifecycleObserver, Object... parameters);

}
