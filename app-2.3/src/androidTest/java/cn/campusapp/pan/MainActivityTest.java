package cn.campusapp.pan;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Pan will prevent two same type view models bind to the same view.
     */
    @Test
    public void testViewModelReuse() {

        MainViewModel vm = Pan.with(mMainRule.getActivity(), MainViewModel.class)
                .controlledBy(MainController.class)
                .getViewModel();

        assertEquals(vm, mMainRule.getActivity().mMainViewModel);

        assertEquals(vm.getRootView().getTag(R.id.PAN_ID_0), vm);
        assertEquals(vm.getRootView().getTag(R.id.PAN_ID_1), mMainRule.getActivity().mAutoRenderTextViewModel);

    }

    @Test
    public void testInnerClassController() {
        InnerMainViewModel vm = Pan.with(mMainRule.getActivity(), new InnerMainViewModel())
                .controlledBy(new InnerMainController())  //will change the controller
                .getViewModel();

        Object tag = new Object();
        vm.vHelloTv.setTag(tag);

        onView(withTagValue(is(tag)))
                .perform(click())
                .check(matches(withText("hello inner class")));


    }

    public class InnerMainViewModel extends MainViewModel{

    }

    public class InnerMainController extends GeneralController<InnerMainViewModel> {

        @Override
        protected void bindEvents() {
            $vm.vHelloTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    $vm.vHelloTv.setText("hello inner class");
                }
            });
        }
    }


}