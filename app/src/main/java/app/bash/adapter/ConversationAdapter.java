package app.bash.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.bash.R;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.model.ConversationModel;
import app.bash.utilitis.S;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private Context cx;
    private ArrayList<ConversationModel> cartModels;

    public ConversationAdapter(Context context, ArrayList<ConversationModel> cartModels) {
        this.cx = context;
        this.cartModels = cartModels;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ConversationModel model = cartModels.get(position);

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        Date d = null;
        try {
            d = input.parse(model.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        Log.e("DATE", "" + formatted);

        holder.tvSenderMessage.setText(model.getMessage());
        holder.tvSenderMessageTime.setText(formatted);
        holder.tvSenderName.setText(model.getReciver_name());

        holder.tvReceiverMessage.setText(model.getMessage());
        holder.tvReceiverMessageTime.setText(formatted);

        if (model.getMessage_type().equals("2")) {
            holder.cardSender.setVisibility(View.GONE);
            holder.cardReceiver.setVisibility(View.GONE);
            holder.cardInvoice.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject = new JSONObject(model.getMessage());
                String order_no = jsonObject.getString("order_no");
                String amount = jsonObject.getString("amount");
                String vat = jsonObject.getString("vat");
                String payment_method = jsonObject.getString("payment_method");

                holder.tvAmount.setText(amount + " SAR");
                holder.tvVat.setText(vat + " SAR");

                if (!amount.equals("null")) {
                    int totalAmoount = Integer.parseInt(amount) + Integer.parseInt(vat);
                    holder.tvTotal.setText(totalAmoount + " SAR");
                } else {
                    holder.tvTotal.setText("0 SAR");
                }

                holder.btnDownloadReceipt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", order_no);
                        bundle.putString("cost", amount);
                        bundle.putString("vat", vat);
                        //@Rafat-4/1/2021
                       // S.I(cx, InvoiceActivity.class, bundle);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (UserDataHelper.getInstance().getUserDataModel().get(0).getUserId().equals(model.getSender_id())) {
                holder.cardSender.setVisibility(View.VISIBLE);
                holder.cardInvoice.setVisibility(View.GONE);
                holder.cardReceiver.setVisibility(View.GONE);
            } else {
                holder.cardSender.setVisibility(View.GONE);
                holder.cardInvoice.setVisibility(View.GONE);
                holder.cardReceiver.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSenderMessage, tvSenderMessageTime, tvSenderName, tvReceiverMessage, tvReceiverMessageTime, tvAmount, tvVat, tvTotal;
        LinearLayout cardSender, cardReceiver, cardInvoice;
        Button btnDownloadReceipt;

        public ViewHolder(View itemView) {
            super(itemView);
            cardSender = itemView.findViewById(R.id.cardSender);
            cardReceiver = itemView.findViewById(R.id.cardReceiver);
            cardInvoice = itemView.findViewById(R.id.cardInvoice);

            tvSenderMessage = itemView.findViewById(R.id.tvSenderMessage);
            tvSenderMessageTime = itemView.findViewById(R.id.tvSenderMessageTime);
            tvSenderName = itemView.findViewById(R.id.tvSenderName);
            tvReceiverMessage = itemView.findViewById(R.id.tvReceiverMessage);
            tvReceiverMessageTime = itemView.findViewById(R.id.tvReceiverMessageTime);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvVat = itemView.findViewById(R.id.tvVat);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnDownloadReceipt = itemView.findViewById(R.id.btnDownloadReceipt);
        }
    }
}