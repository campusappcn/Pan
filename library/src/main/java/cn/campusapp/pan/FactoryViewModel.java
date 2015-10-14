package cn.campusapp.pan;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * 与ViewHolder紧密结合的ViewModel，适用于ViewModelFactory
 * Created by nius on 7/17/15.
 */
public interface FactoryViewModel extends ViewModel {

    /**
     * Init a view model, including:
     *
     * 1. try to inflate the view if null
     * 2. use butterknife to bind views
     *
     * @param context the mActivity attached to
     * @param view the view viewModel attached to, if null, viewModel should inflate one from {@link cn.campusapp.pan.annotaions.Xml} tag.
     * @param container the container for the inflater
     * @param attach tell inflater whether attach the view to the container
     */
    void initViewModel(@NonNull Activity context, @Nullable View view, @Nullable ViewGroup container, boolean attach);

    Controller  getController();

    void setController(GeneralController c);

    Activity getActivity();

    PanFragmentV4 getFragmentV4();

    void setFragment(PanFragmentV4 fragment);

}