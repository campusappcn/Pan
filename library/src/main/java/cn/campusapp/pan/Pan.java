package cn.campusapp.pan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.campusapp.library.R;
import cn.campusapp.pan.autorender.AutoRenderedController;
import cn.campusapp.pan.autorender.AutoRenderedViewModel;
import cn.campusapp.pan.iteraction.OnBackPressed;
import cn.campusapp.pan.iteraction.OnKeyDown;
import cn.campusapp.pan.iteraction.OnKeyUp;
import cn.campusapp.pan.lifecycle.OnActivityResult;
import cn.campusapp.pan.lifecycle.OnDestroyActivity;
import cn.campusapp.pan.lifecycle.OnDestroyViewFragment;
import cn.campusapp.pan.lifecycle.OnPauseActivity;
import cn.campusapp.pan.lifecycle.OnResumeActivity;
import cn.campusapp.pan.lifecycle.OnResumeFragment;
import cn.campusapp.pan.lifecycle.OnSaveOnRestoreActivity;
import cn.campusapp.pan.lifecycle.OnStartActivity;
import cn.campusapp.pan.lifecycle.OnStopActivity;
import cn.campusapp.pan.lifecycle.OnViewCreatedFragment;
import cn.campusapp.pan.lifecycle.OnVisibleFragment;


/**
 * 工厂类，用于实例化ViewModel
 * 同时，如果该ViewModel已经通过Tag绑定到View上了，就使用之前绑定过的
 * Pan - 纪念我们的设计师
 *
 * @param <S>
 */
public class Pan<S extends FactoryViewModel> {
    /**
     * Controller会被加入到这里，从而对相应的Activity进行监听
     */
    final static Map<Activity, List<Controller>> ACTIVITY_CONTROLLER_MAP = new HashMap<Activity, List<Controller>>() {
        @Override
        public List<Controller> get(Object key) {
            if (null == super.get(key)) {
                put((Activity) key, new ArrayList<Controller>());
            }
            return super.get(key);
        }
    };
    // region Fragment的生命周期
    final static Map<PanFragmentV4, List<Controller>> FRAGMENT_CONTROLLER_MAP = new HashMap<PanFragmentV4, List<Controller>>() {
        @Override
        public List<Controller> get(Object key) {
            if (null == super.get(key)) {
                put((PanFragmentV4) key, new ArrayList<Controller>());
            }
            return super.get(key);
        }
    };

    Activity activity;
    PanFragmentV4 fragmentV4;
    Class<S> viewModelClazz;
    Class<? extends GeneralController> controllerClazz;

    /**
     * 用于指定setTag中的tag
     * 如果没有设置，则使用R.id.TAG_GENERAL_VIEW_MODEL
     */
    int tagKey = -1;

    /**
     * 实例化请使用GeneralViewModel.with()
     * 或其他ViewModel的对应方法
     */
    Pan() {
    }

    /**
     * 获得工厂，用于实例化
     *
     * @param activity
     * @param clazz
     * @param <S>
     * @return
     */
    public static <S extends FactoryViewModel> Pan<S> with(@NonNull PanActivityV4 activity, @NonNull Class<S> clazz) {
        Pan<S> f = new Pan<>();
        f.activity = activity;
        f.viewModelClazz = clazz;
        return f;
    }

    /**
     * 获得工厂，用于实例化
     *
     * @param fragment
     * @param clazz
     * @param <S>
     * @return
     */
    public static <S extends FactoryViewModel> Pan<S> with(@NonNull PanFragmentV4 fragment, @NonNull Class<S> clazz) {
        Pan<S> f = new Pan<>();
        f.activity = fragment.getActivity();
        f.fragmentV4 = fragment;
        f.viewModelClazz = clazz;
        return f;
    }

    /**
     * 设置controller
     *
     * @param controllerClazz
     * @return
     */
    public Pan<S> controlledBy(Class<? extends GeneralController> controllerClazz) {
        if (controllerClazz != null) {
            try {
                this.controllerClazz = controllerClazz;
                this.controllerClazz.getDeclaredConstructor().setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(
                        e);
            }
        }
        return this;
    }


    /**
     * 清空这个view的tag，这样可以确保重新实例化一个ViewModel而不使用之前的
     * <p/>
     * 默认使用R.id.TAG_GENERAL_VIEW_MODEL，如果要重新指定TagKey，请使用tagKey(int)
     *
     * @param view
     * @return
     */
    public Pan<S> resetTag(View view) {
        view.setTag(getTagKey(), null);
        return this;
    }

    /**
     * 设置ViewModel绑定到View上时，使用的tag mKey
     * 默认使用的是R.id.TAG_GENERAL_VIEW_MODEL
     *
     * @param key
     * @return
     */
    public Pan<S> tagKey(@IdRes int key) {
        tagKey = key;
        return this;
    }

    int getTagKey() {
        return tagKey < 0 ? R.id.TAG_GENERAL_VIEW_MODEL : tagKey;
    }

    // region getViewModel (builder method)

    /**
     * 如果container的Tag已经有了，就直接返回
     * 否则实例化一个新的viewmodel
     *
     * @param rootView
     * @return
     */
    public S getViewModel(@NonNull View rootView) {
        try {
            if (rootView.getTag(getTagKey()) != null && rootView.getTag(getTagKey()).getClass().equals(viewModelClazz)) {
                return (S) rootView.getTag(getTagKey()); //init 方法会设置Tag
            }

//            Timber.v("实例化新的ViewModel %s", rootView.toString());

            FactoryViewModel vm = viewModelClazz.newInstance();

            vm.setFragment(fragmentV4);

            vm.initViewModel(activity, rootView);
            vm.getRootView().setTag(getTagKey(), vm);

            bindController(vm);

            return (S) vm;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 如果view存在且Tag已经有了，就直接返回
     * <p/>
     * 其他情况都会新实例化一个新的viewmodel对象
     *
     * @param parent
     * @param view
     * @param attach
     * @return
     */
    @SuppressWarnings("unchecked")
    public S getViewModel(@Nullable ViewGroup parent, @Nullable View view, boolean attach) {
        try {

            if (view != null && view.getTag(getTagKey()) != null && view.getTag(getTagKey()).getClass().equals(viewModelClazz)) {
                return (S) view.getTag(getTagKey()); //init 方法会设置Tag
            }

            if (parent != null) {
//                Timber.v("实例化新的ViewModel %s", parent.toString());
            }

            Constructor<S> cons = viewModelClazz.getDeclaredConstructor(); //无参构造函数
            cons.setAccessible(true);
            FactoryViewModel vm = cons.newInstance();

            vm.setFragment(fragmentV4);

            vm.initViewModel(activity, parent, view, attach);
            vm.getRootView().setTag(getTagKey(), vm);


            bindController(vm);

            return (S) vm;
        } catch (Exception e) {
//            Timber.e(e, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void bindController(FactoryViewModel vm) {
        GeneralController contr = vm.getController();

        if (contr == null && controllerClazz != null) {
            try {
                contr = controllerClazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if ((contr == null || !(contr instanceof AutoRenderedController))
                && vm instanceof AutoRenderedViewModel) {
            throw new RuntimeException("AutoRenderedViewModel必须要有一个AutoRenderedController，例如AutoRenderedFragmentController，或者AutoRenderedActivityController");
        }

        if (contr == null) {
            return;
        }

        if(fragmentV4 == null){
            ACTIVITY_CONTROLLER_MAP.get(activity).add(contr);
        }else{
            FRAGMENT_CONTROLLER_MAP.get(fragmentV4).add(contr);
        }

        //TODO 插件化
//        if (contr instanceof EventBusActivityController) {
//            if (!mBus.isRegistered(contr)) {
//                mBus.register(contr);
//            }
//        }
//
//        if (contr instanceof EventBusFragmentController) {
//            if (!mBus.isRegistered(contr)) {
//                mBus.register(contr
//                );
//            }
//        }


        contr.bindViewModel(vm);
        vm.setController(contr);
    }

    /**
     * 适用于Activity获得ViewModel
     * 如果之前实例化过，有Tag标签，就会直接返回之前的ViewModel对象
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public S getViewModel() {
        try {
            View root = activity.getWindow().getDecorView();
            if (root != null && root.getTag(getTagKey()) != null && root.getTag(getTagKey()).getClass().equals(viewModelClazz)) {
                return (S) root.getTag(getTagKey());
            }
            Constructor<S> cons = viewModelClazz.getDeclaredConstructor(); //无参构造函数
            cons.setAccessible(true);
            FactoryViewModel vm = cons.newInstance();

            if (fragmentV4 != null) {
                throw new RuntimeException("既然是用的Fragment，就要用getViewModel(parent, null, attach)");
            }

            vm.initViewModel(activity);
            vm.getRootView().setTag(getTagKey(), vm);

            bindController(vm);

            return (S) vm;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // endregion

    // region lifecycle callbacks
    static void onResume(Activity activity) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnResumeActivity) {
                ((OnResumeActivity) listener).onResume();
            }
        }
    }

    static void onResume(PanFragmentV4 fragment) {
        for (Controller controller : FRAGMENT_CONTROLLER_MAP.get(fragment)) {
            if (controller instanceof OnResumeFragment) {
                ((OnResumeFragment) controller).onResume();
            }
        }
    }

    static void onStart(Activity activity) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnStartActivity) {
                ((OnStartActivity) listener).onStart();
            }
        }
    }

    static void onStop(Activity activity) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnStopActivity) {
                ((OnStopActivity) listener).onStop();
            }
        }
    }

    static void onPause(Activity activity) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnPauseActivity) {
                ((OnPauseActivity) listener).onPause();
            }
        }
    }

    static void onKeyDown(Activity activity, int keyCode, KeyEvent event) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnKeyDown) {
                ((OnKeyDown) listener).onKeyDown(keyCode, event);
            }
        }
    }

    static void onKeyUp(Activity activity, int keyCode, KeyEvent event) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnKeyUp) {
                ((OnKeyUp) listener).onKeyUp(keyCode, event);
            }
        }
    }

    static void onBackPressed(Activity activity) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnBackPressed) {
                ((OnBackPressed) listener).onBackPressed();
            }
        }
    }


    static void onDestroy(Activity activity) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnDestroyActivity) {
                ((OnDestroyActivity) listener).onDestroy();
            }
        }
        //已经destroy了，果取关
        ACTIVITY_CONTROLLER_MAP.remove(activity);
    }

    static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        for (Controller listener : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (listener instanceof OnActivityResult) {
                ((OnActivityResult) listener).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    static void setUserVisibleHint(PanFragmentV4 fragment, boolean isVisibleToUser) {
        for (Controller listener : FRAGMENT_CONTROLLER_MAP.get(fragment)) {
            if (listener instanceof OnVisibleFragment) {
                ((OnVisibleFragment) listener).onVisible(isVisibleToUser);
            }
        }
    }

    static void onViewCreated(PanFragmentV4 fragment, View view, Bundle bundle) {
        for (Controller listener : FRAGMENT_CONTROLLER_MAP.get(fragment)) {
            if (listener instanceof OnViewCreatedFragment) {
                ((OnViewCreatedFragment) listener).onViewCreated(view, bundle);
            }
        }
    }

    static void onDestroyView(PanFragmentV4 fragment) {
        for (Controller listener : FRAGMENT_CONTROLLER_MAP.get(fragment)) {
            if (listener instanceof OnDestroyViewFragment) {
                ((OnDestroyViewFragment) listener).onDestroyView();
            }
        }

        //由于Fragment的绑定一般都在onCreateView中，所以认为onDestroyView，该Fragment的生命周期已结束
        FRAGMENT_CONTROLLER_MAP.remove(fragment);
    }


    /**
     * @return should call super
     */
    static boolean onRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        boolean b = true;
        for (Controller c : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (c instanceof OnSaveOnRestoreActivity) {
                b = b && ((OnSaveOnRestoreActivity) c).onRestoreInstanceState(savedInstanceState);
            }
        }
        return b;
    }

    /**
     * @return should call super
     */
    static boolean onSaveInstanceState(Activity activity, Bundle outState) {
        boolean b = true;
        for (Controller c : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            if (c instanceof OnSaveOnRestoreActivity) {
                b = b && ((OnSaveOnRestoreActivity) c).onSaveInstanceState(outState);
            }
        }
        return b;
    }

    // endregion
}
