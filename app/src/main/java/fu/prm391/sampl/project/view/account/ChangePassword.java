package fu.prm391.sampl.project.view.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fu.prm391.sampl.project.R;
import fu.prm391.sampl.project.helper.PreferencesHelpers;
import fu.prm391.sampl.project.model.user.change_password.ChangePasswordRequest;
import fu.prm391.sampl.project.model.user.change_password.ChangePasswordResponse;
import fu.prm391.sampl.project.remote.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    private TextView txtBack;
    private EditText oldPass, newPass, rePass;
    private Button btnSave;
    private String token;
    private ImageView btnShowOldPass, btnShowNewPass, btnShowRepass;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        context=this;

        token = PreferencesHelpers.loadStringData(ChangePassword.this, "token");

        changePasswordAction();
        showHidePass();
        backAction();
    }

    private void changePasswordAction() {
        oldPass = findViewById(R.id.et_old_pass);
        newPass = findViewById(R.id.et_new_pass);
        rePass = findViewById(R.id.et_re_pass);
        btnSave = findViewById(R.id.btnSaveChangePass);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave.setEnabled(false);
                if (TextUtils.isEmpty(oldPass.getText().toString().trim())
                        || TextUtils.isEmpty(newPass.getText().toString().trim())
                        || TextUtils.isEmpty(rePass.getText().toString().trim())) {
                    Toast.makeText(ChangePassword.this, "请确保填写完整", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                } else if (!newPass.getText().toString().equals(rePass.getText().toString())) {
                    Toast.makeText(ChangePassword.this, "新的密码不一致", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                } else {
                    // proceed change password
                    changePassword();
                }
            }
        });
    }

    private void changePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword(oldPass.getText().toString());
        changePasswordRequest.setPassword(newPass.getText().toString());

        Call<ChangePasswordResponse> changePasswordResponseCall = ApiClient
                .getUserService()
                .changePassword(token, newPass.getText().toString(),oldPass.getText().toString());
        changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    PreferencesHelpers.removeSinglePreference(context,"token");
                    Intent intent = new Intent(context, Login.class);
                    startActivity(intent);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(ChangePassword.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        Toast.makeText(ChangePassword.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    btnSave.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
            }
        });
    }

    private void backAction() {
        txtBack = findViewById(R.id.txtBackChangePass);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showHidePass() {
        btnShowOldPass = findViewById(R.id.btnShowOldPassChangePass);
        btnShowOldPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    ((ImageView) (view)).setImageResource(R.drawable.ic_visibility_off);
                    oldPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ((ImageView) (view)).setImageResource(R.drawable.ic_visibility);
                    oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                oldPass.setSelection(oldPass.length());
            }
        });

        btnShowNewPass = findViewById(R.id.btnShowNewPassChangePass);
        btnShowNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    ((ImageView) (view)).setImageResource(R.drawable.ic_visibility_off);
                    newPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ((ImageView) (view)).setImageResource(R.drawable.ic_visibility);
                    newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                newPass.setSelection(newPass.length());
            }
        });

        btnShowRepass = findViewById(R.id.btnShowRePassChangePass);
        btnShowRepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rePass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    ((ImageView) (view)).setImageResource(R.drawable.ic_visibility_off);
                    rePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ((ImageView) (view)).setImageResource(R.drawable.ic_visibility);
                    rePass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                rePass.setSelection(rePass.length());
            }
        });
    }
}