package app.bash.utilitis;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by SOHEl KHAN on 09-Mar-18.
 */

public class ApiClient {

    //    private static final String BASE_URL = "http://ftp.bash-g.com:3040/";
    private static final String BASE_URL = "http://159.89.208.101/api/";
    private static final String BASE_URL_1 = "http://128.199.179.158/api/";

    public static Retrofit getClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }

    public static Retrofit getClient_1() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_1)
                .client(okHttpClient)
                .build();
    }
}