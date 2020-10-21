package com.example.customcontrollibs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class BaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    /**
     * 数据源
     */
    protected List<T> mData = new ArrayList<>();

    /**
     * item类型
     */
    private int type;

    public BaseAdapter() {

    }

    public BaseAdapter(Collection<T> list) {
        if (list != null) {
            mData.addAll(list);
        }
    }

    public BaseAdapter(T[] data) {
        if (data != null && data.length > 0) {
            mData.addAll(Arrays.asList(data));
        }
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final V holder = getViewHolder(parent, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        bindData(holder, position, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 构建自定义的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    protected abstract V getViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * 绑定数据
     *
     * @param holder
     * @param position 索引
     * @param item     列表项
     */
    protected abstract void bindData(@NonNull V holder, int position, T item);

    /**
     * 加载布局获取控件
     *
     * @param parent   父布局
     * @param layoutId 布局ID
     * @return
     */
    protected View inflateView(ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    /**
     * 设置item类型
     *
     * @param value
     */
    public void setType(int value) {
        this.type = value;
    }

    public int getType(){
        return type;
    }

    /**
     * @return 数据源
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void loadMore(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * 向指定位置添加数据
     *
     * @param pos
     * @param data
     */
    public void add(int pos, T data) {
        mData.add(pos, data);
        notifyItemInserted(pos);
    }

    /**
     * 向头部添加数据
     *
     * @param data
     */
    public void add(T data) {
        mData.add(data);
        notifyItemInserted(mData.size() - 1);
    }

    /**
     * 删除指定位置数据
     *
     * @param position
     */
    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    /**
     * 刷新列表数据
     *
     * @param collection
     * @return
     */
    public void refresh(Collection<T> collection) {
        if (collection != null) {
            mData.clear();
            mData.addAll(collection);
            notifyDataSetChanged();
        }
    }

    /**
     * 刷新列表中指定位置的数据
     *
     * @param position
     * @param item
     * @return
     */
    public void refresh(int position, T item) {
        mData.set(position, item);
        notifyItemChanged(position);
    }
}
