package app.bash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.bash.R;
import app.bash.adapter.ServiceAdapter;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.model.ServiceModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ServiceFragment extends Fragment {
    @BindView(R.id.recyclerviewService)
    RecyclerView recyclerviewService;

    ArrayList<ServiceModel> serviceModelArrayList = new ArrayList<>();
    @BindView(R.id.tvNoService)
    TextView tvNoService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, v);
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            getMyService();
        } else {
            S.T(getActivity(), getString(R.string.no_internet_connection));
        }

        return v;
    }

    private void getMyService() {
        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getMyService(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId()
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.optJSONObject(i);

                            ServiceModel serviceModel = new ServiceModel();
                            serviceModel.setServiceId(data.getString("service_id"));
                            serviceModel.setOrderId(data.getString("id"));
                            serviceModel.setPrice(data.getString("amount"));
                            if (SavedData.getLanguage().equals("ar")) {
                                serviceModel.setSubtitle(data.getString("arabic_service_name"));
                            }else {
                                serviceModel.setSubtitle(data.getString("service_name"));
                            }
                            serviceModel.setSubtitle(data.getString("service_name"));
                            serviceModel.setStatus(Integer.parseInt(data.getString("processing_status")));
                            serviceModelArrayList.add(serviceModel);
                        }
                        if(serviceModelArrayList.size()>0) {
                            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recyclerviewService.setLayoutManager(horizontalLayoutManagaer);
                            ServiceAdapter serviceAdapter = new ServiceAdapter(getActivity(), serviceModelArrayList);
                            recyclerviewService.setAdapter(serviceAdapter);
                            tvNoService.setVisibility(View.GONE);
                        }else {
                            tvNoService.setVisibility(View.VISIBLE);
                        }

                    } else {
                        S.T(getActivity(), jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
      /*  for (int i = 0; i < 7; i++) {
            ServiceModel serviceModel = new ServiceModel();
            serviceModel.setCost("");
            serviceModel.setPrice("");
            serviceModel.setSubtitle("");
            serviceModel.setTitle("");
            serviceModel.setStatus(i);
            serviceModelArrayList.add(serviceModel);
        }
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewService.setLayoutManager(horizontalLayoutManagaer);
        ServiceAdapter serviceAdapter = new ServiceAdapter(getActivity(), serviceModelArrayList);
        recyclerviewService.setAdapter(serviceAdapter);*/
    }
}
