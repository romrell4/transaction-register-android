package com.transactionregister.eric.transactionregisterandroid.Model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by eric on 11/2/16.
 */

public enum PaymentType {
	CREDIT("Credit"),
	DEBIT("Debit"),
	SAVINGS("Savings"),
	PERMANENT_SAVINGS("Permanent Savings");

	private String name;

	PaymentType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
