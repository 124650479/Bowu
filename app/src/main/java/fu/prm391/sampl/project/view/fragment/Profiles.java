package fu.prm391.sampl.project.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import fu.prm391.sampl.project.R;
import fu.prm391.sampl.project.helper.PreferencesHelpers;
import fu.prm391.sampl.project.model.user.User;
import fu.prm391.sampl.project.model.user.UserResponse;
import fu.prm391.sampl.project.remote.ApiClient;
import fu.prm391.sampl.project.view.account.ChangePassword;
import fu.prm391.sampl.project.view.account.Login;
import fu.prm391.sampl.project.view.address.ProfileShippingAddress;
import fu.prm391.sampl.project.view.favorite_product.MyFavoriteProduct;
import fu.prm391.sampl.project.view.order.MyOrderHistory;
import fu.prm391.sampl.project.view.profiles.EditProfiles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profiles extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View viewLogOut, viewMyHistoryOrders, viewShippingAddress;
    private View viewMyFavorites;
    private Button btnEditProfiles;
    private TextView labelVerified, profilesName, emailProfiles;
    private ImageView verifyImage, imageProfiles;
    private String token = "";
    private User user;
    private CardView loadingCard;
    private View viewChangePassword;

    public Profiles() {
    }

    public static Profiles newInstance(String param1, String param2) {
        Profiles fragment = new Profiles();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callAPIProfiles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profiles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        token = PreferencesHelpers.loadStringData(getContext(), "token");

        //检查登陆状态
        checkLoginUser();

        emailProfiles = view.findViewById(R.id.txtEmailProfiles);
        profilesName = view.findViewById(R.id.txtNameProfiles);
        imageProfiles = view.findViewById(R.id.imageProfiles);
        btnEditProfiles = view.findViewById(R.id.btnEditProfiles);
        viewShippingAddress = view.findViewById(R.id.viewShippingAddressProfiles);
        viewLogOut = view.findViewById(R.id.viewLogoutProfile);

        loadingCard = view.findViewById(R.id.loadingCardProfile);
        loadingCard.setVisibility(View.VISIBLE);

        callAPIProfiles();
        editProfileAction();
        moveToOtherActivities(view);
        logoutAction(view);


    }

    private void checkLoginUser() {
        if (token == "") {
            startActivity(new Intent(getContext(), Login.class));
            getActivity().finish();
        }
    }

    private void logoutAction(View view) {
        viewLogOut = view.findViewById(R.id.viewLogoutProfile);
        viewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlert = new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_App_MaterialAlertDialog);
                materialAlert.setTitle("退出账户");
                materialAlert.setMessage("确定要退出您的账号");
                materialAlert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // move to home navigation
                        BottomNavigationView bottomNavigationView;
                        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setSelectedItemId(R.id.home2);
                        PreferencesHelpers.removeSinglePreference(getContext(), "token");
                    }
                });
                materialAlert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                materialAlert.show();
            }
        });
    }

    private void callAPIProfiles() {
        //加载个人信息
        Call<UserResponse> userResponseCall = ApiClient.getUserService().getUserInformation(token);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    loadingCard.setVisibility(View.GONE);
                    user = response.body().getData();
                    loadUserInfoToScreen();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }

    private void loadUserInfoToScreen() {
        if (user.getUsername() == null) {
            profilesName.setText("用户");
        } else {
            profilesName.setText(user.getUsername());
        }
        if (user.getAvatar() != null) {
            Picasso.get().load(user.getAvatar()).fit().into(imageProfiles);
        } else {
            imageProfiles.setImageResource(R.drawable.icon_person);
        }
        emailProfiles.setText(user.getEmail());
    }

    private void moveToOtherActivities(View view) {
        // 收货地址
        viewShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileShippingAddress.class);
                startActivity(intent);
            }
        });

        // 历史订单
        viewMyHistoryOrders = view.findViewById(R.id.viewMyOrdersHistoryProfiles);
        viewMyHistoryOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyOrderHistory.class));
            }
        });

        // 收藏
        viewMyFavorites = view.findViewById(R.id.viewMyFavoritiesProfiles);
        viewMyFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyFavoriteProduct.class));
            }
        });

        viewChangePassword = view.findViewById(R.id.viewChangePassword);
        viewChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChangePassword.class));
            }
        });
    }

    private void editProfileAction() {
        btnEditProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfiles.class);
                intent.putExtra("userInfo", user);
                startActivity(intent);
            }
        });
    }
}