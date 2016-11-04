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

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
