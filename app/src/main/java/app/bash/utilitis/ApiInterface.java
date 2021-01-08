package app.bash.utilitis;

/*
  Created by SOHEL KHAN on 09-Mar-18.
 */

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("submitEventGoingOrInterested")
    Call<ResponseBody> submitEventGoingOrInterested(
            @Query("userid") String userid,
            @Query("eventid") int eventid,
            @Query("isInterested") boolean isInterested,
            @Query("willAttendEvent") boolean willAttendEvent,
            @Header("Authorization") String userkey
    );

    @FormUrlEncoded
    @POST("web/send-otp")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> sendOtp(
            @Field("registration_type") String registration_type,
            @Field("mobile_code") String mobile_code,
            @Field("mobile_number") String mobile_number

    );

    @FormUrlEncoded
    @POST("web/verify-otp")
    Call<ResponseBody> verifyOtp(
            @Field("registration_type") String registration_type,
            @Field("otp") String otp,
            @Field("user_id") String user_id,
            @Field("fcm_token") String fcm_token

    );

    @FormUrlEncoded
    @POST("web/new-account-registration")
    Call<ResponseBody> newRegistration(
            @Field("name") String name,
            @Field("email") String email,
            @Field("occupation") String occupation,
            @Field("gender") String gender,
            @Field("date_of_birth") String date_of_birth,
            @Field("user_id") String user_id
    );


    @GET("web/get-all-active-occupation")
    Call<ResponseBody> getOccupation(
    );

    @GET("category/get-all-active-category")
    Call<ResponseBody> getCategory(
    );


    @FormUrlEncoded
    @POST("service/get-all-service-by-categoryid")
    Call<ResponseBody> getService(
            @Field("id") String name
    );

    @GET("service/get-service-details")
    Call<ResponseBody> getServiceDetail(
            @Query("id") String id
    );

    @GET("service/get-service-reviews")
    Call<ResponseBody> getReviews(
            @Query("id") String id
    );

    @GET("web/get-users-details")
    Call<ResponseBody> getProfile(
            @Query("id") String id
    );

    @GET("web/fetch-active-questions")
    Call<ResponseBody> getQuestion();

    @GET("web/fetch-master-contact-us")
    Call<ResponseBody> getContactDetails();

    @FormUrlEncoded
    @POST("web/make-oppwa-checkout")
    Call<ResponseBody> getCheckoutId(
            @Field("entityId") String entityId,
            @Field("amount") String amount,
            @Field("currency") String currency,
            @Field("paymentType") String paymentType,
            @Field("Authorization") String Authorization,
            @Field("customer") String customer,
            @Field("billing") String billing,
            @Field("merchantTransactionId") String merchantTransactionId
    );

    @FormUrlEncoded
    @POST("web/check-oppwa-payment-status")
    Call<ResponseBody> getPaymentStatus(
            @Field("checkoutId") String checkoutId,
            @Field("entityId") String entityId,
            @Field("Authorization") String Authorization,
            @Field("service_id") String serviceId,
            @Field("user_id") String userId,
            @Field("coupon_id") String couponId,
            @Field("amount") String amount,
            @Field("vat") String vat,
            @Field("payment_method") String payment_method,
            @Field("quantity") String quantity
    );

    @GET("web/fetch-all-vat")
    Call<ResponseBody> getVat(
    );

    @FormUrlEncoded
    @POST("web/check-coupons-availability")
    Call<ResponseBody> CouponAvialability(
            @Field("user_id") String UserId,
            @Field("name") String Name
    );

    @FormUrlEncoded
    @POST("web/get-order-details")
    Call<ResponseBody> getOrderDetail(
            @Field("order_id") String orderId
    );

    @FormUrlEncoded
    @POST("web/fetch-my-services")
    Call<ResponseBody> getMyService(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("web/change_processing_status")
    Call<ResponseBody> cancelOrder(
            @Field("id") String orderId,
            @Field("processing_status") String Status
    );

    @FormUrlEncoded
    @POST("web/send-report")
    Call<ResponseBody> sendReport(
            @Field("order_id") String orderId,
            @Field("user_id") String userId,
            @Field("report") String Report
    );

    @GET("web/fetch-notifications-inapp")
    Call<ResponseBody> getNotifications(
    );

    @FormUrlEncoded
    @POST("web/fetch-notifications-detail-inapp")
    Call<ResponseBody> NotificationDetail(
            @Field("entity_id") String entityId,
            @Field("entity_type") String entityType
    );

    @FormUrlEncoded
    @POST("chat/send-message-live-chat")
    Call<ResponseBody> SendLiveChatMessage(
            @Field("sender_id") String sender_id,
            @Field("message") String message,
            @Field("reciver_name") String reciver_name
    );

    @FormUrlEncoded
    @POST("chat/send-message")
    Call<ResponseBody> sendMessage(
            @Field("sender_id") String sender_id,
            @Field("reciver_id") String reciver_id,
            @Field("order_id") String order_id,
            @Field("user_type") String user_type,
            @Field("message") String message,
            @Field("reciver_name") String reciver_name
    );

    @FormUrlEncoded
    @POST("chat/get-messages-in-livechat")
    Call<ResponseBody> getLiveChatMessage(
            @Field("sender_id") String sender_id
    );

    @FormUrlEncoded
    @POST("chat/get-message")
    Call<ResponseBody> getMessage(
            @Field("sender_id") String sender_id,
            @Field("reciver_id") String reciver_id,
            @Field("order_id") String order_id,
            @Field("user_type") String user_type
    );

    @FormUrlEncoded
    @POST("chat/add-rating")
    Call<ResponseBody> addRating(
            @Field("service_id") String service_id,
            @Field("user_id") String user_id,
            @Field("service_rating_star") String service_rating_star,
            @Field("employe_rating_star") String employe_rating_star,
            @Field("service_review") String service_review,
            @Field("employe_review") String employe_review
    );

    @FormUrlEncoded
    @POST("web/get-user-invoices")
    Call<ResponseBody> getInvoices(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("web/updateWebUserLanguage")
    Call<ResponseBody> updateWebUserLanguage(
            @Field("id") String id,
            @Field("language") String language
    );

    @GET("web/get-about-us")
    Call<ResponseBody> getAboutUs();

    Call<ResponseBody> updateProfile(String userId, String toString, String toString1, String occupationId, String toString2, String toString3, String encodedImageBase64);

    Call<ResponseBody> updateProfileFbUser(String userId, String toString, String occupationId, String toString1, String toString2, String toString3, String encodedImageBase64);

    Call<ResponseBody> facebookLogin(String name, String s, String s1, String s2, String profile, String s3, String email);

    Call<ResponseBody> contactUs(String user_id, String toString, String toString1, String toString2, String toString3);

}