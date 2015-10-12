package cn.campusapp.pan;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Created by nius on 7/17/15.
 *
 */
public abstract class GeneralViewModel<T extends GeneralViewHolder> implements FactoryViewModel {

    private Class<T> mViewHolderClazz;

    protected T $v;

    protected Activity mActivity;

    protected GeneralController mController;

    protected PanFragmentV4 mFragment;

    /**
     * 无参构造函数一定要存在
     */
    public GeneralViewModel(){

    }

    /**
     * ViewHolder准备完毕
     */
    protected void onInit(){}

    @Override
    public void setController(GeneralController c) {
        mController = c;
    }

    @Override
    public GeneralController getController() {
        return mController;
    }

    /**
     * 会给这个View设置Tag，并进行重用，可以用于Adapter
     * 会给View设置Tag一个名为KEY_TAG的Tag，指向this
     *
     * @param context
     * @param parent
     * @param view
     * @param attach
     */
    @Override
    public void initViewModel(@NonNull Activity context, @NonNull ViewGroup parent, @Nullable View view, boolean attach){
        mActivity = context;

//        Timber.d("使用parent实例化了一个ViewModel");
        instantiateViewHolder();

        if(view == null) {
            $v.initWithoutView(context, parent, attach);
        }else{
            $v.init(view);
        }
        onInit();
    }

    /**
     * 会给View设置Tag一个名为KEY_TAG的Tag，指向this
     *
     * @param activity
     */
    @Override
    public void initViewModel(@NonNull Activity activity){
        mActivity = activity;
        instantiateViewHolder();
        $v.init(activity);
        onInit();
    }

    /**
     * 针对已经实例化好View的
     * 会给View设置Tag一个名为KEY_TAG的Tag，指向this
     *
     * @param context
     * @param container
     */
    @Override
    public void initViewModel(@NonNull Activity context, @NonNull View container){
        mActivity = context;

        if(container == null){
            throw new RuntimeException("你想好了没有啊，就乱写");
        }

        instantiateViewHolder();
        $v.init(container);
        onInit();
    }

    private Class<T> getViewHolderClazz() {
        if(mViewHolderClazz == null){
            Type type = getClass().getGenericSuperclass();
            ParameterizedType paramType = (ParameterizedType) type;
            mViewHolderClazz = (Class<T>) paramType.getActualTypeArguments()[0];
        }
        return mViewHolderClazz;
    }

    private void instantiateViewHolder() {
        try {
            $v = getViewHolderClazz().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("你想好了没有啊，就乱写", e);
        }
    }

    @Override
    public T getViewHolder() {
        return $v;
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public PanFragmentV4 getFragment() {
        return mFragment;
    }

    @Override
    public void setFragment(PanFragmentV4 fragment) {
        mFragment = fragment;
    }

    /**
     * 当meta数据可用时
     */
    @Override
    public void onMetaBind() {

    }

    @Override
    public View getRootView() {
        return $v.getRootView();
    }
}
