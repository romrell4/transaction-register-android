package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by eric on 10/22/16.
 */

public class TransactionsFragment extends TXFragment {
	private static final String TAG = TransactionsFragment.class.getSimpleName();
	private TransactionsAdapter adapter = new TransactionsAdapter(null);

	@Override
	public String getTitle() {
		return "Transactions";
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_transactions, container, false);

		((TXRecyclerView) view.findViewById(R.id.transactionsRecyclerView)).setAdapter(adapter);

		loadData();
		return view;
	}

	private void loadData() {
		Client.Api api = (Client.Api) TXApiGenerator.createApi(getActivity(), new Client());

		Calendar cal = Calendar.getInstance();
		Call<List<Transaction>> call = api.getTransactions(null, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
		call.enqueue(new TXCallback<List<Transaction>>() {
			@Override
			public void onSuccess(Call<List<Transaction>> call, Response<List<Transaction>> response) {
				Log.d(TAG, "onSuccess: ");
				adapter.setList(response.body());
			}

			@Override
			public void onFailure(Call<List<Transaction>> call, Exception byuError) {
				Log.d(TAG, "onFailure: ");
				((TXActivity) getActivity()).showErrorDialog(byuError.getMessage());
			}
		});
	}

	private class TransactionsAdapter extends TXRecyclerAdapter<Transaction> {

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
			}

			@Override
			public void bind(Transaction data) {
			}
		}
	}
}
