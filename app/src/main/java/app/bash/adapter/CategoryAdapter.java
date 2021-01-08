package app.bash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.bash.R;
import app.bash.listener.ButtonListener;
import app.bash.model.HomeFirstModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<HomeFirstModel> homeFirstModels;
    ButtonListener buttonListener = null;

    public CategoryAdapter(Context context, ArrayList<HomeFirstModel> homeFirstModels) {
        this.context = context;
        this.homeFirstModels = homeFirstModels;
    }

    public void setButtonListener(ButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(ViewHolder holder, int position) {

        HomeFirstModel model = homeFirstModels.get(position);

        if (model.isStatus()) {
            holder.card.setBackgroundResource(R.drawable.dotted_blue_rectangle);
        } else {
            holder.card.setBackgroundResource(R.drawable.dotted_white_rectangle);
        }

        holder.tvTitle.setText(model.getName());
        if(!model.getImage().equals("")) {
            Picasso.get().load(model.getImage())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonListener != null) {
                    buttonListener.buttonClick(position,"click");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeFirstModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvTitle;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            card = itemView.findViewById(R.id.card);
        }
    }
}

