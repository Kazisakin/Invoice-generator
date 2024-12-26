// File: src/main/java/org/invoice/exception/RepositoryException.java

package org.invoice.exception;

/**
 * Custom unchecked exception for repository layer errors.
 */
public class RepositoryException extends RuntimeException {

    /**
     * Constructs a new RepositoryException with the specified detail message.
     *
     * @param message The detail message.
     */
    public RepositoryException(String message) {
        super(message);
    }

    /**
     * Constructs a new RepositoryException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
