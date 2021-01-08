package app.bash.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.bash.R;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.SavedData;

public class AboutUsFragment extends Fragment {
    private TextView tvDesc;
    private ImageView image1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_us, container, false);
        tvDesc = v.findViewById(R.id.tvDesc);
        image1 = v.findViewById(R.id.image1);
        getAboutUs();
        return v;
    }

    private void getAboutUs() {
        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient_1().create(ApiInterface.class).getAboutUs(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject json = jsonArray.getJSONObject(0);
                        if (SavedData.getLanguage().equals("en")) {
                            tvDesc.setText(json.getString("DESCRIPTION"));
                        } else {
                            tvDesc.setText(json.getString("ARABIC_DESCRIPTION"));
                        }
                        if (json.getString("LOGO").equals("")) {
                            Picasso.get().load(json.getString("LOGO"))
                                    .placeholder(R.drawable.logo)
                                    .error(R.drawable.logo)
                                    .into(image1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}