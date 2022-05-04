package dk.au.mad22spring.appproject.group22.ideanator.finalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.joinActivity.JoinActivity;
import dk.au.mad22spring.appproject.group22.ideanator.mainActivity.MainActivity;

//Viewpaging based on https://developer.android.com/training/animation/screen-slide.html
public class FinalActivity extends FragmentActivity {
    private FinalActivityViewModel vm;
    private static int NUM_PAGES = 1;
    private ViewPager mPager;     //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.

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
        // The pager adapter, which provides the pages to the view pager widget.
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
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
        Button shareButton=findViewById(R.id.final_btn_share);
        shareButton.setOnClickListener(view -> {shareIdea();});
        Button endGameButton=findViewById(R.id.final_btn_endGame);
        endGameButton.setOnClickListener(view -> endGame());
    }

    private void endGame() {
        vm.deleteGame();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Kill stack
        startActivity(intent);
    }

    private void shareIdea() {//Sharing from https://developer.android.com/training/sharing/send
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, vm.getSolution(mPager.getCurrentItem()));
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
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

        @NonNull
        @Override
        public Fragment getItem(int position) {
            FinalFragment fragment= new FinalFragment();
            fragment.setText(vm.getSolution(position));
            return  fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}