package cn.campusapp.pan.autorender;

import cn.campusapp.pan.GeneralViewModel;

/**
 *
 * Automatically trigger render on particular lifecycle status:
 *
 * 1. Activity.onResume
 * 2. Fragment.onResume
 * 3. Fragment.setUserVisibleHint(true)
 *
 * Configure it using {@link AutoRenderViewModel#autoRender()} and {@link AutoRenderViewModel#autoRender(boolean)}
 *
 * Created by nius on 10/13/15.
 */
public abstract class AutoRenderViewModel extends GeneralViewModel implements AutoRender {

    /**
     * Whether trigger render on shown, which in these cases:
     * <p/>
     * 1. Activity.onResume
     * 2. Fragment.onResume
     * 3. Fragment.setUserVisibleHint(true)
     *
     * Default value is false, use {@link #autoRender()} to turn it on
     */
    boolean mShouldRenderOnTrigger = false;

    @Override
    public boolean shouldRenderOnTrigger() {
        return mShouldRenderOnTrigger;
    }

    /**
     * mark the view model should be rendered on shown
     *
     * @see {@link AutoRenderControllerLifecyclePlugin}
     * @see {@link AutoRender}
     */
    public GeneralViewModel autoRender(){
        mShouldRenderOnTrigger = true;
        return this;
    }

    /**
     * mark the view model whether should be rendered on shown
     *
     * @see {@link AutoRenderControllerLifecyclePlugin}
     * @see {@link AutoRender}
     */
    @SuppressWarnings("unused")
    public GeneralViewModel autoRender(boolean shouldRenderOnTrigger){
        mShouldRenderOnTrigger = shouldRenderOnTrigger;
        return this;
    }
}
