package cn.campusapp.pan;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.campusapp.library.BuildConfig;
import cn.campusapp.library.R;
import cn.campusapp.pan.autorender.AutoRenderLifecyclePlugin;
import cn.campusapp.pan.interaction.OnBackPressed;
import cn.campusapp.pan.lifecycle.LifecycleObserved;
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

    public final static Set<LifecyclePlugin> PLUGINS = new HashSet<LifecyclePlugin>() {
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
    /**
     * 是否输入日志
     */
    private static boolean IS_DEBUG = BuildConfig.DEBUG;

    Activity activity;

    @Nullable PanFragmentV4 fragmentV4;
    @Nullable Class<S> viewModelClazz;
    @Nullable S mViewModel;
    @Nullable Class<? extends GeneralController> controllerClazz;
    @Nullable GeneralController mController;
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

    @SuppressWarnings("unused")
    public static void installPlugin(LifecyclePlugin plugin) {
        PLUGINS.add(plugin);
    }

    @SuppressWarnings("unused")
    public static void setDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    public static <S extends FactoryViewModel> Pan<S> with(@NonNull LifecycleObserved lifecycleObserved, @NonNull Class<S> clazz) {
        Pan<S> f = new Pan<>();
        setUpLifecycleObserved(lifecycleObserved, f);
        f.viewModelClazz = clazz;
        return f;
    }

    public static <S extends FactoryViewModel> Pan<S> with(@NonNull LifecycleObserved lifecycleObserved, @NonNull S viewModel){
        Pan<S> f = new Pan<>();
        setUpLifecycleObserved(lifecycleObserved, f);
        f.mViewModel = viewModel;
        return f;
    }

    private static <S extends FactoryViewModel> void setUpLifecycleObserved(@NonNull LifecycleObserved lifecycleObserved, @NonNull Pan<S> f) {
        if(lifecycleObserved instanceof Activity){
            f.activity = (Activity) lifecycleObserved;
        }else if(lifecycleObserved instanceof PanFragmentV4){
            PanFragmentV4 fragment = (PanFragmentV4) lifecycleObserved;
            f.activity = fragment.getActivity();
            f.fragmentV4 = fragment;
        }else {
            throw new RuntimeException("Only support Activity and PanFragmentV4 currently");
        }
    }




    // region getViewModel (builder method)


    //should mention prevention of same class view models
    public S getViewModel(@NonNull View rootView) {
        return getViewModel(null, rootView, false);
    }


    public S getViewModel() {
        View root = activity.getWindow().getDecorView();
        return getViewModel(null, root, false);
    }

    @SuppressWarnings("unchecked")
    public S getViewModel(@Nullable ViewGroup container, @Nullable View view, boolean attach) {
        try {

            S existingVm = tryFindExistingBindings(view);
            if (existingVm != null) {
                return existingVm;
            }

            S vm;

            if(mViewModel == null) {
                vm = instantiateViewModel();
            }else{
                vm = mViewModel; // if set mViewModel
            }

            vm.setFragment(fragmentV4); //set fragment
            vm.initViewModel(activity, view, container, attach);

            //set tag for view holder pattern
            view = vm.getRootView();
            view.setTag(findProperTagKey(view), vm);

            bindController(vm);

            return vm;
        } catch (Exception e) {
            Pan.LOG.error("cannot get view model", e);
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private S instantiateViewModel() throws NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        if(viewModelClazz == null){
            throw new NullPointerException("view model class or view model must exist one");
        }
        Constructor<S> cons = viewModelClazz.getDeclaredConstructor(); //无参构造函数
        cons.setAccessible(true);
        return cons.newInstance();
    }

    private void bindController(S vm) {
        @SuppressWarnings("unchecked")
        GeneralController contr = (GeneralController) vm.getController();

        contr = mController == null ? contr: mController; //if set mController

        if (contr == null) {
            if (controllerClazz != null) {
                try {
                    contr = controllerClazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else { //give it a no-op controller to make everything works right
                contr = new NoopController<>();
            }
        }

        if (fragmentV4 == null) {
            ACTIVITY_CONTROLLER_MAP.get(activity).add(contr);
            if (contr instanceof LifecycleObserver.FragmentOnly && IS_DEBUG) {
                LOG.warn("controller {} is observing to Fragment-only lifecycle, but use in an Activity context", contr.getClass().getSimpleName());
            }
        } else {
            FRAGMENTV4_CONTROLLER_MAP.get(fragmentV4).add(contr);
            if (contr instanceof LifecycleObserver.ActivityOnly && IS_DEBUG) {
                LOG.warn("controller {} is observing to Activity-only lifecycle, but use in a Fragment context", contr.getClass().getSimpleName());
            }
        }

        //noinspection unchecked
        contr.bindViewModel(vm);
        vm.setController(contr);
    }


    // endregion

    // region tag

    private final static int[] TAGS_PRE_DEFINED = new int[]{
            R.id.PAN_ID_0,
            R.id.PAN_ID_1,
            R.id.PAN_ID_2,
            R.id.PAN_ID_3,
            R.id.PAN_ID_4,
            R.id.PAN_ID_5,
            R.id.PAN_ID_6,
            R.id.PAN_ID_7,
            R.id.PAN_ID_8,
            R.id.PAN_ID_9,
    };

    @SuppressWarnings("unchecked")
    @Nullable
    private S tryFindExistingBindings(View view) {

        if(view == null){
            return null;
        }

        if(tagKey >= 0){ //user has set a tag key
            Object object = view.getTag(tagKey);
            if(isTagObjectAViewModel(view.getTag(tagKey))){
                return (S) object;
            }
        }


        //check if is in pre defined tags
        for(int i=0; i<TAGS_PRE_DEFINED.length; i++){
            Object object = view.getTag(TAGS_PRE_DEFINED[i]);
            if(isTagObjectAViewModel(object)){
                return (S) object;
            }
        }

        return null;
    }

    private boolean isTagObjectAViewModel(Object tag) {
        return tag != null
                &&
                (mViewModel != null && mViewModel.equals(tag)
                        || viewModelClazz != null && viewModelClazz.isInstance(tag));

    }


    int findProperTagKey(@NonNull View view) {
        if(tagKey >= 0){
            return tagKey;
        }

        //find the first available tag key
        for(int i=0; i<TAGS_PRE_DEFINED.length; i++){
            Object object = view.getTag(TAGS_PRE_DEFINED[i]);
            if(object == null){
                tagKey = TAGS_PRE_DEFINED[i];
                break;
            }
        }

        return tagKey;
    }

    @SuppressWarnings("unused")
    public Pan<S> tagKey(@IdRes int key) {
        tagKey = key;
        return this;
    }

    // endregion

    // region lifecycle callbacks

    /**
     *
     * Call the corresponding observers of the Fragment in specific lifecycle
     *
     * @param fragmentV4 the Fragment current be shown and observed
     * @param lifecycleClazz lifecycle observer class
     * @param parameters lifecycle parameters from Fragment methods
     * @return should call super method, only for {@link OnRestoreInstanceState}, {@link OnSaveInstanceState}, {@link cn.campusapp.pan.interaction.OnBackPressed}
     */
    static <T extends LifecycleObserver> boolean call(PanFragmentV4 fragmentV4, Class<T> lifecycleClazz, Object... parameters) {
        boolean shouldCallSuper = true;
        for (Controller controller : FRAGMENTV4_CONTROLLER_MAP.get(fragmentV4)) {
            shouldCallSuper = shouldCallSuper && checkAndCall(lifecycleClazz, controller, parameters);
            callPlugins(lifecycleClazz, controller, parameters);
        }
        if (lifecycleClazz.equals(OnDestroyView.class)) {
            //由于Fragment的绑定一般都在onCreateView中，所以认为onDestroyView，该Fragment的生命周期已结束
            FRAGMENTV4_CONTROLLER_MAP.remove(fragmentV4);
        }

        return shouldCallSuper;
    }


    /**
     *
     * Call the corresponding observers of the Activity in specific lifecycle
     *
     * @param activity  activity that has no Fragments. if it has, use PanActivityV4
     * @param lifecycleClazz lifecycle observer class
     * @param parameters lifecycle parameters from Activity methods
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
     *
     * Call the corresponding observers of the Activity in specific lifecycle
     *
     * @param activity  activity that has Fragments of support v4. if it has, use PanActivityV4
     * @param lifecycleClazz lifecycle observer class
     * @param parameters lifecycle parameters from Activity methods
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
        for (LifecyclePlugin plugin : PLUGINS) {
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
     * @param lifecycleClazz    the lifecycle clazz that should call
     * @param lifecycleObserver the controller may be called
     * @param parameters        the parameters for the lifecycle method
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

    @SuppressWarnings("unused")
    public Pan<S> controlledBy(GeneralController controller){
        mController = controller;
        return this;
    }

    // endregion

}
