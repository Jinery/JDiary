package com.kychnoo.jdiary.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.OtherClasses.Grade;
import com.kychnoo.jdiary.R;
import com.kychnoo.jdiary.Tools.ColorTools;

import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradesViewHolder> {

    private List<Grade> gradeList;

    private Context context;

    public GradesAdapter(List<Grade> gradeList, Context context) {
        this.gradeList = gradeList;
        this.context = context;
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

        int backgroundCardColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.card_background);

        switch(grade.getScore()) {
            case 5:
                holder.ivScore.setImageResource(R.drawable.ic_cup_award);
                backgroundCardColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.great_card);
                break;
            case 4:
                holder.ivScore.setImageResource(R.drawable.ic_light_bulb_idea);
                backgroundCardColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.cool_card);
                break;
            case 3:
                holder.ivScore.setImageResource(R.drawable.ic_library_book);
                backgroundCardColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.good_card);
                break;
            case 2:
                holder.ivScore.setImageResource(R.drawable.ic_nerd_emoji);
                backgroundCardColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.bad_card);
                break;
        }

        holder.cardView.setBackgroundColor(backgroundCardColor);

        holder.tvGradeText.setText(grade.getGradeText());
        holder.tvDate.setText(grade.getDate());
        holder.tvScore.setText("Оценка: " + grade.getScore());

        adaptTextColorsToBackground(holder, backgroundCardColor);
    }

    private void adaptTextColorsToBackground(GradesAdapter.GradesViewHolder holder, int backgroundColor) {
        if(ColorTools.isLightColor(backgroundColor)) {
            holder.tvGradeText.setTextColor(ContextCompat.getColor(context, R.color.dimgray));
            holder.tvScore.setTextColor(ContextCompat.getColor(context, R.color.dimgray));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, R.color.dimgray));
        } else {
            holder.tvGradeText.setTextColor(ContextCompat.getColor(context, R.color.white_smoke));
            holder.tvScore.setTextColor(ContextCompat.getColor(context, R.color.white_smoke));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, R.color.white_smoke));
        }
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    public class GradesViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        private TextView tvDate;
        private TextView tvScore;
        private TextView tvGradeText;

        private ImageView ivScore;

        public GradesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvGradeText = itemView.findViewById(R.id.tvGradeText);
            ivScore = itemView.findViewById(R.id.ivscore);
        }
    }
}
