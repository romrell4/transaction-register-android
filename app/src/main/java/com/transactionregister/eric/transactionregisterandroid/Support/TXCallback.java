package com.transactionregister.eric.transactionregisterandroid.Support;

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

	@Override
	public void onResponse(Call<T> call, Response<T> response) {
		if (response.isSuccessful()) {
			onSuccess(call, response);
		} else {
			onFailure(call, new Exception(DEFAULT_ERROR_MESSAGE));
		}
	}

	@Override
	public void onFailure(Call<T> call, Throwable t) {
		Log.e(TAG, "Error!", t);
		onFailure(call, new Exception(DEFAULT_ERROR_MESSAGE, t));
	}

	public abstract void onSuccess(Call<T> call, Response<T> response);

	public abstract void onFailure(Call<T> call, Exception byuError);
}
