package cn.campusapp.pan.lifecycle;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

/**
 * Activity/Fragment life cycle observer
 *
 * Shared:(both is called by android)
 *
 * OnStart
 * OnResume
 * OnPause
 * OnStop
 * OnDestroy
 * OnActivityResult
 * OnConfigurationChanged
 * OnSavedInstanceState

 *
 * Activity only:
 *
 * onRestart
 * onNewIntent
 * OnRestoreInstanceState
 * OnPostCreate
 *
 * Fragment only:
 *
 * OnAttach
 * OnDetach
 * OnViewCreated
 * OnDestroyView
 * OnVisible(setUserVisibleHint)
 * OnHiddenChanged
 * OnActivityCreated
 *
 *
 *
 * OnBackPressed is specially treated. It is called by the {@link Activity#onBackPressed()}, all the controllers whose Fragment is tied to the Activity will be called. see {@link cn.campusapp.pan.Pan#onBackPressedForFragmentV4(FragmentActivity)}
 *
 *
 *
 * Observers can stop super call:
 *
 * OnSavedInstanceState
 * OnRestoreInstanceState
 * OnBackPressed
 * OnConfigurationChanged
 * OnActivityCreated
 *
 * - Why not just FragmentController/ActivityController
 * - Controller is reusable across Fragment/Activity, so it may observe any one of them in different context, but core logic remains the same.
 *   So this way ensures controller will not be limited to choose Activity or Fragment in definition phase.
 *   You can just make it polymorphism by using it in Activity or Fragment.
 *
 * Created by nius on 7/23/15.
 */
public interface LifecycleObserver {

    interface ActivityOnly{}

    interface FragmentOnly{}
}
