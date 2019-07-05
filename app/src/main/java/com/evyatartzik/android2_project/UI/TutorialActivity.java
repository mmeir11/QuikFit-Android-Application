package com.evyatartzik.android2_project.UI;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.Fragments.SlideFragment;
import com.evyatartzik.android2_project.Models.Step;

import java.util.Locale;


public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor =sp.edit();
        editor.putBoolean("isFirstTime",false);
        editor.commit();
    }



    private TextView[] mDots;
    private LinearLayout mDotsLayout;
    private Button buttonNext;
    private Button buttonPrev;
    private int mCurrentPage;
    ViewPager viewPager;
    String phoneLanguage;
    boolean isFirstTime = true;
    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("details",MODE_PRIVATE);
        checkFirstTimeLuanchApp();
        viewPager = findViewById(R.id.slideViewPager);
        phoneLanguage = Locale.getDefault().getLanguage();

        mDotsLayout = findViewById(R.id.dots_layout);
        buttonNext = findViewById(R.id.button_Next);
        buttonPrev = findViewById(R.id.button_Prev);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button)v;
                if(button.getText().toString().equals("Next")||button.getText().toString().equals("הבא"))
                {
                    viewPager.setCurrentItem(mCurrentPage+1);
                }
                else
                {
                    Intent intent = new Intent(TutorialActivity.this, LoginRegister.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage-1);
            }
        });


        SlideAdapter slideAdapter = new SlideAdapter(getSupportFragmentManager());
        viewPager.setAdapter(slideAdapter);
        viewPager.addOnPageChangeListener(viewLisnter);
        addDotIndicator(0);

    }

    private void checkFirstTimeLuanchApp() {
        if(sp.contains("isFirstTime"))
        {
            Intent intent = new Intent(TutorialActivity.this, LoginRegister.class);
            startActivity(intent);
            finish();

        }
    }

    public void addDotIndicator(int position)
    {
        initButtons(position);

        mDots = new TextView[3];
        mDotsLayout.removeAllViews();
        mDots = new TextView[3];
        mDotsLayout.addView(buttonPrev);

        for (int i=0;i<mDots.length;i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotsLayout.addView(mDots[i]);
        }
        mDotsLayout.addView(buttonNext);

        if( mDots.length>0) {
            if (phoneLanguage == "iw") {
                mDots[mDots.length - 1 - position].setTextColor(getResources().getColor(R.color.colorWhite));
            } else if (phoneLanguage == "en") {
                mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
            }
        }
    }

    private void initButtons(int position) {

        if(position==0)
        {
            buttonNext.setEnabled(true);
            buttonPrev.setEnabled(false);
            buttonPrev.setVisibility(View.INVISIBLE);

            buttonNext.setText(R.string.next);
            buttonPrev.setText("");

        }
        else if (position== mDots.length -1)
        {
            buttonNext.setEnabled(true);
            buttonPrev.setEnabled(true);
            buttonPrev.setVisibility(View.VISIBLE);

            buttonNext.setText(R.string.finish);
            buttonPrev.setText(R.string.back);
        }
        else
        {
            buttonNext.setEnabled(true);
            buttonPrev.setEnabled(true);
            buttonPrev.setVisibility(View.VISIBLE);

            buttonNext.setText(R.string.next);
            buttonPrev.setText(R.string.back);
        }
    }


    ViewPager.OnPageChangeListener viewLisnter = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            addDotIndicator(i);
            mCurrentPage = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };


    private class SlideAdapter extends FragmentStatePagerAdapter{
        public SlideAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int postion) {
            return SlideFragment.newInstance(postion);
        }
        @Override
        public int getCount() {
            return Step.values().length;
        }
    }



}
