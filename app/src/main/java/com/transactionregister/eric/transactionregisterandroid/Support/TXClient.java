package com.transactionregister.eric.transactionregisterandroid.Support;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eric on 10/23/16.
 */

public abstract class TXClient {
	private static final String TAG = TXClient.class.getSimpleName();

	public abstract String getBaseUrl();

	public abstract Class getApi();
}
