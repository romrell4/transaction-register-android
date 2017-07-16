package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.transactionregister.eric.transactionregisterandroid.Support.TXRecyclerAdapter;
import com.transactionregister.eric.transactionregisterandroid.Support.TXRecyclerView;
import com.transactionregister.eric.transactionregisterandroid.Support.TXViewHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CategoryActivity extends TXActivity {
	public static final String CATEGORY_ID = "CATEGORY_ID";
	public static final String MONTH = "MONTH";
	public static final String YEAR = "YEAR";

	private static final String TAG = CategoryActivity.class.getSimpleName();
	private TransactionAdapter adapter = new TransactionAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);

		Intent intent = getIntent();
		Integer categoryId = (Integer) intent.getSerializableExtra(CATEGORY_ID);
		Integer month = (Integer) intent.getSerializableExtra(MONTH);
		Integer year = (Integer) intent.getSerializableExtra(YEAR);
		Client.Api api = TXApiGenerator.createApi(this, new Client());
		enqueueCall(api.getTransactions(null, categoryId, month, year), new TXCallback<List<Transaction>>(this) {
			@Override
			public void onSuccess(Call<List<Transaction>> call, Response<List<Transaction>> response) {
				adapter.setList(response.body());
			}

			@Override
			public void onFailure(Call<List<Transaction>> call, Exception error) {
				showErrorDialog(error.getMessage());
			}
		});
		((TXRecyclerView) findViewById(R.id.recyclerView)).setAdapter(adapter);
	}

	//TODO: Merge this with the one in TransactionsFragment
	private class TransactionAdapter extends TXRecyclerAdapter<Transaction> {

		TransactionAdapter() {
			super(null);
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
			public void bind(final Transaction data) {
				businessTextView.setText(data.getBusiness());
				dateTextView.setText(data.getPrettyPurchaseDateString());
				amountTextView.setText(data.getPrettyAmount());
				categoryTextView.setText(data.getCategoryName());

				if ("?".equals(data.getDescription())) {
					itemView.setBackgroundColor(ContextCompat.getColor(CategoryActivity.this, R.color.warning));
				} else {
					itemView.setBackgroundColor(ContextCompat.getColor(CategoryActivity.this, R.color.white));
				}
			}
		}
	}
}
