package fu.prm391.sampl.project.model.product.get_product_by_id;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fu.prm391.sampl.project.model.product.Product;

public class ProductResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Product data;
    @SerializedName("code")
    @Expose
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Product getData() {
        return data;
    }

    public void setResult(Product result) {
        this.data = result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}
