package cn.campusapp.pan.autorender;


import cn.campusapp.pan.lifecycle.OnViewCreatedFragment;
import cn.campusapp.pan.lifecycle.OnVisibleFragment;

/**
 * 如果一个ViewModel是AutoRenderedViewModel，Controller必须是一个AutoRenderedController
 * <p/>
 * Created by nius on 7/29/15.
 */
public interface AutoRenderedFragmentController extends AutoRenderedController, OnVisibleFragment, OnViewCreatedFragment {

}
