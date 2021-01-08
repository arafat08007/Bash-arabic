package app.bash.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import app.bash.model.InvoiceModel;
import app.bash.utilitis.S;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    Context context;
    ArrayList<InvoiceModel> invoiceModels;

    public InvoiceAdapter(Context context, ArrayList<InvoiceModel> invoiceModels) {
        this.context = context;
        this.invoiceModels = invoiceModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        InvoiceModel model = invoiceModels.get(position);

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm a yyyy/MM/dd");

        Date d = null;
        try {
            d = input.parse(model.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        Log.e("DATE", "" + formatted);

        holder.tvDate.setText(formatted);
        holder.tvId.setText("#" + model.getId());
        holder.tvPrice.setText(model.getAmount());
        if (model.getPayment_status().equals("1")) {
            holder.tvStatus.setText(R.string.paid);
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.image.setImageResource(R.drawable.rect_blue_bg);
        } else {
            holder.tvStatus.setText(R.string.refund);
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.image.setImageResource(R.drawable.rect_red_bg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", model.getId());
                bundle.putString("cost", model.getAmount());
                bundle.putString("vat", model.getVat());
                 //@Rafat-4/1/2021
              //  S.I(context, InvoiceActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoiceModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvDate, tvInvoiceNo, tvTotalOf, tvStatus, tvId, tvPrice, tvCurrency;
        CardView card;
        LinearLayout imgNext;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvInvoiceNo = itemView.findViewById(R.id.tvInvoiceNo);
            tvTotalOf = itemView.findViewById(R.id.tvTotalOf);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCurrency = itemView.findViewById(R.id.tvCurrency);
            tvId = itemView.findViewById(R.id.tvId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgNext = itemView.findViewById(R.id.imgNext);
            card = itemView.findViewById(R.id.card);
        }
    }
}