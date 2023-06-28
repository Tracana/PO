package ggc.core.exception;

public class TooMuchException extends Exception{
     /** Class serial number. */
  private static final long serialVersionUID = 201409301048L;

  /** too much amount specification. */
  private int _tooMuchSpecification;

    /**
   * @param duplicateSpecification
   */
  public TooMuchException(int tooMuchSpecification) {
    _tooMuchSpecification = tooMuchSpecification;
  }

  /**
   * @param tooMuchSpecification
   * @param cause
   */
  public TooMuchException(int tooMuchSpecification, Exception cause) {
    super(cause);
    _tooMuchSpecification = tooMuchSpecification;
  }

  /**
   * @return the bad entry specification.
   */
  public int getTooMuchException() {
    return _tooMuchSpecification;
  }
}