package com.transactionregister.eric.transactionregisterandroid.Support;

import retrofit2.Call;

/**
 * Created by romrell4 on 3/15/17.
 */

public interface TXCallManager {
	int INITIAL_CAPACITY = 5;

	<T> void enqueueCall(Call<T> call, TXCallback<T> callback);

	void removeCall(Call call);
}
