package com.transactionregister.eric.transactionregisterandroid.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eric on 10/23/16.
 */

public class Transaction implements Parcelable {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	private static final SimpleDateFormat prettyDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
	private static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);

	private int transactionId;
	private PaymentType paymentType;
	private Date purchaseDate;
	private String business;
	private double amount;
	private int categoryId;
	private String categoryName;
	private String description;

	public Transaction() {}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public String getSimplePurchaseDateString() {
		return simpleDateFormat.format(purchaseDate);
	}

	public String getPrettyPurchaseDateString() {
		return prettyDateFormat.format(purchaseDate);
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

	public String getPrettyAmount() {
		return numberFormat.format(amount);
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.transactionId);
		dest.writeInt(this.paymentType == null ? -1 : this.paymentType.ordinal());
		dest.writeLong(this.purchaseDate != null ? this.purchaseDate.getTime() : -1);
		dest.writeString(this.business);
		dest.writeDouble(this.amount);
		dest.writeInt(this.categoryId);
		dest.writeString(this.categoryName);
		dest.writeString(this.description);
	}

	private Transaction(Parcel in) {
		this.transactionId = in.readInt();
		int tmpPaymentType = in.readInt();
		this.paymentType = tmpPaymentType == -1 ? null : PaymentType.values()[tmpPaymentType];
		long tmpPurchaseDate = in.readLong();
		this.purchaseDate = tmpPurchaseDate == -1 ? null : new Date(tmpPurchaseDate);
		this.business = in.readString();
		this.amount = in.readDouble();
		this.categoryId = in.readInt();
		this.categoryName = in.readString();
		this.description = in.readString();
	}

	public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
		@Override
		public Transaction createFromParcel(Parcel source) {
			return new Transaction(source);
		}

		@Override
		public Transaction[] newArray(int size) {
			return new Transaction[size];
		}
	};
}
