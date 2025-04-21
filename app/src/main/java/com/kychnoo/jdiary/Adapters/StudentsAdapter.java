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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
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

        if(position < 3) {
            holder.ivRatingCup.setVisibility(View.VISIBLE);

            switch(position) {
                case 0:
                    holder.cardView.setBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.darkslate_blue));
                    holder.ivRatingCup.setImageResource(R.drawable.ic_gold_cup);
                    break;
                case 1:
                    holder.cardView.setBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.lavender));
                    holder.ivRatingCup.setImageResource(R.drawable.ic_silver_cup);
                    break;
                case 2:
                    holder.cardView.setBackgroundColor(ContextCompat.getColor(holder.cardView.getContext(), R.color.sienna));
                    holder.ivRatingCup.setImageResource(R.drawable.ic_bronze_cup);
                    int textColorIndex = ContextCompat.getColor(holder.cardView.getContext(), R.color.white_smoke);
                    holder.tvDescription.setTextColor(textColorIndex);
                    holder.tvExperincePoints.setTextColor(textColorIndex);
                    break;
            }
        }
        holder.tvPosition.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView cardView;

        TextView usernameText;
        TextView tvDescription;
        TextView tvExperincePoints;
        TextView tvPosition;
        ImageView ivUserIcon;

        ImageView ivRatingCup;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            usernameText = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvExperincePoints = itemView.findViewById(R.id.tvExperiencePoints);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            ivUserIcon = itemView.findViewById(R.id.ivIcon);
            ivRatingCup = itemView.findViewById(R.id.ivRatingCup);
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
