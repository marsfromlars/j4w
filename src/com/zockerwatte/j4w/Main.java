package com.zockerwatte.j4w;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {


  public static void main( String[] args ) throws IOException {

    print( "j4w (C) 2020 zockerwatte.com" );

    BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

    print( "" );
    print( "0 - human vs human" );
    print( "1 - human vs cpu1" );
    print( "2 - human vs cpu2" );
    print( "3 - cpu1 vs human" );
    print( "4 - cpu2 vs human" );
    print( "5 - cpu1 vs cpu2" );
    print( "6 - cpu2 vs cpu1" );
    print( "7 - cpu1 vs cpu1" );
    print( "8 - cpu2 vs cpu2" );
    print( "9 - cpu1 vs cpu3" );
    print( "10- cpu2 vs cpu3" );

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
    else if( mode == 7 ) {
      plays.add( '1' );
      plays.add( '1' );
    }
    else if( mode == 8 ) {
      plays.add( '2' );
      plays.add( '2' );
    }
    else if( mode == 9 ) {
      plays.add( '1' );
      plays.add( '3' );
    }
    else if( mode == 10 ) {
      plays.add( '2' );
      plays.add( '3' );
    }

    int games = 1;
    if( ( plays.get( 0 ) != 'h' ) && ( plays.get( 1 ) != 'h' ) ) {

      print( "Number of games: " );
      games = Integer.parseInt( in.readLine() );
    }

    int redWins = 0;
    int blueWins = 0;
    int draws = 0;
    long t0 = System.currentTimeMillis();
    Map<Character,Long> thinkingTimes = new TreeMap<>();

    for( int i = 0; i < games; i++ ) {

      int playIndex = 0;
      CPU1 cpu1 = new CPU1();
      CPU2 cpu2 = new CPU2();
      CPU3 cpu3 = new CPU3();
      Board board = new Board();
      Field.Status player = Field.Status.RED;

      while( !board.hasWinner() && !board.isDraw() ) {

        long moveT0 = System.currentTimeMillis();

        char currentPlayer = plays.get( playIndex++ );
        if( playIndex > 1 ) {
          playIndex = 0;
        }

        print( "" );
        print( board.toString() );
        print( "" );

        int drop = -1;
        if( currentPlayer == 'h' ) {
          boolean ok = false;
          while( !ok ) {
            try {
              print( "" );
              print( player + " to move. Enter column: " );
              drop = Integer.parseInt( in.readLine() );
              board = board.drop( drop, player == Field.Status.RED );
              player = player.opponent();
              ok = true;
            }
            catch( Exception ex ) {
              print( "" + ex );
            }
          }
        }
        else {
          if( currentPlayer == '1' ) {
            drop = cpu1.play( board, player );
          }
          else if( currentPlayer == '2' ) {
            drop = cpu2.play( board, player );
          }
          else {
            drop = cpu3.play( board, player );
          }
          print( "CPU" + currentPlayer + " chooses: " + drop );
          board = board.drop( drop, player == Field.Status.RED );
          player = player.opponent();
        }

        long moveTime = System.currentTimeMillis() - moveT0;
        if( !thinkingTimes.containsKey( currentPlayer) ) {
          thinkingTimes.put( currentPlayer, moveTime );
        }
        else {
          thinkingTimes.put( currentPlayer, moveTime + thinkingTimes.get( currentPlayer ) );
        }

      }

      print( "" );
      print( board.toString() );
      if( board.isDraw() ) {
        print( "Draw. No winner." );
        draws++;
      }
      else {
        print( "Winner is " + ( board.winnerIsRed() ? "RED" : "BLUE" ) );
        if( board.winnerIsRed() ) {
          redWins++;
        }
        else {
          blueWins++;
        }
      }

    }

    if( games > 1 ) {

      print( "" );
      print( "Final results:" );
      print( "" );
      print( "Red (CPU" + plays.get( 0 ) + ") wins " + redWins + " games. (" + (int) ((float) redWins / (float) ( redWins + blueWins + draws ) * 100 )+ "%)" );
      print( "Blue (CPU" + plays.get( 1 ) + ") wins " + blueWins + " games. (" + (int) ((float) blueWins / (float) ( redWins + blueWins + draws ) * 100) + "%)" );
      print( draws + " draws." );
      print( "Total time " + (System.currentTimeMillis() - t0) + " ms" );
      thinkingTimes.keySet().stream().forEach( player -> {
        print( "Thinking time CPU" + player + " " + thinkingTimes.get( player ) + " ms" );
      });

    }

  }


  private static void print( String line ) {
    System.out.println( line );
  }

}
