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

    private void applyRarityStyle(ViewHolder holder, int rarity) {
        int cardColor = ContextCompat.getColor(context, R.color.white_smoke);
        int textColor = ContextCompat.getColor(context, R.color.near_black);

        switch (rarity) {
            case Achievement.RARITY_BRONZE:
                cardColor = ContextCompat.getColor(context, R.color.white_smoke);
                textColor = ContextCompat.getColor(context, R.color.near_black);
            case Achievement.RARITY_SILVER:
                cardColor = ContextCompat.getColor(context, R.color.silver);
                textColor = ContextCompat.getColor(context, R.color.light_blue);
                break;
            case Achievement.RARITY_GOLD:
                cardColor = ContextCompat.getColor(context, R.color.epic_mystic_purple);
                textColor = ContextCompat.getColor(context, R.color.gold);
                break;
        }

        holder.cardView.setCardBackgroundColor(cardColor);
        holder.tvTitle.setTextColor(textColor);
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
