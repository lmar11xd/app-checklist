package com.smartory.network;

import com.smartory.models.APKVersionControl;
import com.smartory.models.CheckListModel;
import com.smartory.models.CreateLocationModel;
import com.smartory.models.Credentials;
import com.smartory.models.CredentialsPinCode;
import com.smartory.models.GetCheckListModel;
import com.smartory.models.GetLevObsModel;
import com.smartory.models.GetLocationCreateModel;
import com.smartory.models.GetPatchValidateModel;
import com.smartory.models.ItemCheckListModel;
import com.smartory.models.LocationModel;
import com.smartory.models.NonComplianceLifting;
import com.smartory.models.OperacionModel;
import com.smartory.models.PaginationModel;
import com.smartory.models.PatchItemCheckListModel;
import com.smartory.models.PatchLevItemModel;
import com.smartory.models.PatchValidarModel;
import com.smartory.models.PostItemCheckListModel;
import com.smartory.models.ResponseImageItemChecklist;
import com.smartory.models.ResponseImageLigthNoCompliance;
import com.smartory.models.ResponseLevItemModel;
import com.smartory.models.ResponsePostLigth;
import com.smartory.models.SendState;
import com.smartory.models.TractoModel;

import com.smartory.models.operations.CarrierByOperation;
import com.smartory.models.operations.Route;
import com.smartory.models.operations.SupervisorByOperation;
import com.smartory.models.operations.InspectorByOperation;
import com.smartory.models.operations.DriverByOperation;
import com.smartory.models.operations.TransportUnitByOperation;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InterfaceAPI {
    @POST("authentication/login/")
    Call<Credentials> loginWithCredentials(@Body Credentials user);

    @POST("authentication/authenticate/first-stage/")
    Call<Credentials> loginfirstStage(@Body Credentials user);

    @POST("authentication/authenticate/second-stage/{user_id}/")
    Call<Credentials> loginsecondStage(@Body CredentialsPinCode user, @Path("user_id") int user_id);

    @GET("checklist/checklist_pdf/{idCheckList}/")
    Call<ResponseBody> pdfCheckList(@Header("authorization") String auth, @Path("idCheckList") String idCheckList);

    @GET("authentication/current-user/")
    Call<Credentials> getType(@Header("authorization") String auth);

    @GET ("operations/operations/list/")
    Call<List<OperacionModel>> getOperations(@Header("authorization") String auth,
                                             @Query(value = "updated__gte", encoded = true) String updated__gte);

    @GET("operations/carrier_by_operation/")
    Call <List<CarrierByOperation>> getCarriers(@Header("authorization") String auth,
                                                @Query(value = "updated__gte", encoded = true) String updated__gte);

    @GET("operations/supervisors_by_operation/")
    Call <List<SupervisorByOperation>> getSupervisors(@Header("authorization") String auth,
                                                      @Query(value = "updated__gte", encoded = true) String updated__gte);

    @GET("operations/inspector_by_operation/")
    Call <List<InspectorByOperation>> getInspectors(@Header("authorization") String auth,
                                                    @Query(value = "updated__gte", encoded = true) String updated__gte);

    @GET("operations/operations/routes/")
    Call<List<Route>> getRoute(@Header("authorization") String auth);

    @GET("operations/drivers_by_operation/")
    Call<List<DriverByOperation>> getDriver(@Header("authorization") String auth,
                                            @Query(value = "updated__gte", encoded = true) String updated__gte);

    @GET("operations/transport_unit_by_operation/")
    Call <List<TransportUnitByOperation>> getTranportUnit(@Header("authorization") String auth,
                                                          @Query(value = "updated__gte", encoded = true) String updated__gte);

    @GET ("checklist/locations/")
    Call<List<LocationModel>> getLocation(@Header("authorization") String auth);

    @POST ("checklist/locations/")
    Call<GetLocationCreateModel> postLocation(@Header("authorization") String auth, @Body CreateLocationModel locationModel);

    @GET("checklist/checklist/")
    Call<List<GetCheckListModel>> getCheckList(@Header("authorization") String aut,
                                               @Query(value = "datetime__gte", encoded = true) String datetime__gte,
                                               @Query(value = "datetime__lte", encoded = true) String datetime__lte,
                                               @Query(value = "updated__gte", encoded = true) String updated__gte);

    @POST("checklist/checklist/")
    Call<CheckListModel> postCheckList(@Header("authorization") String auth, @Body CheckListModel checkListModel);

    @PATCH("checklist/checklist/{idItem}/")
    Call<GetPatchValidateModel> patchValidate(@Header("authorization")String auth, @Path("idItem") int idItem, @Body PatchValidarModel patchLevItemModel);

    @GET ("checklist/checklist-items/")
    Call<List<ItemCheckListModel>> getCheckListItem(@Header("authorization") String token, @Query(value = "updated__gte", encoded = true) String updated__gte);

    @POST("checklist/checklist-section-items/")
    Call<PostItemCheckListModel> postItemCheckList(@Header("authorization") String auth, @Body PostItemCheckListModel itemCheckListModel);

    @GET ("checklist/checklist-section-item-image/")
    Call<List<ResponseImageItemChecklist>> getItemImageCheckList(@Header("authorization") String auth);


    @GET ("checklist/non-compliance-lifting-image-mobile/")
    Call<List<ResponseImageItemChecklist>> getItemImageLiftingMobile(@Header("authorization") String auth);

    @Multipart
    @POST("checklist/checklist-section-item-image/")
    Call<ResponseImageItemChecklist> postItemImageCheckList(@Header("authorization") String auth,
                                                            @Part MultipartBody.Part image,
                                                            @Part("label") RequestBody label,
                                                            @Part("comment") RequestBody comment,
                                                            @Part("gps_longitude") RequestBody gps_longitude,
                                                            @Part("gps_latitude") RequestBody gps_latitude,
                                                            @Part("datetime") RequestBody datetime,
                                                            @Part("checklist_section_item") RequestBody checklist_section_item
    );

    @Multipart
    @POST("checklist/non-compliance-lifting-image/")
    Call<ResponseImageLigthNoCompliance> postItemImageLifting(@Header("authorization") String auth,
                                                              @Part MultipartBody.Part image,
                                                              @Part("label") RequestBody label,
                                                              @Part("comment") RequestBody comment,
                                                              @Part("gps_longitude") RequestBody gps_longitude,
                                                              @Part("gps_latitude") RequestBody gps_latitude,
                                                              @Part("datetime") RequestBody datetime,
                                                              @Part("non_compliance_lifting") RequestBody non_compliance_lifting
    );

    @GET("checklist/checklist-section-items/{idCheckList}")
    Call<List<ItemCheckListModel>> getCheckListItemByCheckList(@Header("authorization") String auth,
                                                               @Path("idCheckList") String idCheckList,
                                                               @Query(value = "updated__gte", encoded = true) String updated__gte);

    @GET("checklist/checklist-section-items/")
    Call<List<ItemCheckListModel>> getCheckListItemSections(@Header("authorization") String auth,
                                                            @Query(value = "updated__gte", encoded = true) String updated__gte);

    @GET ("checklist/non_compliance/")
    Call<List<GetLevObsModel>> getLevObs(@Header("authorization") String auth);


    @GET ("checklist/apk-version/")
    Call<List<APKVersionControl>> getAPKChecklist(@Header("authorization") String auth, @Query(value = "is_active") boolean is_active);


    @PUT("checklist/checklist_section_items/{idItem}/")
    Call<PostItemCheckListModel> putItem(@Header("authorization")String auth, @Path("idItem") String idItem, @Body PostItemCheckListModel itemCheckListModel);

    @PATCH("checklist/checklist_section_items/{idItem}/")
    Call<PostItemCheckListModel> patchItem(@Header("authorization")String auth, @Path("idItem") String idItem, @Body PatchItemCheckListModel itemCheckListModel);

    @PATCH("checklist/checklist_section_items/{id}/")
    Call<ResponseLevItemModel> patchItem(@Header("authorization")String auth, @Path("id") String id, @Body PatchLevItemModel patchLevItemModel);

    @PATCH("checklist/non_compliance_detail/{idNonCompliance}/")
    Call<ResponseLevItemModel> patchSectionItem(@Header("authorization")String auth, @Path("idNonCompliance") String id, @Body SendState sendState);

    @POST("checklist/non-compliance-lifting/")
    Call<ResponsePostLigth> postNonComplianceLifting(@Header("authorization")String auth, @Body NonComplianceLifting nonComplianceLifting);

    @PATCH("checklist/non-compliance-lifting/{id}/")
    Call<ResponsePostLigth> patchNonComplianceLifting(@Header("authorization")String auth, @Path("id") String id, @Body SendState nonComplianceLifting);

    @GET("checklist/non-compliance-lifting/")
    Call<List<ResponsePostLigth>> getNonComplianceLifting(@Header("authorization")String auth, @Query(value = "non_compliance") String non_compliance
            , @Query(value = "state") String state);

    @GET("transport-units/tractos/all/")
    Call<List<TractoModel>> getTractoByPlate(@Header("authorization") String auth, @Query(value = "search", encoded = true) String plate);

    @GET("checklist/checklist/")
    Call<PaginationModel> getLastCheckListByTransportUnit(@Header("authorization") String aut,
                                                                                   @Query(value = "transport_unit", encoded = true) int transport_unit,
                                                                                   @Query(value = "limit", encoded = true) int limit);
}
