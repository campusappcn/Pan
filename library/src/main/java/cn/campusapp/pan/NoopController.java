package cn.campusapp.pan;

/**
 * <p>Do nothing. </p>
 * <p>所有的ViewModel都必须要有一个Controller对象，以使生命周期插件运作。当用户没有指定时，就默认使用该Controller</p>
 * <p/>
 * Created by nius on 10/13/15.
 */
public class NoopController<T extends FactoryViewModel> extends GeneralController<T>{
    @Override
    protected void bindEvents() {

    }
}
