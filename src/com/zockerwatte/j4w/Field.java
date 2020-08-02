package com.zockerwatte.j4w;

public class Field {

  public static enum Status {
    RED,
    BLUE,
    EMPTY;

    public String getChar() {
      switch( this ) {
        case RED: return "r";
        case BLUE: return "b";
        default: return ".";
      }
    }
  };

  private final Status status;

  public Field( Status status ) {
    this.status = status;
  }

  public boolean isEmpty() {
    return status == Status.EMPTY;
  }

  public boolean isRed() {
    return status == Status.RED;
  }

  public boolean isBlue() {
    return status == Status.BLUE;
  }

  public String getChar() {
    return status.getChar();
  }

  public Status getStatus() {
    return status;
  }

}
