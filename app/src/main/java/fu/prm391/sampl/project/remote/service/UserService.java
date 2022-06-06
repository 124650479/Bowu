package fu.prm391.sampl.project.remote.service;

import fu.prm391.sampl.project.model.user.UpdateUserInfoRequest;
import fu.prm391.sampl.project.model.user.UpdateUserInfoResponse;
import fu.prm391.sampl.project.model.user.User;
import fu.prm391.sampl.project.model.user.active_account.ActiveAccountRequest;
import fu.prm391.sampl.project.model.user.active_account.ActiveAccountResponse;
import fu.prm391.sampl.project.model.user.change_password.ChangePasswordRequest;
import fu.prm391.sampl.project.model.user.change_password.ChangePasswordResponse;
import fu.prm391.sampl.project.model.user.forgot_password.ForgotPassRequest;
import fu.prm391.sampl.project.model.user.forgot_password.ForgotPassResponse;
import fu.prm391.sampl.project.model.user.login.LoginRequest;
import fu.prm391.sampl.project.model.user.login.LoginResponse;
import fu.prm391.sampl.project.model.user.register.RegisterRequest;
import fu.prm391.sampl.project.model.user.register.RegisterResponse;
import fu.prm391.sampl.project.model.user.reset_password.ResetPassRequest;
import fu.prm391.sampl.project.model.user.reset_password.ResetPassResponse;
import fu.prm391.sampl.project.model.user.UserResponse;
import fu.prm391.sampl.project.model.user.send_email_active_account.EmailActiveAccountResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {

    @POST("/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("/check_token")
    Call<LoginResponse> check_token(@Header("token") String token,
                                    @Query("jwt")String jwt);

    @POST("/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);


    @GET("user/get_user")
    Call<UserResponse> getUserInformation(@Header("token") String token);

    @POST("user/update_user")
    Call<UpdateUserInfoResponse> updateUserInformation(@Header("token") String token,
                                                       @Body User user);

    @POST("user/change_password")
    Call<ChangePasswordResponse> changePassword(@Header("token") String token,
                                                @Query("newpwd") String pwdn,
                                                @Query("oldpwd") String pwdo);
}
