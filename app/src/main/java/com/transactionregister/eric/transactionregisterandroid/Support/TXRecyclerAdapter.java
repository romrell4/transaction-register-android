package com.transactionregister.eric.transactionregisterandroid.Support;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

/**
 * Created by eric on 10/22/16
 */
public abstract class TXRecyclerAdapter<DataType> extends RecyclerView.Adapter<TXViewHolder<DataType>> {
	private static final String TAG = TXRecyclerAdapter.class.getSimpleName();
	private List<DataType> list;

	public TXRecyclerAdapter(List<DataType> list) {
		this.list = list;
	}

	public List<DataType> getList() {
		return list;
	}

	public void setList(List<DataType> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public DataType get(int position) {
		return this.list.get(position);
	}

	@Override
	public void onBindViewHolder(TXViewHolder<DataType> holder, int position) {
		holder.bind(list.get(position));
	}

	@Override
	public int getItemCount() {
		return list == null ? 0 : list.size();
	}

	protected void addAllToList(List<DataType> list) {
		this.list.addAll(list);
		notifyItemRangeInserted(this.list.size(), list.size());
	}

	protected void addAllToList(int position, List<DataType> list) {
		this.list.addAll(position, list);
		notifyItemRangeInserted(position, list.size());
	}

	protected void remove(int position) {
		list.remove(position);
		notifyItemRemoved(position);
	}
}
