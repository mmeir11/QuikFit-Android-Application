package com.evyatartzik.android2_project.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.evyatartzik.android2_project.Fragments.ActivityFragment;
import com.evyatartzik.android2_project.Fragments.ChatFragment;
import com.evyatartzik.android2_project.Fragments.CreateActivityFragment;
import com.evyatartzik.android2_project.Fragments.GroupsFragment;
import com.evyatartzik.android2_project.Fragments.HomeFragment;
import com.evyatartzik.android2_project.Fragments.ProfileFragment;
import com.evyatartzik.android2_project.Fragments.SearchFragment;
import com.evyatartzik.android2_project.Fragments.SettingsFragment;
import com.evyatartzik.android2_project.Interfaces.FragmentToActivity;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements FragmentToActivity {

    private FirebaseAuth mAuth;
    DatabaseReference users;
    List<User> userList;
    FirebaseDatabase database;

    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;
    ViewPager viewPager;

    HomeFragment homeFragment;
    SettingsFragment settingsFragment;
    SearchFragment searchFragment;
    ProfileFragment profileFragment;
    ChatFragment chatFragment;
    GroupsFragment groupsFragment;
    ActivityFragment activityFragment;

    /*onStop calls when you want to kill all fragments ( == signout)*/
    @Override
    protected void onStop() {
        super.onStop();
        //finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /*Firebase data and user logged-in data */
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        users = database.getReference("users");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /*Config MenuActivity*/
        viewPager = findViewById(R.id.pager);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        setupViewPager(viewPager);
        setBottomNavigation();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

     private void
     setupViewPager(ViewPager viewPager)
     {
         ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


         homeFragment=new HomeFragment();
//         activityFragment = new ActivityFragment();
         settingsFragment = new SettingsFragment();
         searchFragment = new SearchFragment();
         profileFragment = new ProfileFragment();
//         chatFragment = new ChatFragment();
         groupsFragment = new GroupsFragment();

         adapter.addFragment(homeFragment);
         adapter.addFragment(searchFragment);
         adapter.addFragment(profileFragment);
//         adapter.addFragment(chatFragment);
         adapter.addFragment(groupsFragment);
         adapter.addFragment(settingsFragment);


         viewPager.setAdapter(adapter);
         viewPager.setOffscreenPageLimit(4);

         viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
             @Override
             public void onPageScrolled(int i, float v, int i1) {

             }

             @Override
             public void onPageSelected(int i) {
                 getSupportFragmentManager().popBackStack();
                 if (prevMenuItem != null) {
                     prevMenuItem.setChecked(false);
                 } else {
                     bottomNavigationView.getMenu().getItem(0).setChecked(false);
                 }
                 bottomNavigationView.getMenu().getItem(i).setChecked(true);
                 prevMenuItem = bottomNavigationView.getMenu().getItem(i);
             }

             @Override
             public void onPageScrollStateChanged(int i) {

             }
         });

     }
    private void setBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.action_search:
                        viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.action_profile:
                        viewPager.setCurrentItem(2, true);
                        break;
                    case R.id.action_chats:
                        viewPager.setCurrentItem(3, true);
                        break;
                    case R.id.action_more:
                        viewPager.setCurrentItem(4, true);
                        break;
                    default:
                        getSupportFragmentManager().popBackStack();
                }

                return false;
            }
        });
    }

    @Override
    public void finish_task() {


        homeFragment.ShowFloatingButton();

    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);

        }

    }
    @Override
    public void onBackPressed() {

        if(viewPager.getCurrentItem() != 0 ){

            if(getSupportFragmentManager().getBackStackEntryCount() == 0)
                viewPager.setCurrentItem(0,true);
            else {getSupportFragmentManager().popBackStack();}

        }
        else if (viewPager.getCurrentItem() == 0 && getSupportFragmentManager().getBackStackEntryCount() == 0) {
                getSupportFragmentManager().popBackStack();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);


        }
        else
            {
                if(getSupportFragmentManager().getBackStackEntryCount() != 0)
                    getSupportFragmentManager().popBackStack();
//                if(viewPager.getCurrentItem() == 1){viewPager.setCurrentItem(0,true);}

            }


    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if(fragment instanceof CreateActivityFragment){
          CreateActivityFragment createActivityFragment =  (CreateActivityFragment) fragment;
          createActivityFragment.setOnActivityCreatedListener(this);
        }
        if(fragment instanceof ActivityFragment) {
            ActivityFragment activityFragment = (ActivityFragment) fragment;
            activityFragment.setOnActivityOpenListener(this);
        }

        super.onAttachFragment(fragment);
    }
}
