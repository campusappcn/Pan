package cn.campusapp.pan;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.campusapp.library.BuildConfig;
import cn.campusapp.library.R;
import cn.campusapp.pan.autorender.AutoRenderLifecyclePlugin;
import cn.campusapp.pan.interaction.OnBackPressed;
import cn.campusapp.pan.lifecycle.LifecycleObserver;
import cn.campusapp.pan.lifecycle.LifecyclePlugin;
import cn.campusapp.pan.lifecycle.OnDestroy;
import cn.campusapp.pan.lifecycle.OnDestroyView;
import cn.campusapp.pan.lifecycle.OnRestoreInstanceState;
import cn.campusapp.pan.lifecycle.OnSaveInstanceState;


/**
 * 工厂类，用于实例化ViewModel
 * 同时，如果该ViewModel已经通过Tag绑定到View上了，就使用之前绑定过的
 * Pan - 纪念我们的设计师
 *
 * @param <S>
 */
@SuppressWarnings("ForLoopReplaceableByForEach")
public class Pan<S extends FactoryViewModel> {

    public final static Logger LOG = LoggerFactory.getLogger(Pan.class);

    public final static List<LifecyclePlugin> PLUGINS = new ArrayList<LifecyclePlugin>(){
        {
            add(new AutoRenderLifecyclePlugin());
        }
    };

    /**
     * Controller会被加入到这里，从而对相应的Activity进行监听
     */
    final static Map<Activity, List<GeneralController>> ACTIVITY_CONTROLLER_MAP = new HashMap<Activity, List<GeneralController>>() {
        @Override
        public List<GeneralController> get(Object key) {
            if (null == super.get(key)) {
                put((Activity) key, new ArrayList<GeneralController>());
            }
            return super.get(key);
        }
    };
    // region Fragment的生命周期
    final static Map<PanFragmentV4, List<GeneralController>> FRAGMENTV4_CONTROLLER_MAP = new HashMap<PanFragmentV4, List<GeneralController>>() {
        @Override
        public List<GeneralController> get(Object key) {
            if (null == super.get(key)) {
                put((PanFragmentV4) key, new ArrayList<GeneralController>());
            }
            return super.get(key);
        }
    };
    private static final String TAG = "Pan";
    /**
     * 是否输入日志
     */
    private static boolean IS_DEBUG = BuildConfig.DEBUG;
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

    public static void setDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    /**
     * 获得工厂，用于实例化
     *
     * @param activity
     * @param clazz
     * @param <S>
     * @return
     */
    public static <S extends FactoryViewModel> Pan<S> with(@NonNull PanFragmentActivity activity, @NonNull Class<S> clazz) {
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

    // region lifecycle callbacks

    /**
     * @param fragmentV4
     * @param lifecycleClazz
     * @param parameters
     * @param <T>
     * @return should call super method, only for {@link OnRestoreInstanceState}, {@link OnSaveInstanceState}, {@link cn.campusapp.pan.interaction.OnBackPressed}
     */
    static <T extends LifecycleObserver> boolean call(PanFragmentV4 fragmentV4, Class<T> lifecycleClazz, Object... parameters) {
        boolean shouldCallSuper = true;
        for (Controller controller : FRAGMENTV4_CONTROLLER_MAP.get(fragmentV4)) {
            shouldCallSuper = shouldCallSuper && checkAndCall(lifecycleClazz, controller, parameters);
        }
        if (lifecycleClazz.equals(OnDestroyView.class)) {
            //由于Fragment的绑定一般都在onCreateView中，所以认为onDestroyView，该Fragment的生命周期已结束
            FRAGMENTV4_CONTROLLER_MAP.remove(fragmentV4);
        }

        return shouldCallSuper;
    }


    /**
     * @param activity       activity that has no Fragments. if it has, use PanActivityV4
     * @param lifecycleClazz
     * @param parameters
     * @param <T>
     * @return should call super method, only for {@link OnRestoreInstanceState}, {@link OnSaveInstanceState}, {@link cn.campusapp.pan.interaction.OnBackPressed}
     */
    static <T extends LifecycleObserver> boolean call(PanActivity activity, Class<T> lifecycleClazz, Object... parameters) {
        boolean shouldCallSuper = true;
        for (Controller controller : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            shouldCallSuper = shouldCallSuper && checkAndCall(lifecycleClazz, controller, parameters);
            callPlugins(lifecycleClazz, controller, parameters);
        }

        if (lifecycleClazz.equals(OnDestroy.class)) {
            //已经destroy了，果取关
            ACTIVITY_CONTROLLER_MAP.remove(activity);
        }
        return shouldCallSuper;
    }

    /**
     * @param activity
     * @param lifecycleClazz
     * @param parameters
     * @param <T>
     * @return should call super method, only for {@link OnRestoreInstanceState}, {@link OnSaveInstanceState}, {@link cn.campusapp.pan.interaction.OnBackPressed}
     */
    static <T extends LifecycleObserver> boolean call(PanFragmentActivity activity, Class<T> lifecycleClazz, Object... parameters) {
        boolean shouldCallSuper = true;
        for (Controller controller : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            shouldCallSuper = shouldCallSuper && checkAndCall(lifecycleClazz, controller, parameters);

            callPlugins(lifecycleClazz, controller, parameters);
        }

        //call onBackPressed for associated Fragments
        if (lifecycleClazz.equals(OnBackPressed.class)) {
            onBackPressedForFragmentV4(activity);
        }

        if (lifecycleClazz.equals(OnDestroy.class)) {
            //已经destroy了，果取关
            ACTIVITY_CONTROLLER_MAP.remove(activity);
        }
        return shouldCallSuper;
    }

    private static <T extends LifecycleObserver> void callPlugins(Class<T> lifecycleClazz, Controller controller, Object[] parameters) {
        for (LifecyclePlugin plugin: PLUGINS){
            plugin.call(controller, lifecycleClazz, parameters);
        }
    }

    private static void onBackPressedForFragmentV4(FragmentActivity activity) {
        for (android.support.v4.app.Fragment sf : activity.getSupportFragmentManager().getFragments()) {
            if (sf instanceof PanFragmentV4) {
                call((PanFragmentV4) sf, OnBackPressed.class);
            }
        }
    }

    private static <T extends LifecycleObserver> boolean checkAndCall(Class<T> lifecycleClazz, Controller controller, Object[] parameters) {
        return !(controller instanceof LifecycleObserver) ||
                checkAndCall(lifecycleClazz, (LifecycleObserver) controller, parameters);
    }

    /**
     * call the controller if it is instance of lifecycleClazz
     *
     * @param lifecycleClazz the lifecycle clazz that should call
     * @param lifecycleObserver     the controller may be called
     * @param parameters     the parameters for the lifecycle method
     * @return whether should call super lifecycle method, only for {@link OnRestoreInstanceState}, {@link OnSaveInstanceState}, {@link cn.campusapp.pan.interaction.OnBackPressed}
     */
    private static <T extends LifecycleObserver> boolean checkAndCall(Class<T> lifecycleClazz, LifecycleObserver lifecycleObserver, Object[] parameters) {
        if (lifecycleClazz.isInstance(lifecycleObserver)) {

            String methodName = getMethodName(lifecycleClazz);

            // if any callback returns a boolean, && that result
            boolean shouldCallSuper = true;

            //invoke the method with the same name
            Method[] methods = lifecycleClazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(methodName)) {
                    try {
                        Object result = methods[i].invoke(lifecycleObserver, parameters);
                        if (result != null && result instanceof Boolean) {
                            shouldCallSuper = shouldCallSuper && (boolean) result;
                        }
                    } catch (Exception e) {
                        if (BuildConfig.DEBUG) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            return shouldCallSuper;
        }
        return true;
    }

    @NonNull
    private static <T extends LifecycleObserver> String getMethodName(Class<T> lifecycleClazz) {
        char[] methodNameChars = lifecycleClazz.getSimpleName().toCharArray();
        methodNameChars[0] = Character.toLowerCase(methodNameChars[0]);
        return new String(methodNameChars);
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

    // endregion

    // region tag

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

    // endregion

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

            bindControllerAndApplyPlugins(vm);

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


            bindControllerAndApplyPlugins(vm);

            return (S) vm;
        } catch (Exception e) {
//            Timber.e(e, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void bindControllerAndApplyPlugins(FactoryViewModel vm) {
        bindController(vm);
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

        if (contr == null) {
            return;
        }

        if (fragmentV4 == null) {
            ACTIVITY_CONTROLLER_MAP.get(activity).add(contr);
            if (contr instanceof LifecycleObserver.FragmentOnly && IS_DEBUG) {
                Log.w(TAG, "controller " + contr.getClass().getSimpleName() + " is observing to Fragment-only lifecycle, but use in an Activity context");
            }
        } else {
            FRAGMENTV4_CONTROLLER_MAP.get(fragmentV4).add(contr);
            if (contr instanceof LifecycleObserver.ActivityOnly && IS_DEBUG) {
                Log.w(TAG, "controller " + contr.getClass().getSimpleName() + " is observing to Activity-only lifecycle, but use in a Fragment context");
            }
        }

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

            bindControllerAndApplyPlugins(vm);

            return (S) vm;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // endregion
}
