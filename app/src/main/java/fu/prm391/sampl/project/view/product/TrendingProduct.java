package fu.prm391.sampl.project.view.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import fu.prm391.sampl.project.R;
import fu.prm391.sampl.project.model.product.Product;
import fu.prm391.sampl.project.model.product.get_list_product.ProductListResponse;
import fu.prm391.sampl.project.adapter.product.ProductGridLayoutItemAdapter;
import fu.prm391.sampl.project.remote.ApiClient;
import fu.prm391.sampl.project.view.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingProduct extends AppCompatActivity {

    private ImageView imageViewBack;
    private RecyclerView recyclerView;
    private ConstraintLayout loadingConstraintLayout;
    //起始页
    private int page=1;
    //每页限制
    private int limit=6;
    private int total=0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_product);
        recyclerView = findViewById(R.id.recyclerViewTrendingProduct);
        context=this;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItemPos;
            int visibleItemCount;
            int itemCount;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();
                lastVisibleItemPos=layoutManager.findLastVisibleItemPosition();
                visibleItemCount=layoutManager.getChildCount();
                itemCount=layoutManager.getItemCount();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (visibleItemCount>0&&lastVisibleItemPos==itemCount-1&& newState ==RecyclerView.SCROLL_STATE_IDLE){
                    nextpage();
                }
            }
            public void nextpage(){
                page++;
                if((page-1)*limit<total) {
                    getListTrendingProduct();
                }
                else{
                    Toast.makeText(context,"已经是最后一页了",Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadingConstraintLayout = findViewById(R.id.loadingConstraintLayoutTopTrending);
        loadingConstraintLayout.setVisibility(View.VISIBLE);

        Call<Integer> productcountResponseCall = ApiClient.getProductService().get_products_count();
        productcountResponseCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    //第一次进入获取总记录数量
                    total=response.body();
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) { }
        });



        getListTrendingProduct();
        backAction();
    }

    private void getListTrendingProduct() {
        Call<ProductListResponse> productResponseCall = ApiClient.getProductService().getAllProduct(page,limit);
        productResponseCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Product> products = (ArrayList<Product>) response.body().getData();
                    recyclerView.setAdapter(new ProductGridLayoutItemAdapter(TrendingProduct.this, products));
                    GridLayoutManager layoutManager = new GridLayoutManager(TrendingProduct.this, 2);
                    recyclerView.setLayoutManager(layoutManager);
                    loadingConstraintLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
            }
        });
    }

    private void backAction() {
        imageViewBack = findViewById(R.id.imageViewBackTrendingProduct);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrendingProduct.this, MainActivity.class));
                finish();
            }
        });
    }

}