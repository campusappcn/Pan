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

    void initViewModel(@NonNull Activity context, @Nullable ViewGroup parent, @Nullable View view, boolean attach);

    void initViewModel(@NonNull Activity activity);

    void initViewModel(@NonNull Activity context, @NonNull View container);

    GeneralViewHolder getViewHolder();

    GeneralController getController();

    void setController(GeneralController c);

    Activity getActivity();

    PanFragmentV4 getFragment();

    void setFragment(PanFragmentV4 fragment);

    void onMetaBind();
}
