package com.banyan.naajilshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.banyan.naajilshop.R;
import com.banyan.naajilshop.model.My_Orders_Model;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<My_Orders_Model> my_order_list;
    private Context mContext;
    private int i = 0 ;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView order_status, order_for, order_id, order_date, order_subtotal, order_discount, order_shipping_charges, order_total_payable;
        public Button btn_reorder;

        public MyViewHolder(View view) {
            super(view);

            order_status = (TextView) view.findViewById(R.id.list_txt_order_status);
            order_for = (TextView) view.findViewById(R.id.list_txt_order_customer);
            order_id = (TextView) view.findViewById(R.id.list_txt_order_id);
            order_date = (TextView) view.findViewById(R.id.list_txt_order_date);
            order_subtotal = (TextView) view.findViewById(R.id.list_txt_order_subtotal);
            order_discount = (TextView) view.findViewById(R.id.list_txt_order_discount);
            order_shipping_charges = (TextView) view.findViewById(R.id.list_txt_order_shipping_charges);
            order_total_payable = (TextView) view.findViewById(R.id.list_txt_order_payable);

            btn_reorder = (Button) view.findViewById(R.id.list_btn_reorder);
        }
    }


    public MyOrderAdapter(Context mContext, List<My_Orders_Model> my_order_list) {
        this.my_order_list = my_order_list;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_order_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        My_Orders_Model movie = my_order_list.get(position);

        holder.order_status.setText(movie.getOrder_status());
        holder.order_for.setText(movie.getCustomername());
        holder.order_id.setText(movie.getOrder_id());
        holder.order_date.setText(movie.getOrder_createdat());
        holder.order_subtotal.setText(movie.getOrder_subtotal());
        holder.order_discount.setText(movie.getOrder_discount());
        holder.order_shipping_charges.setText(movie.getOrder_shipping_charges());
        holder.order_total_payable.setText(movie.getOrder_grand_total());

    }

    @Override
    public int getItemCount() {
        return my_order_list.size();
    }
}
