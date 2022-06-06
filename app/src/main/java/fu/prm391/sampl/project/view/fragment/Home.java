package fu.prm391.sampl.project.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import fu.prm391.sampl.project.R;
import fu.prm391.sampl.project.adapter.product.ProductTrendingItemAdapter;
import fu.prm391.sampl.project.helper.PreferencesHelpers;
import fu.prm391.sampl.project.model.category.Category;
import fu.prm391.sampl.project.adapter.category.CategoryTop4Adapter;
import fu.prm391.sampl.project.model.category.CategoryResponse;
import fu.prm391.sampl.project.model.product.Product;
import fu.prm391.sampl.project.model.product.get_list_product.ProductListResponse;

//此类为后台服务器类
import fu.prm391.sampl.project.model.user.login.LoginResponse;
import fu.prm391.sampl.project.remote.ApiClient;

import fu.prm391.sampl.project.view.category.AllCategory;
import fu.prm391.sampl.project.view.product.TrendingProduct;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewTop4Category, recyclerViewTopTrendingProduct;
    private TextView txtViewAllCategory, txtViewAllTrendingProduct;
    private ImageView imageCart, imageTopDiscount, imageNewArrival;
    private ConstraintLayout loadingLayout;
    private Button button;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //加载动画
        loadingLayout = view.findViewById(R.id.loadingConstraintLayoutHome);
        loadingLayout.setVisibility(View.VISIBLE);

        recyclerViewTop4Category = view.findViewById(R.id.recyclerViewTop4Cate);
        recyclerViewTopTrendingProduct = view.findViewById(R.id.recyclerViewTopTrendingProductHome);
        //加载一组数据
        getTopCategory();
        //加载二组数据
        getTrendingProducts();
        checktoken();

        moveToOtherActivities(view);
        moveToOtherNavigationTab(view);
    }

    //获取分类列表
    private void getTopCategory() {
        Call<CategoryResponse> categoryResponseCall = ApiClient.getCategoryService().
                getCategorylist(4);
        categoryResponseCall.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Category> categories = (ArrayList<Category>) response.body().getData();
                    recyclerViewTop4Category.setAdapter(new CategoryTop4Adapter(getContext(), categories));
                    //此处Layout有什么作用
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false) {
                        @Override
                        public boolean canScrollHorizontally() {
                            return false;
                        }
                    };
                    recyclerViewTop4Category.setLayoutManager(layoutManager);
                }
            }
            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
            }
        });
    }

    //获取商品列表
    private void getTrendingProducts() {
        Call<ProductListResponse> productResponseCall = ApiClient.getProductService().
                getProductlist(5);
        productResponseCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Product> products = (ArrayList<Product>) response.body().getData();
                    recyclerViewTopTrendingProduct.setAdapter(new ProductTrendingItemAdapter(getContext(), products));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    recyclerViewTopTrendingProduct.setLayoutManager(layoutManager);
                    loadingLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
            }
        });
    }

    private void checktoken(){
        String token = PreferencesHelpers.loadStringData(getContext(), "token");
        //验证本地令牌是否有效
        if (token.equals("")) {
            Toast.makeText(getContext(), "您尚未登录，只能使用部分功能", Toast.LENGTH_SHORT).show();
        }
        else{
            Call<LoginResponse> loginResponseCall = ApiClient.getUserService().check_token(token,token);
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()){
                        Log.d("提示","令牌有效");
                    }
                    else{
                        Log.d("提示","令牌失效");
                        PreferencesHelpers.removeSinglePreference(getContext(),"token");
                        Toast.makeText(getContext(), "登陆状态已经过期，只能使用部分功能", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                }
            });
            //校验令牌有效
        }
    }

    private void moveToOtherActivities(View view) {
        // All Category
        txtViewAllCategory = view.findViewById(R.id.txtViewAllCategoryHome);
        txtViewAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AllCategory.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        // Trending
        txtViewAllTrendingProduct = view.findViewById(R.id.txtViewAllTrendingProductHome);
        txtViewAllTrendingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TrendingProduct.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void moveToOtherNavigationTab(View view) {
        imageCart = view.findViewById(R.id.imageCartHome);
        imageCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomNavigationView bottomNavigationView;
                bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.cart);
            }
        });

        button=view.findViewById(R.id.more_other);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingLayout.setVisibility(View.VISIBLE);
                getTrendingProducts();
            }
        });
    }
}