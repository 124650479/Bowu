package fu.prm391.sampl.project.remote.service;

import fu.prm391.sampl.project.model.address.create_new_address.CreateNewAddressRequest;
import fu.prm391.sampl.project.model.address.create_new_address.CreateNewAddressResponse;
import fu.prm391.sampl.project.model.address.delete_address.DeleteAddressRequest;
import fu.prm391.sampl.project.model.address.delete_address.DeleteAddressResponse;
import fu.prm391.sampl.project.model.address.get_all_address.GetAllAddressResponse;
import fu.prm391.sampl.project.model.address.get_district.GetDistrictResponse;
import fu.prm391.sampl.project.model.address.get_information_address.GetInformationAddressResponse;
import fu.prm391.sampl.project.model.address.get_province.GetProvinceResponse;
import fu.prm391.sampl.project.model.address.get_ward.GetWardResponse;
import fu.prm391.sampl.project.model.address.update_address.UpdateAddressRequest;
import fu.prm391.sampl.project.model.address.update_address.UpdateAddressResponse;
import fu.prm391.sampl.project.model.address.update_default.UpdateDefaultAddressRequest;
import fu.prm391.sampl.project.model.address.update_default.UpdateDefaultAddressResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AddressService {

    @GET("order/get_address")
    Call<GetAllAddressResponse> getAllAddress(@Header("token") String token);

    @PATCH("address/update-default")
    Call<UpdateDefaultAddressResponse> updateDefaultAddress(@Header("Authorization") String token, @Body UpdateDefaultAddressRequest request);

    @GET("address/get_provinces")
    Call<GetProvinceResponse> getProvinceAddress();

    @GET("address/get_districts")
    Call<GetDistrictResponse> getDistrictAddress(@Query("proid") int proid);

    @GET("address/get_wards")
    Call<GetWardResponse> getWardAddress(@Query("proid") int proid,
                                         @Query("disid") int disid);

    @POST("address/user/add")
    Call<CreateNewAddressResponse> createNewAddress(@Header("token") String token,
                                                    @Body CreateNewAddressRequest createNewAddressRequest);

    @HTTP(method = "DELETE", path = "address/delete", hasBody = true)
    Call<DeleteAddressResponse> deleteAddress(@Header("Authorization") String token, @Body DeleteAddressRequest deleteAddressRequest);

    @GET("address/get")
    Call<GetInformationAddressResponse> getInformationAddress(@Header("Authorization") String token, @Query("id") int id);

    @PUT("address/update")
    Call<UpdateAddressResponse> updateAddress(@Header("Authorization") String token, @Body UpdateAddressRequest updateAddressRequest);
}
