package com.transactionregister.eric.transactionregisterandroid.Support;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;

/**
 * Created by eric on 10/22/16.
 */

public abstract class TXFragment extends Fragment implements TXCallManager {
	private Set<Call> calls = new HashSet<>(TXCallManager.INITIAL_CAPACITY);

	public abstract String getTitle();

	public void showErrorDialog(String message) {
		new AlertDialog.Builder(getActivity())
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						getActivity().onBackPressed();
					}
				}).show();
	}

	@Override
	public <T> void enqueueCall(Call<T> call, TXCallback<T> callback) {
		calls.add(call);
		call.enqueue(callback);
	}

	@Override
	public void removeCall(Call call) {
		calls.remove(call);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		for (Call call : calls) {
			call.cancel();
		}
	}
}
