package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by eric on 10/22/16.
 */

public class TransactionsFragment extends TXFragment {
	private static final String TAG = TransactionsFragment.class.getSimpleName();
	private TransactionsAdapter adapter = new TransactionsAdapter(null);
	private PaymentType filterType;

	@Override
	public String getTitle() {
		return "Transactions";
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_transactions, container, false);

		((TXRecyclerView) view.findViewById(R.id.transactionsRecyclerView)).setAdapter(adapter);
		view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO: Add transaction dialog
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

	private void loadData() {
		Client.Api api = (Client.Api) TXApiGenerator.createApi(getActivity(), new Client());

		Calendar cal = Calendar.getInstance();
		Call<List<Transaction>> call = api.getTransactions(filterType, cal.get(Calendar.MONTH) + 1, null);//cal.get(Calendar.YEAR));
		call.enqueue(new TXCallback<List<Transaction>>() {
			@Override
			public void onSuccess(Call<List<Transaction>> call, Response<List<Transaction>> response) {
				adapter.setList(response.body());
			}

			@Override
			public void onFailure(Call<List<Transaction>> call, Exception byuError) {
				((TXActivity) getActivity()).showErrorDialog(byuError.getMessage());
			}
		});
	}

	private class TransactionsAdapter extends TXRecyclerAdapter<Transaction> {
		private SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
		private NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

		TransactionsAdapter(List<Transaction> list) {
			super(list);
		}

		@Override
		public TXViewHolder<Transaction> onCreateViewHolder(ViewGroup parent, int viewType) {
			return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false));
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
			public void bind(Transaction data) {
				businessTextView.setText(data.getBusiness());
				dateTextView.setText(format.format(data.getPurchaseDate()));
				amountTextView.setText(numberFormat.format(data.getAmount()));
				categoryTextView.setText(data.getCategoryName());
			}
		}
	}
}
