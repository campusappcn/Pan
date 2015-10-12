package cn.campusapp.pan.autorender;


import cn.campusapp.pan.lifecycle.OnStart;

/**
 * 如果一个ViewModel是AutoRenderedViewModel，Controller必须是一个AutoRenderedController
 *
 * Created by nius on 7/29/15.
 */
public interface AutoRenderedActivityController extends AutoRenderedController, OnStart {

}
