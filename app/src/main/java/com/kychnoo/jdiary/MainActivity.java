package com.kychnoo.jdiary;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.kychnoo.jdiary.Achievements.AchievementsHelper;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Fragments.ClassFragment;
import com.kychnoo.jdiary.Fragments.DiaryFragment;
import com.kychnoo.jdiary.Fragments.NotesFragment;
import com.kychnoo.jdiary.Fragments.ProfileFragment;
import com.kychnoo.jdiary.Fragments.ScheduleFragment;
import com.kychnoo.jdiary.Fragments.TestsFragment;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;
import com.kychnoo.jdiary.TestsHelper.TestsInitializator;

public class MainActivity extends AppCompatActivity implements ToolbarTitleSetter {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DatabaseHelper databaseHelper;
    private String phone;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        phone = getIntent().getStringExtra("phone");

        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);

        TestsInitializator testsInitializator= new TestsInitializator(this);
        testsInitializator.initializeAllTests();

        if (!databaseHelper.isExistScheduleIsDate("21.02.2025", "5А")) {
            long scheduleId = databaseHelper.addSchedule("21.02.2025", 3, "5А");
            databaseHelper.addLesson(scheduleId, "Литература", "Дочитать произведение: \"Война и мир.\"");
            databaseHelper.addLesson(scheduleId, "Математика", "Решить задачи №1-10.");
            databaseHelper.addLesson(scheduleId, "История", "Параграф 15");
        }

        AchievementsHelper.initialize(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Home");

        View headerView = navigationView.getHeaderView(0);
        TextView tvPhone = headerView.findViewById(R.id.tvProfile);
        TextView tvClass = headerView.findViewById(R.id.tvClass);
        Cursor cursor = databaseHelper.getUserByPhone(phone);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
            String userClass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            tvPhone.setText(username);
            tvClass.setText("Ученик " + userClass + " класса.");
            cursor.close();
        } else {
            Toast.makeText(MainActivity.this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
        }

        AchievementsHelper.startApp(phone);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            //Navigate on Fragments.
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.nav_diary) {
                    navigateToFragment(new DiaryFragment(), R.id.nav_diary);
                }
                else if(id == R.id.nav_schedule){
                    navigateToFragment(new ScheduleFragment(), R.id.nav_schedule);
                }
                else if(id == R.id.nav_profile) {
                    navigateToFragment(new ProfileFragment(), R.id.nav_profile);
                }
                else if(id == R.id.nav_myclass) {
                    navigateToFragment(new ClassFragment(), R.id.nav_myclass);
                }
                else if(id == R.id.nav_tests) {
                    navigateToFragment(new TestsFragment(), R.id.nav_tests);
                }
                else if(id == R.id.nav_notes) {
                    navigateToFragment(new NotesFragment(), R.id.nav_notes);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if (savedInstanceState == null) {
            navigateToFragment(new ProfileFragment(), R.id.nav_profile);
        }
    }

    private void navigateToFragment(Fragment fragment, int menuItemId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        navigationView.setCheckedItem(menuItemId);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        if(toolbar != null)
            toolbar.setTitle(title);
    }
}