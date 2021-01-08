package app.bash.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import app.bash.R;
import app.bash.activity.ContinuePaymentActivity;
import app.bash.adapter.CartAdapter;
import app.bash.data.data_helper.MyCartHelper;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.listener.ButtonListener;
import app.bash.model.CartModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CartFragment extends Fragment implements ButtonListener {

    ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
    @BindView(R.id.recyclerviewCart)
    RecyclerView recyclerviewCart;
    @BindView(R.id.tvsubTitle)
    TextView tvsubTitle;
    @BindView(R.id.tvRenewingVisa)
    TextView tvRenewingVisa;
    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;
    @BindView(R.id.tvCurrency)
    TextView tvCurrency;
    @BindView(R.id.tvVat)
    TextView tvVat;
    @BindView(R.id.tvSubTotalVat)
    TextView tvSubTotalVat;
    @BindView(R.id.tvCurrencyVat)
    TextView tvCurrencyVat;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.etCoupen)
    EditText etCoupen;
    @BindView(R.id.tvTotalAmount)
    TextView tvTotalAmount;
    @BindView(R.id.tvCurrencyTotal)
    TextView tvCurrencyTotal;
    @BindView(R.id.btnPay)
    Button btnPay;
    @BindView(R.id.tvEmptyCart)
    TextView tvEmptyCart;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.imgApply)
    ImageView imgApply;
    String vat = "", amount, discount = "";
    @BindView(R.id.layoutCoupon)
    RelativeLayout layoutCoupon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, v);
        setList();
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            fetchVat();
        } else {
            S.T(getActivity(), getString(R.string.no_internet_connection));
        }

        // checkCouponAvialability();
        if (MyCartHelper.getInstance().getMyCartModel().size() > 0) {
            setData(MyCartHelper.getInstance().getMyCartModel().get(0).getQuantity());
        }
        etCoupen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    imgApply.setVisibility(View.VISIBLE);
                } else {
                    imgApply.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return v;
    }

    private void checkCouponAvialability(String Coupon) {
        new JSONParser(getActivity()).parseRetrofitRequestWithoutProgress(ApiClient.getClient().create(ApiInterface.class).CouponAvialability(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                Coupon
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.optJSONObject(i);
                            discount = data.getString("discount");
                        }
                        imgApply.setVisibility(View.INVISIBLE);
//                        etCoupen.setText("");
                        setData(MyCartHelper.getInstance().getMyCartModel().get(0).getQuantity());
                    } else {
                        S.T(getActivity(), jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void fetchVat() {
        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getVat(

        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.optJSONObject(i);
                            vat = data.getString("vat");
                        }
                        setData("1");

                    } else {
                        S.T(getActivity(), jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setList() {

        if (MyCartHelper.getInstance().getMyCartModel().size() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
            layoutCoupon.setVisibility(View.VISIBLE);
            btnPay.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerviewCart.setLayoutManager(horizontalLayoutManagaer);
            CartAdapter cartAdapter = new CartAdapter(getActivity(), MyCartHelper.getInstance().getMyCartModel());
            recyclerviewCart.setAdapter(cartAdapter);
            cartAdapter.setButtonListner(this);
        } else {
            linearLayout.setVisibility(View.GONE);
            layoutCoupon.setVisibility(View.GONE);
            btnPay.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void buttonClick(int position, String buttonText) {
        setData(buttonText);
    }

    private void setData(String quantity) {
        double TotalAmount = 0;
        double vattoatal = 0;
        double discounttoatal = 0;
        double price = 0;

        amount = MyCartHelper.getInstance().getMyCartModel().get(0).getAmount();
        S.E("amount::" + amount);
        S.E("getQuantity::" + quantity);
        S.E("vat::" + vat);
        try {
            if (amount != null && amount.length() > 0) {
                price = Double.parseDouble(amount) * Double.parseDouble(quantity);
            }
        } catch (NumberFormatException e) {
            price = 0; // your default value
        }

        S.E("discount::" + discount);
        if (vat != null && vat.length() > 0) {
//            vattoatal = Double.parseDouble(vat);
            vattoatal = price * Double.parseDouble(vat) / 100;
        } else {
            vattoatal = 0;
        }
        if (discount != null && discount.length() > 0) {
            discounttoatal = Double.parseDouble(discount);
        } else {
            discounttoatal = 0;
        }

        final DecimalFormat df = new DecimalFormat("#.##");
        TotalAmount = price + vattoatal - discounttoatal;

        S.E("TotalAmount :: " + TotalAmount);
        S.E("vattoatal :: " + vattoatal);

        tvSubTotal.setText(amount);
//        tvSubTotalVat.setText(String.format(Locale.ENGLISH, "%d", (int) vattoatal));
//        tvTotalAmount.setText(String.format(Locale.ENGLISH, "%d", (int) TotalAmount));

        tvSubTotalVat.setText(vattoatal + "");
        tvTotalAmount.setText(TotalAmount + "");
    }

    @OnClick({R.id.imgApply, R.id.btnPay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgApply:
                if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    checkCouponAvialability(etCoupen.getText().toString());
                } else {
                    S.T(getActivity(), getString(R.string.no_internet_connection));
                }
                break;
            case R.id.btnPay:
                Bundle bundle = new Bundle();
                bundle.putString("amount", tvTotalAmount.getText().toString());
                bundle.putString("vat", tvSubTotalVat.getText().toString());
                bundle.putString("quantity", MyCartHelper.getInstance().getMyCartModel().get(0).getQuantity());
                bundle.putString("serviceId", MyCartHelper.getInstance().getMyCartModel().get(0).getServiceId());
                S.I(getActivity(), ContinuePaymentActivity.class, bundle);
                break;
        }
    }
}
