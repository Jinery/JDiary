package com.kychnoo.jdiary.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.kychnoo.jdiary.Adapters.AchievementsAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;
import com.kychnoo.jdiary.Managers.LevelManager;
import com.kychnoo.jdiary.Notifications.NotificationHelper;
import com.kychnoo.jdiary.OtherClasses.Achievement;
import com.kychnoo.jdiary.R;
import com.kychnoo.jdiary.Tools.BitmapTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private DatabaseHelper databaseHelper;
    private BitmapTools bitmapTools;
    private LevelManager levelManager;
    private String phone;


    private TextView tvDescription;
    private TextView tvUserPoints;

    private ShapeableImageView ivUserIcon;

    private ProgressBar pbLevel;

    private ToolbarTitleSetter toolbarTitleSetter;

    private RecyclerView rvAchievements;

    private AchievementsAdapter achievementsAdapter;

    private List<Achievement> achievementsList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarTitleSetter)
            toolbarTitleSetter = (ToolbarTitleSetter) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toolbarTitleSetter = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbarTitleSetter.setToolbarTitle("Профиль");

        databaseHelper = new DatabaseHelper(requireContext());
        bitmapTools = BitmapTools.getInstance();
        levelManager = LevelManager.getInstance(requireContext());

        phone = requireActivity().getIntent().getStringExtra("phone");

        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvClass = view.findViewById(R.id.tvUserClass);
        tvDescription = view.findViewById(R.id.tvUserDescription);
        tvUserPoints = view.findViewById(R.id.tvUserPoints);
        ivUserIcon = view.findViewById(R.id.ivUserIcon);
        pbLevel =  view.findViewById(R.id.pbLevel);

        rvAchievements = view.findViewById(R.id.rvAchievements);
        rvAchievements.setLayoutManager(new LinearLayoutManager(requireContext()));

        achievementsList = new ArrayList<>();

        achievementsAdapter = new AchievementsAdapter(achievementsList, requireContext());
        rvAchievements.setAdapter(achievementsAdapter);

        tvDescription.setOnClickListener(v -> showEditDesriptionWindow(phone));

        Cursor cursor = databaseHelper.getUserByPhone(phone);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
            String userClass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            String userDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            String userPoints = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPERIENCE_POINTS));
            int experiencePoints = Integer.parseInt(userPoints);
            updateUserLevelInfo(experiencePoints);
            String userIconPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ICON));
            tvUsername.setText(username);
            tvClass.setText("Ученик " + userClass + " класса.");
            updateUserDescription(userDescription);
            updateUserIcon(userIconPath);
            cursor.close();
        } else {
            NotificationHelper.show(requireActivity(), "Пользователь не найден", NotificationHelper.NotificationColor.ERROR, 1000);
        }

        loadAchievements();

        ivUserIcon.setOnClickListener(v -> showIconSelectionDialog());

        return view;
    }

    private void showEditDesriptionWindow(String phone) {
        Cursor cursor = databaseHelper.getUserByPhone(phone);
        if(cursor != null && cursor.moveToFirst()) {
            String currentDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_discription, null);
            builder.setView(dialogView);

            EditText etDescription = dialogView.findViewById(R.id.etDescription);
            TextView tvAvailableSymbols = dialogView.findViewById(R.id.tvAvialableSymvols);

            etDescription.setText(currentDescription);
            updateCharCount(etDescription.getText().length(), tvAvailableSymbols);

            etDescription.setFilters(new InputFilter[] { new InputFilter.LengthFilter(35)});

            etDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updateCharCount(s.length(), tvAvailableSymbols);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            builder.setPositiveButton("Сохранить", (dialog, which) -> {
                String newDescription = etDescription.getText().toString();
                databaseHelper.updateUserDescription(phone, newDescription);
                updateUserDescription(newDescription);
            });

            builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            NotificationHelper.show(requireActivity(), "Не удаётся загрузить данные об описании", NotificationHelper.NotificationColor.WARNING, 1000);
        }
    }

    private void updateCharCount(int length,  TextView tvAvialableSymvols) {
        int remainingChars = 35 - length;
        tvAvialableSymvols.setText(remainingChars + " символов осталось");
    }

    private void updateUserDescription(String newDescription) {
        if(TextUtils.isEmpty(newDescription))
            tvDescription.setText("Описание отсутствует.");
        else
            tvDescription.setText(newDescription);
    }

    private void updateUserLevelInfo(int experiencePoints) {
        if(levelManager == null) return;


        int level = levelManager.getLevel(experiencePoints);
        int progress = levelManager.getProgress(experiencePoints);
        int nextLevelThreshold = levelManager.getNextLevelThreshold(experiencePoints);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(level == 1) {
                pbLevel.setMin(0);
            } else {
                pbLevel.setMin(levelManager.getCurrentLevelThreshold(experiencePoints));
            }
        }
        pbLevel.setMax(nextLevelThreshold);
        pbLevel.setProgress(progress);
        tvUserPoints.setText(String.format("У вас %d из %d очков", experiencePoints, nextLevelThreshold));

        int colorForLevel = levelManager.getColorForLevel(level);
        ivUserIcon.setStrokeColor(ColorStateList.valueOf(colorForLevel));
        pbLevel.setProgressTintList(ColorStateList.valueOf(colorForLevel));
    }

    private void showIconSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Выбрать иконку");

        builder.setPositiveButton("Из галереи", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            requireContext().getContentResolver().takePersistableUriPermission(
                    selectedImageUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            updateUserIcon(selectedImageUri.toString());
            databaseHelper.updateUserIcon(phone, selectedImageUri.toString());
        }
    }

    private void updateUserIcon(String imageUriString) {

        ImageView ivHeaderIcon = null;


        Activity activity = getActivity();
        if(activity instanceof AppCompatActivity) {
            NavigationView navigationView = activity.findViewById(R.id.nav_view);
            if(navigationView != null) {
                View headerView = navigationView.getHeaderView(0);
                ivHeaderIcon = headerView.findViewById(R.id.ivHeaderUserIcon);
            }
        }
        if (TextUtils.isEmpty(imageUriString)) {
            ivUserIcon.setImageResource(R.drawable.ic_user_icon);
            return;
        }

        Uri imageUri = Uri.parse(imageUriString);
        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri)) {
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap roundedBitmap = bitmapTools.getRoundedBitmap(bitmap);
                ivUserIcon.setImageBitmap(roundedBitmap);
                if(ivHeaderIcon != null)
                    ivHeaderIcon.setImageBitmap(roundedBitmap);
            } else {
                throw new IOException("Не удалось открыть файл");
            }
        } catch (IOException e) {
            e.printStackTrace();
            NotificationHelper.show(requireActivity(), "Ошибка загрузки изображения", NotificationHelper.NotificationColor.ERROR, 1500);
            ivUserIcon.setImageResource(R.drawable.ic_user_icon);
        }
    }

    private void loadAchievements() {
        Cursor cursor = databaseHelper.getUserAchievements(phone);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_CONTENT));
                String iconResName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_ICON));
                int rarity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_RARITY));

                int iconResId = requireContext().getResources().getIdentifier(iconResName, "drawable", requireContext().getPackageName());
                int backgroundColorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_BACKGROUND_COLOR));
                achievementsList.add(new Achievement(id, title, content, iconResId, rarity, backgroundColorId));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
