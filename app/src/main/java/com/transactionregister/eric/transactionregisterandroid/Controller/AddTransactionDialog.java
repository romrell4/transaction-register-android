package com.transactionregister.eric.transactionregisterandroid.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
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
import com.transactionregister.eric.transactionregisterandroid.Support.TXActivity;
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
	public static final String TRANSACTION = "transaction";

	private Transaction transaction;
	private List<Category> categories;

	private TabLayout tabs;
	private EditText businessEditText;
	private EditText dateEditText;
	private EditText amountEditText;
	private Spinner categorySpinner;
	private EditText descriptionEditText;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final TXActivity activity = (TXActivity) getActivity();

		final View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_transaction, null);

		tabs = (TabLayout) view.findViewById(R.id.paymentTypes);
		businessEditText = (EditText) view.findViewById(R.id.businessEditText);
		dateEditText = (EditText) view.findViewById(R.id.dateEditText);
		amountEditText = (EditText) view.findViewById(R.id.amountEditText);
		categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
		descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);

		//Create dialog from inflated view
		AlertDialog dialog = new AlertDialog.Builder(activity)
				.setView(view)
				.setPositiveButton("Save", new DialogInterface.OnClickListener() {
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
				Transaction newTx = getTransactionIfValid();
				if (newTx != null) {
					Client.Api api = TXApiGenerator.createApi(activity, new Client());
					if (transaction == null) {
						activity.enqueueCall(api.createTransaction(newTx), new TXCallback<Transaction>(activity) {
							@Override
							public void onSuccess(Call<Transaction> call, Response<Transaction> response) {
								Toast.makeText(activity, "Transaction Created!", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(Call<Transaction> call, Exception error) {
								activity.showErrorDialog(error.getMessage());
							}
						});
					} else {
						activity.enqueueCall(api.editTransaction(transaction.getTransactionId(), newTx), new TXCallback<Transaction>(activity) {
							@Override
							public void onSuccess(Call<Transaction> call, Response<Transaction> response) {
								Toast.makeText(activity, "Transaction Edited!", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(Call<Transaction> call, Exception error) {
								activity.showErrorDialog(error.getMessage());
							}
						});
					}
					dismiss();
				}
			}
		});
		return dialog;
	}

	@Override
	public void onStart() {
		super.onStart();

		transaction = getArguments().getParcelable(TRANSACTION);

		//Populate the auto complete adapter with active categories
		categories = getArguments().getParcelableArrayList(CATEGORIES);
		if (categories == null) { throw new RuntimeException("Categories not loaded properly");	}
		categorySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories));

		//Create a tab for each payment type
		for (PaymentType paymentType : PaymentType.values()) {
			tabs.addTab(tabs.newTab().setText(paymentType.getName()));
		}

		//Load the data if we're editing an existing transaction
		if (transaction != null) {
			TabLayout.Tab tab = tabs.getTabAt(transaction.getPaymentType().ordinal());
			if (tab != null) {
				tab.select();
			}

			int selectedCategoryIndex = 0;
			for (int i = 0; i < categories.size(); i++) {
				if (categories.get(i).getCategoryId() == transaction.getCategoryId()) {
					selectedCategoryIndex = i;
					break;
				}
			}

			businessEditText.setText(transaction.getBusiness());
			dateEditText.setText(transaction.getSimplePurchaseDateString());
			amountEditText.setText(transaction.getPrettyAmount().replace("$", ""));
			categorySpinner.setSelection(selectedCategoryIndex);
			descriptionEditText.setText(transaction.getDescription());
		} else {
			//Select a default tab based on their filter. This will work because getInt returns 0 if the argument wasn't passed in
			TabLayout.Tab defaultTab = tabs.getTabAt(getArguments().getInt(DEFAULT_PAYMENT_TYPE));
			if (defaultTab != null) {
				defaultTab.select();
			}
		}
	}

	private Transaction getTransactionIfValid() {
		List<EditText> invalidFields = new ArrayList<>();

		Transaction newTx = new Transaction();

		newTx.setPaymentType(PaymentType.values()[tabs.getSelectedTabPosition()]);
		try {
			newTx.setBusiness(getText(businessEditText));
		} catch (InvalidFieldException e) {
			invalidFields.add(businessEditText);
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
			newTx.setPurchaseDate(format.parse(getText(dateEditText)));
		} catch (InvalidFieldException | ParseException e) {
			invalidFields.add(dateEditText);
		}
		try {
			newTx.setAmount(Double.parseDouble(getText(amountEditText)));
		} catch (InvalidFieldException | NumberFormatException e) {
			invalidFields.add(amountEditText);
		}

		Category category = (Category) categorySpinner.getSelectedItem();
		newTx.setCategoryId(category.getCategoryId());

		try {
			newTx.setDescription(getText(descriptionEditText));
		} catch (InvalidFieldException e) {
			invalidFields.add(descriptionEditText);
		}

		if (invalidFields.size() == 0) {
			return newTx;
		} else {
			for (EditText invalidField : invalidFields) {
				invalidField.setError("Invalid");
			}
		}
		return null;
	}

	private String getText(EditText editText) throws InvalidFieldException {
		String text = editText.getText().toString();
		if (TextUtils.isEmpty(text)) {
			throw new InvalidFieldException();
		}
		return text;
	}

	private class InvalidFieldException extends Exception {}
}
