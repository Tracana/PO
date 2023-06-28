package ggc.core.exception;

public class InvalidTimeException extends Exception{
    /** Class serial number. */
 private static final long serialVersionUID = 201409301048L;

 /** No id specification. */
 private int _invalidTimeSpecification;

   /**
  * @param duplicateSpecification
  */
 public InvalidTimeException(int invalidTimeSpecification) {
    _invalidTimeSpecification = invalidTimeSpecification;
 }

 /**
  * @param dinvalidTimeSpecification
  * @param cause
  */
 public InvalidTimeException(int invalidTimeSpecification, Exception cause) {
   super(cause);
   _invalidTimeSpecification = invalidTimeSpecification;
 }

 /**
  * @return the bad entry specification.
  */
 public int getInvalidTimeException() {
   return _invalidTimeSpecification;
 }
}