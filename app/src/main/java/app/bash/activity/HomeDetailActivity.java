package app.bash.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import app.bash.R;
import app.bash.adapter.HomeDetailAdapter;
import app.bash.data.data_helper.MyCartHelper;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.data.data_model.MyCartModel;
import app.bash.model.HomeDetailModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Const;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.bash.adapter.HomeServiceAdapter.getDateFormattedString;

public class HomeDetailActivity extends BaseActivity {
    @BindView(R.id.btnDetail)
    Button btnDetail;
    @BindView(R.id.btnRequirement)
    Button btnRequirement;
    @BindView(R.id.btnFee)
    Button btnFee;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.recyclerviewPassport)
    RecyclerView recyclerviewPassport;
    @BindView(R.id.btnAskUs)
    Button btnAskUs;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.btnBuyService)
    Button btnBuyService;
    @BindView(R.id.linearRatingBar)
    LinearLayout linearRatingBar;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.topLayout)
    RelativeLayout topLayout;

    @BindView(R.id.tvDetail)
    TextView tvDetail;
    @BindView(R.id.ivCircle)
    ImageView ivCircle;
    ArrayList<String> homeDetailModelArrayList = new ArrayList<>();
    String strDetail, strGovtFee, strServiceId, strPrice, strServiceNmae;
    long millis;
    @BindView(R.id.imageService)
    ImageView imageService;
    private String no_of_days_to_complete;
    private String strCategoryName;

    @Override
    protected int getContentResId() {
        return R.layout.activity_home_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        String id = this.getIntent().getStringExtra("id");
        String strCategoryName = this.getIntent().getStringExtra("name");
        recyclerviewPassport.setVisibility(View.GONE);
        if (NetworkUtil.isNetworkAvailable(HomeDetailActivity.this)) {
            getServiceDetail(id);
        } else {
            S.T(HomeDetailActivity.this, getString(R.string.no_internet_connection));
        }

        if (SavedData.getLanguage().equals("ar")) {
            tvTitle.setGravity(View.FOCUS_LEFT);
        } else {
            tvTitle.setGravity(View.FOCUS_RIGHT);
        }
        //   getList();
    }

    private void getServiceDetail(String id) {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getServiceDetail(
                id
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // if (jsonObject.getString("status").equals("true")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        HomeDetailModel homeDetailModel = new HomeDetailModel();
                        homeDetailModel.setId(data.getString("id"));
                        homeDetailModel.setStatus(data.getString("status"));
                        homeDetailModel.setGovernmental_fees(data.getString("governmental_fees"));
                        homeDetailModel.setLogo_file(data.getString("logo_file"));
                        homeDetailModel.setPrice(data.getString("price"));
                        homeDetailModel.setUpdated_at(data.getString("updated_at"));
                        homeDetailModel.setCategory_name(data.getString("category_name"));
                        homeDetailModel.setCategory_id(data.getString("category_id"));
                        homeDetailModel.setNo_of_days_to_complete(data.getString("no_of_days_to_complete"));

                        if (SavedData.getLanguage().equals("ar")) {
                            homeDetailModel.setTitle(data.getString("arabic_name"));
                            homeDetailModel.setDetails(data.getString("arabic_details"));
                            homeDetailModel.setRequirements(data.getString("arabic_requirements"));

                            homeDetailModelArrayList = new ArrayList(Arrays.asList(data.getString("arabic_requirements").split(",")));

                            strServiceNmae = data.getString("arabic_name");
                            strDetail = data.getString("arabic_details");
                            tvTitle.setText(data.getString("arabic_name"));
                        } else {
                            homeDetailModel.setTitle(data.getString("name"));
                            homeDetailModel.setDetails(data.getString("details"));
                            homeDetailModel.setRequirements(data.getString("requirements"));

                            homeDetailModelArrayList = new ArrayList(Arrays.asList(data.getString("requirements").split(",")));

                            strServiceNmae = data.getString("name");
                            strDetail = data.getString("details");
                            tvTitle.setText(data.getString("name"));
                        }

                        //homeDetailModelArrayList.add(data.getString("requirements"));
                        strPrice = data.getString("price");

                        strServiceId = data.getString("id");

                        strGovtFee = data.getString("governmental_fees");
                        no_of_days_to_complete = data.getString("no_of_days_to_complete");

                        tvDetail.setText(strDetail);

                        if (!data.getString("logo_file").equals("")) {
                            Picasso.get().load(Const.URL.IMAGE_URL + data.getString("logo_file"))
                                    .placeholder(R.drawable.ic_musand)
                                    .error(R.drawable.ic_musand).resize(500, 500)
                                    .into(imageService);
                        }

                        String duration = data.getString("updated_at");
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
                            millis = date.getTime();
                            // holder.tvDuration.setText(TimeAgo.getTimeAgo(millis));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(HomeDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerviewPassport.setLayoutManager(horizontalLayoutManagaer);
                    HomeDetailAdapter homeDetailAdapter = new HomeDetailAdapter(HomeDetailActivity.this, homeDetailModelArrayList);
                    recyclerviewPassport.setAdapter(homeDetailAdapter);
                   /* } else {
                        S.T(getActivity(), jsonObject.getString("message"));
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.btnDetail, R.id.btnRequirement, R.id.btnFee, R.id.ratingBar, R.id.btnAskUs,
            R.id.btnBuyService, R.id.linearRatingBar, R.id.btnBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnDetail:
                btnDetail.setBackgroundResource(R.drawable.btn_bg);
                btnRequirement.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFee.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnDetail.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnRequirement.setTextColor(getResources().getColor(R.color.colorBlue));
                btnFee.setTextColor(getResources().getColor(R.color.colorBlue));
                btnAskUs.setVisibility(View.VISIBLE);
                tvDetail.setVisibility(View.VISIBLE);
                recyclerviewPassport.setVisibility(View.GONE);
                ivCircle.setVisibility(View.GONE);
                tvDetail.setText(strDetail);
                break;
            case R.id.btnRequirement:
                btnRequirement.setBackgroundResource(R.drawable.btn_bg);
                btnDetail.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFee.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnRequirement.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnDetail.setTextColor(getResources().getColor(R.color.colorBlue));
                btnFee.setTextColor(getResources().getColor(R.color.colorBlue));
                recyclerviewPassport.setVisibility(View.VISIBLE);
                btnAskUs.setVisibility(View.GONE);
                tvDetail.setVisibility(View.GONE);
                ivCircle.setVisibility(View.GONE);
                break;
            case R.id.btnFee:
                btnFee.setBackgroundResource(R.drawable.btn_bg);
                btnDetail.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnRequirement.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFee.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnDetail.setTextColor(getResources().getColor(R.color.colorBlue));
                btnRequirement.setTextColor(getResources().getColor(R.color.colorBlue));
                btnAskUs.setVisibility(View.GONE);
                tvDetail.setVisibility(View.VISIBLE);
                ivCircle.setVisibility(View.VISIBLE);
                recyclerviewPassport.setVisibility(View.GONE);
                tvDetail.setText("SAR " + strGovtFee);
                break;
            case R.id.ratingBar:
            case R.id.linearRatingBar:
                Bundle bundle = new Bundle();
                bundle.putString("serviceid", strServiceId);
                S.I(HomeDetailActivity.this, ReviewsActivity.class, bundle);
                break;
            case R.id.btnAskUs:
                if (UserDataHelper.getInstance().getUserDataModel().size() > 0) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("callingFrom", "details");
               //     S.I(HomeDetailActivity.this, ChatActivity.class, bundle1);
                } else {
                    S.T(this, "You need to login to use this service.");
                }
                break;
            case R.id.btnBuyService:
                MyCartHelper.getInstance().delete();
                MyCartModel myCartModel = new MyCartModel();
                myCartModel.setServiceId(strServiceId);
                myCartModel.setQuantity("1");
                myCartModel.setDuration(no_of_days_to_complete);
                myCartModel.setAmount(strPrice);
                myCartModel.setCatName(strCategoryName);
                myCartModel.setServicename(strServiceNmae);
                MyCartHelper.getInstance().insertMyCartModel(myCartModel);
                Bundle bundle2 = new Bundle();
                bundle2.putString("selected", "cart");
                S.I(HomeDetailActivity.this, MainActivity.class, bundle2);
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

}
