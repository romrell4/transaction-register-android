package com.transactionregister.eric.transactionregisterandroid.Support;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eric on 10/23/16.
 */

public class TXApiGenerator {
	public static <C extends TXClient> Object createApi(Context context, C client) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(client.getBaseUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.client(new OkHttpClient.Builder().build())
				.build();
		return retrofit.create(client.getApi());
	}
}
