package cn.campusapp.pan;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

import cn.campusapp.pan.annotaions.Xml;

/**
 * 与ViewHolder紧密结合的ViewModel，适用于ViewModelFactory
 * <p/>
 * Created by nius on 7/17/15.
 */
public interface FactoryViewModel extends ViewModel {


    ViewFactory DEFAULT_VIEW_FACTORY = new FactoryViewModel.ViewFactory();

    Controller getController();

    void setController(GeneralController c);

    Activity getActivity();

    void setActivity(Activity activity);

    Fragment getFragment();

    void setFragment(Fragment fragment);

    void bindViews();

    void setRootView(View rootView);

    class ViewFactory {

        public View inflat(@NonNull Activity context, @Nullable View view, @Nullable ViewGroup container, boolean attach, Class clazz) {
            return view != null ? view : initWithoutView(context, container, attach, clazz);
        }

        /**
         * 如果view没有创建好，这边自己inflat一个
         * 如果你是Activity，请不要使用这个！
         *
         * @param attach 对于Fragment和Adapter，一般attach都直接传false，如果是动态生成View的场景，可以传true
         */
        public View initWithoutView(Context context, ViewGroup parent, boolean attach, Class clazz) {
            return LayoutInflater.from(context).inflate(getLayout(clazz), parent, attach);
        }

        @LayoutRes
        public int getLayout(Class clazz) {
//        this.getClass().getAnnotation(Xml.class).value();
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

        public <T extends FactoryViewModel> T createViewAndViewModel(Class<T> clazz, Activity activity, View rootView, @Nullable ViewGroup container, boolean attach) {
            Constructor constructor;
            rootView = inflat(activity, rootView, container, attach, clazz);

            try {
                Class baseClass = clazz.getSuperclass();
                while (baseClass != null) {
                    if (baseClass.equals(GeneralViewModel.class)) {
                        constructor = clazz.getConstructor();
                        GeneralViewModel vm = (GeneralViewModel) constructor.newInstance();
                        vm.mRootView = rootView;
                        //noinspection unchecked
                        return (T) vm;
                    } else if (baseClass.equals(RecyclerViewModel.class)) {
                        constructor = clazz.getConstructor(View.class);
                        //noinspection unchecked
                        return (T) constructor.newInstance(rootView);
                    }

                    baseClass = baseClass.getSuperclass();
                }
            } catch (Exception e) {
                Pan.LOG.info("{}'s constructor has something wrong: {}", clazz.getSimpleName(), e.getMessage());
            }

            throw new UnsupportedOperationException("create instance failed for " + clazz.getSimpleName() + ", consider use Pan.with(object, activity) to instantiate the class yourself");
        }

    }
}