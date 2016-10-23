package com.transactionregister.eric.transactionregisterandroid.Support;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by eric on 10/22/16.
 */

public abstract class TXViewHolder<DataType> extends RecyclerView.ViewHolder {
	public TXViewHolder(View itemView) {
		super(itemView);
	}

	public abstract void bind(DataType data);
}
