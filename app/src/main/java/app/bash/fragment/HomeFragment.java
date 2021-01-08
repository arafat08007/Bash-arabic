package app.bash.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.bash.R;
import app.bash.adapter.CategoryAdapter;
import app.bash.adapter.HomeServiceAdapter;
import app.bash.listener.ButtonListener;
import app.bash.model.HomeFirstModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements ButtonListener {
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.recyclerviewMain)
    RecyclerView recyclerviewMain;
    @BindView(R.id.recyclerviewSecond)
    RecyclerView recyclerviewSecond;

    private ArrayList<HomeFirstModel> categoryArrayList = new ArrayList<>();
    private ArrayList<HomeFirstModel> serviceArrayList = new ArrayList<>();
    private ArrayList<HomeFirstModel> tempserviceArrayList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            getCategory();
        } else {
            S.T(getActivity(), getString(R.string.no_internet_connection));
        }
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    if (serviceArrayList.size() > 0) {
                        recyclerviewSecond.setVisibility(View.VISIBLE);
                        tempserviceArrayList.clear();
                        for (int i = 0; i < serviceArrayList.size(); i++) {
                            if (serviceArrayList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                                tempserviceArrayList.add(serviceArrayList.get(i));
                            }
                        }
                        if (tempserviceArrayList.size() > 0) {
                            S.E("templistsize::" + tempserviceArrayList.size());

                            LinearLayoutManager horizontalLayoutManagaer1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recyclerviewSecond.setLayoutManager(horizontalLayoutManagaer1);
                            HomeServiceAdapter homeServiceAdapter = new HomeServiceAdapter(getActivity(), tempserviceArrayList);
                            recyclerviewSecond.setAdapter(homeServiceAdapter);

                        } else {
                            recyclerviewSecond.setVisibility(View.GONE);
                        }
                    } else {
                        recyclerviewSecond.setVisibility(View.GONE);
                    }

                } else {
                    LinearLayoutManager horizontalLayoutManagaer1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerviewSecond.setLayoutManager(horizontalLayoutManagaer1);
                    HomeServiceAdapter homeServiceAdapter = new HomeServiceAdapter(getActivity(), serviceArrayList);
                    recyclerviewSecond.setAdapter(homeServiceAdapter);
                }
            }



            @Override
            public void afterTextChanged(Editable s) {
                //  filter(s.toString());
            }
        });
        return v;
    }

    private void getCategory() {
        HomeFirstModel homeFirstModel = new HomeFirstModel();
        homeFirstModel.setId("0");
        homeFirstModel.setImage("");
        if (SavedData.getLanguage().equals("ar")) {
            homeFirstModel.setName("جميع الخدمات");
        } else {
            homeFirstModel.setName("All Services");
        }
        homeFirstModel.setStatus(true);
        categoryArrayList.add(homeFirstModel);
        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getCategory(

        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.optJSONObject(i);
                            HomeFirstModel homeFirstModel = new HomeFirstModel();
                            homeFirstModel.setId(data.getString("id"));
                            homeFirstModel.setImage(data.getString("logo_file").replace("67.225.255.179","ftp.bash-g.com"));

                            if (SavedData.getLanguage().equals("ar")) {
                                homeFirstModel.setName(data.getString("arabic_name"));
                            } else {
                                homeFirstModel.setName(data.getString("name"));
                            }

                            if (i == -1) {
                                homeFirstModel.setStatus(true);
                            } else {
                                homeFirstModel.setStatus(false);
                            }
                            categoryArrayList.add(homeFirstModel);
                        }
                        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerviewMain.setLayoutManager(horizontalLayoutManagaer);
                        categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);
                        recyclerviewMain.setAdapter(categoryAdapter);
                        categoryAdapter.setButtonListener(HomeFragment.this);
                        if (NetworkUtil.isNetworkAvailable(getActivity())) {
                            getSevicebyCategory(categoryArrayList.get(0).getId());
                        } else {
                            S.T(getActivity(), getString(R.string.no_internet_connection));
                        }

                    } else {
                        S.T(getActivity(), jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getSevicebyCategory(String id) {
        edtSearch.setText("");
        serviceArrayList.clear();
        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getService(
                id
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                    try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);
                        HomeFirstModel homeFirstModel = new HomeFirstModel();
                        homeFirstModel.setId(data.getString("id"));
                        homeFirstModel.setImage(data.getString("logo_file"));
                        if (SavedData.getLanguage().equals("ar")) {
                            homeFirstModel.setName(data.getString("arabic_name"));
                        } else {
                            homeFirstModel.setName(data.getString("name"));
                        }
                        homeFirstModel.setPrice(data.getString("price"));
                        homeFirstModel.setUpdated_at(data.getString("updated_at"));
                        homeFirstModel.setNo_of_days_to_complete(data.getString("no_of_days_to_complete"));
                        homeFirstModel.setStatus(false);
                        serviceArrayList.add(homeFirstModel);
                    }
                    LinearLayoutManager horizontalLayoutManagaer1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerviewSecond.setLayoutManager(horizontalLayoutManagaer1);
                    HomeServiceAdapter homeServiceAdapter = new HomeServiceAdapter(getActivity(), serviceArrayList);
                    recyclerviewSecond.setAdapter(homeServiceAdapter);
                    } else {
                        S.T(getActivity(), jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void buttonClick(int position, String buttonText) {
        if (buttonText.equals("click")) {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                getSevicebyCategory(categoryArrayList.get(position).getId());
            } else {
                S.T(getActivity(), getString(R.string.no_internet_connection));
            }

            for (int i = 0; i < categoryArrayList.size(); i++) {
                if (i == position) {
                    if (categoryArrayList.get(position).isStatus()) {
                        categoryArrayList.get(i).setStatus(false);
                    } else {
                        categoryArrayList.get(i).setStatus(true);
                    }
                } else {
                    categoryArrayList.get(i).setStatus(false);
                }
            }
            categoryAdapter.notifyDataSetChanged();
        }
    }
}