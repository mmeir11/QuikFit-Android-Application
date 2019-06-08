package com.evyatartzik.android2_project;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Load slider view */
        ViewPager viewPager = findViewById(R.id.slideViewPager);
        SlideAdapter slideAdapter = new SlideAdapter(getSupportFragmentManager());
        viewPager.setAdapter(slideAdapter);
    }



    private class SlideAdapter extends FragmentStatePagerAdapter{

        List<String> stringLottieAnimation;
        public SlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int postion) {
            return SlideFragment.newInstance(stringLottieAnimation,postion);
        }

        @Override
        public int getCount() {
            return Step.values().length;
        }
    }

}
