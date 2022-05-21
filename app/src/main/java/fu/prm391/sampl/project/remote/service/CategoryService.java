package fu.prm391.sampl.project.remote.service;

import fu.prm391.sampl.project.model.category.CategoryResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {

    @GET("get_category_random")
    Call<CategoryResponse> getCategorylist(@Query("num") int num);

    @GET("get_all_category")
    Call<CategoryResponse> getAllCategories();
}
