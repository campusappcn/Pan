package cn.campusapp.pan.lifecycle;

import android.app.Activity;
import android.support.v4.app.Fragment;

import cn.campusapp.pan.Controller;

/**
 * Just do something on proper lifecycle for every Activity/Fragment.<br>
 * It differs from {@link ControllerLifecyclePlugin} for it is only called once per lifecycle event. <br>
 * And it DOES NOT depend on the controllers to take effects.
 * <p>
 * Created by nius on 12/18/15.
 * </p>
 */
public interface PanLifecyclePlugin {

    /**
     * Called on every activity lifecycle for every activity controlled by Pan.
     * <br>
     *
     * @param target target Activity
     * @param lifecycle e.g. {@link OnResume}
     * @param parameters the params passed by the system for that lifecycle
     */
    void onActivityLifecycle(Activity target, Class<? extends LifecycleObserver> lifecycle, Object... parameters);

    /**
     * Called on every fragment lifecycle for every fragment controller by Pan.
     *
     * @param target target Fragment
     * @param lifecycle lifecycle e.g {@link OnVisible}
     * @param parameters the params passed by the system for that lifecycle
     */
    void onFragmentLifecycle(Fragment target, Class<? extends LifecycleObserver> lifecycle, Object... parameters);

}
