package com.kychnoo.jdiary.Adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.kychnoo.jdiary.OtherClasses.Answer;
import com.kychnoo.jdiary.R;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private final List<Answer> answers;
    private final OnAnswerSelectedListener listener;
    private boolean answersBlocked = false;


    public interface OnAnswerSelectedListener {
        void onAnswerSelected(Answer answer, int position);
    }

    public AnswerAdapter(List<Answer> answers, OnAnswerSelectedListener listener) {
        this.answers = answers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_card, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answers.get(position);
        holder.tvAnswer.setText(answer.getText());
        holder.rbChoice.setChecked(answer.isSelected());

        if (answer.isRevealed()) {
            if (answer.isCorrect()) {
                holder.answerCard.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                holder.answerCard.setStrokeWidth(10);
            } else if (answer.isSelected()) {
                holder.answerCard.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
                holder.answerCard.setStrokeWidth(8);
            } else {
                holder.answerCard.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_blue));
                holder.answerCard.setStrokeWidth(1);
                holder.answerCard.setAlpha(0.5f);
            }
        } else {
            if (answer.isSelected()) {
                holder.answerCard.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_blue));
                holder.answerCard.setStrokeWidth(8);
            } else {
                holder.answerCard.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.main_text));
                holder.answerCard.setStrokeWidth(1);
            }
            holder.answerCard.setAlpha(1.0f);
        }

        holder.itemView.setClickable(!answersBlocked);
        holder.itemView.setOnClickListener(v -> {
            if (answersBlocked) return;

            int oldSelected = findSelectedPosition();
            if (oldSelected != -1) {
                answers.get(oldSelected).setSelected(false);
                notifyItemChanged(oldSelected);
            }

            answer.setSelected(true);
            notifyItemChanged(position);

            listener.onAnswerSelected(answer, position);
        });

        setEnterAnimation(holder.itemView, position);
    }

    private int findSelectedPosition() {
        for (int index = 0; index < answers.size(); index++) {
            if (answers.get(index).isSelected()) {
                return index;
            }
        }
        return -1;
    }

    private void setEnterAnimation(View view, int position) {
        view.setAlpha(0f);
        view.setTranslationY(50f);

        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(position * 100L)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void showAnswerResult() {
        for (Answer answer : answers) {
            answer.setRevealed(true);
        }
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public Answer getSelectedAnswer() {
        int pos = getSelectedPosition();
        return pos != -1 ? answers.get(pos) : null;
    }

    public void lockAnswers() {
        this.answersBlocked = true;
        notifyDataSetChanged();
    }

    public void unlockAnswers() {
        this.answersBlocked = false;
        notifyDataSetChanged();
    }

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAnswer;
        final MaterialRadioButton rbChoice;
        final MaterialCardView answerCard;
        final LinearLayout answerItemLayout;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            rbChoice = itemView.findViewById(R.id.rbChoice);
            answerCard = itemView.findViewById(R.id.answer_card);
            answerItemLayout = itemView.findViewById(R.id.answer_item_layout);
        }
    }
}
