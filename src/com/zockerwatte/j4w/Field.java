package com.zockerwatte.j4w;

public class Field {

  public boolean isEmpty() {
    return false;
  }

  public boolean isRed() {
    return true;
  }

  public boolean isBlue() {
    return !isRed();
  }

}
