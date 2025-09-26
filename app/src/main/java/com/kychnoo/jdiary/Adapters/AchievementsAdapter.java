package com.kychnoo.jdiary.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.kychnoo.jdiary.OtherClasses.Achievement;
import com.kychnoo.jdiary.R;
import com.kychnoo.jdiary.Tools.ColorTools;

import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder> {

    private List<Achievement> achievements;
    private Context context;

    public AchievementsAdapter(List<Achievement> achievements, Context context) {
        this.achievements = achievements;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);

        holder.tvTitle.setText(achievement.getTitle());
        holder.tvContent.setText(achievement.getContent());

        holder.ivAchievement.setImageResource(achievement.getIconResId());
        holder.ivStatus.setImageResource(getIconForRarity(achievement.getRarity()));

        holder.cardView.setCardBackgroundColor(achievement.getBackgroundColor());

        adaptTextColorsToBackground(holder, achievement.getBackgroundColor());
        applyRarityStyle(holder, achievement.getRarity());
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    private int getIconForRarity(int rarity) {
        switch (rarity) {
            case Achievement.RARITY_BRONZE: return R.drawable.ic_trophy_bronze;
            case Achievement.RARITY_SILVER: return R.drawable.ic_trophy_silver;
            case Achievement.RARITY_GOLD: return R.drawable.ic_trophy_gold;
            default: return R.drawable.ic_trophy_bronze;
        }
    }

    private void adaptTextColorsToBackground(ViewHolder holder, int backgroundColor) {
        if(ColorTools.isLightColor(backgroundColor)) {
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.near_black));
            holder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.dimgray));
        } else {
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.white_smoke));
            holder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.grainsboro));
        }
    }

    private void applyRarityStyle(ViewHolder holder, int rarity) {
        switch (rarity) {
            case Achievement.RARITY_SILVER:
                holder.cardView.setStrokeWidth(3);
                holder.cardView.setStrokeColor(ContextCompat.getColor(context, R.color.silver));
                break;
            case Achievement.RARITY_GOLD:
                holder.cardView.setStrokeWidth(4);
                holder.cardView.setStrokeColor(ContextCompat.getColor(context, R.color.gold));
                break;
            case Achievement.RARITY_BRONZE:
                holder.cardView.setStrokeWidth(2);
                holder.cardView.setStrokeColor(ContextCompat.getColor(context, R.color.bronze));
                break;
            default:
                holder.cardView.setStrokeWidth(2);
                holder.cardView.setStrokeColor(ContextCompat.getColor(context, R.color.card_stroke));
                break;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView ivAchievement;
        TextView tvTitle;
        TextView tvContent;
        ImageView ivStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            ivAchievement = itemView.findViewById(R.id.ivAchievement);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivStatus = itemView.findViewById(R.id.ivStatus);
        }
    }
}
