package cn.campusapp.pan.autorender;

import android.support.annotation.NonNull;

import cn.campusapp.pan.Controller;
import cn.campusapp.pan.Pan;
import cn.campusapp.pan.lifecycle.LifecycleObserver;
import cn.campusapp.pan.lifecycle.LifecyclePlugin;
import cn.campusapp.pan.lifecycle.OnResume;
import cn.campusapp.pan.lifecycle.OnVisible;

/**
 * Trigger render on shown, which means these cases:
 * <p/>
 * 1. Activity.onResume
 * 2. Fragment.onResume
 * 3. Fragment.setUserVisibleHint(true)
 *
 * Created by nius on 10/13/15.
 */
public class AutoRenderLifecyclePlugin implements LifecyclePlugin {

    /**
     * @param controller        the controller that observes the lifecycle
     * @param lifecycleObserver the lifecycle currently undergoing
     * @param parameters        parameters
     */
    @Override
    public void call(Controller controller, Class<? extends LifecycleObserver> lifecycleObserver, Object... parameters) {
        if (controller == null || !(controller.getViewModel() instanceof AutoRender)) {
            return;
        }

        //onResume
        if (OnResume.class.equals(lifecycleObserver)) {
            autoRender(controller);
        }
        //setUserVisibleHint(true)
        else if (OnVisible.class.equals(lifecycleObserver) &&
                parameters[0] instanceof Boolean) {

            // visible == true
            if (parameters[0].equals(Boolean.TRUE)) {
                autoRender(controller);
            }
        }
    }

    private void autoRender(@NonNull Controller controller) {
        AutoRender vm = ((AutoRender) controller.getViewModel());

        if(!vm.shouldRenderOnTrigger()){
            return;
        }

        vm.loadDataQuickly();
        vm.render();
        Pan.LOG.debug("Auto-rendering triggered by {}", controller.getClass().getSimpleName());
    }
}
