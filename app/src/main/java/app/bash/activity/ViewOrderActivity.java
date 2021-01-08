package app.bash.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.bash.R;
import app.bash.adapter.ConversationAdapter;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.model.ConversationModel;
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

public class ViewOrderActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvSubtitle)
    TextView tvSubtitle;
    @BindView(R.id.titleService)
    LinearLayout titleService;
    @BindView(R.id.btnMenu)
    ImageView btnMenu;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.imgAttach)
    ImageView imgAttach;
    @BindView(R.id.imgMic)
    ImageView imgMic;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btnSend)
    ImageView btnSend;
    @BindView(R.id.imgDone)
    ImageView imgDone;
    @BindView(R.id.viewDone)
    View viewDone;
    @BindView(R.id.imgProcess)
    ImageView imgProcess;
    @BindView(R.id.viewProcessing)
    View viewProcessing;
    @BindView(R.id.imgReceived)
    ImageView imgReceived;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    String orderId, processingStatus, orderStatus;
    @BindView(R.id.tvCancelled)
    TextView tvCancelled;
    @BindView(R.id.bottomChatLayout)
    LinearLayout bottomChatLayout;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.recyclerviewChat)
    RecyclerView recyclerviewChat;
    @BindView(R.id.bottomLayout)
    CardView bottomLayout;
    private String order_transfered_employe_id;
    private ArrayList<ConversationModel> arrayList = new ArrayList<>();

    @Override
    protected int getContentResId() {
        return R.layout.activity_view_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        if (SavedData.getLanguage().equals("ar")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBack.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnBack.setLayoutParams(params);

            RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) btnMenu.getLayoutParams();
            params_1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnMenu.setLayoutParams(params_1);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBack.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnBack.setLayoutParams(params);

            RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) btnMenu.getLayoutParams();
            params_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnMenu.setLayoutParams(params_1);
        }

        orderId = this.getIntent().getStringExtra("orderId");
        if (NetworkUtil.isNetworkAvailable(ViewOrderActivity.this)) {
            getOrderDetail(orderId);
        } else {
            S.T(ViewOrderActivity.this, getString(R.string.no_internet_connection));
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void getOrderDetail(String orderId) {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getOrderDetail(
                orderId
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.optJSONObject(i);
                            processingStatus = data.getString("processing_status");
                            orderStatus = data.getString("order_status");
                            if (data.getString("order_transfered_employe_id").equals("0")) {
                                order_transfered_employe_id = data.getString("employe_id");
                            } else {
                                order_transfered_employe_id = data.getString("order_transfered_employe_id");
                            }
                        }
                        S.E("processingStatus :: " + processingStatus);
                        switch (processingStatus) {
                            case "0":
                                imgDone.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                                imgProcess.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                                imgReceived.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green_button));
                                viewDone.setBackgroundColor(getResources().getColor(R.color.colorUnSelectedGray));
                                viewProcessing.setBackgroundColor(getResources().getColor(R.color.colorUnSelectedGray));
                                break;
                            case "1":
                                imgDone.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                                imgProcess.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green_button));
                                imgReceived.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green_button));
                                viewDone.setBackgroundColor(getResources().getColor(R.color.colorUnSelectedGray));
                                viewProcessing.setBackgroundColor(getResources().getColor(R.color.colorSelectedGreen));
                                break;
                            case "2":
                                imgDone.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green_button));
                                imgProcess.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green_button));
                                imgReceived.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green_button));
                                viewDone.setBackgroundColor(getResources().getColor(R.color.colorSelectedGreen));
                                viewProcessing.setBackgroundColor(getResources().getColor(R.color.colorSelectedGreen));
                                break;
                            case "3":
                                imgDone.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_red_button));
                                imgProcess.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_red_button));
                                imgReceived.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_red_button));
                                viewDone.setBackgroundColor(getResources().getColor(R.color.colorSelectedRed));
                                viewProcessing.setBackgroundColor(getResources().getColor(R.color.colorSelectedRed));
                                tvCancelled.setVisibility(View.VISIBLE);
                                bottomChatLayout.setVisibility(View.GONE);
                                break;
                            default:
                                imgDone.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                                imgProcess.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                                imgReceived.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                                viewDone.setBackgroundColor(getResources().getColor(R.color.colorUnSelectedGray));
                                viewProcessing.setBackgroundColor(getResources().getColor(R.color.colorUnSelectedGray));
                                break;
                        }

                        getMessage();
                    } else {
                        imgDone.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                        imgProcess.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                        imgReceived.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_button));
                        viewDone.setBackgroundColor(getResources().getColor(R.color.colorUnSelectedGray));
                        viewProcessing.setBackgroundColor(getResources().getColor(R.color.colorUnSelectedGray));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessage() {
        new JSONParser(this).parseRetrofitRequestWithoutProgress(ApiClient.getClient().create(ApiInterface.class).sendMessage(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                order_transfered_employe_id,
                orderId,
                "2",
                etMessage.getText().toString(),
                UserDataHelper.getInstance().getUserDataModel().get(0).getFullName()
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        etMessage.setText("");
                        getMessage();
                    } else {
                        S.T(ViewOrderActivity.this, "Message not sent!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getMessage() {
        new JSONParser(this).parseRetrofitRequestWithoutProgress(ApiClient.getClient().create(ApiInterface.class).getMessage(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                order_transfered_employe_id,
                orderId,
                "2"
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        arrayList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json = data.getJSONObject(i);
                            ConversationModel conversationModel = new ConversationModel();
                            conversationModel.setId(json.getString("id"));
                            conversationModel.setSender_id(json.getString("sender_id"));
                            conversationModel.setReciver_id(json.getString("reciver_id"));
                            conversationModel.setReciver_name(json.getString("reciver_name"));
                            conversationModel.setMessage(json.getString("message"));
//                            conversationModel.setAssign_to(json.getString("assign_to"));
                            conversationModel.setCreated_at(json.getString("created_at"));
                            conversationModel.setUpdated_at(json.getString("updated_at"));
                            conversationModel.setMessage_type(json.getString("message_type"));
                            arrayList.add(conversationModel);
                        }
                        recyclerviewChat.setLayoutManager(new LinearLayoutManager(ViewOrderActivity.this));
                        ConversationAdapter conversationAdapter = new ConversationAdapter(ViewOrderActivity.this, arrayList);
                        recyclerviewChat.setAdapter(conversationAdapter);
                        recyclerviewChat.scrollToPosition(arrayList.size()-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.btnBack, R.id.btnMenu, R.id.btnSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnMenu:
                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ViewOrderActivity.this);
                View sheetView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                mBottomSheetDialog.setContentView(sheetView);
                TextView tvCancelOrder = (TextView) sheetView.findViewById(R.id.tvCancelOrder);
                TextView tvReport = (TextView) sheetView.findViewById(R.id.tvReport);
                tvCancelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.cancel();
                        final Dialog dialog = new Dialog(ViewOrderActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_cancel_order);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.gravity = Gravity.CENTER;
                        dialog.getWindow().setAttributes(lp);
                        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
                        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
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
                                if (NetworkUtil.isNetworkAvailable(ViewOrderActivity.this)) {
                                    cancelOrder(orderId, processingStatus);
                                } else {
                                    S.T(ViewOrderActivity.this, getString(R.string.no_internet_connection));
                                }
                            }
                        });
                    }
                });

                tvReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.cancel();
                        final Dialog dialog = new Dialog(ViewOrderActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_report);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.gravity = Gravity.CENTER;
                        dialog.getWindow().setAttributes(lp);
                        Button btnSend = (Button) dialog.findViewById(R.id.btnSend);
                        EditText edtMessage = (EditText) dialog.findViewById(R.id.edtMessage);
                        dialog.show();
//                        dialog.setCancelable(false);
                        btnSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (edtMessage.getText().toString().equals("")) {
                                    S.T(ViewOrderActivity.this, getString(R.string.enter_report));
                                } else {
                                    if (NetworkUtil.isNetworkAvailable(ViewOrderActivity.this)) {
                                        sendReport(orderId, edtMessage.getText().toString());
                                        dialog.dismiss();
                                    } else {
                                        S.T(ViewOrderActivity.this, getString(R.string.no_internet_connection));
                                    }
                                }
                            }
                        });

                    }
                });
                mBottomSheetDialog.show();
                break;
            case R.id.btnSend:
                break;
        }
    }

    private void sendReport(String orderId, String message) {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).sendReport(
                orderId,
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                message
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {

                    } else {
                        S.T(ViewOrderActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void cancelOrder(String orderId, String processingStatus) {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).cancelOrder(
                orderId,
                "3"
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId", orderId);
                        S.I(ViewOrderActivity.this, OrderCancelActivity.class, bundle);

                    } else {
                        //  S.T(ViewOrderActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
