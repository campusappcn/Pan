package cn.campusapp.pan;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;


/**
 * 兼具ViewModel和ViewHolder功能
 * <p/>
 * 需要一个无参构造方法
 * <p/>
 * Created by nius on 7/17/15.
 */
public abstract class ViewBundle extends ButterViewHolder implements FactoryViewModel {

    protected Activity mActivity;
     protected GeneralController mController;
     protected PanFragmentV4 mFragment;


    public ViewBundle() {
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
    public void initViewModel(@NonNull Activity context, @Nullable ViewGroup parent, @Nullable View view, boolean attach) {
        mActivity = context;

        if (view == null) {
            initWithoutView(context, parent, attach);
        } else {
            init(view);
        }
    }

    @Override
    public void initViewModel(@NonNull Activity activity) {
        mActivity = activity;
        init(activity);
    }

    @Override
    public void initViewModel(@NonNull Activity context, @NonNull View container) {
        mActivity = context;

        init(container);

    }

    @Override
    public ViewBundle getViewHolder() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    public void startActivity(@NonNull Intent intent) {
        mActivity.startActivity(intent);
    }

    public void startActivityForResult(@NonNull Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    public PanFragmentV4 getFragment() {
        return mFragment;
    }

    @Override
    public void setFragment(PanFragmentV4 fragment) {
        mFragment = fragment;
    }

    @Override
    public void onMetaBind() {

    }
}
