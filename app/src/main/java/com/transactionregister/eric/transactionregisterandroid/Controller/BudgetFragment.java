package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transactionregister.eric.transactionregisterandroid.Model.Category;
import com.transactionregister.eric.transactionregisterandroid.R;
import com.transactionregister.eric.transactionregisterandroid.Service.Client;
import com.transactionregister.eric.transactionregisterandroid.Support.TXActivity;
import com.transactionregister.eric.transactionregisterandroid.Support.TXApiGenerator;
import com.transactionregister.eric.transactionregisterandroid.Support.TXCallback;
import com.transactionregister.eric.transactionregisterandroid.Support.TXFragment;
import com.transactionregister.eric.transactionregisterandroid.Support.TXRecyclerAdapter;
import com.transactionregister.eric.transactionregisterandroid.Support.TXRecyclerView;
import com.transactionregister.eric.transactionregisterandroid.Support.TXViewHolder;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

		loadCategories();
		return view;
	}

	private void loadCategories() {
		Client.Api api = (Client.Api) TXApiGenerator.createApi(getActivity(), new Client());

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		Call<List<Category>> call = api.getCategories(null, month, year);

		call.enqueue(new TXCallback<List<Category>>() {
			@Override
			public void onSuccess(Call<List<Category>> call, Response<List<Category>> response) {
				adapter.setList(response.body());
			}

			@Override
			public void onFailure(Call<List<Category>> call, Exception byuError) {
				((TXActivity) getActivity()).showErrorDialog(byuError.getMessage());
			}
		});

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
