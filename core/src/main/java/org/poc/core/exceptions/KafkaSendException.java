/**
 * 
 */
package org.poc.core.exceptions;

/**
 * @author Sankha
 *
 */
public class KafkaSendException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2243409831671849892L;

	/**
	 * 
	 */
	public KafkaSendException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public KafkaSendException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public KafkaSendException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public KafkaSendException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public KafkaSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
