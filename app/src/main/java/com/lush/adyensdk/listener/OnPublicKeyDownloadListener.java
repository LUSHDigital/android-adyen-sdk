package com.lush.adyensdk.listener;

/**
 * Events that respond to downloading the public key.
 *
 * @author Matt Allen
 */
public interface OnPublicKeyDownloadListener
{
	void onSuccess(String publicKey);
	void onFailure();
}
