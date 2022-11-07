/**
 * 
 */
package org.poc.bloodbank.exception;

/**
 * @author Sankha
 *
 */
public class DuplicateException extends RuntimeException {

/**
	 * 
	 */
	public DuplicateException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public DuplicateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public DuplicateException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DuplicateException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
