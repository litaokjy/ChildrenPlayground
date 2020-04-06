package com.ee.cp.http;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ee.cp.R;
import com.ee.cp.databinding.ItemViewBinding;
import com.ee.cp.http.bean.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int variableId;
    private List<User> list;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, int variableId, List<User> list) {
        this.variableId = variableId;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot().getRootView());
        viewHolder.setBinding(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.getBinding().setVariable(variableId, list.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemViewBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setBinding(ItemViewBinding binding) {
            this.binding = binding;
        }

        public ItemViewBinding getBinding() {
            return binding;
        }
    }
}
