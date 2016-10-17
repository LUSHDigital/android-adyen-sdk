package com.lush.adyensdk.cse;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Matt Allen
 */
public class CardPaymentData
{
	@SerializedName("generationtime") private String generationTime;
	private String number;
	private String expiryMonth;
	private String expiryYear;
	private String holderName;
	private String cvc;

	public CardPaymentData(String holderName, String number, String expiryMonth, String expiryYear, String cvc)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
		generationTime = sdf.format(new Date());
		this.holderName = holderName;
		this.number = number;
		this.expiryMonth = expiryMonth;
		this.expiryYear = expiryYear;
		this.cvc = cvc;
	}

	public String getNumber()
	{
		return number;
	}

	public String getExpiryMonth()
	{
		return expiryMonth;
	}

	public String getExpiryYear()
	{
		return expiryYear;
	}

	public String getCardHolderName()
	{
		return holderName;
	}

	public String getCvc()
	{
		return cvc;
	}

	public String getGenerationTime()
	{
		return generationTime;
	}
}
