package app.bash.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.bash.R;
import app.bash.activity.HomeDetailActivity;
import app.bash.model.NotificationModel;
import app.bash.model.ServiceModel;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<NotificationModel> notificationModels;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModels) {
        this.context = context;
        this.notificationModels = notificationModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationModel model = notificationModels.get(position);
        holder.tvTitle.setText(model.getServicename());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a, yyyy/MM/dd");
        Date date = null;
        try {
            date = inputFormat.parse(model.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        System.out.println(formattedDate);
        S.E("time:" + formattedDate);
        holder.tvDate.setText(formattedDate);

        if (SavedData.getLanguage().equals("ar")) {
            holder.imgNext.setBackground(context.getResources().getDrawable(R.drawable.btn_blue_half_bg_arabic));
        } else {
            holder.imgNext.setBackground(context.getResources().getDrawable(R.drawable.btn_blue_half_bg));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", model.getId());
                S.I(context, HomeDetailActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvTitle, tvSubtitle, tvDate;
        CardView card;
        LinearLayout imgNext;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgNext = itemView.findViewById(R.id.imgNext);
            card = itemView.findViewById(R.id.card);
        }
    }
}