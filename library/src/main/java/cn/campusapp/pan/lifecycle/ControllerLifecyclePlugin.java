package cn.campusapp.pan.lifecycle;

import cn.campusapp.pan.Controller;

/**
 * Lifecycle plugin can be plugged into Pan, so it will observe the lifecycle and behave accordingly.<br>
 * It can only take effects with the help of controllers. <br>
 * The {@link ControllerLifecyclePlugin#call(Controller, Class, Object...)}
 * will be called for each controllers in proper lifecycle.
 * <p>
 *     This means if your controller is binded in OnCreate phase, then there will be no call on this plugin because controller is not even functional at that time.
 * </p>
 * Created by nius on 10/13/15.
 */
public interface ControllerLifecyclePlugin {


    void call(Controller controller, Class<? extends LifecycleObserver> lifecycleObserver, Object... parameters);

}
