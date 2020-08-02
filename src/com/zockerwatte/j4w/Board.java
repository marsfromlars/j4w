package com.zockerwatte.j4w;

import java.util.Map;

public class Board {

  private final boolean isEmpty;

  public Board( boolean isEmpty ) {
    this.isEmpty = isEmpty;
  }

  public Board() {
    this.isEmpty = true;
  }

  public boolean isEmpty() {
    return isEmpty;
  }

  public Board drop( int column, boolean isRed ) {
    return new Board( false );
  }

  public Field get( int column, int row ) {
    return new Field();
  }

}
