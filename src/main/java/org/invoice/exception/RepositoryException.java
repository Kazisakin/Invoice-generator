// src/main/java/org/invoice/exception/RepositoryException.java
package org.invoice.exception;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String m) { super(m); }
    public RepositoryException(String m, Throwable c) { super(m,c); }
}
