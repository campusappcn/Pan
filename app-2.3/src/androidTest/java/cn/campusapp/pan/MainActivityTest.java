package cn.campusapp.pan;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 *
 * Created by nius on 10/14/15.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> mMainRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Pan will prevent two same type view models bind to the same view.
     */
    @Test
    public void testViewModelReuse(){

        MainViewModel vm = Pan.with(mMainRule.getActivity(), MainViewModel.class)
                .controlledBy(MainController.class)
                .getViewModel();

        assertEquals(vm, mMainRule.getActivity().mMainViewModel);

        assertEquals(vm.getRootView().getTag(R.id.PAN_ID_0), vm);
        assertEquals(vm.getRootView().getTag(R.id.PAN_ID_1), mMainRule.getActivity().mAutoRenderTextViewModel);

    }


}