package com.mobiato.sfa.rest;

import com.google.gson.JsonObject;
import com.mobiato.sfa.App;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {

//    @GET(App.COMMON_URL)
//    Call<JsonObject> getLogin(@Query("method") String method,
//                              @Query("username") String username,
//                              @Query("password") String password,
//                              @Query("token_no") String token);

    @GET(App.COMMON_URL)
    Call<JsonObject> getLogin(@Query("method") String method,
                              @Query("username") String username,
                              @Query("password") String password,
                              @Query("token_no") String token,
                              @Query("login_time") String time,
                              @Query("login_latitudde") String latitude,
                              @Query("login_longitudde") String longitude,
                              @Query("version") String version);

    @FormUrlEncoded
    @POST(App.COMMON_URL)
    Call<JsonObject> getLogoutAPI(@Field("method") String method,
                                  @Field("logout_time") String time,
                                  @Field("logout_latitudde") String latitude,
                                  @Field("logout_longitudde") String longitude,
                                  @Field("Logged_id") String loggId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getCustomer(@Query("method") String method,
                                 @Query("routeid") String routeid,
                                 @Query("type") String type);

    @GET(App.COMMON_URL)
    Call<JsonObject> getCustomerDeport(@Query("method") String method,
                                       @Query("routeid") String routeid,
                                       @Query("type") String type);

    @GET(App.COMMON_URL)
    Call<JsonObject> getCustomerTechnician(@Query("method") String method,
                                           @Query("user_id") String user_id);

    @GET(App.COMMON_URL)
    Call<JsonObject> getNotificationTechnician(@Query("method") String method,
                                               @Query("user_id") String user_id);

    @GET(App.COMMON_URL)
    Call<JsonObject> getChillerTechnician(@Query("method") String method,
                                          @Query("user_id") String user_id);

    @GET(App.COMMON_URL)
    Call<JsonObject> getCustomer_otc(@Query("method") String method,
                                     @Query("routeid") String routeid,
                                     @Query("type") String type);

    @GET(App.COMMON_URL)
    Call<JsonObject> getCustomer_depot(@Query("method") String method,
                                       @Query("depot") String depot,
                                       @Query("type") String type);

    @GET(App.COMMON_URL)
    Call<JsonObject> getItem(@Query("method") String method);

    @GET(App.COMMON_URL)
    Call<JsonObject> getUOM(@Query("method") String method);

    @GET(App.COMMON_URL)
    Call<JsonObject> getLoadList(@Query("method") String method,
                                 @Query("routeid") String routeId,
                                 @Query("salesman_id") String salesmanId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getNatureOfCallList(@Query("method") String method,
                                         @Query("salesman_id") String salesmanId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getCollectionList(@Query("method") String method,
                                       @Query("routeid") String routeId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getDeliveryList(@Query("method") String method,
                                     @Query("salesman_id") String routeId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getDepotRoute(@Query("method") String method,
                                   @Query("depot_id") String routeId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getRouteSalesmanList(@Query("method") String method,
                                          @Query("route_id") String routeId,
                                          @Query("type") String type);


    /*@POST(App.COMMON_POST_URL)
    Call<JsonObject> getUpdateCustomer(@Query("method") String method,
                                       @Query("cust_id") String cust_id,
                                       @Query("customername") String customername,
                                       @Query("customeraddress1") String customeraddress1,
                                       @Query("customeraddress2") String customeraddress2,
                                       @Query("street") String street,
                                       @Query("customercity") String customercity,
                                       @Query("customerstate") String customerstate,
                                       @Query("customerzip") String customerzip,
                                       @Query("customerphone") String customerphone,
                                       @Query("category") String category,
                                       @Query("salesman_id") String salesman_id,
                                       @Query("fridge") String fridge,
                                       @Query("fridge_serial_number") String fridge_serial_number,
                                       @Query("longitude") String longitude,
                                       @Query("latitude") String latitude);*/

    @Headers({
            "Accept: application/json", "Content-Type: application/json"
    })
    @POST(App.COMMON_POST_URL)
    Call<JsonObject> getUpdateCustomer(@Body JsonObject data);


    @POST(App.COMMON_URL)
    Call<JsonObject> getUpdateChiller(@Body JsonObject data);

    @FormUrlEncoded
    @POST(App.COMMON_URL)
    Call<JsonObject> getUpdateChiller1(@Field("method") String method,
                                       @Field("status") String status,
                                       @Field("schudule_date") String schudule_date,
                                       @Field("id") String id, @Field("iro_id") String iro_id);


    @FormUrlEncoded
    @POST(App.COMMON_URL)
    Call<JsonObject> getUpdateChillerClose(@Field("method") String method,
                                           @Field("ir_id") String ir_id);

    @FormUrlEncoded
    @POST(App.COMMON_URL)
    Call<JsonObject> getUpdatePrice(@Field("method") String method,
                                    @Field("sman_id") String sman_id);

    @FormUrlEncoded
    @POST(App.COMMON_URL)
    Call<JsonObject> getRejectChillerClose(@Field("method") String method,
                                           @Field("crf_id") String ir_id,
                                           @Field("fridge_id") String fridge_id);

    @GET(App.COMMON_URL)
    Call<JsonObject> getOrderDetail(@Query("method") String method,
                                    @Query("order_id") String orderId);

    @GET(App.COMMON_URL)
    Call<JsonObject> loadConfirm(@Query("method") String method,
                                 @Query("sub_loadid") String routeId);

    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postLoadConfirm(@Part("method") RequestBody data,
                                     @Part("sub_loadid") RequestBody loadId,
                                     @Part List<MultipartBody.Part> files);

    @GET(App.COMMON_URL)
    Call<JsonObject> getPricingList(@Query("method") String method,
                                    @Query("routeid") String routeId,
                                    @Query("agent_id") String agentId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getCustomerDiscountList(@Query("method") String method,
                                             @Query("routeId") String routeId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getRoutePromotion(@Query("method") String method,
                                       @Query("routeid") String routeId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getAgentPromotion(@Query("method") String method,
                                       @Query("cust_id") String agentId);

    @GET(App.COMMON_URL)
    Call<JsonObject> getDiscountList(@Query("method") String method,
                                     @Query("routeid") String routeId);

    @GET(App.COMMON_URL)
    Call<JsonObject> deleteDelivery(@Query("method") String method,
                                    @Query("delivery_id") String deliveryId);

    @Headers({
            "Accept: application/json", "Content-Type: application/json"
    })
    @POST(App.COMMON_POST_URL)
    Call<JsonObject> postInvoice(@Body JsonObject data);

    @GET(App.COMMON_MERCHENT_URL)
    Call<JsonObject> getPlanogramList(@Query("method") String method,
                                      @Query("routeid") String routeId,
                                      @Query("associated_salesman") String salesmanId);

    @GET(App.COMMON_MERCHENT_URL)
    Call<JsonObject> getSurveyTool(@Query("method") String method);

    @GET(App.COMMON_MERCHENT_URL)
    Call<JsonObject> getDeliveryMerchantList(@Query("method") String method,
                                             @Query("routeid") String routeId);

    @Multipart
    @POST(App.COMMON_MERCHENT_URL)
    Call<JsonObject> postPlanogramData(@Part("method") RequestBody data,
                                       @Part("route_id") RequestBody routeId,
                                       @Part("salesman_id") RequestBody salesmanId,
                                       @Part("customer_id") RequestBody customerId,
                                       @Part("distribution_tool_id") RequestBody distrubution,
                                       @Part("planogram_id") RequestBody planogram,
                                       @Part("description") RequestBody description,
                                       @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(App.COMMON_MERCHENT_URL)
    Call<JsonObject> postComplainData(@Part("method") RequestBody data,
                                      @Part("route_id") RequestBody routeId,
                                      @Part("salesman_id") RequestBody salesmanId,
                                      @Part("title") RequestBody title,
                                      @Part("description") RequestBody description,
                                      @Part("category") RequestBody category,
                                      @Part("item_id") RequestBody itemId,
                                      @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(App.COMMON_MERCHENT_URL)
    Call<JsonObject> postCompatitorData(@Part("method") RequestBody data,
                                        @Part("salesman_id") RequestBody salesman_id,
                                        @Part("company_name") RequestBody company_name,
                                        @Part("brand") RequestBody brand,
                                        @Part("item_name") RequestBody item_name,
                                        @Part("price") RequestBody price,
                                        @Part("note") RequestBody note,
                                        @Part("promotion") RequestBody Promotion,
                                        @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postAttendanceIn(@Part("method") RequestBody data,
                                      @Part("salesman_id") RequestBody salesman_id,
                                      @Part("route_id") RequestBody routeId,
                                      @Part("time_in") RequestBody timeIn,
                                      @Part("type") RequestBody type,
                                      @Part("latitude_in") RequestBody latitude,
                                      @Part("longitude_in") RequestBody longitude,
                                      @Part("attendance_date") RequestBody date,
                                      @Part MultipartBody.Part files);

    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postAttendanceOut(@Part("method") RequestBody data,
                                       @Part("salesman_id") RequestBody salesman_id,
                                       @Part("route_id") RequestBody routeId,
                                       @Part("time_out") RequestBody timeIn,
                                       @Part("type") RequestBody type,
                                       @Part("latitude_out") RequestBody latitude,
                                       @Part("longitude_out") RequestBody longitude,
                                       @Part("attendance_date") RequestBody date,
                                       @Part MultipartBody.Part files);

    @Multipart
    @POST(App.COMMON_MERCHENT_URL)
    Call<JsonObject> postFridgeData(@Part("method") RequestBody data,
                                    @Part("route_id") RequestBody route_id,
                                    @Part("salesman_id") RequestBody salesman_id,
                                    @Part("customer_id") RequestBody customer_id,
                                    @Part("have_fridge") RequestBody have_fridge,
                                    @Part("latitude") RequestBody latitude,
                                    @Part("longitude") RequestBody longitude,
                                    @Part("comments") RequestBody comments,
                                    @Part("complaint_type") RequestBody complaint_type,
                                    @Part("serial_no") RequestBody serial_no,
                                    @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postAddFridgeData(@Part("method") RequestBody data,
                                       @Part("depot_id") RequestBody depot_id,
                                       @Part("salesman_id") RequestBody salesman_id,
                                       @Part("customer_id") RequestBody customer_id,
                                       @Part("asset_no") RequestBody asset_no, @Part List<MultipartBody.Part> files);


    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postChillerData(@Part("method") RequestBody data,
                                     @Part("chiller_id") RequestBody chiller_id,
                                     @Part("depot_id") RequestBody depot_id,
                                     @Part("salesman_id") RequestBody salesman_id,
                                     @Part("route_id") RequestBody route_id,
                                     @Part("customer_id") RequestBody customer_id,
                                     @Part("owner_name") RequestBody ownername,
                                     @Part("contact_number") RequestBody contact_number,
                                     @Part("postal_address") RequestBody postal_address,
                                     @Part("landmark") RequestBody landmark,
                                     @Part("location") RequestBody location,
                                     @Part("outlet_type") RequestBody outlet_type,
                                     @Part("specify_if_other_type") RequestBody specify_if_other_type,
                                     @Part("existing_coolers") RequestBody existing_coolers,
                                     @Part("stock_share_with_competitor") RequestBody stock_share_with_competitor,
                                     @Part("outlet_weekly_sale_volume") RequestBody outlet_weekly_sale_volume,
                                     @Part("outlet_weekly_sales") RequestBody outlet_weekly_sales,
                                     @Part("chiller_size_requested") RequestBody chiller_size_requested,
                                     @Part("chiller_safty_grill") RequestBody chiller_safty_grill,
                                     @Part("display_location") RequestBody display_location,
                                     @Part("national_id") RequestBody national_id,
                                     @Part("password_photo") RequestBody password_photo,
                                     @Part("outlet_address_proof") RequestBody outlet_address_proof,
                                     @Part("outlet_stamp") RequestBody outlet_stamp,
                                     @Part("lc_letter") RequestBody lc_letter,
                                     @Part("trading_licence") RequestBody trading_licence,
                                     @Part("is_merchandiser") RequestBody isMerchandiser,
                                     @Part List<MultipartBody.Part> files, @Part MultipartBody.Part sign__customer_file);


    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postAddChillerData(@Part("method") RequestBody data,
                                        @Part("chiller_id") RequestBody chiller_id,
                                        @Part("depot_id") RequestBody depot_id,
                                        @Part("salesman_id") RequestBody salesman_id,
                                        @Part("route_id") RequestBody route_id,
                                        @Part("customer_id") RequestBody customer_id,
                                        @Part("owner_name") RequestBody ownername,
                                        @Part("serial_no") RequestBody serialno,
                                        @Part("contact_number") RequestBody contact_number,
                                        @Part("postal_address") RequestBody postal_address,
                                        @Part("landmark") RequestBody landmark,
                                        @Part("location") RequestBody location,
                                        @Part("outlet_type") RequestBody outlet_type,
                                        @Part("specify_if_other_type") RequestBody specify_if_other_type,
                                        @Part("existing_coolers") RequestBody existing_coolers,
                                        @Part("stock_share_with_competitor") RequestBody stock_share_with_competitor,
                                        @Part("outlet_weekly_sale_volume") RequestBody outlet_weekly_sale_volume,
                                        @Part("outlet_weekly_sales") RequestBody outlet_weekly_sales,
                                        @Part("chiller_size_requested") RequestBody chiller_size_requested,
                                        @Part("chiller_safty_grill") RequestBody chiller_safty_grill,
                                        @Part("display_location") RequestBody display_location,
                                        @Part("national_id") RequestBody national_id,
                                        @Part("password_photo") RequestBody password_photo,
                                        @Part("outlet_address_proof") RequestBody outlet_address_proof,
                                        @Part("outlet_stamp") RequestBody outlet_stamp,
                                        @Part("lc_letter") RequestBody lc_letter,
                                        @Part("trading_licence") RequestBody trading_licence,
                                        @Part("is_merchandiser") RequestBody isMerchandiser,
                                        @Part List<MultipartBody.Part> files, @Part MultipartBody.Part sign__customer_file,
                                        @Part MultipartBody.Part sign_sales__customer_file);


    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postAgreementData(@Part("method") RequestBody data,
                                       @Part("behaf_reciver_old_signature") RequestBody behaf_reciver_old_signature,
                                       @Part("ms") RequestBody ms,
                                       @Part("ms_of") RequestBody ms_of,
                                       @Part("address") RequestBody address,
                                       @Part("asset_number") RequestBody asset_number,
                                       @Part("serial_number") RequestBody serial_number,
                                       @Part("model_branding") RequestBody model_branding,
                                       @Part("behaf_hariss_name_contact") RequestBody behaf_hariss_name_contact,
                                       @Part("behaf_hariss_date") RequestBody behaf_hariss_date,
                                       @Part("behaf_reciver_name_contact") RequestBody behaf_reciver_name_contact,
                                       @Part("behaf_reciver_date") RequestBody behaf_reciver_date,
                                       @Part("presence_sales_name") RequestBody presence_sales_name,
                                       @Part("presence_sales_contact") RequestBody presence_sales_contact,
                                       @Part("presence_lc_name") RequestBody presence_lc_name,
                                       @Part("presence_lc_contact") RequestBody presence_lc_contact,
                                       @Part("presence_landloard_name") RequestBody presence_landloard_name,
                                       @Part("presence_landloard_contact") RequestBody presence_landloard_contact,
                                       @Part("salesman_id") RequestBody salesman_id,
                                       @Part("customer_id") RequestBody customer_id,
                                       @Part("fridge_id") RequestBody fridge_id,
                                       @Part("ir_id") RequestBody ir_id,
                                       @Part("crf_id") RequestBody crf_id,
                                       @Part MultipartBody.Part image, @Part MultipartBody.Part image1,
                                       @Part MultipartBody.Part image2, @Part MultipartBody.Part image3, @Part MultipartBody.Part image4,
                                       @Part MultipartBody.Part image5, @Part MultipartBody.Part image6, @Part MultipartBody.Part image7);


    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postChillerRequestData(@Part("method") RequestBody data,
                                            @Part("depot_id") RequestBody depot_id,
                                            @Part("salesman_id") RequestBody salesman_id,
                                            @Part("route_id") RequestBody route_id,
                                            @Part("customer_id") RequestBody customer_id,
                                            @Part("owner_name") RequestBody ownername,
                                            @Part("contact_number") RequestBody contact_number,
                                            @Part("landmark") RequestBody landmark,
                                            @Part("location") RequestBody location,
                                            @Part("outlet_type") RequestBody outlet_type,
                                            @Part("specify_if_other_type") RequestBody specify_if_other_type,
                                            @Part("existing_coolers") RequestBody existing_coolers,
                                            @Part("stock_share_with_competitor") RequestBody stock_share_with_competitor,
                                            @Part("outlet_weekly_sale_volume") RequestBody outlet_weekly_sale_volume,
                                            @Part("outlet_weekly_sales") RequestBody outlet_weekly_sales,
                                            @Part("asset_number") RequestBody asset_number,
                                            @Part("serial_number") RequestBody serial_number,
                                            @Part("type_of_the_machine") RequestBody type_of_the_machine,
                                            @Part("reason_for_withdrawal") RequestBody reason_for_withdrawal,
                                            @Part("history_of_the_outlet") RequestBody history_of_the_outlet,
                                            @Part("national_id") RequestBody national_id,
                                            @Part("password_photo") RequestBody password_photo,
                                            @Part("outlet_address_proof") RequestBody outlet_address_proof,
                                            @Part("outlet_stamp") RequestBody outlet_stamp,
                                            @Part("lc_letter") RequestBody lc_letter,
                                            @Part("trading_licence") RequestBody trading_licence,
                                            @Part List<MultipartBody.Part> files,
                                            @Part MultipartBody.Part sign__customer_file);

    @Multipart
    @POST(App.COMMON_MERCHENT_URL)
    Call<JsonObject> postCampaignData(@Part("method") RequestBody data,
                                      @Part("salesman_id") RequestBody salesmanId,
                                      @Part("campaign_id") RequestBody CampaignID,
                                      @Part("customer_id") RequestBody CustomerID,
                                      @Part("feedback") RequestBody Feedback,
                                      @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(App.COMMON_MERCHENT_URL)
    Call<JsonObject> postPromotionalData(@Part("method") RequestBody data,
                                         @Part("salesman_id") RequestBody salesmanId,
                                         @Part("customer_name") RequestBody name,
                                         @Part("phone") RequestBody phone,
                                         @Part("amount_spend") RequestBody amount,
                                         @Part("item_array") RequestBody itemId,
                                         @Part("invoice_id") RequestBody invoiceNo,
                                         @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(App.COMMON_MERCHENT_URL)
    Call<JsonObject> postAssetsData(@Part("method") RequestBody data,
                                    @Part("salesman_id") RequestBody salesmanId,
                                    @Part("asset_id") RequestBody CompanyName,
                                    @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(App.COMMON_MERCHENT_URL)
    Call<JsonObject> postDistImageData(@Part("method") RequestBody data,
                                       @Part("salesman_id") RequestBody salesmanId,
                                       @Part("tool_id") RequestBody CampaignID,
                                       @Part("customer_id") RequestBody CustomerID,
                                       @Part List<MultipartBody.Part> files);


    @Headers({
            "Accept: application/json", "Content-Type: application/json"
    })
    @POST(App.COMMON_MERCHENT_POST_URL)
    Call<JsonObject> postMerchandising(@Body JsonObject data);

    @GET(App.COMMON_URL)
    Call<JsonObject> callChangePassword(@Query("method") String method,
                                        @Query("old_password") String username,
                                        @Query("new_password") String password,
                                        @Query("salesman_id") String token);

    @Multipart
    @POST(App.COMMON_URL)
    Call<JsonObject> postServiceVisitData(@Part("method") RequestBody data,
                                          @Part("ticket_type") RequestBody ticket_type,
                                          @Part("ticket_no") RequestBody ticket_no,
                                          @Part("time_in") RequestBody time_in,
                                          @Part("time_out") RequestBody time_out,
                                          @Part("model_no") RequestBody model_no,
                                          @Part("asset_no") RequestBody asset_no,
                                          @Part("serial_no") RequestBody serial_no,
                                          @Part("branding") RequestBody branding,
                                          @Part("outlet_name") RequestBody outlet_name,
                                          @Part("owner_name") RequestBody owner_name,
                                          @Part("landmark") RequestBody landmark,
                                          @Part("location") RequestBody location,
                                          @Part("town_village") RequestBody town_village,
                                          @Part("contact_no") RequestBody contact_no,
                                          @Part("longitude") RequestBody longitude,
                                          @Part("latitude") RequestBody latitude,
                                          @Part("technician_id") RequestBody technician_id,
                                          @Part("is_machine_in_working") RequestBody is_machine_in_working,
                                          @Part("cleanliness") RequestBody cleanliness,
                                          @Part("condensor_coil_cleand") RequestBody condensor_coil_cleand,
                                          @Part("gaskets") RequestBody gaskets,
                                          @Part("light_working") RequestBody light_working,
                                          @Part("branding_no") RequestBody branding_no,
                                          @Part("propper_ventilation_available") RequestBody propper_ventilation_available,
                                          @Part("leveling_positioning") RequestBody leveling_positioning,
                                          @Part("stock_availability_in") RequestBody stock_availability_in,
                                          @Part("work_done_type") RequestBody complaint_type,
                                          @Part("comment") RequestBody comment,
                                          @Part("any_dispute") RequestBody any_dispute,
                                          @Part("contact_no2") RequestBody contacNo2,
                                          @Part("contact_person") RequestBody ccontactPerson,
                                          @Part("technical_behavior") RequestBody techRating,
                                          @Part("service_quality") RequestBody qualityRating,
                                          @Part("current_voltage") RequestBody current_voltage,
                                          @Part("amps") RequestBody amps,
                                          @Part("cabin_temperature") RequestBody cabin_temperature,
                                          @Part("work_status") RequestBody work_status,
                                          @Part("wrok_status_pending_reson") RequestBody wrok_status_pending_reson,
                                          @Part("spare_request") RequestBody spare_request,
                                          @Part("spare_details") RequestBody spareDetail,
                                          @Part("nature_of_call_id") RequestBody natureOfCall,
                                          @Part("ct_status") RequestBody ct_status,
                                          @Part("district") RequestBody district,
                                          @Part("cts_comment") RequestBody ctcComment,
                                          @Part("pending_other_comment") RequestBody otherComment,
                                          @Part MultipartBody.Part condition, @Part MultipartBody.Part cleanles,
                                          @Part MultipartBody.Part coil, @Part MultipartBody.Part gasket, @Part MultipartBody.Part brandingImage,
                                          @Part MultipartBody.Part lighting, @Part MultipartBody.Part ventilation, @Part MultipartBody.Part leveling,
                                          @Part MultipartBody.Part stock,
                                          @Part MultipartBody.Part custSignature,
                                          @Part MultipartBody.Part detaill1,
                                          @Part MultipartBody.Part dettail2,
                                          @Part MultipartBody.Part scanImage,
                                          @Part MultipartBody.Part coolerImage,
                                          @Part MultipartBody.Part coolerImage2);

}
