package app.bash.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.bash.R;
import app.bash.adapter.HomeDetailAdapter;
import app.bash.adapter.InvoiceAdapter;
import app.bash.adapter.ReviewsAdapter;
import app.bash.model.HomeDetailModel;
import app.bash.model.InvoiceModel;
import app.bash.model.ReviewModel;
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

public class ReviewsActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.recyclerviewReviews)
    RecyclerView recyclerviewReviews;
    ArrayList<ReviewModel>reviewModelArrayList=new ArrayList<>();
    String serviceid;

    @Override
    protected int getContentResId() {
        return R.layout.activity_reviews;
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

        serviceid=getIntent().getStringExtra("serviceid");
        if (NetworkUtil.isNetworkAvailable(ReviewsActivity.this)) {
           getReviews(serviceid);
        } else {
            S.T(ReviewsActivity.this, getString(R.string.no_internet_connection));
        }    }
    private void getReviews(String serviceid) {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getReviews(
                serviceid
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                   if (jsonObject.getString("status").equals("true")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        ReviewModel reviewModel = new ReviewModel();
                        reviewModel.setId(data.getString("id"));
                        reviewModel.setStatus(data.getString("status"));
                        reviewModel.setService_id(data.getString("service_id"));
                        reviewModel.setUser_name(data.getString("user_name"));
                        reviewModel.setUser_id(data.getString("user_id"));
                        reviewModel.setRating_star(data.getString("rating_star"));
                        reviewModel.setReview(data.getString("review"));
                        reviewModel.setUpdated_at(data.getString("updated_at"));
                        reviewModel.setProfile(data.getString("profile"));
                        reviewModel.setService_name(data.getString("service_name"));
                        reviewModelArrayList.add(reviewModel);
                    }
                    LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ReviewsActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerviewReviews.setLayoutManager(horizontalLayoutManagaer);
                    ReviewsAdapter reviewsAdapter = new ReviewsAdapter(ReviewsActivity.this, reviewModelArrayList);
                    recyclerviewReviews.setAdapter(reviewsAdapter);
                    } else {
                        S.T(ReviewsActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btnBack)
    public void onViewClicked() {
        onBackPressed();
    }
}