package app.bash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import app.bash.R;
import app.bash.model.HomeDetailModel;
import app.bash.model.NotificationModel;
import app.bash.utilitis.S;

public class HomeDetailAdapter extends RecyclerView.Adapter<HomeDetailAdapter.ViewHolder> {
    Context context;
    ArrayList<String> homeDetailModels;
    String str;

    public HomeDetailAdapter(Context context, ArrayList<String> homeDetailModels){
        this.context=context;
        this.homeDetailModels= homeDetailModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_home_detail, parent,false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder( ViewHolder holder, int position) {
        //HomeDetailModel model=homeDetailModels.get(position);
    //    List<String> items = Arrays.asList(model.getRequirements().split("\\s*,\\s*"));
        holder.tvTitle.setText(homeDetailModels.get(position));


    }

    @Override
    public int getItemCount() {
        return homeDetailModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCircle;
        TextView tvTitle;
        public ViewHolder( View itemView) {
            super(itemView);
            ivCircle = itemView.findViewById(R.id.ivCircle);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}