package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transactionregister.eric.transactionregisterandroid.Model.Category;
import com.transactionregister.eric.transactionregisterandroid.R;
import com.transactionregister.eric.transactionregisterandroid.Support.TXRecyclerAdapter;
import com.transactionregister.eric.transactionregisterandroid.Support.TXFragment;
import com.transactionregister.eric.transactionregisterandroid.Support.TXRecyclerView;
import com.transactionregister.eric.transactionregisterandroid.Support.TXViewHolder;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by eric on 10/22/16.
 */

public class BudgetFragment extends TXFragment {
	private static final String TAG = BudgetFragment.class.getSimpleName();
	private final BudgetAdapter adapter = new BudgetAdapter(null);

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_budget, container, false);

		((TXRecyclerView) view.findViewById(R.id.budgetRecyclerView)).setAdapter(adapter);

		loadCategories(adapter);
		return view;
	}

	private void loadCategories(BudgetAdapter adapter) {
		List<Category> categories = Arrays.asList(new Category(0, "Food", null, 300.25, 0, 500),
				new Category(1, "Misc", null, 153.62, 0, 500));
		adapter.setList(categories);
	}

	@Override
	public String getTitle() {
		return "Budget";
	}

	private class BudgetAdapter extends TXRecyclerAdapter<Category> {

		public BudgetAdapter(List<Category> list) {
			super(list);
		}

		@Override
		public TXViewHolder<Category> onCreateViewHolder(ViewGroup parent, int viewType) {
			Log.d(TAG, "onCreateViewHolder: ");
			return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.default_row, parent, false));
		}

		private class CategoryViewHolder extends TXViewHolder<Category> {
			private TextView textView;
			private TextView detailTextView;

			public CategoryViewHolder(View itemView) {
				super(itemView);
				textView = (TextView) itemView.findViewById(R.id.textView);
				detailTextView = (TextView) itemView.findViewById(R.id.detailTextView);
			}

			@Override
			public void bind(Category data) {
				NumberFormat format = NumberFormat.getCurrencyInstance();
				textView.setText(data.getName());
				detailTextView.setText(format.format(data.getAmountSpent()));
				detailTextView.setTextColor(data.getAmountSpent() <= data.getAmountBudgeted() ?
						getResources().getColor(R.color.green) :
						getResources().getColor(R.color.red));

			}
		}
	}
}
