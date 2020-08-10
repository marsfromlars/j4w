package com.zockerwatte.j4w;

public class Field {

  private final int row;
  private final int column;

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

    public Status opponent() {
      return this == RED ? BLUE : RED;
    }
  };

  private final Status status;

  public Field( Status status, int column, int row ) {
    this.status = status;
    this.row = row;
    this.column = column;
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

  public int getColumn() {
    return column;
  }

  public int getRow() {
    return row;
  }

  public String toString() {
    return column + ":" + row + " " + status.name();
  }

}
