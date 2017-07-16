package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transactionregister.eric.transactionregisterandroid.Model.Category;
import com.transactionregister.eric.transactionregisterandroid.Model.PaymentType;
import com.transactionregister.eric.transactionregisterandroid.Model.Transaction;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by eric on 10/22/16
 */

public class TransactionsFragment extends TXFragment {
	private static final String TAG = TransactionsFragment.class.getSimpleName();
	private TransactionsAdapter adapter = new TransactionsAdapter();
	private PaymentType filterType;
	private List<Category> activeCategories;
	private Client.Api api;
	private Calendar queryCalendar = Calendar.getInstance();
	private Calendar stopCalendar = new GregorianCalendar(2014, 10, 1);

	@Override
	public String getTitle() {
		return "Transactions";
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_transactions, container, false);

		api = TXApiGenerator.createApi(getActivity(), new Client());
		enqueueCall(api.getActiveCategories(), new TXCallback<List<Category>>(this) {
			@Override
			public void onSuccess(Call<List<Category>> call, Response<List<Category>> response) {
				activeCategories = response.body();
			}

			@Override
			public void onFailure(Call<List<Category>> call, Exception error) {
				showErrorDialog(error.getMessage());
			}
		});

		((TXRecyclerView) view.findViewById(R.id.transactionsRecyclerView)).setAdapter(adapter);
		view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popUp(null);
			}
		});

		setHasOptionsMenu(true);

		loadData();
		return view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_filter) {
			final List<PaymentType> paymentTypes = Arrays.asList(PaymentType.values());
			String[] items = new String[paymentTypes.size()];
			for (int i = 0; i < paymentTypes.size(); i++) {
				items[i] = paymentTypes.get(i).getName();
			}

			new AlertDialog.Builder(getActivity())
					.setSingleChoiceItems(items, paymentTypes.indexOf(filterType), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							filterType = paymentTypes.get(i);
						}
					})
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							loadData();
						}
					})
					.setNegativeButton("Clear", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							if (filterType != null) {
								filterType = null;
								loadData();
							}
						}
					})
					.setTitle("What account would you like to filter by?")
					.show();
		}
		return true;
	}

	private void popUp(Transaction transaction) {
		Bundle bundle = new Bundle();
		if (filterType != null) {
			bundle.putInt(AddTransactionDialog.DEFAULT_PAYMENT_TYPE, filterType.ordinal());
		}
		if (transaction != null) {
			bundle.putParcelable(AddTransactionDialog.TRANSACTION, transaction);
		}
		bundle.putParcelableArrayList(AddTransactionDialog.CATEGORIES, (ArrayList<Category>) activeCategories);

		AddTransactionDialog dialog = new AddTransactionDialog();
		dialog.setArguments(bundle);
		dialog.show(getFragmentManager(), null);
	}

	private void loadData() {
		enqueueCall(api.getTransactions(filterType, null, queryCalendar.get(Calendar.MONTH) + 1, queryCalendar.get(Calendar.YEAR)), new TXCallback<List<Transaction>>(this) {
			@Override
			public void onSuccess(Call<List<Transaction>> call, Response<List<Transaction>> response) {
				adapter.setFirstPage(response.body());
			}

			@Override
			public void onFailure(Call<List<Transaction>> call, Exception error) {
				showErrorDialog(error.getMessage());
			}
		});
	}

	private void loadMore() {
		queryCalendar.add(Calendar.MONTH, -1);
		enqueueCall(api.getTransactions(filterType, null, queryCalendar.get(Calendar.MONTH) + 1, queryCalendar.get(Calendar.YEAR)), new TXCallback<List<Transaction>>(this) {
			@Override
			public void onSuccess(Call<List<Transaction>> call, Response<List<Transaction>> response) {
				adapter.addPage(response.body());
			}

			@Override
			public void onFailure(Call<List<Transaction>> call, Exception error) {
				showErrorDialog(error.getMessage());
			}
		});
	}

	private class TransactionsAdapter extends TXRecyclerAdapter<Transaction> {
		private static final int LOADING_VIEW_TYPE = 0;
		private static final int NOT_LOADING_VIEW_TYPE = 1;

		private boolean isLoading;

		TransactionsAdapter() {
			super(null);
		}

		@Override
		public int getItemViewType(int position) {
			return get(position) == null ? LOADING_VIEW_TYPE : NOT_LOADING_VIEW_TYPE;
		}

		@Override
		public TXViewHolder<Transaction> onCreateViewHolder(ViewGroup parent, int viewType) {
			if (viewType == LOADING_VIEW_TYPE) {
				return new LoadingViewHolder(parent);
			} else {
				return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false));
			}
		}

		void setFirstPage(List<Transaction> firstPage) {
			firstPage.add(null);
			setList(firstPage);
		}

		void addPage(List<Transaction> page) {
			if (queryCalendar.before(stopCalendar)) {
				stopLoading();
				addAllToList(page);
			} else {
				int lastIndex = getItemCount() - 1;
				addAllToList(lastIndex, page);
				notifyItemChanged(lastIndex);
			}
			isLoading = false;
		}

		void stopLoading() {
			int last = getItemCount() - 1;
			if (get(last) == null) {
				remove(last);
			}
		}

		private class LoadingViewHolder extends TXViewHolder<Transaction> {
			private LoadingViewHolder(ViewGroup parent) {
				super(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_spinner, parent, false));
			}

			@Override
			public void bind(Transaction data) {
				if (!isLoading) {
					isLoading = true;
					loadMore();
				}
			}
		}

		private class TransactionViewHolder extends TXViewHolder<Transaction> {
			private TextView businessTextView;
			private TextView dateTextView;
			private TextView amountTextView;
			private TextView categoryTextView;

			TransactionViewHolder(View itemView) {
				super(itemView);
				businessTextView = (TextView) itemView.findViewById(R.id.businessTextView);
				dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
				amountTextView = (TextView) itemView.findViewById(R.id.amountTextView);
				categoryTextView = (TextView) itemView.findViewById(R.id.categoryTextView);
			}

			@Override
			public void bind(final Transaction data) {
				businessTextView.setText(data.getBusiness());
				dateTextView.setText(data.getPrettyPurchaseDateString());
				amountTextView.setText(data.getPrettyAmount());
				categoryTextView.setText(data.getCategoryName());

				if ("?".equals(data.getDescription())) {
					itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.warning));
				} else {
					itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
				}

				itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						popUp(data);
					}
				});
			}
		}
	}
}
