package cn.campusapp.sample_databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * user entity
 * Created by nius on 4/11/16.
 */
public class User extends BaseObservable {

    String name;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(cn.campusapp.sample_databinding.BR.name);
    }

    public User(){}

    public User(String name){
        this.name = name;
    }
}
