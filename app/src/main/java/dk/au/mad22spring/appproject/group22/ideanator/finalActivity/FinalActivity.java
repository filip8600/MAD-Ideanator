package dk.au.mad22spring.appproject.group22.ideanator.finalActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;


import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.joinActivity.JoinActivityViewModel;

//Viewpaging based on https://developer.android.com/training/animation/screen-slide.html
public class FinalActivity extends FragmentActivity {
    private FinalActivityViewModel vm;
    private FragmentContainerView container;
    private static int NUM_PAGES = 1;
    private ViewPager mPager;     //The pager widget, which handles animation and allows swiping horizontally to access previousand next wizard steps.
    private PagerAdapter pagerAdapter;     // The pager adapter, which provides the pages to the view pager widget.

    private TextView currentNumberTextBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        vm = new ViewModelProvider(this).get(FinalActivityViewModel.class);
        NUM_PAGES=vm.getNumberOfRounds();

        setupSimpleViewItems();
        setUpViewPager();


    }

    private void setUpViewPager() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager =  findViewById(R.id.FinalFragmentContainerView);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentNumberTextBox.setText(String.valueOf(position+1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupSimpleViewItems() {
        currentNumberTextBox=findViewById(R.id.FinalTxtNumber);
        currentNumberTextBox.setText("1");
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return new FinalFragment(vm.getSolution(position));
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}