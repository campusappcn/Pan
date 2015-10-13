package cn.campusapp.pan;

import android.widget.TextView;

import butterknife.Bind;
import cn.campusapp.pan.annotaions.Xml;
import cn.campusapp.pan.autorender.AutoRenderViewModel;

/**
 * Created by nius on 10/13/15.
 */
@Xml(R.layout.view_component)
public class AutoRenderTextViewModel extends AutoRenderViewModel {

    @Bind(R.id.auto_render)
    TextView vTextView;

    String mText;

    @Override
    public void loadDataQuickly() {
        mText = "hello auto render";
    }

    @Override
    public ViewModel render() {
        vTextView.setText(mText);
        return this;
    }
}
