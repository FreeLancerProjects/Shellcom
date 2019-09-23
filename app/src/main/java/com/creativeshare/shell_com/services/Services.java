package com.creativeshare.shell_com.services;


import com.creativeshare.shell_com.models.CityModel;
import com.creativeshare.shell_com.models.ContainerSizeModel;
import com.creativeshare.shell_com.models.ContainerTypeModel;
import com.creativeshare.shell_com.models.ContainersModel;
import com.creativeshare.shell_com.models.ContainersOrderDetailsModel;
import com.creativeshare.shell_com.models.CustomClearanceOrderDetailsModel;
import com.creativeshare.shell_com.models.EngineeringOrderDetailsModel;
import com.creativeshare.shell_com.models.Engineering_Type_Model;
import com.creativeshare.shell_com.models.Equipment_Model;
import com.creativeshare.shell_com.models.NotificationDataModel;
import com.creativeshare.shell_com.models.OrderDataModel;
import com.creativeshare.shell_com.models.OrderIdModel;
import com.creativeshare.shell_com.models.OtherServiceContainerSizeModel;
import com.creativeshare.shell_com.models.PlaceGeocodeData;
import com.creativeshare.shell_com.models.PlaceMapDetailsData;
import com.creativeshare.shell_com.models.RentalOrderDetailsModel;
import com.creativeshare.shell_com.models.Rental_equipment_Model;
import com.creativeshare.shell_com.models.ServicesModel;
import com.creativeshare.shell_com.models.ShippingOrderDetailsModel;
import com.creativeshare.shell_com.models.SliderDataModel;
import com.creativeshare.shell_com.models.TermsModel;
import com.creativeshare.shell_com.models.UserModel;
import com.creativeshare.shell_com.models.WaterOrderDetailsModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface Services {

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> sign_up(
            @Field("username") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone_code") String phone_code,
            @Field("phone") String phone,
            @Field("software_type") int software_type
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> sign_in(
            @Field("email") String email,
            @Field("password") String password

    );

    @GET("api/all-sliders")
    Call<SliderDataModel> getSliders();

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @GET("api/all-cities")
    Call<List<CityModel>> getCities();

    @GET("api/all-services")
    Call<List<ServicesModel>> getServices();

    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> contact_us(@Field("username") String name,
                                  @Field("email") String email,
                                  @Field("message") String message
    );

    @GET("api/terms-and-conditions")
    Call<TermsModel> getTerms();

    @GET("api/about-us")
    Call<TermsModel> getAboutUs();

    @GET("api/all-wide-sizes")
    Call<List<ContainerSizeModel>> getContainerSize();

    @Multipart
    @POST("/api/upgrade")
    Call<ResponseBody> upgradeToCompany(@Part("company_email") RequestBody company_email,
                                        @Part("title") RequestBody title,
                                        @Part("company_information") RequestBody company_information,
                                        @Part("latitude") RequestBody latitude,
                                        @Part("longitude") RequestBody longitude,
                                        @Part("city") RequestBody city_id,
                                        @Part("address") RequestBody address,
                                        @Part("user_id") RequestBody user_id,
                                        @Part("services_ids[]") List<RequestBody> services_ids,
                                        @Part MultipartBody.Part commercial_register_image

    );


    @GET("api/type-of-truck")
    Call<List<ContainersModel>> getContainers();

    @FormUrlEncoded
    @POST("api/order/connect-water")
    Call<OrderIdModel> sendDeliveryWaterOrder(@Field("user_id") String user_id,
                                              @Field("order_type") String order_type,
                                              @Field("arrival_time") long arrival_time,
                                              @Field("latitude") double latitude,
                                              @Field("longitude") double longitude,
                                              @Field("city") String city_id,
                                              @Field("address") String address,
                                              @Field("wide_size") String wide_size

    );


    @Multipart
    @POST("/api/order/shipping")
    Call<OrderIdModel> sendShippingOrder(@Part("order_type") RequestBody order_type,
                                         @Part("user_id") RequestBody user_id,
                                         @Part("shipment_description") RequestBody description,
                                         @Part("container_id") RequestBody container_id,
                                         @Part("shipment_type_id") RequestBody shipment_type_id,
                                         @Part("shipment_amount") RequestBody shipment_amount,
                                         @Part("shipment_number") RequestBody shipment_number,
                                         @Part("shipment_size_id") RequestBody shipment_size_id,
                                         @Part("phone_code_from") RequestBody phone_code_from,
                                         @Part("phone_from") RequestBody phone_from,
                                         @Part("company_name_from") RequestBody company_name_from,
                                         @Part("responsible_from") RequestBody responsible_from,
                                         @Part("email_from") RequestBody email_from,
                                         @Part("city_from") RequestBody city_from,
                                         @Part("address_from") RequestBody address_from,
                                         @Part("lat_from") RequestBody lat_from,
                                         @Part("long_from") RequestBody long_from,
                                         @Part("date_from") RequestBody load_date_from,
                                         @Part("phone_code_to") RequestBody phone_code_to,
                                         @Part("phone_to") RequestBody phone_to,
                                         @Part("company_name_to") RequestBody company_name_to,
                                         @Part("responsible_to") RequestBody responsible_to,
                                         @Part("city_to") RequestBody city_to,
                                         @Part("address_to") RequestBody address_to,
                                         @Part("email_to") RequestBody email_to,
                                         @Part("lat_to") RequestBody lat_to,
                                         @Part("long_to") RequestBody long_to,
                                         @Part("shipment_value") RequestBody shipment_value,
                                         @Part("shipment_weight") RequestBody Weight,
                                         @Part("payment") RequestBody payment_method,
                                         @Part("date_to") RequestBody date_to,
                                         @Part MultipartBody.Part image1,
                                         @Part MultipartBody.Part image2


    );

    @Multipart
    @POST("/api/order/engConsultances")
    Call<OrderIdModel> sendEngineeringOrder(@Part("order_type") RequestBody order_type,
                                            @Part("user_id") RequestBody user_id,
                                            @Part("type_id") RequestBody type_id,
                                            @Part("propertyArea") RequestBody propertyArea,
                                            @Part("description") RequestBody description,
                                            @Part("address") RequestBody address,
                                            @Part("latitude") RequestBody latitude,
                                            @Part("longitude") RequestBody longitude,
                                            @Part MultipartBody.Part propertyImage


    );

    @FormUrlEncoded
    @POST("api/token/update")
    Call<ResponseBody> updateToken(@Field("user_id") int user_id,
                                   @Field("firebase_token") String firebase_token
    );


    @FormUrlEncoded
    @POST("api/single-equipment")
    Call<Equipment_Model> getequipsize(@Field("equipment_id") Integer equipment_id);


    @FormUrlEncoded
    @POST("api/order/rental-equipment")
    Call<Rental_equipment_Model> equipmentorder(
            @Field("order_type") Integer order_type,
            @Field("user_id") int user_id,
            @Field("equ_type") int equ_type,
            @Field("equ_size") int equ_size,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("city") String city,
            @Field("address") String address,
            @Field("start_time") long start_time,
            @Field("used_time") String used_time,
            @Field("num_of_equ") int num_of_equ


    );

    @GET("api/all-equipments")
    Call<List<Equipment_Model>> get_equipments();

    @GET("api/all-containers-type")
    Call<List<ContainerTypeModel>> getContainerType();


    @GET("api/all-containers-sizes")
    Call<OtherServiceContainerSizeModel> getOtherContainerSize(@Query("containerType_id") int containerType_id);


    @FormUrlEncoded
    @POST("api/order/container_order")
    Call<OrderIdModel> sendContainerOrder(@Field("user_id") String user_id,
                                          @Field("order_type") String order_type,
                                          @Field("latitude") double latitude,
                                          @Field("longitude") double longitude,
                                          @Field("address") String address,
                                          @Field("type") int type,
                                          @Field("size") int size

    );


    @Multipart
    @POST("api/order/customs_clearances")
    Call<OrderIdModel> sendCustomerOrder(
            @Part("user_id") RequestBody user_id_part,
            @Part("order_type") RequestBody order_type_part,
            @Part("description") RequestBody description_part,
            @Part MultipartBody.Part image1_part,
            @Part MultipartBody.Part image2_part,
            @Part MultipartBody.Part image3_part,
            @Part MultipartBody.Part image4_part,
            @Part MultipartBody.Part image5_part);


    @GET("api/all-notifications")
    Call<NotificationDataModel> getNotifications(@Query("user_type") String user_type,
                                                 @Query("user_id") String user_id,
                                                 @Query("company_id") String company_id,
                                                 @Query("page") int page
    );

    @GET("api/engineeringType")
    Call<List<Engineering_Type_Model>> getEngineeringType();

    @GET("api/order-data")
    Call<WaterOrderDetailsModel> getWaterOrderDetails(@Query("order_id") int order_id,
                                                      @Query("order_type") String order_type
    );

    @GET("api/order-data")
    Call<ShippingOrderDetailsModel> getShipmentOrderDetails(@Query("order_id") int order_id,
                                                            @Query("order_type") String order_type
    );

    @GET("api/order-data")
    Call<RentalOrderDetailsModel> getRentalOrderDetails(@Query("order_id") int order_id,
                                                        @Query("order_type") String order_type
    );

    @GET("api/order-data")
    Call<ContainersOrderDetailsModel> getContainersOrderDetails(@Query("order_id") int order_id,
                                                                @Query("order_type") String order_type
    );

    @GET("api/order-data")
    Call<CustomClearanceOrderDetailsModel> getCustomClearanceOrderDetails(@Query("order_id") int order_id,
                                                                          @Query("order_type") String order_type
    );

    @GET("api/order-data")
    Call<EngineeringOrderDetailsModel> getEngineeringOrderDetails(@Query("order_id") int order_id,
                                                                  @Query("order_type") String order_type
    );

    @FormUrlEncoded
    @POST("api/offer/make")
    Call<ResponseBody> companySendOffer(@Field("campany_id") int campany_id,
                                        @Field("order_id") String order_id,
                                        @Field("notification_id") String notification_id,
                                        @Field("price") String price
    );

    @FormUrlEncoded
    @POST("api/offer/refuse-make-offer")
    Call<ResponseBody> companyRefuseOrder(@Field("notification_id") int notification_id
    );


    @FormUrlEncoded
    @POST("api/offer/accept")
    Call<ResponseBody> clientAcceptOffer(@Field("offer_id") String offer_id,
                                         @Field("notification_id") String notification_id,
                                         @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("api/refuse_offer")
    Call<ResponseBody> clientRefuseOffer(@Field("offer_id") String offer_id,
                                         @Field("notification_id") String notification_id
    );

    @FormUrlEncoded
    @POST("api/profile/company/available")
    Call<ResponseBody> changeAvailability(@Field("available") int available,
                                          @Field("user_id") int user_id

    );

    @FormUrlEncoded
    @POST("api/finished_offer")
    Call<ResponseBody> companyFinishOrder(@Field("order_id") String order_id

    );

    @GET("api/my-orders")
    Call<OrderDataModel> getOrders(@Query("user_type") String user_type,
                                   @Query("user_id") int user_id,
                                   @Query("company_id") int company_id,
                                   @Query("order_status") String order_status,
                                   @Query("page") int page


    );
}
