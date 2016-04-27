package cn.campusapp.pan;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.campusapp.library.BuildConfig;
import cn.campusapp.library.R;
import cn.campusapp.pan.autorender.AutoRenderControllerLifecyclePlugin;
import cn.campusapp.pan.lifecycle.ControllerLifecyclePlugin;
import cn.campusapp.pan.lifecycle.LifecycleObserved;
import cn.campusapp.pan.lifecycle.LifecycleObserver;
import cn.campusapp.pan.lifecycle.OnDestroy;
import cn.campusapp.pan.lifecycle.OnDestroyView;
import cn.campusapp.pan.lifecycle.OnRestoreInstanceState;
import cn.campusapp.pan.lifecycle.OnSaveInstanceState;
import cn.campusapp.pan.lifecycle.PanLifecyclePlugin;


/**
 * <p>
 * 工厂类，用于实例化ViewModel
 * </p><p>
 * 同时，如果该ViewModel已经通过Tag绑定到View上了，就使用之前绑定过的
 * </p>
 * <p>
 * <br/>
 * <strong>Pan - 纪念我们的设计师</strong>
 * </p>
 * @param <S> 用于限定工厂对象的ViewModel
 */
@SuppressWarnings("ForLoopReplaceableByForEach")
public class Pan<S extends FactoryViewModel> {

    public final static Logger LOG = LoggerFactory.getLogger(Pan.class);

    public final static Set<ControllerLifecyclePlugin> CONTROLLER_PLUGINS = new HashSet<ControllerLifecyclePlugin>() {
        {
            add(new AutoRenderControllerLifecyclePlugin());
        }
    };

    /**
     * 插件集合
     */
    public final static Set<PanLifecyclePlugin> PAN_PLUGINS = new HashSet<>();

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
    // Fragment的控制器
    final static Map<Fragment, List<GeneralController>> FRAGMENT_CONTROLLER_MAP = new HashMap<Fragment, List<GeneralController>>() {
        @Override
        public List<GeneralController> get(Object key) {
            if (null == super.get(key)) {
                put((Fragment) key, new ArrayList<GeneralController>());
            }
            return super.get(key);
        }
    };


    //Lifecycle class method cache
    final static Map<Class, Method> LIFECYCLE_METHOD_CACHE = new HashMap<>();

    /**
     * 是否输入日志
     */
    private static boolean IS_DEBUG = BuildConfig.DEBUG;

    Activity mActivity;

    @Nullable Fragment mFragment;

    @Nullable Class<S> mViewModelClazz;
    @Nullable S mViewModel;

    @Nullable Class<? extends GeneralController> mControllerClazz;
    @Nullable GeneralController mController;

    @NonNull FactoryViewModel.ViewFactory mViewFactory = FactoryViewModel.DEFAULT_VIEW_FACTORY;

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
     * use this to print logs
     */
    @SuppressWarnings("unused")
    public static void setDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    //region factory entry "with"

    /**
     * 工厂方法入口。适用于ViewModel带有公有构造函数的场景。
     *
     * @param lifecycleObserved PanActivity/PanFragment, 或者其他实现了{@link LifecycleObserved}接口的基类，具体实现请参考{@link LifecycleObserved}
     * @param clazz ViewModel的类型，通常是{@link GeneralViewModel}或者{@link RecyclerViewModel}的子类
     * @param <S> ViewModel类型泛型
     * @return Pan工厂对象
     */
    public static <S extends FactoryViewModel> Pan<S> with(@NonNull LifecycleObserved lifecycleObserved, @NonNull Class<S> clazz) {
        Pan<S> f = new Pan<>();
        setUpLifecycleObserved(lifecycleObserved, f);
        f.mViewModelClazz = clazz;
        return f;
    }

    /**
     * 工厂方法入口。适用于ViewModel没有公有构造函数，直接传入对象（例如非静态内部类）。
     *
     * @param lifecycleObserved PanActivity/PanFragment, 或者其他实现了{@link LifecycleObserved}接口的基类，具体实现请参考{@link LifecycleObserved}
     * @param viewModel ViewModel对象
     * @param <S> ViewModel类型泛型
     * @return Pan工厂对象
     */
    public static <S extends FactoryViewModel> Pan<S> with(@NonNull LifecycleObserved lifecycleObserved, @NonNull S viewModel){
        Pan<S> f = new Pan<>();
        setUpLifecycleObserved(lifecycleObserved, f);
        f.mViewModel = viewModel;
        return f;
    }

    private static <S extends FactoryViewModel> void setUpLifecycleObserved(@NonNull LifecycleObserved lifecycleObserved, @NonNull Pan<S> f) {
        if(lifecycleObserved instanceof Activity){
            f.mActivity = (Activity) lifecycleObserved;
        }else if(lifecycleObserved instanceof PanFragment){
            PanFragment fragment = (PanFragment) lifecycleObserved;
            f.mActivity = fragment.getActivity();
            f.mFragment = fragment;
        }else {
            throw new RuntimeException("Only support Activity and PanFragment currently");
        }
    }

    //endregion

    // region getViewModel (viewFactory method)


    //should mention prevention of same class view models
    public S getViewModel(@NonNull View rootView) {
        return getViewModel(null, rootView, false);
    }


    public S getViewModel() {
        View root = ((ViewGroup)mActivity.findViewById(android.R.id.content)).getChildAt(0);
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
                vm = mViewFactory.createViewAndViewModel(mViewModelClazz, mActivity, view, container, attach);
            }else{
                vm = mViewModel; // if set mViewModel
                if(vm.getRootView() == null){ //inflat view if absent
                    vm.setRootView(mViewFactory.inflat(mActivity, view, container, attach, vm.getClass()));
                }
            }

            vm.setActivity(mActivity);
            vm.setFragment(mFragment); //set fragment
            vm.bindViews();

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

    private void bindController(S vm) {
        GeneralController controller;

        if(mController != null){
            controller = mController;
        }
        else if (mControllerClazz != null){
            try {
                controller = mControllerClazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            controller = (GeneralController) vm.getController();
        }

        if(controller == null){
            controller = new NoopController();
        }

        if (mFragment == null) {
            ACTIVITY_CONTROLLER_MAP.get(mActivity).add(controller);
            if (controller instanceof LifecycleObserver.ForFragment && IS_DEBUG) {
                LOG.warn("controller {} is observing to Fragment-only lifecycle, but use in an Activity context", controller.getClass().getSimpleName());
            }
        } else {
            FRAGMENT_CONTROLLER_MAP.get(mFragment).add(controller);
            if (controller instanceof LifecycleObserver.ForActivity && IS_DEBUG) {
                LOG.warn("controller {} is observing to Activity-only lifecycle, but use in a Fragment context", controller.getClass().getSimpleName());
            }
        }

        //noinspection unchecked
        controller.bindViewModel(vm);
        vm.setController(controller);
    }


    // endregion

    // region tag for ViewHolder pattern

    /**
     * 用于处理ViewHolder模式，将ViewModel绑定到View上。最多支持一个View绑定10个ViewModel
     */
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
                        || mViewModelClazz != null && mViewModelClazz.isInstance(tag));

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
     * @param fragment the Fragment current be shown and observed
     * @param lifecycleClazz lifecycle observer class
     * @param parameters lifecycle parameters from Fragment methods
     * @return should call super method, only for {@link OnRestoreInstanceState}, {@link OnSaveInstanceState}, {@link cn.campusapp.pan.interaction.OnBackPressed}
     */
    static <T extends LifecycleObserver> boolean call(Fragment fragment, Class<T> lifecycleClazz, Object... parameters) {
        boolean shouldCallSuper = true;
        for (Controller controller : FRAGMENT_CONTROLLER_MAP.get(fragment)) {
            shouldCallSuper = shouldCallSuper && checkAndCall(lifecycleClazz, controller, parameters);
            callPlugins(lifecycleClazz, controller, parameters);
        }

        if (OnDestroyView.class.equals(lifecycleClazz) /*由于Fragment的绑定一般都在onCreateView中，所以认为onDestroyView，该Fragment的生命周期已结束*/
                || OnDestroy.class.equals(lifecycleClazz) /* double check to avoid memory leak.*/
                ) {
            FRAGMENT_CONTROLLER_MAP.remove(fragment);
        }

        //call plugins on lifecycle
        for (PanLifecyclePlugin plugin: PAN_PLUGINS){
            try {
                plugin.onFragmentLifecycle(fragment, lifecycleClazz, parameters);
            }catch (Throwable e){
                LOG.error("wtf! Your plugin is shit!", e);
            }
        }

        return shouldCallSuper;
    }


    /**
     *
     * Call the corresponding observers of the Activity in specific lifecycle
     *
     * @param activity  mActivity
     * @param lifecycleClazz lifecycle observer class
     * @param parameters lifecycle parameters from Activity methods
     * @return should call super method, only for {@link OnRestoreInstanceState}, {@link OnSaveInstanceState}, {@link cn.campusapp.pan.interaction.OnBackPressed}
     */
    static <T extends LifecycleObserver> boolean call(Activity activity, Class<T> lifecycleClazz, Object... parameters) {
        boolean shouldCallSuper = true;
        for (Controller controller : ACTIVITY_CONTROLLER_MAP.get(activity)) {
            shouldCallSuper = shouldCallSuper && checkAndCall(lifecycleClazz, controller, parameters);
            callPlugins(lifecycleClazz, controller, parameters);
        }

        if (lifecycleClazz.equals(OnDestroy.class)) {
            //已经destroy了，果取关
            ACTIVITY_CONTROLLER_MAP.remove(activity);
        }

        //call plugins on lifecycle
        for (PanLifecyclePlugin plugin: PAN_PLUGINS){
            try {
                plugin.onActivityLifecycle(activity, lifecycleClazz, parameters);
            }catch (Throwable e){
                LOG.error("wtf! Your plugin is shit!", e);
            }
        }


        return shouldCallSuper;
    }

    //endregion

    //region lifecycle plugins

    @SuppressWarnings("unused")
    public static void installPlugin(ControllerLifecyclePlugin plugin) {
        CONTROLLER_PLUGINS.add(plugin);
    }

    public static void installPlugin(PanLifecyclePlugin plugin){
        PAN_PLUGINS.add(plugin);
    }


    private static <T extends LifecycleObserver> void callPlugins(Class<T> lifecycleClazz, Controller controller, Object[] parameters) {
        for (ControllerLifecyclePlugin plugin : CONTROLLER_PLUGINS) {
            plugin.call(controller, lifecycleClazz, parameters);
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

            // if any callback returns a boolean, take that value
            boolean shouldCallSuper = true;

            Method method = getLifecycleMethod(lifecycleClazz);
            try {
                Object result = method.invoke(lifecycleObserver, parameters);
                if (result != null && result instanceof Boolean) {
                    shouldCallSuper = (boolean) result;
                }
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    throw new RuntimeException(e);
                }
            }
            return shouldCallSuper;
        }
        return true;
    }

    /**
     * 缓存反射的方法
     */
    private static <T extends LifecycleObserver> Method getLifecycleMethod(Class<T> lifecycleClazz) {
        //check method cache
        Method method = LIFECYCLE_METHOD_CACHE.get(lifecycleClazz);
        if(method != null){
            return method;
        }

        //invoke the method of the clazz
        //the method name is NOT checked, for the lifecycle class should only have one method
        //method count is checked to prevent design failure
        Method[] methods = lifecycleClazz.getMethods();
        if(methods.length > 1){
            throw new RuntimeException("The lifecycle observer should only have one method, reconsider your design");
        }
        method = methods[0];

        LIFECYCLE_METHOD_CACHE.put(lifecycleClazz, method); //add to cache

        return method;
    }
    //endregion

    //region controller setter

    /**
     * 设置控制器，适用于控制器有公有无参构造函数的情况
     * @param controllerClazz 控制器类型，一般是{@link GeneralController}的子类
     * @return Pan工厂
     */
    public Pan<S> controlledBy(Class<? extends GeneralController> controllerClazz) {
        if (controllerClazz != null) {
            try {
                this.mControllerClazz = controllerClazz;
                this.mControllerClazz.getDeclaredConstructor().setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(
                        e);
            }
        }
        return this;
    }

    /**
     * 设置控制器，适用于控制器没有公有无参构造函数的情况
     * @param controller 控制器对象
     * @return Pan工厂
     */
    @SuppressWarnings("unused")
    public Pan<S> controlledBy(GeneralController controller){
        mController = controller;
        return this;
    }

    //endregion

    /**
     * 设置ViewFactory，用于inflate ViewModel对应的Xml。
     * 可以参考sample-databinding中的使用
     * @param viewFactory ViewFactory对象
     * @return Pan工厂
     */
    public Pan<S> viewFactory(@NonNull FactoryViewModel.ViewFactory viewFactory){
        mViewFactory = viewFactory;
        return this;
    }

}
