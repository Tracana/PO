package ggc.core.exception;

/**
 * Exception for unknown ID.
 */
public class NoProductIdException  extends Exception{
    /** Class serial number. */
  private static final long serialVersionUID = 201409301048L;

  /** No id specification. */
  private String _noIDSpecification;

    /**
   * @param noIDSpecification
   */
  public NoProductIdException(String noIDSpecification) {
    _noIDSpecification = noIDSpecification;
  }

  /**
   * @param entrySpecification
   * @param cause
   */
  public NoProductIdException(String noIDSpecification, Exception cause) {
    super(cause);
    _noIDSpecification = noIDSpecification;
  }

  /**
   * @return the bad entry specification.
   */
  public String getNoProductIdException() {
    return _noIDSpecification;
  }
}
