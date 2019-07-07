package com.evyatartzik.android2_project.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.evyatartzik.android2_project.Fragments.ActivityFragment;
import com.evyatartzik.android2_project.Fragments.CreateActivityFragment;
import com.evyatartzik.android2_project.Fragments.HomeFragment;
import com.evyatartzik.android2_project.Fragments.MyChatFragment;
import com.evyatartzik.android2_project.Fragments.ProfileFragment;
import com.evyatartzik.android2_project.Fragments.SearchFragment;
import com.evyatartzik.android2_project.Fragments.SettingsFragment;
import com.evyatartzik.android2_project.Interfaces.FragmentToActivity;
import com.evyatartzik.android2_project.OpenActivityReceiver;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity implements FragmentToActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
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
//    GroupsFragment groupsFragment;
    ActivityFragment activityFragment;
    MyChatFragment myChatFragment;

    private DatabaseReference ref;
    DatabaseReference databaseUsers;
    private DatabaseReference databaseUserPref;
    private User myUser;

    private AlarmManager alarmManager;
    private boolean is_notif_on;
    private SharedPreferences prefs;

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
        currentUser = mAuth.getCurrentUser();
        users = database.getReference("users");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        initFirebase();

        /*Config MenuActivity*/
        viewPager = findViewById(R.id.pager);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        is_notif_on = prefs.getBoolean("notification", false);




    }

     private void
     setupViewPager(ViewPager viewPager)
     {
         ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


         homeFragment=new HomeFragment();
         settingsFragment = new SettingsFragment();
         searchFragment = new SearchFragment();
         profileFragment = new ProfileFragment();
//         groupsFragment = new GroupsFragment();
         myChatFragment = new MyChatFragment();
         Bundle bundle = new Bundle();
         bundle.putSerializable("user",myUser);
         profileFragment.setArguments(bundle);
         settingsFragment.setArguments(bundle);

         adapter.addFragment(homeFragment);
         adapter.addFragment(searchFragment);
         adapter.addFragment(profileFragment);
         adapter.addFragment(myChatFragment);
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




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void finish_task(int id, String str, String str2) {
        if(id != 4){ homeFragment.ShowFloatingButton();}
        if(id == 3){

            UpdateUserProfilePicURL(str);
        }
        if(id == 4) {
            if (is_notif_on) {
                Intent intent = new Intent(MenuActivity.this, OpenActivityReceiver.class);
                intent.putExtra("activity_id", str);
                intent.putExtra("date", str2);

                PendingIntent pendingIntent =  PendingIntent.getBroadcast(MenuActivity.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yy", Locale.ENGLISH);
                try {
                    calendar.setTime(sdf.parse(str2));// all done                PendingIntent pendingIntent =  PendingIntent.getBroadcast(MenuActivity.this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

            }
        }

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
        if(fragment instanceof SettingsFragment){
            SettingsFragment settingsFragment = (SettingsFragment) fragment;
            settingsFragment.setOnUserChangeListener(this);
        }

        super.onAttachFragment(fragment);
    }


    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        databaseUsers = ref.child("users").child(currentUser.getUid());

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myUser =  dataSnapshot.getValue(User.class);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void UpdateUserProfilePicURL(String url){

        myUser.setProfile_pic_path(url);
    }



}
