// File: src/main/java/org/invoice/exception/ServiceException.java

package org.invoice.exception;

/**
 * Custom unchecked exception for service layer errors.
 */
public class ServiceException extends RuntimeException {

    /**
     * Constructs a new ServiceException with the specified detail message.
     *
     * @param message The detail message.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServiceException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
