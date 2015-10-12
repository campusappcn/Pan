package cn.campusapp.pan;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.campusapp.pan.annotaions.Id;
import cn.campusapp.pan.annotaions.Xml;


/**
 * 用于存储一个Activity或者Fragment的界面对象
 *
 * 继承后，通过@Xml注解来绑定xml，需要使用此界面的类，使用getLayout获得resId
 *
 * 使用@Id来对子类的View对象进行绑定，会自动进行注入
 *
 * 注意，如果搭配GeneralViewModel，需要有一个无参构造函数
 *
 * Created by nius on 5/22/15.
 */
public abstract class GeneralViewHolder {

    public View mRootView = null;

    public void init(Activity activity){
        init(activity.getWindow().getDecorView());
    }

    /**
     * 如果view没有创建好，这边自己inflat一个
     * 如果你是Activity，请不要使用这个！
     */
    public View initWithoutView(Context context, ViewGroup parent){
        init(
            LayoutInflater.from(context).inflate(getLayout(), parent)
        );
        return mRootView;
    }

    /**
     * 如果view没有创建好，这边自己inflat一个
     * 如果你是Activity，请不要使用这个！
     *
     * @param attach 对于Fragment和Adapter，一般attach都直接传false，如果是动态生成View的场景，可以传true
     */
    public View initWithoutView(Context context, ViewGroup parent, boolean attach){
        init(
                LayoutInflater.from(context).inflate(getLayout(), parent, attach)
        );
        return mRootView;
    }

    /**
     * 可以在Activity、Fragment、Adapter.getView中使用。
     * 注意，在Adapter中，只需要在创建时init一次，后续重用即可
     *
     * @param root
     */
    public void init(View root){
        mRootView = root;
        injectViews(root);
        onInit();
    }

    protected void injectViews(View root) {
        for(Field f: getAllFields(this.getClass())){
            try {
                Id idAnno = f.getAnnotation(Id.class);
                if(idAnno == null) continue;

                f.setAccessible(true);
                f.set(this, root.findViewById(idAnno.value()));

            }catch(Exception e){
                e.printStackTrace(); //TODO log alternatives
            }
        }
    }


    /**
     * 在注入了views之后会被调用，允许子类进行一些初始化操作
     */
    protected void onInit(){

    }

    public static List<Field> getAllFields(Class<?> type){
        return getAllFields(new ArrayList<Field>(), type);
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
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
     * @return
     */
    public View getRootView(){
        return mRootView;
    }
}
