package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.transactionregister.eric.transactionregisterandroid.Model.Category;
import com.transactionregister.eric.transactionregisterandroid.Model.PaymentType;
import com.transactionregister.eric.transactionregisterandroid.Model.Transaction;
import com.transactionregister.eric.transactionregisterandroid.R;
import com.transactionregister.eric.transactionregisterandroid.Service.Client;
import com.transactionregister.eric.transactionregisterandroid.Support.TXApiGenerator;
import com.transactionregister.eric.transactionregisterandroid.Support.TXCallback;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by eric on 11/4/16
 */

public class AddTransactionDialog extends DialogFragment {
	private static final String TAG = AddTransactionDialog.class.getSimpleName();
	public static final String DEFAULT_PAYMENT_TYPE = "DefaultPaymentType";
	public static final String CATEGORIES = "Categories";

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_transaction, null);

		//Create a tab for each payment type
		TabLayout tabs = (TabLayout) view.findViewById(R.id.paymentTypes);
		for (PaymentType paymentType : PaymentType.values()) {
			tabs.addTab(tabs.newTab().setText(paymentType.getName()));
		}

		//Select a default tab based on their filter. This will work because getInt returns 0 if the argument wasn't passed in
		TabLayout.Tab defaultTab = tabs.getTabAt(getArguments().getInt(DEFAULT_PAYMENT_TYPE));
		if (defaultTab != null) {
			defaultTab.select();
		}

		//Populate the auto complete adapter with active categories
		final List<Category> categories = getArguments().getParcelableArrayList(CATEGORIES);
		if (categories == null) { throw new RuntimeException("Categories not loaded properly");	}
		Spinner categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
		categorySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories));

		//Create dialog from inflated view
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setView(view)
				.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						//Do nothing. We will override this button below. However, if we do not define this function here, the code throws a null pointer exception
					}
				})
				.setNegativeButton("Cancel", null)
				.show();
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _) {
				Transaction newTx = getTransactionIfValid(view, categories);
				if (newTx != null) {
					Client.Api api = (Client.Api) TXApiGenerator.createApi(getActivity(), new Client());
					Call<Transaction> call = api.createTransaction(newTx);
					call.enqueue(new TXCallback<Transaction>(getActivity()) {
						@Override
						public void onSuccess(Call<Transaction> call, Response<Transaction> response) {
							Toast.makeText(getActivity(), "Transaction Created!", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailure(Call<Transaction> call, Exception byuError) {
							//TODO: Display error
						}
					});
					dismiss();
				}
			}
		});
		return dialog;
	}

	private Transaction getTransactionIfValid(View view, List<Category> categories) {
		List<EditText> invalidFields = new ArrayList<>();

		Transaction transaction = new Transaction();
		transaction.setPaymentType(PaymentType.values()[((TabLayout) view.findViewById(R.id.paymentTypes)).getSelectedTabPosition()]);
		try {
			transaction.setBusiness(getEditText(view, R.id.businessEditText));
		} catch (InvalidFieldException e) {
			invalidFields.add(e.getInvalidField());
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
			transaction.setPurchaseDate(format.parse(getEditText(view, R.id.dateEditText)));
		} catch (InvalidFieldException | ParseException e) {
			invalidFields.add((EditText) view.findViewById(R.id.dateEditText));
		}
		try {
			NumberFormat format = NumberFormat.getCurrencyInstance();
			transaction.setAmount(format.parse(getEditText(view, R.id.amountEditText)).doubleValue());
		} catch (InvalidFieldException | ParseException e) {
			invalidFields.add((EditText) view.findViewById(R.id.amountEditText));
		}

		//TODO: Get category ID. Maybe a spinner?
		try {
			String category = getEditText(view, R.id.categorySpinner);

		} catch (InvalidFieldException e) {
			invalidFields.add(e.getInvalidField());
		}

		try {
			transaction.setDescription(getEditText(view, R.id.descriptionEditText));
		} catch (InvalidFieldException e) {
			invalidFields.add(e.getInvalidField());
		}

		if (invalidFields.size() == 0) {
			return transaction;
		} else {
			for (EditText invalidField : invalidFields) {
				invalidField.setError("Something");
			}
		}
		return null;
	}

	private String getEditText(View view, int resource) throws InvalidFieldException {
		EditText editText = (EditText) view.findViewById(resource);
		String text = editText.getText().toString();
		if (text.isEmpty()) {
			throw new InvalidFieldException(editText);
		}
		return text;
	}

	private class InvalidFieldException extends Exception {
		private EditText invalidField;

		private InvalidFieldException(EditText invalidField) {
			this.invalidField = invalidField;
		}

		private EditText getInvalidField() {
			return invalidField;
		}
	}
}
