package app.bash.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.bash.R;
import app.bash.activity.ViewOrderActivity;
import app.bash.model.ServiceModel;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    Context context;
    ArrayList<ServiceModel> arrayList;

    public ServiceAdapter(Context context, ArrayList<ServiceModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(ViewHolder holder, int position) {

        ServiceModel model = arrayList.get(position);
        holder.tvSubtitle.setText(model.getSubtitle());
        holder.tvId.setText("#" + model.getServiceId());
        holder.tvCost.setText("Cost of service " + model.getPrice() + " SAR");
        S.E("model.getStatus() :: " + model.getStatus());

        if (SavedData.getLanguage().equals("ar")) {
            holder.imgNext.setBackground(context.getResources().getDrawable(R.drawable.btn_blue_half_bg_arabic));
        } else {
            holder.imgNext.setBackground(context.getResources().getDrawable(R.drawable.btn_blue_half_bg));
        }

        if (model.getStatus() == 0) {
            holder.image.setImageResource(R.drawable.rect_blue_bg);
            holder.tvId.setTextColor(context.getResources().getColor(R.color.colorBlue));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorBlue));
            holder.tvStatus.setText("Received");
        } else if (model.getStatus() == 1) {
            holder.image.setImageResource(R.drawable.rect_yelllow_bg);
            holder.tvId.setTextColor(context.getResources().getColor(R.color.colorYellow));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorYellow));
            holder.tvStatus.setText("Processing");
        } else if (model.getStatus() == 2) {
            holder.image.setImageResource(R.drawable.rect_green_bg);
            holder.tvId.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.tvStatus.setText("Done");
        } else if (model.getStatus() == 3) {
            holder.image.setImageResource(R.drawable.rect_red_bg);
            holder.tvId.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.tvStatus.setText("Cancelled");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getStatus() == 0) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("callingFrom", "live");
                    //@Rafat-4/1/2021
                    //S.I(context, ChatActivity.class, bundle1);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId", model.getOrderId());
                    S.I(context, ViewOrderActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvTitle, tvSubtitle, tvCost, tvId, tvStatus;
        CardView card;
        LinearLayout imgNext;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvId = itemView.findViewById(R.id.tvId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgNext = itemView.findViewById(R.id.imgNext);
            card = itemView.findViewById(R.id.card);
        }
    }
}

