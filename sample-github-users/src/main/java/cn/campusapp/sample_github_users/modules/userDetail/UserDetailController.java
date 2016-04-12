package cn.campusapp.sample_github_users.modules.userDetail;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.campusapp.pan.GeneralController;
import cn.campusapp.pan.lifecycle.OnResume;
import cn.campusapp.pan.lifecycle.OnStart;
import cn.campusapp.sample_github_users.entity.GithubEvent;
import cn.campusapp.sample_github_users.rest.GitHubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nius on 4/12/16.
 */
public class UserDetailController extends GeneralController<UserDetailViewModel> implements OnStart {

    @Override
    protected void bindEvents() {
    }

    @Override
    public void onStart() {

        //此时ViewModel已经设置过User
        Call<List<GithubEvent>> call = GitHubService.Impl.getEvents($vm.getUser().getLogin());

        call.enqueue(new Callback<List<GithubEvent>>() {
            @Override
            public void onResponse(Call<List<GithubEvent>> call, Response<List<GithubEvent>> response) {
                List<GithubEvent> ret = response.body();

                $vm.getEvents().clear();

                if(ret != null) {
                    for (GithubEvent e : ret) {
                        if (GithubEvent.TYPE_PUSH.equals(e.getType())) {
                            $vm.getEvents().add(e.toMap());
                        }
                    }
                }

                $vm.render();
            }

            @Override
            public void onFailure(Call<List<GithubEvent>> call, Throwable t) {
                Log.e("wtf", "network", t);
                Toast.makeText(getActivity(), "network is gone!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
