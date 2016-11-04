package com.transactionregister.eric.transactionregisterandroid.Support;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eric on 10/23/16
 */

public abstract class TXCallback<T> implements Callback<T> {
	private static final String TAG = TXCallback.class.getSimpleName();
	private static final String DEFAULT_ERROR_MESSAGE = "There was an error loading the data from the service. Please talk to your husband about it. :)";
	private ProgressDialog dialog;

	public TXCallback(Context context) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage("Loading...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
		this.dialog = dialog;
	}

	@Override
	public void onResponse(Call<T> call, Response<T> response) {
		dismiss();

		if (response.isSuccessful()) {
			onSuccess(call, response);
		} else {
			onFailure(call, new Exception(DEFAULT_ERROR_MESSAGE));
		}
	}

	@Override
	public void onFailure(Call<T> call, Throwable t) {
		dismiss();

		Log.e(TAG, "Error!", t);
		onFailure(call, new Exception(DEFAULT_ERROR_MESSAGE, t));
	}

	private void dismiss() {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
	}

	public abstract void onSuccess(Call<T> call, Response<T> response);

	public abstract void onFailure(Call<T> call, Exception byuError);
}
