package com.zockerwatte.j4w;

public class CPU2 implements CPU {

  @Override
  public int play( Board board, Field.Status player ) {
    return (int) (Math.random() * 7);
  }

}
