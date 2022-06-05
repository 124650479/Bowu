package fu.prm391.sampl.project.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import fu.prm391.sampl.project.R;
import fu.prm391.sampl.project.adapter.order.OrderCartAdapter;
import fu.prm391.sampl.project.helper.PreferencesHelpers;
import fu.prm391.sampl.project.helper.StringHelpers;
import fu.prm391.sampl.project.model.order.Order;
import fu.prm391.sampl.project.model.order.delete_order.DeleteOrderRequest;
import fu.prm391.sampl.project.model.order.delete_order.DeleteOrderResponse;
import fu.prm391.sampl.project.model.order.get_all_order.GetAllOrderResponse;
import fu.prm391.sampl.project.remote.ApiClient;
import fu.prm391.sampl.project.view.account.Login;
import fu.prm391.sampl.project.view.checkout.CheckOutAddress;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private String token;

    private RecyclerView recyclerViewMainCart;

    private ProgressBar progressBarCart;

    private TextView txtCartTotalValue;
    private TextView txtCartEstimatingTaxValue;
    private TextView txtCartShippingFeeValue;
    private TextView txtCardSubTotalValue;

    private Button btnCartCheckout;

    private Call<GetAllOrderResponse> call;

    private List<Order> list;

    private OrderCartAdapter orderCartAdapter;

    private double total;
    private double subTotal;
    private double shippingFee;
    private double tax;

    public Cart() {
    }

    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取登陆令牌
        this.token = PreferencesHelpers.loadStringData(getContext(), "token");

        this.recyclerViewMainCart = view.findViewById(R.id.recyclerViewMainCart);

        this.progressBarCart = view.findViewById(R.id.progressBarCart);

        this.txtCartTotalValue = view.findViewById(R.id.txtCartTotalValue);
        this.txtCartEstimatingTaxValue = view.findViewById(R.id.txtCartEstimatingTaxValue);
        this.txtCartShippingFeeValue = view.findViewById(R.id.txtCartShippingFeeValue);
        this.txtCardSubTotalValue = view.findViewById(R.id.txtCardSubTotalValue);

        this.btnCartCheckout = view.findViewById(R.id.btnCartCheckout);
        progressBarCart.setVisibility(View.VISIBLE);

        //验证本地令牌是否有效
        if (token.equals("")) {
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        } else {
            //加载数据
            loadAllListOrder();
            setEventBtnCheckout();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (call != null) {
            call.cancel();
        }
    }

    private void setEventBtnCheckout() {
        btnCartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CheckOutAddress.class);
                intent.putExtra("subTotal", subTotal);
                intent.putExtra("fee", shippingFee);
                intent.putExtra("tax", tax);
                intent.putExtra("total", total);
                startActivity(intent);
            }
        });
    }

    private void loadAllListOrder() {

        call = ApiClient.getOrderService().getAllOrder(this.token);
        call.enqueue(new Callback<GetAllOrderResponse>() {
            @Override
            public void onResponse(Call<GetAllOrderResponse> call, Response<GetAllOrderResponse> response) {
                if (response.isSuccessful()) {
                    list = response.body().getData();
                    orderCartAdapter = new OrderCartAdapter(list, getContext(), Cart.this);
                    recyclerViewMainCart.setAdapter(orderCartAdapter);
                    recyclerViewMainCart.setLayoutManager(new LinearLayoutManager(getContext()));
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(cartSimpleCallback);
                    itemTouchHelper.attachToRecyclerView(recyclerViewMainCart);
                    progressBarCart.setVisibility(View.INVISIBLE);
                    renderCheckout(list);
                    if (list.size() != 0) {
                        btnCartCheckout.setEnabled(true);
                    }
                } else {
                    progressBarCart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetAllOrderResponse> call, Throwable t) {
            }
        });
    }

    public void renderCheckout(List<Order> list) {
        total = 0;
        subTotal = 0;
        shippingFee = (list.size() == 0) ? 0 : 2;

        for (int index = 0; index < list.size(); index++) {
            Order order = list.get(index);
            subTotal += ((order.getProduct().getPrice() * (100 - order.getProduct().getDiscount())) / 100) * order.getQuantity();
        }

        tax = subTotal / 10;
        total = subTotal + shippingFee + tax;

        txtCardSubTotalValue.setText(StringHelpers.currencyFormatter((double) subTotal));
        txtCartEstimatingTaxValue.setText(StringHelpers.currencyFormatter((double) tax));
        txtCartShippingFeeValue.setText(StringHelpers.currencyFormatter((double) shippingFee));
        txtCartTotalValue.setText(StringHelpers.currencyFormatter((double) total));

        btnCartCheckout.setEnabled(list.size() != 0);
    }

    ItemTouchHelper.SimpleCallback cartSimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            Call<DeleteOrderResponse> call = ApiClient.getOrderService().deleteOrder(token, new DeleteOrderRequest(list.get(position).getProduct().getId()));
            call.enqueue(new Callback<DeleteOrderResponse>() {
                @Override
                public void onResponse(Call<DeleteOrderResponse> call, Response<DeleteOrderResponse> response) {
                    if (response.isSuccessful()) {
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<DeleteOrderResponse> call, Throwable t) {
                }
            });

            list.remove(position);
            orderCartAdapter.notifyItemRemoved(position);
            recyclerViewMainCart.setAdapter(orderCartAdapter);
            renderCheckout(list);
        }
    };

}