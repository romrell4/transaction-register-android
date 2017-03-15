package com.transactionregister.eric.transactionregisterandroid.Service;

import com.transactionregister.eric.transactionregisterandroid.Model.Category;
import com.transactionregister.eric.transactionregisterandroid.Model.PaymentType;
import com.transactionregister.eric.transactionregisterandroid.Model.Transaction;
import com.transactionregister.eric.transactionregisterandroid.Support.TXClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by eric on 10/23/16
 */

public class Client extends TXClient {
	@Override
	public String getBaseUrl() {
		return "https://transaction-register.herokuapp.com/";
	}

	@Override
	public Class getApi() {
		return Api.class;
	}

	public interface Api {
		@GET("categories")
		Call<List<Category>> getBudget(@Query("categoryId") Integer categoryId, @Query("month") Integer month, @Query("year") Integer year);

		@GET("categories/active")
		Call<List<Category>> getActiveCategories();

		@GET("transactions")
		Call<List<Transaction>> getTransactions(@Query("type") PaymentType paymentType, @Query("month") Integer month, @Query("year") Integer year);

		@POST("transactions")
		Call<Transaction> createTransaction(@Body Transaction transaction);

		@PUT("transactions/{id}")
		Call<Transaction> editTransaction(@Path("id") Integer transactionId, @Body Transaction transaction);

		@GET("transactions/sums")
		Call<Object> getSums();
	}
}
