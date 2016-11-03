package com.transactionregister.eric.transactionregisterandroid.Model;

import java.util.Date;

/**
 * Created by eric on 10/23/16.
 */

public class Transaction {
	private int transactionId;
	private PaymentType paymentType;
	private Date purchaseDate;
	private String business;
	private double amount;
	private int categoryId;
	private String categoryName;
	private String description;

	public int getTransactionId() {
		return transactionId;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public String getBusiness() {
		return business;
	}

	public double getAmount() {
		return amount;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getDescription() {
		return description;
	}
}
