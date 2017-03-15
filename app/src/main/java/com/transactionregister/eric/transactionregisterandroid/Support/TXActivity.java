package com.transactionregister.eric.transactionregisterandroid.Support;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;

/**
 * Created by eric on 10/22/16.
 */

public class TXActivity extends AppCompatActivity implements TXCallManager {
	private Set<Call> calls = new HashSet<>(INITIAL_CAPACITY);

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public void showErrorDialog(String message) {
		new AlertDialog.Builder(this)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						onBackPressed();
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
