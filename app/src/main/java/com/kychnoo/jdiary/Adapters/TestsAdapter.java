package com.kychnoo.jdiary.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.OthetClasses.Test;
import com.kychnoo.jdiary.R;

import java.util.List;

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.TestViewHolder> {

    private List<Test> testList;

    private OnTestClickListener listener;

    public interface OnTestClickListener {
        void onTestClick(Test test);
    }

    public TestsAdapter(List<Test> testList, OnTestClickListener listener) {
        this.testList = testList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test test = testList.get(position);
        holder.bind(test);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTestName;
        private TextView tvQuestionsCount;
        private TextView tvPoints;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTestName = itemView.findViewById(R.id.tvTestName);
            tvQuestionsCount = itemView.findViewById(R.id.tvQuestionsCount);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            itemView.setOnClickListener(this);
        }

        public void bind(Test test) {
            tvTestName.setText(test.getName());
            tvQuestionsCount.setText(test.getQuestionsCount() + " вопросов");
            tvPoints.setText(test.getPoints() + " баллов");
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION) {
                listener.onTestClick(testList.get(pos));
            }
        }
    }
}
