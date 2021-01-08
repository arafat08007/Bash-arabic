package app.bash.utilitis;

import android.content.Context;

import java.io.IOException;

import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by SOHEl KHAN on 12-Feb-18.
 */

public class JSONParser {
  private Context cx;

  public JSONParser(Context cx) {
    this.cx = cx;
  }

  public void parseRetrofitRequest(final boolean isProgress, Call<ResponseBody> call, final Helper h) {
    final ACProgressFlower materialDialog = S.initProgressDialog(cx);
    if (!isProgress)
      materialDialog.dismiss();
    if (NetworkUtil.isNetworkAvailable(cx)) {
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
          materialDialog.dismiss();
          try {
            if (response.isSuccessful()) {
              String strResponse = response.body().string();
              S.E("URL : " + call.request().url() + " : Response : " + strResponse);
              h.backResponse(strResponse);
            } else {
              S.E("URL : " + call.request().url() + " : Response : ");
              h.backResponse("error");
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
          materialDialog.dismiss();
          h.backResponse("error");
          S.T(cx,"Something went wrong.!");
          S.E("sdcard-err2:" + t.toString());
          S.E("Something went wrong.!");
//                    S.openInternetErrorPopup(isProgress, cx, "slow");
        }
      });
    } else {
      materialDialog.dismiss();
//            S.openInternetErrorPopup(isProgress, cx, "");
    }
  }

  public void parseRetrofitRequestWithoutProgress(Call<ResponseBody> call, final Helper h) {

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        try {
          if (response.isSuccessful()) {
            String strResponse = response.body().string();
            S.E("URL : " + call.request().url() + " : Response : " + strResponse);
            h.backResponse(strResponse);
          } else {
            S.E("URL : " + call.request().url() + " : Response : ");
            h.backResponse("error");
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        h.backResponse("error");
        S.E("sdcard-err2:" + t.toString());
        S.E("Something went wrong.!");
      }
    });

  }
}