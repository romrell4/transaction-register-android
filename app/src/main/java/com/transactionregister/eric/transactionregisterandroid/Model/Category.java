package com.transactionregister.eric.transactionregisterandroid.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eric on 10/22/16.
 */

public class Category implements Parcelable {
	private int categoryId;
	private String name;
	private String month;
	private double amountSpent;
	private double amountLeft;
	private double amountBudgeted;

	private Category(Parcel parcel) {
		this.categoryId = parcel.readInt();
		this.name = parcel.readString();
		this.month = parcel.readString();
		this.amountSpent = parcel.readDouble();
		this.amountLeft = parcel.readDouble();
		this.amountBudgeted = parcel.readDouble();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmountSpent() {
		return amountSpent;
	}

	public double getAmountBudgeted() {
		return amountBudgeted;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(categoryId);
		parcel.writeString(name);
		parcel.writeString(month);
		parcel.writeDouble(amountSpent);
		parcel.writeDouble(amountLeft);
		parcel.writeDouble(amountBudgeted);
	}

	public static final Creator CREATOR = new Creator<Category>() {

		@Override
		public Category createFromParcel(Parcel parcel) {
			return new Category(parcel);
		}

		@Override
		public Category[] newArray(int i) {
			return new Category[i];
		}
	};

	@Override
	public String toString() {
		return name;
	}
}
