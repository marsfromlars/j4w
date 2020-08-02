package com.zockerwatte.j4w;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {


  public static void main( String[] args ) {

    print( "j4w (C) 2020 zockerwatte.com" );

    Board board = new Board();
    Field.Status player = Field.Status.RED;
    BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

    while( !board.hasWinner() ) {

      print( "" );
      print( board.toString() );
      print( "" );
      print( player + " to move. Enter column: " );
      try {
        int column = Integer.parseInt( in.readLine() );
        board = board.drop( column, player == Field.Status.RED );
        player = player.opponent();
      }
      catch( Exception ex ) {
        print( ex.getLocalizedMessage() );
      }

    }

    print( "" );
    print( board.toString() );
    print( "Winner is " + ( board.winnerIsRed() ? "RED" : "BLUE" ) );

  }


  private static void print( String line ) {
    System.out.println( line );
  }

}
