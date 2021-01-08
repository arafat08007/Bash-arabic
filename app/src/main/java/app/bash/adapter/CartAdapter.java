package app.bash.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.bash.R;
import app.bash.activity.MainActivity;
import app.bash.activity.WelcomeActivity;
import app.bash.data.data_helper.MyCartHelper;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.data.data_model.MyCartModel;
import app.bash.listener.ButtonListener;
import app.bash.model.CartModel;
import app.bash.model.HomeFirstModel;
import app.bash.utilitis.S;
import app.bash.utilitis.TimeAgo;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    ArrayList<MyCartModel> cartModels;
    int Quantity = 1;
    String SetQuantity;
    private ButtonListener buttonListner = null;

    public CartAdapter(Context context, ArrayList<MyCartModel> cartModels) {
        this.context = context;
        this.cartModels = cartModels;
    }

    public void setButtonListner(ButtonListener buttonListner) {
        this.buttonListner = buttonListner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(ViewHolder holder, int position) {
        MyCartModel myCartModel = cartModels.get(position);
        holder.tvsubTitle.setText(myCartModel.getServicename());
        holder.tvDuration.setText(myCartModel.getDuration() + context.getString(R.string.work_days));
        holder.tvPrice.setText(myCartModel.getAmount());
        holder.tvQuantity.setText(myCartModel.getQuantity());
        holder.tvTitle.setText(myCartModel.getCatName());

        holder.linearDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupForClearCart();
            }
        });

        holder.linearPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetQuantity = holder.tvQuantity.getText().toString();
                if (holder.tvQuantity.getText().toString().equals("") || holder.tvQuantity.getText().toString().equals("0")) {
                    S.T(context, "Enter Quantity Minimum 1");
                } else {
                    Quantity = Integer.parseInt(SetQuantity);
                    if (Quantity >= 1) {
                        holder.tvQuantity.setText(SetQuantity);
                        Quantity++;
                        holder.tvQuantity.setText(String.valueOf(Quantity));
                        if (buttonListner != null)
                            buttonListner.buttonClick(position, String.valueOf(Quantity));
                        MyCartHelper.getInstance().delete();
                        myCartModel.setServiceId(myCartModel.getServiceId());
                        myCartModel.setQuantity(String.valueOf(Quantity));
                        myCartModel.setDuration(myCartModel.getDuration());
                        myCartModel.setAmount(myCartModel.getAmount());
                        myCartModel.setServicename(myCartModel.getServicename());
                        MyCartHelper.getInstance().insertMyCartModel(myCartModel);

                    }
                }
            }
        });
        holder.linearMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetQuantity = holder.tvQuantity.getText().toString();
                if (holder.tvQuantity.getText().toString().equals("") || holder.tvQuantity.getText().toString().equals("0")) {
                    S.T(context, "Enter Quantity Minimum 1");
                } else {
                    Quantity = Integer.parseInt(SetQuantity);
                    if (Quantity > 1) {
                        holder.tvQuantity.setText(SetQuantity);
                        Quantity--;
                        holder.tvQuantity.setText(String.valueOf(Quantity));

                        if (buttonListner != null)
                            buttonListner.buttonClick(position, String.valueOf(Quantity));
                        MyCartHelper.getInstance().delete();
                        myCartModel.setServiceId(myCartModel.getServiceId());
                        myCartModel.setQuantity(String.valueOf(Quantity));
                        myCartModel.setDuration(myCartModel.getDuration());
                        myCartModel.setAmount(myCartModel.getAmount());
                        myCartModel.setServicename(myCartModel.getServicename());
                        MyCartHelper.getInstance().insertMyCartModel(myCartModel);
                    }
                }
            }
        });
    }

    private void openPopupForClearCart() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        Button tvTitle = dialog.findViewById(R.id.tvTitle);
        Button text = dialog.findViewById(R.id.text);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);

        tvTitle.setText("Delete!");
        text.setText(R.string.you_want_to_clear_cart);
        dialog.show();
        dialog.setCancelable(false);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MyCartHelper.getInstance().delete();
                S.I_clear(context, MainActivity.class, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTitle, tvsubTitle, tvDuration, tvPrice, tvCurrency, tvQuantity;
        LinearLayout linearMinus, linearPlus, linearDelete;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvsubTitle = itemView.findViewById(R.id.tvsubTitle);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCurrency = itemView.findViewById(R.id.tvCurrency);
            linearMinus = itemView.findViewById(R.id.linearMinus);
            linearPlus = itemView.findViewById(R.id.linearPlus);
            linearDelete = itemView.findViewById(R.id.linearDelete);
            card = itemView.findViewById(R.id.card);
        }
    }
}