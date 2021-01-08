package app.bash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.bash.R;
import app.bash.adapter.NotificationAdapter;
import app.bash.adapter.ServiceAdapter;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.model.NotificationModel;
import app.bash.model.ServiceModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationFragment extends Fragment {
  @BindView(R.id.recyclerviewNotification)
  RecyclerView recyclerviewNotification;

  ArrayList<NotificationModel>notificationModelArrayList=new ArrayList<>();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_notification, container, false);
    ButterKnife.bind(this, v);
    if (NetworkUtil.isNetworkAvailable(getActivity())) {
      setList();
    } else {
      S.T(getActivity(), getString(R.string.no_internet_connection));
    }

    return v;
  }

  private void setList() {
    new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getNotifications(

    ), new Helper() {
      @Override
      public void backResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          if (jsonObject.getString("status").equals("true")) {
            JSONArray jsonArray = jsonObject.optJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
              JSONObject data = jsonArray.optJSONObject(i);

              NotificationModel notificationModel = new NotificationModel();
              notificationModel.setDate(data.getString("updated_at"));
              notificationModel.setId(data.getString("entity_id"));
              notificationModel.setServicename(data.getString("entity_name"));
              notificationModelArrayList.add(notificationModel);
            }
            if(notificationModelArrayList.size()>0) {
              LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
              recyclerviewNotification.setLayoutManager(horizontalLayoutManagaer);
              NotificationAdapter notificationAdapter = new NotificationAdapter(getActivity(), notificationModelArrayList);
              recyclerviewNotification.setAdapter(notificationAdapter);
            //  tvNoService.setVisibility(View.GONE);
            }else {
            //  tvNoService.setVisibility(View.VISIBLE);
            }

          } else {
            S.T(getActivity(), jsonObject.getString("msg"));
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
    /*NotificationModel notificationModel = new NotificationModel();
    notificationModel.setDate("");
    notificationModel.setTitle("");
    notificationModel.setSubtitle("");
    for (int i = 0; i < 7; i++) {
      notificationModelArrayList.add(notificationModel);
    }
    LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    recyclerviewNotification.setLayoutManager(horizontalLayoutManagaer);
    NotificationAdapter notificationAdapter = new NotificationAdapter(getActivity(), notificationModelArrayList);
    recyclerviewNotification.setAdapter(notificationAdapter);*/
  }
}