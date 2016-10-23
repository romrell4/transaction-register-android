package com.transactionregister.eric.transactionregisterandroid.Model;

/**
 * Created by eric on 10/22/16.
 */

public class Category {
	private int categoryId;
	private String name;
	private String month;
	private double amountSpent;
	private double amountLeft;
	private double amountBudgeted;

	public Category(int categoryId, String name, String month, double amountSpent, double amountLeft, double amountBudgeted) {
		this.categoryId = categoryId;
		this.name = name;
		this.month = month;
		this.amountSpent = amountSpent;
		this.amountLeft = amountLeft;
		this.amountBudgeted = amountBudgeted;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public double getAmountSpent() {
		return amountSpent;
	}

	public void setAmountSpent(double amountSpent) {
		this.amountSpent = amountSpent;
	}

	public double getAmountLeft() {
		return amountLeft;
	}

	public void setAmountLeft(double amountLeft) {
		this.amountLeft = amountLeft;
	}

	public double getAmountBudgeted() {
		return amountBudgeted;
	}

	public void setAmountBudgeted(double amountBudgeted) {
		this.amountBudgeted = amountBudgeted;
	}

}
