package com.zockerwatte.j4w;

public interface CPU {

  /**
   * Return column for next move
   *
   * @param board Current board
   * @param player Color to play
   * @return Column to drop chip into
   *
   */
  int play( Board board, Field.Status player );

}
