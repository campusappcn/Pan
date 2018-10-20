package cn.campusapp.sample_github_users.modules.userDetail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.campusapp.pan.GeneralViewModel;
import cn.campusapp.pan.Pan;
import cn.campusapp.pan.ViewModel;
import cn.campusapp.pan.annotaions.Xml;
import cn.campusapp.sample_github_users.R;
import cn.campusapp.sample_github_users.entity.GithubEvent;
import cn.campusapp.sample_github_users.entity.GithubUser;
import cn.campusapp.sample_github_users.modules.user.UserController;
import cn.campusapp.sample_github_users.modules.user.UserViewModel;

/**
 * Created by nius on 4/12/16.
 */
@Xml(R.layout.activity_detail)
public class UserDetailViewModel extends GeneralViewModel {

    UserViewModel mUserViewModel;

    GithubUser mGithubUser;

    List<Map<String, String>> mEvents = new ArrayList<>();

    @BindView(R.id.event_list)
    public ListView vEventListView;

    protected BaseAdapter mEventAdapter;

    @Override
    protected void onInit() {
        super.onInit();

        //delegation
        mUserViewModel = Pan.with(getObserving(), UserViewModel.class)
                .controlledBy(UserController.class)
                .getViewModel(getRootView());

        initListViewAdapter();
    }

    @Override
    public UserDetailViewModel render() {

        mUserViewModel.render(); //delegation

        mEventAdapter.notifyDataSetChanged();
        return this;
    }

    public UserDetailViewModel setUser(GithubUser user){
        mGithubUser = user;
        mUserViewModel.setUser(user);
        return this;
    }

    public void initListViewAdapter(){
        mEventAdapter = new SimpleAdapter(getActivity(),
                mEvents,
                R.layout.view_event,
                new String[]{"event"},
                new int[]{R.id.event_text}
                );
        vEventListView.setAdapter(mEventAdapter);
    }

    public GithubUser getUser(){
        return mGithubUser;
    }
    public List<Map<String, String>> getEvents(){
        return mEvents;
    }
}
