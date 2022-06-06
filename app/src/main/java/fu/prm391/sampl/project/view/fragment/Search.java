package fu.prm391.sampl.project.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import fu.prm391.sampl.project.R;
import fu.prm391.sampl.project.adapter.product.ProductGridLayoutItemAdapter;
import fu.prm391.sampl.project.helper.PreferencesHelpers;
import fu.prm391.sampl.project.model.product.Product;
import fu.prm391.sampl.project.model.product.get_list_product.ProductListResponse;
import fu.prm391.sampl.project.remote.ApiClient;
import fu.prm391.sampl.project.view.account.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewSearchedProduct;
    private ImageButton btnSearch;
    private EditText txtSearchQuery;
    private final int limitSearch = 10;

    private String token;

    public Search() {
        // Required empty public constructor
    }


    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setSaveEnabled(false);
        this.token = PreferencesHelpers.loadStringData(getContext(), "token");
        //验证本地令牌是否有效

        if (token.equals("")) {
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        }
        recyclerViewSearchedProduct = view.findViewById(R.id.recyclerViewSearchedProduct);
        txtSearchQuery = view.findViewById(R.id.txtProductSearchQuery);
        txtSearchQuery.setText("");
        btnSearch = view.findViewById(R.id.imageButtonSearchProduct);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(txtSearchQuery.getText().toString())) {
                    Toast.makeText(getContext(), "输入关键字", Toast.LENGTH_SHORT).show();
                } else {
                    searchProductsAction();
                }
            }
        });
    }

    private void searchProductsAction() {
        Call<ProductListResponse> productResponseCall = ApiClient.getProductService().searchProducts(token,txtSearchQuery.getText().toString(), limitSearch);
        productResponseCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Product> products = (ArrayList<Product>) response.body().getData();
                    if (products.size() == 0) {
                        Toast.makeText(getContext(), "没有找到相关商品!", Toast.LENGTH_SHORT).show();
                    }
                    recyclerViewSearchedProduct.setAdapter(new ProductGridLayoutItemAdapter(getContext(), products));
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                    recyclerViewSearchedProduct.setLayoutManager(layoutManager);
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
            }
        });
    }
}