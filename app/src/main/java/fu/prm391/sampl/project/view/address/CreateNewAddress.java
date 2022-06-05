package fu.prm391.sampl.project.view.address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fu.prm391.sampl.project.R;
import fu.prm391.sampl.project.adapter.address.DistrictAdapter;
import fu.prm391.sampl.project.adapter.address.ProvinceAdapter;
import fu.prm391.sampl.project.adapter.address.WardAdapter;
import fu.prm391.sampl.project.helper.PreferencesHelpers;
import fu.prm391.sampl.project.model.address.create_new_address.CreateNewAddressRequest;
import fu.prm391.sampl.project.model.address.create_new_address.CreateNewAddressResponse;
import fu.prm391.sampl.project.model.address.get_district.District;
import fu.prm391.sampl.project.model.address.get_district.GetDistrictResponse;
import fu.prm391.sampl.project.model.address.get_province.GetProvinceResponse;
import fu.prm391.sampl.project.model.address.get_province.Province;
import fu.prm391.sampl.project.model.address.get_ward.GetWardResponse;
import fu.prm391.sampl.project.model.address.get_ward.Ward;
import fu.prm391.sampl.project.model.address.update_address.UpdateAddressRequest;
import fu.prm391.sampl.project.model.address.update_address.UpdateAddressResponse;
import fu.prm391.sampl.project.remote.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewAddress extends AppCompatActivity {

    private Button btnCnaBack;
    private Button btnCnaSubmit;

    private Spinner spinnerCnaProvince;
    private Spinner spinnerCnaDistrict;
    private Spinner spinnerCnaWard;

    private List<Province> provinceList = new ArrayList<>();
    private List<District> districtList = new ArrayList<>();
    private List<Ward> wardList = new ArrayList<>();

    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextDetail;

    private TextView title;

    private int provinceId;
    private int districtId;
    private int wardId;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_address);

        token = PreferencesHelpers.loadStringData(CreateNewAddress.this, "token");

        btnCnaBack = findViewById(R.id.btnCnaBack);
        btnCnaSubmit = findViewById(R.id.btnCnaSubmit);

        spinnerCnaProvince = findViewById(R.id.spinnerCnaProvince);
        spinnerCnaDistrict = findViewById(R.id.spinnerCnaDistrict);
        spinnerCnaWard = findViewById(R.id.spinnerCnaWard);

        editTextName = findViewById(R.id.editText_cna_name);
        editTextPhone = findViewById(R.id.editText_cna_phone);
        editTextDetail = findViewById(R.id.editText_cna_detail);

        title = findViewById(R.id.txtCnaTitle);

        provinceId = 0;
        districtId = 0;
        wardId = 0;

        setInitLayout();

        setDefaultProvince();
        setDefaultDistrict();
        setDefaultWard();

        loadSpinnerProvince();

        setEventChangeProvince();
        setEventChangeDistrict();
        setEventChangeWard();

        setEventBtnBack();
        setEventBtnSubmit();
    }

    private void setInitLayout() {
        if (isUpdateAddress()) {
            Intent intent = getIntent();
            title.setText("Update Address");
            btnCnaSubmit.setText("Update");

            editTextName.setText(intent.getStringExtra("fullName"));
            editTextPhone.setText(intent.getStringExtra("phone"));
            editTextDetail.setText(intent.getStringExtra("detail"));
        }
    }

    private boolean isUpdateAddress() {
        Intent intent = getIntent();
        return intent.hasExtra("updateAddress");
    }

    private void setEventBtnBack() {
        btnCnaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setEventBtnSubmit() {
        btnCnaSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String detail = editTextDetail.getText().toString().trim();

                if (name.equals("")) {
                    Toast.makeText(CreateNewAddress.this, "请输入名字", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.equals("") || phone.length() != 11) {
                    Toast.makeText(CreateNewAddress.this, "请输入规范的电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (provinceId == 0) {
                    Toast.makeText(CreateNewAddress.this, "请选择省份", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (districtId == 0) {
                    Toast.makeText(CreateNewAddress.this, "请选择城市", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (wardId == 0) {
                    Toast.makeText(CreateNewAddress.this, "请选择区域", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (detail.equals("") || detail.length() < 2 || detail.length() > 100) {
                    Toast.makeText(CreateNewAddress.this, "请填写详细地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                btnCnaSubmit.setEnabled(false);
                if (isUpdateAddress()) {
                    Intent intent = getIntent();

                    UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();

                    updateAddressRequest.setId(intent.getIntExtra("id", 0));
                    updateAddressRequest.setFullName(name);
                    updateAddressRequest.setPhone(phone);
                    updateAddressRequest.setProvinceId(provinceId);
                    updateAddressRequest.setDistrictId(districtId);
                    updateAddressRequest.setWardId(wardId);
                    updateAddressRequest.setDetail(detail);

                    Call<UpdateAddressResponse> call = ApiClient.getAddressService().updateAddress(token, updateAddressRequest);
                    call.enqueue(new Callback<UpdateAddressResponse>() {
                        @Override
                        public void onResponse(Call<UpdateAddressResponse> call, Response<UpdateAddressResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(CreateNewAddress.this, "更新成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateAddressResponse> call, Throwable t) {

                        }
                    });
                } else {
                    CreateNewAddressRequest createNewAddressRequest = new CreateNewAddressRequest();

                    createNewAddressRequest.setFullName(name);
                    createNewAddressRequest.setPhone(phone);
                    createNewAddressRequest.setDetail(detail);
                    createNewAddressRequest.setProvinceId(provinceId);
                    createNewAddressRequest.setDistrictId(districtId);
                    createNewAddressRequest.setWardId(wardId);

                    Call<CreateNewAddressResponse> call = ApiClient.getAddressService().createNewAddress(
                            token,
                            createNewAddressRequest);
                    call.enqueue(new Callback<CreateNewAddressResponse>() {
                        @Override
                        public void onResponse(Call<CreateNewAddressResponse> call, Response<CreateNewAddressResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(CreateNewAddress.this, "添加地址成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CreateNewAddress.this, "错误", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CreateNewAddressResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void setEventChangeProvince() {
        spinnerCnaProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    setDefaultDistrict();
                    provinceId = 0;
                } else {
                    loadSpinnerDistrict(provinceList.get(i).getId());
                    provinceId = provinceList.get(i).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setEventChangeDistrict() {
        spinnerCnaDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    setDefaultWard();
                    districtId = 0;
                } else {
                    loadSpinnerWard(districtList.get(i).getId());
                    districtId = districtList.get(i).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setEventChangeWard() {
        spinnerCnaWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    wardId = 0;
                } else {
                    wardId = wardList.get(i).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadSpinnerProvince() {
        Call<GetProvinceResponse> call = ApiClient.getAddressService().getProvinceAddress();
        call.enqueue(new Callback<GetProvinceResponse>() {
            @Override
            public void onResponse(Call<GetProvinceResponse> call, Response<GetProvinceResponse> response) {
                if (response.isSuccessful()) {
                    List<Province> list = response.body().getList();
                    list.add(0, new Province(0, "-- 选择省份 --", ""));
                    provinceList = list;
                    ProvinceAdapter provinceAdapter = new ProvinceAdapter(CreateNewAddress.this, R.layout.item_spinner_new_address, list);
                    spinnerCnaProvince.setAdapter(provinceAdapter);
                    spinnerCnaProvince.setSelection(0);
                }
            }

            @Override
            public void onFailure(Call<GetProvinceResponse> call, Throwable t) {

            }
        });
    }

    private void loadSpinnerDistrict(int id) {
        Call<GetDistrictResponse> call = ApiClient.getAddressService().getDistrictAddress(id);
        call.enqueue(new Callback<GetDistrictResponse>() {
            @Override
            public void onResponse(Call<GetDistrictResponse> call, Response<GetDistrictResponse> response) {
                if (response.isSuccessful()) {
                    List<District> list = response.body().getList();
                    list.add(0, new District(0, "-- 选择城市 --", ""));

                    districtList = list;

                    DistrictAdapter districtAdapter = new DistrictAdapter(CreateNewAddress.this, R.layout.item_spinner_new_address, list);
                    spinnerCnaDistrict.setAdapter(districtAdapter);

                    spinnerCnaDistrict.setSelection(0);
                }
            }

            @Override
            public void onFailure(Call<GetDistrictResponse> call, Throwable t) {

            }
        });
    }

    private void loadSpinnerWard(int id) {
        Call<GetWardResponse> call = ApiClient.getAddressService().getWardAddress(provinceId,id);
        call.enqueue(new Callback<GetWardResponse>() {
            @Override
            public void onResponse(Call<GetWardResponse> call, Response<GetWardResponse> response) {
                if (response.isSuccessful()) {
                    List<Ward> list = response.body().getList();
                    list.add(0, new Ward(0, "-- 选择地区 --", ""));
                    wardList = list;
                    WardAdapter wardAdapter = new WardAdapter(CreateNewAddress.this, R.layout.item_spinner_new_address, list);
                    spinnerCnaWard.setAdapter(wardAdapter);
                    spinnerCnaWard.setSelection(0);
                }
            }

            @Override
            public void onFailure(Call<GetWardResponse> call, Throwable t) {

            }
        });
    }

    private void setDefaultProvince() {
        List<Province> list = new ArrayList<>();
        list.add(new Province(0, "-- 选择省份 --", ""));

        spinnerCnaProvince.setAdapter(new ProvinceAdapter(CreateNewAddress.this, R.layout.item_spinner_new_address, list));
        spinnerCnaProvince.setSelection(0);
    }

    private void setDefaultDistrict() {
        List<District> list = new ArrayList<>();
        list.add(new District(0, "-- 选择城市 --", ""));

        spinnerCnaDistrict.setAdapter(new DistrictAdapter(CreateNewAddress.this, R.layout.item_spinner_new_address, list));
        spinnerCnaDistrict.setSelection(0);
    }

    private void setDefaultWard() {
        List<Ward> list = new ArrayList<>();
        list.add(new Ward(0, "-- 选择区域 --", ""));

        spinnerCnaWard.setAdapter(new WardAdapter(CreateNewAddress.this, R.layout.item_spinner_new_address, list));
        spinnerCnaWard.setSelection(0);
    }
}