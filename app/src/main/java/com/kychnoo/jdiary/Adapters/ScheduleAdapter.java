package com.kychnoo.jdiary.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.MainActivity;
import com.kychnoo.jdiary.OtherClasses.Schedule;
import com.kychnoo.jdiary.R;
import com.kychnoo.jdiary.ScheduleDetailActivity;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{

    private Context context;
    private List<Schedule> scheduleItems;

    public ScheduleAdapter(Context context, List<Schedule> scheduleItems) {
        this.context = context;
        this.scheduleItems = scheduleItems;
    }


    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule item = scheduleItems.get(position);
        holder.tvScheduleDate.setText("Расписание на " + item.getDate());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ScheduleDetailActivity.class);
            intent.putExtra("scheduleId", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvScheduleDate;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            tvScheduleDate = itemView.findViewById(R.id.tvScheduleDate);
        }
    }
}
