package app.bash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.bash.R;
import app.bash.adapter.InvoiceAdapter;
import app.bash.adapter.NotificationAdapter;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.model.InvoiceModel;
import app.bash.model.NotificationModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import butterknife.BindView;
import butterknife.ButterKnife;


public class InvoiceFragment extends Fragment {

    @BindView(R.id.recyclerviewInvoice)
    RecyclerView recyclerviewInvoice;
    ArrayList<InvoiceModel> invoiceModelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invoice, container, false);
        ButterKnife.bind(this, v);
        setList();
        return v;
    }

    private void setList() {
    /*InvoiceModel invoiceModel = new InvoiceModel();
    invoiceModel.setDate("");
    invoiceModel.setCost("");
    invoiceModel.setId("");
    invoiceModel.setInvoiveNo("");
    invoiceModel.setStatus("");
    invoiceModel.setTotal("");
    for (int i = 0; i < 7; i++) {
      invoiceModelArrayList.add(invoiceModel);
    }
    LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    recyclerviewInvoice.setLayoutManager(horizontalLayoutManagaer);
    InvoiceAdapter invoiceAdapter = new InvoiceAdapter(getActivity(), invoiceModelArrayList);
    recyclerviewInvoice.setAdapter(invoiceAdapter);*/

        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getInvoices(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId()
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json = data.getJSONObject(i);
                            InvoiceModel invoiceModel = new InvoiceModel();
                            invoiceModel.setId(json.getString("id"));
                            invoiceModel.setUser_id(json.getString("user_id"));
                            invoiceModel.setService_id(json.getString("service_id"));
                            invoiceModel.setCoupon_id(json.getString("coupon_id"));
                            invoiceModel.setTransaction_id(json.getString("transaction_id"));
                            invoiceModel.setPayment_response_json(json.getString("payment_response_json"));
                            invoiceModel.setAmount(json.getString("amount"));
                            invoiceModel.setVat(json.getString("vat"));
                            invoiceModel.setPayment_method(json.getString("payment_method"));
                            invoiceModel.setQuantity(json.getString("quantity"));
                            invoiceModel.setOrder_status(json.getString("order_status"));
                            invoiceModel.setProcessing_status(json.getString("processing_status"));
                            invoiceModel.setEmploye_id(json.getString("employe_id"));
                            invoiceModel.setOrder_transfered_employe_id(json.getString("order_transfered_employe_id"));
                            invoiceModel.setParent_id(json.getString("parent_id"));
                            invoiceModel.setOrder_name(json.getString("order_name"));
                            invoiceModel.setOrder_category_id(json.getString("order_category_id"));
                            invoiceModel.setOrder_details(json.getString("order_details"));
                            invoiceModel.setOrder_date_of_working_days(json.getString("order_date_of_working_days"));
                            invoiceModel.setPayment_status(json.getString("payment_status"));
                            invoiceModel.setStatus(json.getString("status"));
                            invoiceModel.setCreated_at(json.getString("created_at"));
                            invoiceModel.setUpdated_at(json.getString("updated_at"));
                            invoiceModelArrayList.add(invoiceModel);
                        }
                        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyclerviewInvoice.setLayoutManager(horizontalLayoutManagaer);
                        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(getActivity(), invoiceModelArrayList);
                        recyclerviewInvoice.setAdapter(invoiceAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}