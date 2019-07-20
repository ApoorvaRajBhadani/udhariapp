package com.arb222.udhari;

import com.arb222.udhari.ContactDB.ContactContract;
import com.arb222.udhari.ContactDB.ContactDbHelper;
import com.arb222.udhari.ContactDB.UpdateContactDb;
import com.arb222.udhari.TabbedActivity.HomeFragment;
import com.arb222.udhari.TabbedActivity.NotificationsFragment;
import com.arb222.udhari.TabbedActivity.ProfileFragment;
import com.arb222.udhari.TabbedActivity.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    //https://www.youtube.com/watch?v=sJ-Z9G0SDhc

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ContactDbHelper contactDbHelper;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_mainactivity:
                return true;
            case R.id.action_about_mainactivity:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        contactDbHelper = new ContactDbHelper(this);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new HomeFragment(), "Home");
        adapter.AddFragment(new NotificationsFragment(), "Notifications");
        adapter.AddFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        SQLiteDatabase contactDb = contactDbHelper.getReadableDatabase();
        Cursor userInContactDb = contactDb.query(ContactContract.ContactEntry.TABLE_NAME, null, null, null, null, null, null);
        //if(userInContactDb.getCount()==0){
        UpdateContactDb updateContactDb = new UpdateContactDb();
        updateContactDb.initializeContactDb(this);
        //}
        //todo:Do contact refresh in background
        userInContactDb.close();
    }

    private void updateUI(FirebaseUser currentUser) {

    }


}
