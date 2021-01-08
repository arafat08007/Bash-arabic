package app.bash.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.exception.PaymentException;
import com.oppwa.mobile.connect.payment.BrandsValidation;
import com.oppwa.mobile.connect.payment.CheckoutInfo;
import com.oppwa.mobile.connect.payment.ImagesRequest;
import com.oppwa.mobile.connect.payment.PaymentParams;
import com.oppwa.mobile.connect.payment.card.CardPaymentParams;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.ITransactionListener;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.service.ConnectService;
import com.oppwa.mobile.connect.service.IProviderBinder;

import org.json.JSONException;
import org.json.JSONObject;

import app.bash.R;
import app.bash.data.data_helper.CardHelper;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.data.data_model.CardModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContinuePaymentActivity extends BaseActivity implements ITransactionListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvSubtitle)
    TextView tvSubtitle;
    @BindView(R.id.titleService)
    LinearLayout titleService;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.cardMada)
    CardView cardMada;
    @BindView(R.id.cardVisa)
    CardView cardVisa;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.cardType)
    TextView cardType;
    @BindView(R.id.edtCardNumber)
    EditText edtCardNumber;
    @BindView(R.id.edtFullName)
    EditText edtFullName;
    @BindView(R.id.edtDate)
    EditText edtDate;
    @BindView(R.id.edtCVV)
    EditText edtCVV;
    @BindView(R.id.edtStreet)
    EditText edtStreet;
    @BindView(R.id.edtCity)
    EditText edtCity;
    @BindView(R.id.edtState)
    EditText edtState;
    @BindView(R.id.edtCountryCode)
    EditText edtCountryCode;
    @BindView(R.id.edtPostCode)
    EditText edtPostCode;
    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;
    @BindView(R.id.tvCurrency)
    TextView tvCurrency;
    @BindView(R.id.linearPay)
    LinearLayout linearPay;
    @BindView(R.id.selectedMada)
    ImageView selectedMada;
    @BindView(R.id.selectedVisa)
    ImageView selectedVisa;
    @BindView(R.id.selectedMasterCard)
    ImageView selectedMasterCard;
    @BindView(R.id.cardMasterCard)
    CardView cardMasterCard;
    @BindView(R.id.checkSaveCard)
    CheckBox checkSaveCard;

    private IProviderBinder binder;
    private String checkoutId = "";
    //    Entity ID (VISA, MASTER)
//    TEST
//    private String entityIdCard = "8ac7a4c872512e06017254e01f1d07d2";
//    LIVE
    private String entityIdCard = "8acda4cc75bc80e00175d53dff603693";
    //    private String entityIdCard = "8ac7a4c872512e06017254e117f607dc";
    //    Entity ID (MADA)
//    String entityIdMada = "8ac7a4c872512e06017254e01f1d07d2";
    //    private String Authorization = "OGFjN2E0Yzg3MjUxMmUwNjAxNzI1NGRmZDUyYjA3Y2V8OGpiYjJya2NTYg==";
//    TEST
//    private final String Authorization = "OGFjN2E0Yzg3MjUxMmUwNjAxNzI1NGRmZDUyYjA3Y2V8OGpiYjJya2NTYg==";
//    LIVE
    private final String Authorization = "OGFjZGE0Y2M3NWJjODBlMDAxNzVkNTNjYTQzNzM2Nzd8eEFXRmE5VzltZA==";
    String amount, quantity, serviceId;
    String Card = "";
    private String vat;

    @Override
    protected int getContentResId() {
        return R.layout.activity_continue_payment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        if (SavedData.getLanguage().equals("ar")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBack.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnBack.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBack.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnBack.setLayoutParams(params);
        }

        if (SavedData.getLanguage().equals("ar")) {
            edtCardNumber.setGravity(View.FOCUS_LEFT);
            edtFullName.setGravity(View.FOCUS_LEFT);
            edtDate.setGravity(View.FOCUS_LEFT);
            edtCVV.setGravity(View.FOCUS_LEFT);

            edtCardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_credit_card, 0);
            edtFullName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_name, 0);
            edtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calander, 0);
            edtCVV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cvv, 0);
        } else {
            edtCardNumber.setGravity(View.FOCUS_RIGHT);
            edtFullName.setGravity(View.FOCUS_RIGHT);
            edtDate.setGravity(View.FOCUS_RIGHT);
            edtCVV.setGravity(View.FOCUS_RIGHT);

            edtCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_credit_card, 0, 0, 0);
            edtFullName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_name, 0, 0, 0);
            edtDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calander, 0, 0, 0);
            edtCVV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cvv, 0, 0, 0);
        }

        selectedMada.setVisibility(View.VISIBLE);
        selectedVisa.setVisibility(View.GONE);
        selectedMasterCard.setVisibility(View.GONE);
        cardType.setText(R.string.pay_mada);
        Card = "MADA";

        String strAmount = this.getIntent().getStringExtra("amount");
        quantity = this.getIntent().getStringExtra("quantity");
        serviceId = this.getIntent().getStringExtra("serviceId");
        vat = this.getIntent().getStringExtra("vat");

        double dblAmount = Double.parseDouble(strAmount);

        amount = String.format("%.2f", dblAmount);

        S.E("amount ::: :: " + amount);
        S.E("amount ::: :: " + String.format("%.2f", dblAmount));
        S.E("vat ::: :: " + vat);

        if (CardHelper.getInstance().getCardModel().size() > 0) {
            edtCardNumber.setText(CardHelper.getInstance().getCardModel().get(0).getCardnumber());
            edtFullName.setText(CardHelper.getInstance().getCardModel().get(0).getHoldername());
            edtDate.setText(CardHelper.getInstance().getCardModel().get(0).getExpiredate());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binder.addTransactionListener(ContinuePaymentActivity.this);
            }
        }, 2000);
    }

    @OnClick({R.id.btnBack, R.id.cardMada, R.id.cardVisa, R.id.cardMasterCard, R.id.linearPay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.cardMada:
                selectedMada.setVisibility(View.VISIBLE);
                selectedVisa.setVisibility(View.GONE);
                selectedMasterCard.setVisibility(View.GONE);
                cardType.setText(R.string.pay_mada);
                Card = "MADA";
                break;
            case R.id.cardVisa:
                selectedVisa.setVisibility(View.VISIBLE);
                selectedMada.setVisibility(View.GONE);
                selectedMasterCard.setVisibility(View.GONE);
                cardType.setText(R.string.pay_visa);
                Card = "VISA";
                break;
            case R.id.cardMasterCard:
                selectedMasterCard.setVisibility(View.VISIBLE);
                selectedVisa.setVisibility(View.GONE);
                selectedMada.setVisibility(View.GONE);
                cardType.setText(R.string.pay_mastercard);
                Card = "MASTERCARD";
                break;
            case R.id.linearPay:
                S.E("Card ::::::: " + Card);
                S.E("Card ::::::: " + edtCardNumber.getText().toString());
                if (Card.equals("")) {
                    S.T(this, getString(R.string.select_card));
                } else if (edtCardNumber.getText().toString().equals("")) {
                    S.T(this, getString(R.string.enter_card_number));
                } else if (edtFullName.getText().toString().equals("")) {
                    S.T(this, getString(R.string.enter_name));
                } else if (edtDate.getText().toString().equals("")) {
                    S.T(this, getString(R.string.enter_date));
                } else if (edtDate.getText().toString().length() < 6) {
                    S.T(this, getString(R.string.enter_date));
                } else if (edtCVV.getText().toString().equals("")) {
                    S.T(this, getString(R.string.enter_cvv));
                } else {
                    if (NetworkUtil.isNetworkAvailable(this)) {
                        CardHelper.getInstance().delete();
                        if (checkSaveCard.isChecked()) {
                            CardModel cardModel = new CardModel();
                            cardModel.setCardnumber(edtCardNumber.getText().toString());
                            cardModel.setHoldername(edtFullName.getText().toString());
                            cardModel.setExpiredate(edtDate.getText().toString());
                            CardHelper.getInstance().insertCardModel(cardModel);
                        } else {
                            CardModel cardModel = new CardModel();
                            cardModel.setCardnumber("");
                            cardModel.setHoldername("");
                            cardModel.setExpiredate("");
                            CardHelper.getInstance().insertCardModel(cardModel);
                        }
                        getCheckoutId();
                    } else {
                        S.T(this, getString(R.string.no_internet_connection));
                    }
                }

                // S.I(ContinuePaymentActivity.this, PleaseWaitActivity.class,null);
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IProviderBinder) service;
            /* we have a connection to the service */
            try {
                binder.initializeProvider(Connect.ProviderMode.LIVE);
                S.E("binder.getProviderMode() ::: " + binder.getProviderMode());
            } catch (PaymentException ee) {
                S.E("PaymentException ::: " + ee.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    private void getCheckoutId() {
        JSONObject customerJson = new JSONObject();
        try {
            String[] separated = UserDataHelper.getInstance().getUserDataModel().get(0).getFullName().split(" ");
            customerJson.put("email", UserDataHelper.getInstance().getUserDataModel().get(0).getEmail());
            customerJson.put("givenName", separated[0]);
            customerJson.put("surname", separated[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject billingJson = new JSONObject();
        try {
            billingJson.put("billing.street1", edtStreet.getText().toString());
            billingJson.put("billing.city", edtCity.getText().toString());
            billingJson.put("billing.state", edtState.getText().toString());
            billingJson.put("billing.country", edtCountryCode.getText().toString());
            billingJson.put("billing.postcode", edtPostCode.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        S.E("customerJson.toString() :: " + customerJson.toString());
        S.E("billingJson.toString() :: " + billingJson.toString());
        String date = edtDate.getText().toString();
        String month = date.substring(0, 2);
        String year = date.substring(2);
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getCheckoutId(
                entityIdCard,
                "11.50",
                "SAR",
                "DB",
                Authorization,
                String.valueOf(customerJson),
                String.valueOf(billingJson),
                String.valueOf(System.currentTimeMillis())
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        checkoutId = data.getString("id");
                        S.E("checkoutId ::: ::: " + checkoutId);
                        try {
                            PaymentParams paymentParams = new CardPaymentParams(
                                    checkoutId,
                                    Card,
                                    edtCardNumber.getText().toString().replace("-", ""),
                                    edtFullName.getText().toString(),
                                    month,
                                    year,
                                    edtCVV.getText().toString()
                            );

                            paymentParams.setShopperResultUrl("https://test.oppwa.com//result");

                            Transaction transaction = null;
                            transaction = new Transaction(paymentParams);
                            binder.submitTransaction(transaction);
                        } catch (PaymentException ee) {
                            S.E("EXCEPTION :: " + ee.getMessage());
                            ee.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void brandsValidationRequestSucceeded(BrandsValidation brandsValidation) {

    }

    @Override
    public void brandsValidationRequestFailed(PaymentError paymentError) {

    }

    @Override
    public void imagesRequestSucceeded(ImagesRequest imagesRequest) {

    }

    @Override
    public void imagesRequestFailed() {

    }


    @Override
    public void paymentConfigRequestSucceeded(CheckoutInfo checkoutInfo) {
        S.E("Transaction : checkoutInfo : " + checkoutInfo);
    }

    @Override
    public void paymentConfigRequestFailed(PaymentError paymentError) {
        S.E("Transaction : paymentError 1 : " + paymentError);
    }

    @Override
    public void transactionCompleted(Transaction transaction) {
        S.E("Transaction : Complete : " + transaction.getTransactionType());
        getPaymentStatus();
    }

    private void getPaymentStatus() {

        S.E(" UserDataHelper.getInstance().getUserDataModel().get(0).getUserId() : " + UserDataHelper.getInstance().getUserDataModel().get(0).getUserId());

        new JSONParser(this).parseRetrofitRequestWithoutProgress(ApiClient.getClient().create(ApiInterface.class).getPaymentStatus(
                checkoutId,
                entityIdCard,
                Authorization,
                serviceId,
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                "",
                amount,
                vat,
                "MADA",
                quantity
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        S.I(ContinuePaymentActivity.this, PleaseWaitActivity.class, null);
                    } else {
                        S.T(ContinuePaymentActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void transactionFailed(Transaction transaction, PaymentError paymentError) {
        S.E("Transaction : paymentError : " + paymentError.getErrorMessage());
        S.E("Transaction : paymentError : " + paymentError.getErrorInfo());
        S.E("Transaction : transaction : " + transaction.getPaymentParams());
    }

    @Override
    public void onStart() {
        super.onStart();
        S.E("service start");
        Intent intent = new Intent(this, ConnectService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        S.E("service end");
        unbindService(serviceConnection);
        stopService(new Intent(this, ConnectService.class));
    }

}
