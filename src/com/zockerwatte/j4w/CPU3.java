package com.zockerwatte.j4w;

public class CPU3 implements CPU {

  @Override
  public int play( Board board, Field.Status player ) {

    while( true ) {
      int column = (int) ( Math.random() * 7 );
      if( board.getColumnHeight( column ) < 6 ) {
        return column;
      }
    }

  }

}
