package com.transactionregister.eric.transactionregisterandroid.Support;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eric on 10/23/16.
 */

public class TXApiGenerator {
	public static <C extends TXClient> Object createApi(Context context, C client) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(client.getBaseUrl())
				.addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
						.registerTypeAdapter(Date.class, new DateAdapter())
						.create()))
				.client(createClient())
				.build();

		return retrofit.create(client.getApi());
	}

	private static OkHttpClient createClient() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
		return builder.build();
	}

	private static class DateAdapter extends TypeAdapter<Date> {
		private static final String TAG = DateAdapter.class.getSimpleName();

		@Override
		public void write(JsonWriter out, Date value) throws IOException {
			if (value == null) {
				out.nullValue();
			} else {
				out.value(value.toString());
			}
		}

		@Override
		public Date read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			} else {
				try {
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
					return format.parse(in.nextString());
				} catch (ParseException e) {
					Log.e(TAG, "Failed to convert JSON date", e);
					throw new IOException(e);
				}
			}
		}
	}
}
