package cn.campusapp.sample_github_users.modules.userList;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.campusapp.pan.GeneralController;
import cn.campusapp.pan.lifecycle.OnResume;
import cn.campusapp.sample_github_users.entity.GithubUser;
import cn.campusapp.sample_github_users.modules.userDetail.DetailActivity;

/**
 * Created by nius on 4/12/16.
 */
public class UserListController extends GeneralController<UserListViewModel> implements OnResume {

    static final List<GithubUser> CONTRIBUTORS = new ArrayList<>();
    static {
        CONTRIBUTORS.add(new GithubUser("rightgenius"));
        CONTRIBUTORS.add(new GithubUser("choichen"));
        CONTRIBUTORS.add(new GithubUser("beautifulSoup"));
        CONTRIBUTORS.add(new GithubUser("PrototypeZ"));
    }

    @Override
    protected void onBindViewModel() {
        super.onBindViewModel();

        $vm.setUsers(CONTRIBUTORS);
    }

    @Override
    protected void bindEvents() {
        $vm.vListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = DetailActivity.makeIntent(getActivity(),
                        ((GithubUser) $vm.mAdapter.getItem(position)).getLogin());
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        Toast.makeText(getActivity(), "welcome to Pan!", Toast.LENGTH_SHORT).show();
    }
}
