package com.transactionregister.eric.transactionregisterandroid.Support;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by eric on 10/23/16.
 */

public class TXRecyclerView extends RecyclerView {
	public TXRecyclerView(Context context) {
		this(context, null);
	}

	public TXRecyclerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		setHasFixedSize(true);
		setLayoutManager(new LinearLayoutManager(context));
		addItemDecoration(new HorizontalDividerItemDecoration.Builder(context).build());
	}
}
