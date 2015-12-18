package cn.campusapp.pan.eventbus;

import cn.campusapp.pan.Controller;
import cn.campusapp.pan.lifecycle.LifecycleObserver;
import cn.campusapp.pan.lifecycle.ControllerLifecyclePlugin;
import cn.campusapp.pan.lifecycle.OnDestroy;
import cn.campusapp.pan.lifecycle.OnDestroyView;
import cn.campusapp.pan.lifecycle.OnPostCreate;
import cn.campusapp.pan.lifecycle.OnViewCreated;
import de.greenrobot.event.EventBus;

/**
 * Optional plugin. If your controller implements {@link EventBusController} and this plugin is installed,
 * the controller will be registered/unregister in specific lifecycle:
 *
 * - OnPostCreate/OnDestroy for Activity
 * - OnViewCreated/OnDestroyView for Fragment
 *
 * Created by nius on 10/13/15.
 */
public class EventBusControllerLifecyclePlugin implements ControllerLifecyclePlugin {

    EventBus mEventBus;

    public EventBusControllerLifecyclePlugin(EventBus eventBus) {
        mEventBus = eventBus;
    }

    @Override
    public void call(Controller controller, Class<? extends LifecycleObserver> lifecycleObserver, Object... parameters) {

        if (controller == null
                || !(controller instanceof EventBusController)) {
            return;
        }


        //register/unregister for Activity
        if (OnPostCreate.class.equals(lifecycleObserver)) {
            register(controller);
        } else if (OnDestroy.class.equals(lifecycleObserver)) {
            unregister(controller);
        }
        // register/unregister for Fragment
        else if (OnViewCreated.class.equals(lifecycleObserver)) {
            register(controller);
        } else if (OnDestroyView.class.equals(lifecycleObserver)) {
            unregister(controller);
        }

    }

    private void unregister(Controller controller) {
        if(controller == null || !mEventBus.isRegistered(controller)){
            return;
        }
        mEventBus.unregister(controller);
    }

    private void register(Controller controller) {
        if (controller == null || mEventBus.isRegistered(controller)) {
            return;
        }
        mEventBus.register(controller);
    }
}
