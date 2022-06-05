package fu.prm391.sampl.project.remote.service;

import fu.prm391.sampl.project.model.product.favorite_product.add_favorite.AddFavoriteRequest;
import fu.prm391.sampl.project.model.product.favorite_product.add_favorite.AddFavoriteResponse;
import fu.prm391.sampl.project.model.product.favorite_product.delete_favorite.DeleteFavoriteRequest;
import fu.prm391.sampl.project.model.product.favorite_product.delete_favorite.DeleteFavoriteResponse;
import fu.prm391.sampl.project.model.product.get_list_product.ProductListResponse;
import fu.prm391.sampl.project.model.product.get_product_by_id.ProductResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductService {
    @GET("/allow/get_product_random")
    Call<ProductListResponse> getProductlist(@Query("num") int num);

    @GET("/allow/get_all_product")
    Call<ProductListResponse> getAllProduct(@Query("page") int page,
                                            @Query("limit") int limit);

    @GET("product/get-top-discount")
    Call<ProductListResponse> getTopDiscountProduct();

    @GET("product/get-top-newest")
    Call<ProductListResponse> getNewArrivalsProduct();

    @GET("/allow/get_products_by_caterory")
    Call<ProductListResponse> getProductByCategoryId(@Query("categoryId") int categoryId);

    @GET("product/search")
    Call<ProductListResponse> searchProducts(@Query("query") String query,
                                             @Query("limit") int limit);

    @GET("/allow/get_product_by_id")
    Call<ProductResponse> getProductById(@Query("id") int id);

    @GET("favorites")
    Call<ProductListResponse> getFavoriteProducts(@Header("Authorization") String token);

    @POST("/product/favorite/add")
    Call<AddFavoriteResponse> addFavoriteProduct(@Header("token") String token,
                                                 @Query("id") int id);

    @POST("/product/favorite/del")
    Call<DeleteFavoriteResponse> deleteFavoriteProduct(@Header("token") String token,
                                                       @Query("id") int id);

    @GET("/allow/get_products_by_caterory")
    Call<ProductListResponse> getSimilarProductByCategoryId(@Query("categoryId") int categoryId);

    @GET("/product/get_product_by_id")
    Call<ProductResponse> getProductByIdWithToken(@Header("token") String token,
                                                  @Query("id") int id);

    @GET("/allow/get_products_count")
    Call<Integer> get_products_count();
}