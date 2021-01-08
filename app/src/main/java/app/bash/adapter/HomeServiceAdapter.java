package app.bash.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.bash.R;
import app.bash.activity.HomeDetailActivity;
import app.bash.model.HomeFirstModel;
import app.bash.utilitis.S;
import app.bash.utilitis.TimeAgo;

public class HomeServiceAdapter extends RecyclerView.Adapter<HomeServiceAdapter.ViewHolder> {
    Context context;
    ArrayList<HomeFirstModel> homeFirstModels;

    public HomeServiceAdapter(Context context, ArrayList<HomeFirstModel> homeFirstModels) {
        this.context = context;
        this.homeFirstModels = homeFirstModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_home_second, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeFirstModel model = homeFirstModels.get(position);

        holder.tvTitle.setText(model.getName());
        holder.tvPrice.setText(model.getPrice());
       if(!model.getImage().equals("")) {
           Picasso.get().load(model.getImage())
                   .placeholder(R.drawable.image_2)
                   .error(R.drawable.image_2).resize(500, 500)
                   .into(holder.image);
       }


        String duration=model.getUpdated_at();
        String[] str = duration.split("\\.");
        String str1 = str[0];
        String totalString = str1;
        //"2020-06-02T20:25:16.000Z"
        String[] parts = totalString.split("T");
        String path = parts[0];
        String path1 = parts[1];

        String resultantDate = getDateFormattedString("yyyy-mm-dd", path, "mm/dd/yy");
        Log.e("resultantDate", "" + resultantDate);
        Date date = null;
        try {
            date = new Date(resultantDate);
            long millis = date.getTime();
            holder.tvDuration.setText(model.getNo_of_days_to_complete()+" Work Days");
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("id",model.getId());
                bundle.putString("name",model.getName());
                S.I(context, HomeDetailActivity.class, bundle);
            }
        });


    }
    public static String getDateFormattedString(String sourceFormat, String dateString, String targetFormat) {
        SimpleDateFormat mOriginalFormat = new SimpleDateFormat(sourceFormat);//3/1/2016
        SimpleDateFormat mTargetFormat = new SimpleDateFormat(targetFormat);//Mar/1/2016

        String reqstring = null;
        try {
            reqstring = mTargetFormat.format(mOriginalFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reqstring;
    }
    @Override
    public int getItemCount() {
        return homeFirstModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, img, imgNext;
        RatingBar ratingBar;
        TextView tvTitle, tvDuration, tvPrice, tvCurrency;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            img = itemView.findViewById(R.id.img);
            imgNext = itemView.findViewById(R.id.imgNext);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCurrency = itemView.findViewById(R.id.tvCurrency);
            card = itemView.findViewById(R.id.card);
        }
    }
}

