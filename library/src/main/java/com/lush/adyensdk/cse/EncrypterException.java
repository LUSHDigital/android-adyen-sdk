package com.lush.adyensdk.cse;

/**
 * Created by andrei on 11/12/15
 */
public class EncrypterException extends Exception
{
	private static final long serialVersionUID = 2699577095011945291L;

	/**
	 * Wrapping exception for all JCE encryption related exceptions
	 *
	 * @param message message
	 * @param cause cause
	 */
	public EncrypterException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
