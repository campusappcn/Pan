package cn.campusapp.pan.annotaions;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于表示某个view对应的xml
 * <p>
 * Created by Jason on 2015/5/30.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Xml {
    @LayoutRes int value();
}
