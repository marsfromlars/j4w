package com.zockerwatte.j4w;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {


  public static void main( String[] args ) throws IOException {

    print( "j4w (C) 2020 zockerwatte.com" );

    Board board = new Board();
    Field.Status player = Field.Status.RED;
    BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

    print( "" );
    print( "0 - human vs human" );
    print( "1 - human vs cpu1" );
    print( "2 - human vs cpu2" );
    print( "3 - cpu1 vs human" );
    print( "4 - cpu2 vs human" );
    print( "5 - cpu1 vs cpu2" );
    print( "6 - cpu2 vs cpu1" );

    int mode = Integer.parseInt( in.readLine() );
    if( mode < 0 && mode > 6 ) {
      throw new RuntimeException( "Invalid mode." );
    }

    List<Character> plays = new ArrayList<>();

    if( mode == 0 ) {
      plays.add( 'h' );
      plays.add( 'h' );
    }
    else if( mode == 1 ) {
      plays.add( 'h' );
      plays.add( '1' );
    }
    else if( mode == 2 ) {
      plays.add( 'h' );
      plays.add( '2' );
    }
    else if( mode == 3 ) {
      plays.add( '1' );
      plays.add( 'h' );
    }
    else if( mode == 4 ) {
      plays.add( '2' );
      plays.add( 'h' );
    }
    else if( mode == 5 ) {
      plays.add( '1' );
      plays.add( '2' );
    }
    else if( mode == 6 ) {
      plays.add( '2' );
      plays.add( '1' );
    }

    int playIndex = 0;
    CPU1 cpu1 = new CPU1();
    CPU2 cpu2 = new CPU2();

    while( !board.hasWinner() ) {

      char currentPlayer = plays.get( playIndex++ );
      if( playIndex > 1 ) {
        playIndex = 0;
      }

      print( "" );
      print( board.toString() );
      print( "" );
      print( player + " to move. Enter column: " );

      try {
        int drop = -1;
        if( currentPlayer == 'h' ) {
          drop = Integer.parseInt( in.readLine() );
        }
        else {
          if( currentPlayer == '1' ) {
            drop = cpu1.play( board, player );
          }
          else {
            drop = cpu2.play( board, player );
          }
          print( "CPU chooses: " + drop );
        }
        board = board.drop( drop, player == Field.Status.RED );
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
