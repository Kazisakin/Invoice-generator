// src/main/java/org/invoice/exception/ServiceException.java
package org.invoice.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String m) { super(m); }
    public ServiceException(String m, Throwable c) { super(m,c); }
}
