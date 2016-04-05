package cn.campusapp.pan.lifecycle;

/**
 * <p>
 * Activity/Fragment life cycle observer
 * </p>
 * Shared:(both is called by android)
 * <ul>
 * <li>OnStart</li>
 * <li>OnResume</li>
 * <li>OnPause</li>
 * <li>OnStop</li>
 * <li>OnDestroy</li>
 * <li>OnActivityResult</li>
 * <li>OnConfigurationChanged</li>
 * <li>OnSavedInstanceState</li>
 * </ul>
 * Activity only:
 * <ul>
 * <li>onRestart</li>
 * <li>onNewIntent</li>
 * <li>OnRestoreInstanceState</li>
 * <li>OnPostCreate</li>
 * <li>OnBackPressed</li>
 * </ul>
 * Fragment only:
 * <ul>
 * <li>OnAttach</li>
 * <li>OnDetach</li>
 * <li>OnViewCreated</li>
 * <li>OnDestroyView</li>
 * <li>OnVisible(setUserVisibleHint)</li>
 * <li>OnHiddenChanged</li>
 * <li>OnActivityCreated
 * </ul>
 * <p/>
 * Observers can stop super call:
 * <p/>
 * <ul>
 * <li>OnSavedInstanceState</li>
 * <li>OnRestoreInstanceState</li>
 * <li>OnBackPressed</li>
 * <li>OnConfigurationChanged</li>
 * <li>OnActivityCreated</li>
 * </ul>
 * <p/>
 * <p>
 * - Why not just FragmentController/ActivityController?
 * </p><p>
 * - Controller is reusable across Fragment/Activity, so it may observe any one of them in different context, but core logic remains the same.
 * So this way ensures controller will not be limited to choose Activity or Fragment in definition phase.
 * </p>
 * You can just make it polymorphism by using it in Activity or Fragment.
 * <p/>
 * Created by nius on 7/23/15.
 */
public interface LifecycleObserver {

    interface ForActivity {
    }

    interface ForFragment {
    }
}
