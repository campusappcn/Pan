package cn.campusapp.pan;

import android.widget.TextView;

import butterknife.Bind;
import cn.campusapp.pan.annotaions.Xml;
import cn.campusapp.pan.autorender.AutoRenderViewModel;

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
    public AutoRenderTextViewModel render() {
        vTextView.setText(mText);
        return this;
    }

    @Override
    public AutoRenderTextViewModel autoRender() {
        super.autoRender();
        return this;
    }
}
