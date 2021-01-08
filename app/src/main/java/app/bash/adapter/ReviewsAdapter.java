package app.bash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import app.bash.R;
import app.bash.model.ReviewModel;
import app.bash.model.ServiceModel;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    Context context;
    ArrayList<ReviewModel> reviewModels;

    public ReviewsAdapter(Context context, ArrayList<ReviewModel> reviewModels){
        this.context=context;
        this.reviewModels= reviewModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_reviews, parent,false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder( ViewHolder holder, int position) {
       ReviewModel model=reviewModels.get(position);
       holder.tvName.setText(model.getUser_name());
       holder.tvMessage.setText(model.getReview());
       holder.ratingBar.setRating(Float.parseFloat(model.getRating_star()));
        if(!model.getProfile().equals(""))
            Picasso.get().load(model.getProfile())
                    .placeholder(R.drawable.ic_user_1)
                    .error(R.drawable.ic_user_1).resize(500,500)
                    .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return reviewModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView tvName,tvMessage;
        CardView card;
        RatingBar ratingBar;
        public ViewHolder( View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvName = itemView.findViewById(R.id.tvName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            card = itemView.findViewById(R.id.card);
        }
    }
}
