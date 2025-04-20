package com.kychnoo.jdiary.Adapters;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.Notifications.NotificationHelper;
import com.kychnoo.jdiary.OtherClasses.Student;
import com.kychnoo.jdiary.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {

    private List<Student> studentList;

    public StudentsAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.usernameText.setText(student.getUsername());
        final String userDescription = student.getDescriptionText();
        if(TextUtils.isEmpty(userDescription))
            holder.tvDescription.setText("Описание отсутствует.");
        else
            holder.tvDescription.setText(userDescription);

        holder.tvExperincePoints.setText("Очков: " + student.getExperiencePoints());
        holder.bindIcon(student.getIconPath());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView tvDescription;
        TextView tvExperincePoints;
        ImageView ivUserIcon;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvExperincePoints = itemView.findViewById(R.id.tvExperiencePoints);
            ivUserIcon = itemView.findViewById(R.id.ivIcon);
        }

        public void bindIcon(String imageUri) {
            if (imageUri == null || imageUri.isEmpty()) {
                ivUserIcon.setImageResource(R.drawable.ic_user_icon);
                return;
            }

            try (InputStream inputStream = itemView.getContext().getContentResolver().openInputStream(Uri.parse(imageUri))) {
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ivUserIcon.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                ivUserIcon.setImageResource(R.drawable.ic_user_icon);
            }
        }
    }
}
