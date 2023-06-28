package ggc.core.exception;

public class DuplicateIDException extends Exception{
     /** Class serial number. */
  private static final long serialVersionUID = 201409301048L;

  /** No id specification. */
  private String _duplicateIDSpecification;

    /**
   * @param duplicateSpecification
   */
  public DuplicateIDException(String duplicateSpecification) {
    _duplicateIDSpecification = duplicateSpecification;
  }

  /**
   * @param duplicateSpecification
   * @param cause
   */
  public DuplicateIDException(String duplicateSpecification, Exception cause) {
    super(cause);
    _duplicateIDSpecification = duplicateSpecification;
  }

  /**
   * @return the bad entry specification.
   */
  public String getDuplicateIDException() {
    return _duplicateIDSpecification;
  }
}
