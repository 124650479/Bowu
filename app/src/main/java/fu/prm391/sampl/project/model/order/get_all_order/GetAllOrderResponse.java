package fu.prm391.sampl.project.model.order.get_all_order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fu.prm391.sampl.project.model.order.Order;

public class GetAllOrderResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Order> data = null;
    @SerializedName("code")
    @Expose
    private String code;

    public GetAllOrderResponse() {
    }

    public GetAllOrderResponse(String message, List<Order> data,String code) {
        this.message = message;
        this.data = data;
        this.code=code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
