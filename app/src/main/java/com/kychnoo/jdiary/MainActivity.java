package com.kychnoo.jdiary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.database.Cursor;
import android.os.Build;
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
import com.kychnoo.jdiary.Adapters.AchievementsAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Fragments.ClassFragment;
import com.kychnoo.jdiary.Fragments.DiaryFragment;
import com.kychnoo.jdiary.Fragments.HomeFragment;
import com.kychnoo.jdiary.Fragments.NotesFragment;
import com.kychnoo.jdiary.Fragments.ProfileFragment;
import com.kychnoo.jdiary.Fragments.ScheduleFragment;
import com.kychnoo.jdiary.Fragments.TestsFragment;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;

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

        if(!databaseHelper.isTestExists("История. Крестовые походы", "5А")) {
            long testId1 = databaseHelper.addTest("История. Крестовые походы", 5, 10, "5А");

            long q1Id = databaseHelper.addQuestion((int)testId1, "В каком году начались Первые крестовые походы?");
            databaseHelper.addAnswer((int)q1Id, "1096", true);
            databaseHelper.addAnswer((int)q1Id, "1066", false);
            databaseHelper.addAnswer((int)q1Id, "1204", false);

            long q2Id = databaseHelper.addQuestion((int)testId1, "Как назывался поход, завершившийся взятием Иерусалима?");
            databaseHelper.addAnswer((int)q2Id, "Первый крестовый поход", true);
            databaseHelper.addAnswer((int)q2Id, "Второй крестовый поход", false);
            databaseHelper.addAnswer((int)q2Id, "Третий крестовый поход", false);
            databaseHelper.addAnswer((int)q2Id, "Четвёртый крестовый поход", false);

            long q3Id = databaseHelper.addQuestion((int)testId1, "Кто возглавил крестовые походы на Востоке?");
            databaseHelper.addAnswer((int)q3Id, "Папа Урбан II", true);
            databaseHelper.addAnswer((int)q3Id, "Фридрих Барбаросса", false);
            databaseHelper.addAnswer((int)q3Id, "Ричард Львиное Сердце", false);

            long q4Id = databaseHelper.addQuestion((int)testId1, "Кто из этих лидеров участвовал в Первом крестовом походе?");
            databaseHelper.addAnswer((int)q4Id, "Готфрид Бульонский", true);
            databaseHelper.addAnswer((int)q4Id, "Чингисхан", false);
            databaseHelper.addAnswer((int)q4Id, "Наполеон", false);

            long q5Id = databaseHelper.addQuestion((int)testId1, "Что было основной целью крестовых походов?");
            databaseHelper.addAnswer((int)q5Id, "Освобождение Святых мест", true);
            databaseHelper.addAnswer((int)q5Id, "Завоевание новых земель", false);
            databaseHelper.addAnswer((int)q5Id, "Установление торговых связей", false);
            databaseHelper.addAnswer((int)q5Id, "Расширение империи", false);
        }
        if(!databaseHelper.isTestExists("Математика. Алгебра", "5Б")) {
            long testId2 = databaseHelper.addTest("Математика. Алгебра", 4, 8, "5Б");

            long q1Id_math = databaseHelper.addQuestion((int)testId2, "Решите уравнение: 2x + 5 = 13.");
            databaseHelper.addAnswer((int)q1Id_math, "x = 4", true);
            databaseHelper.addAnswer((int)q1Id_math, "x = 5", false);
            databaseHelper.addAnswer((int)q1Id_math, "x = 3", false);

            long q2Id_math = databaseHelper.addQuestion((int)testId2, "Какой дискриминант у уравнения: x² - 4x + 3 = 0?");
            databaseHelper.addAnswer((int)q2Id_math, "D = 4", true);
            databaseHelper.addAnswer((int)q2Id_math, "D = 2", false);
            databaseHelper.addAnswer((int)q2Id_math, "D = 0", false);

            long q3Id_math = databaseHelper.addQuestion((int)testId2, "Результат выражения: 3² - 2*3 + 1 равен?");
            databaseHelper.addAnswer((int)q3Id_math, "4", true);
            databaseHelper.addAnswer((int)q3Id_math, "6", false);
            databaseHelper.addAnswer((int)q3Id_math, "2", false);
            databaseHelper.addAnswer((int)q3Id_math, "5", false);

            long q4Id_math = databaseHelper.addQuestion((int)testId2, "Как называется прямая, проходящая через центр окружности?");
            databaseHelper.addAnswer((int)q4Id_math, "Диаметр", true);
            databaseHelper.addAnswer((int)q4Id_math, "Хорда", false);
            databaseHelper.addAnswer((int)q4Id_math, "Тангенс", false);
        }
        if(!databaseHelper.isTestExists("Общество и культура. Всемирные традиции", "5В")) {
            long testId3 = databaseHelper.addTest("Общество и культура. Всемирные традиции", 6, 12, "5В");

            long q1Id_soc = databaseHelper.addQuestion((int)testId3, "Как называется японская традиция любования цветущей сакурой?");
            databaseHelper.addAnswer((int)q1Id_soc, "Ханами", true);
            databaseHelper.addAnswer((int)q1Id_soc, "Икебана", false);
            databaseHelper.addAnswer((int)q1Id_soc, "Каллиграфия", false);

            long q2Id_soc = databaseHelper.addQuestion((int)testId3, "Что символизирует немецкая традиция Октоберфест?");
            databaseHelper.addAnswer((int)q2Id_soc, "Празднование урожая", false);
            databaseHelper.addAnswer((int)q2Id_soc, "Праздник пива и баварской культуры", true);
            databaseHelper.addAnswer((int)q2Id_soc, "День reunification", false);

            long q3Id_soc = databaseHelper.addQuestion((int)testId3, "Назовите традиционный мексиканский праздник, отмечаемый 1-2 ноября.");
            databaseHelper.addAnswer((int)q3Id_soc, "День Мёртвых", true);
            databaseHelper.addAnswer((int)q3Id_soc, "День независимости", false);
            databaseHelper.addAnswer((int)q3Id_soc, "Фиеста де ла Санация", false);

            long q4Id_soc = databaseHelper.addQuestion((int)testId3, "Кто из следующих писателей является классиком мировой литературы?");
            databaseHelper.addAnswer((int)q4Id_soc, "Шекспир", true);
            databaseHelper.addAnswer((int)q4Id_soc, "Достоевский", false);
            databaseHelper.addAnswer((int)q4Id_soc, "Толстой", false);

            long q5Id_soc = databaseHelper.addQuestion((int)testId3, "Что из перечисленного является древней цивилизацией?");
            databaseHelper.addAnswer((int)q5Id_soc, "Месопотамия", true);
            databaseHelper.addAnswer((int)q5Id_soc, "Рим", false);
            databaseHelper.addAnswer((int)q5Id_soc, "Древний Египет", true);
            databaseHelper.addAnswer((int)q5Id_soc, "Древняя Индия", true);

            long q6Id_soc = databaseHelper.addQuestion((int)testId3, "Как называется праздник, отмечаемый в Индии в честь победы добра над злом?");
            databaseHelper.addAnswer((int)q6Id_soc, "Дивали", true);
            databaseHelper.addAnswer((int)q6Id_soc, "Холи", false);
            databaseHelper.addAnswer((int)q6Id_soc, "Наваратри", false);
        }

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

                if (id == R.id.nav_home) {
                    navigateToFragment(new HomeFragment(), R.id.nav_home);
                }
                else if(id == R.id.nav_diary) {
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
            navigateToFragment(new HomeFragment(), R.id.nav_home);
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