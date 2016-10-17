package com.lush.adyensdk;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lush.adyensdk.cse.CardPaymentData;
import com.lush.adyensdk.cse.CardType;
import com.lush.adyensdk.cse.ClientSideEncrypter;
import com.lush.adyensdk.cse.EncrypterException;
import com.lush.adyensdk.listener.OnPublicKeyDownloadListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Entry point of the Adyen functionality.
 *
 * @author Matt Allen
 */
public class Adyen
{
	private static final String TAG = Adyen.class.getSimpleName();

	private String token;
	private boolean useTestBackend;
	private RequestQueue mQueue;

	public Adyen(Context context, String token)
	{
		this(context, token, false);
	}

	public Adyen(Context context, String token, boolean useTestBackend)
	{
		this.token = token;
		this.useTestBackend = useTestBackend;
		mQueue = Volley.newRequestQueue(context);
	}

	public void fetchPublicKey(final OnPublicKeyDownloadListener listener)
	{
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, createUrl(), new Response.Listener<JSONObject>()
		{
			@Override
			public void onResponse(JSONObject response)
			{
				try
				{
					if (response != null && !TextUtils.isEmpty(response.getString("publicKey")))
					{
						listener.onSuccess(response.getString("publicKey"));
					}
					else
					{
						listener.onFailure();
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					listener.onFailure();
				}
			}
		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				listener.onFailure();
			}
		});
		mQueue.add(request);
	}

	/**
	 * Create the URL that the public key will be retrieved from.
	 *
	 * @return URL for correct environment.
	 */
	private String createUrl()
	{
		String host = (useTestBackend) ? "test" : "live";
		return String.format("https://%s.adyen.com/hpp/cse/%s/json.shtml", host, token);
	}

	/**
	 * The type of the card that the user is trying to pay with
	 * can be inferred from the card number itself.
	 *
	 * This can typically only be inferred after the first 4
	 * digits have been entered but this can vary.
	 *
	 * @param longCardNumber Number on the front of the card.
	 * @return The card type
	 */
	public CardType determineCardType(String longCardNumber)
	{
		return CardType.detect(longCardNumber);
	}

	/**
	 * Encrypt the card data against the public key for it to be sent
	 * to the order store where it can be decrypted against the
	 * private key and processed as a payment.
	 *
	 * @param card Encapsulation of the card data.
	 * @param publicKey The public key for the environment.
	 * @return The card data as it should be sent to the order store.
	 * @throws EncrypterException
	 */
	public String serialise(CardPaymentData card, String publicKey) throws EncrypterException
	{
		ClientSideEncrypter encrypter = new ClientSideEncrypter(publicKey);
		Gson gson = new Gson();
		String cardString = gson.toJson(card);
		return encrypter.encrypt(cardString);
	}
}
