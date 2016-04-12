package cn.campusapp.sample_github_users.modules.userList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.campusapp.pan.GeneralViewModel;
import cn.campusapp.pan.Pan;
import cn.campusapp.pan.annotaions.Xml;
import cn.campusapp.sample_github_users.entity.GithubUser;
import cn.campusapp.sample_github_users.R;
import cn.campusapp.sample_github_users.modules.user.UserController;
import cn.campusapp.sample_github_users.modules.user.UserViewModel;

/**
 * Created by nius on 4/12/16.
 */
@Xml(R.layout.activity_main)
public class UserListViewModel extends GeneralViewModel {

    @Bind(R.id.user_list)
    public ListView vListView;

    protected List<GithubUser> mUsers = new ArrayList<>();

    protected BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mUsers.size();
        }

        @Override
        public GithubUser getItem(int position) {
            return mUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return Pan.with(getObserving(), UserViewModel.class)
                    .controlledBy(UserController.class)
                    .getViewModel(parent, convertView, false)
                    .setUser(getItem(position))
                    .render()
                    .getRootView();
        }
    };

    @Override
    protected void onInit() {
        super.onInit();
        vListView.setAdapter(mAdapter);
    }

    @Override
    public UserListViewModel render() {
        mAdapter.notifyDataSetChanged();
        return this;
    }

    public void setUsers(List<GithubUser> users) {
        mUsers = users;
    }
}
