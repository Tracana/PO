package ggc.core.exception;

/**
 * Exception for unknown ID.
 */
public class NoIDException  extends Exception{
    /** Class serial number. */
  private static final long serialVersionUID = 201409301048L;

  /** No id specification. */
  private String _noIDSpecification;

    /**
   * @param noIDSpecification
   */
  public NoIDException(String noIDSpecification) {
    _noIDSpecification = noIDSpecification;
  }

  /**
   * @param entrySpecification
   * @param cause
   */
  public NoIDException(String noIDSpecification, Exception cause) {
    super(cause);
    _noIDSpecification = noIDSpecification;
  }

  /**
   * @return the bad entry specification.
   */
  public String getNoIDException() {
    return _noIDSpecification;
  }
}
