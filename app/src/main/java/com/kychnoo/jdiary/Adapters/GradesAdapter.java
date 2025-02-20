package com.kychnoo.jdiary.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.OtherClasses.Grade;
import com.kychnoo.jdiary.R;

import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradesViewHolder> {

    private List<Grade> gradeList;

    public GradesAdapter(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }


    @NonNull
    @Override
    public GradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesViewHolder holder, int pos) {
        Grade grade = gradeList.get(pos);
        holder.bind(grade);
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    public class GradesViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate;
        private TextView tvScore;
        private TextView tvGradeText;

        public GradesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvGradeText = itemView.findViewById(R.id.tvGradeText);
        }

        public void bind(Grade grade) {
            tvGradeText.setText(grade.getGradeText());
            tvDate.setText(grade.getDate());
            tvScore.setText("Оценка: " + grade.getScore());
        }
    }
}
