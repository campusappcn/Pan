package cn.campusapp.pan;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import cn.campusapp.pan.annotaions.Xml;


/**
 * 兼具ViewModel和ViewHolder功能
 * <p/>
 * 需要一个无参构造方法
 * <p/>
 * Created by nius on 7/17/15.
 */
public abstract class GeneralViewModel implements FactoryViewModel {

    public View mRootView = null;
    protected Activity mActivity;
    protected GeneralController mController;

    protected PanFragmentV4 mFragment;

    public GeneralViewModel() {
    }

    public static List<Field> getAllFields(Class<?> type) {
        return getAllFields(new ArrayList<Field>(), type);
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    @Override
    public GeneralController getController() {
        return mController;
    }

    @Override
    public void setController(GeneralController c) {
        mController = c;
    }

    @Override
    public void initViewModel(@NonNull Activity context, @Nullable View view, @Nullable ViewGroup container, boolean attach) {
        mActivity = context;

        if (view == null) {
            initWithoutView(context, container, attach);
        } else {
            init(view);
        }
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public PanFragmentV4 getFragmentV4() {
        return mFragment;
    }

    @Override
    public void setFragment(PanFragmentV4 fragment) {
        mFragment = fragment;
    }

    /**
     * 如果view没有创建好，这边自己inflat一个
     * 如果你是Activity，请不要使用这个！
     *
     * @param attach 对于Fragment和Adapter，一般attach都直接传false，如果是动态生成View的场景，可以传true
     */
    public View initWithoutView(Context context, ViewGroup parent, boolean attach) {
        init(
                LayoutInflater.from(context).inflate(getLayout(), parent, attach)
        );
        return mRootView;
    }

    /**
     * 可以在Activity、Fragment、Adapter.getView中使用。
     * 注意，在Adapter中，只需要在创建时init一次，后续重用即可
     *
     * @param root the view that binds the view model
     */
    public void init(View root) {
        mRootView = root;
        injectViews(root);
        onInit();
    }

    /**
     * 在注入了views之后会被调用，允许子类进行一些初始化操作
     */
    protected void onInit() {

    }

    @LayoutRes
    public int getLayout() {
//        this.getClass().getAnnotation(Xml.class).value();
        Class clazz = this.getClass();
        Xml annotation = (Xml) clazz.getAnnotation(Xml.class);
        while (annotation == null) {
            clazz = clazz.getSuperclass();
            if (clazz == null) {
                throw new RuntimeException("Can't find layout.");
            }
            annotation = (Xml) clazz.getAnnotation(Xml.class);
        }
        return annotation.value();
    }

    /**
     * 用于在整体绑定事件
     *
     * @return the root view of the view model binds to
     */
    public View getRootView() {
        return mRootView;
    }

    protected void injectViews(View root) {
        ButterKnife.bind(this, root);
    }
}
