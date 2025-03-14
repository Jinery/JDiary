package com.kychnoo.jdiary.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.OtherClasses.Student;
import com.kychnoo.jdiary.R;

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
        if(userDescription == null || userDescription.isEmpty())
            holder.tvDescription.setText("Описание отсутствует.");
        else
            holder.tvDescription.setText(userDescription);

        holder.tvExperincePoints.setText("Очков: " + student.getExperiencePoints());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView tvDescription;
        TextView tvExperincePoints;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvExperincePoints = itemView.findViewById(R.id.tvExperiencePoints);
        }
    }
}
