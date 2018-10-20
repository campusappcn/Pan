package cn.campusapp.sample_github_users.modules.user;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import butterknife.BindView;
import cn.campusapp.pan.GeneralViewModel;
import cn.campusapp.pan.annotaions.Xml;
import cn.campusapp.sample_github_users.entity.GithubUser;
import cn.campusapp.sample_github_users.R;

import static android.text.TextUtils.isEmpty;

/**
 * Created by nius on 4/12/16.
 */
@Xml(R.layout.view_user)
public class UserViewModel extends GeneralViewModel{

    @BindView(R.id.user_avatar)
    public ImageView vAvatarView;

    @BindView(R.id.user_name)
    public TextView vUserNameView;

    protected GithubUser mGithubUser;

    @Override
    public UserViewModel render() {
        if(mGithubUser == null){
            return this;
        }

        if(!isEmpty(mGithubUser.getAvatarUrl())) {
            Picasso.with(getActivity()).load(mGithubUser.getAvatarUrl())
                    .fit()
                    .centerCrop()
                    .into(vAvatarView);
        }else{
            vAvatarView.setImageBitmap(null);
        }

        String name = mGithubUser.getLogin();
        if(mGithubUser.getName() != null){
            name += "/" + mGithubUser.getName();
        }
        vUserNameView.setText(name);
        return this;
    }

    public UserViewModel setUser(GithubUser githubUser){
        mGithubUser = githubUser;
        ((UserController) getController()).updateInfoIfNecessary();
        return this;
    }
}
