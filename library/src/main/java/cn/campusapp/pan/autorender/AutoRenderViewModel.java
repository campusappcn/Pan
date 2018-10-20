package cn.campusapp.pan.autorender;

import cn.campusapp.pan.GeneralViewModel;

/**
 *
 * Automatically trigger render on particular lifecycle status:
 * <ol>
 * <li>Activity.onResume/Fragment.onResume</li>
 * <li>Fragment.setUserVisibleHint(true)</li>
 * </ol>
 * Configure it using {@link AutoRenderViewModel#autoRender()} and {@link AutoRenderViewModel#autoRender(boolean)}
 * <p>
 * Created by nius on 10/13/15.
 */
public abstract class AutoRenderViewModel extends GeneralViewModel implements AutoRender {

    /**
     * Whether trigger render on shown, which in these cases:
     *
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
     * @see AutoRenderControllerLifecyclePlugin
     * @see AutoRender
     */
    public GeneralViewModel autoRender(){
        mShouldRenderOnTrigger = true;
        return this;
    }

    /**
     * mark the view model whether should be rendered on shown
     *
     * @see AutoRenderControllerLifecyclePlugin
     * @see AutoRender
     */
    @SuppressWarnings("unused")
    public GeneralViewModel autoRender(boolean shouldRenderOnTrigger){
        mShouldRenderOnTrigger = shouldRenderOnTrigger;
        return this;
    }
}
