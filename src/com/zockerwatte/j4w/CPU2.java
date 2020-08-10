package com.zockerwatte.j4w;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CPU2 implements CPU {

  private static final int MAX_DEPTH = 1;
  public static int[] NORTH = { 0, 1 };
  public static int[] SOUTH = { 0, -1 };
  public static int[] EAST = { 1, 0 };
  public static int[] WEST = { -1, 0 };
  public static int[] NORTHEAST = { 1, 1 };
  public static int[] NORTHWEST = { -1, 1 };
  public static int[] SOUTHEAST = { 1, -1 };
  public static int[] SOUTHWEST = { -1, -1 };

  public static List<int[]> directions = new ArrayList<>();

  static {
    directions.add( NORTH );
    directions.add( SOUTH );
    directions.add( EAST );
    directions.add( WEST );
    directions.add( NORTHEAST );
    directions.add( NORTHWEST );
    directions.add( SOUTHEAST );
    directions.add( SOUTHWEST );
  }

  @Override
  public int play( Board board, Field.Status player ) {

//    int column = (int) ( Math.random() * 7 );
//    return column;

    List<Heuristic> bestResults = calculateBestMoves( board, player, 0 );

    // if there are multiple best results, choose one randomly
    int random = (int) (Math.random() * bestResults.size());

    System.out.println( "BEST MOVES: " );
    bestResults.stream().forEach( m -> {
      System.out.println( m );
    });

    return bestResults.get( random ).columnIndex;

  }

  private List<Heuristic> calculateBestMoves( Board board, Field.Status player, int depth ) {

    List<Heuristic> results = IntStream.range( 0, 7 )
      .filter( column -> board.getColumnHeight( column ) < 6 )
      .mapToObj( column -> dropIfNotYetWon( board, column, player ) )
      .map( newBoard -> calculate( newBoard, player, newBoard.getLastDrop(), depth ) )
      .collect( Collectors.toList() );

    Heuristic best = results.stream().max( Heuristic::compareTo ).get();

    //System.out.println( "BEST " + best );

    return results.stream()
      .filter( result -> result.value == best.value )
      .collect( Collectors.toList() );

  }

  private Board dropIfNotYetWon( Board board, int column, Field.Status player ) {
    if( board.hasWinner() ) {
      return board;
    }
    else {
      return board.drop( column, player == Field.Status.RED );
    }
  }

  Heuristic calculate( Board board, Field.Status player, int columnIndex, int depth ) {

    if( depth <= 1 ) {
      System.out.println( "Calculate: C" + columnIndex + " " + player + " DEP:" + depth );
    }

    if( board.hasWinner() && board.getWinner() == player ) {
      // instant win
      return new Heuristic( columnIndex, Integer.MAX_VALUE, player, depth );
    }
    else {

      int value = IntStream.range( 0, 6 )
        .mapToObj( row -> calculateRow( board, player, row ) )
        .reduce( 0, ( a, b ) -> a + b ); // sum

      int opponentValue = 0;
      if( depth < MAX_DEPTH ) {
        // calculate counter moves
        Field.Status opponent = player.opponent();
        opponentValue = IntStream.range( 0, 7 )
          .filter( column -> board.getColumnHeight( column ) < 6 )
          .mapToObj( column -> board.drop( column, opponent == Field.Status.RED ) )
          .map( newBoard -> calculateBestMoves( newBoard, opponent, depth + 1 ).get( 0 ) )
          .map( heuristic -> heuristic.value )
          .reduce( 0, ( a, b ) -> a + b ); // sum
      }

      Heuristic h = new Heuristic( columnIndex, value - opponentValue, player, depth );
      if( depth <= 1 ) {
        System.out.println( "Own: " + value );
        System.out.println( "Opp: " + opponentValue );
        System.out.println( h );
      }
      return h;

    }

  }

  private int calculateRow( Board board, Field.Status player, int row ) {

    return IntStream.range( 0, 7 )
      .mapToObj( column -> calculateField( board, player, column, row ) )
      .reduce( 0, (a,b) -> a+b ); // sum

  }

  private int calculateField( Board board, Field.Status player, int column, int row ) {

    if( board.get( column, row ).getStatus() == player ) {

      return directions.stream()
        .map( direction -> getLine( board, column, row, direction ) )
        .map( line -> calculateLineValue( line, board, player, column, row ) )
        .reduce( 0, (a,b) -> a+b ); // sum

    }
    else {
      return 0; // field not our color
    }

  }

  private int calculateLineValue( List<Field> line, Board board, Field.Status player, int column, int row ) {

    int totalValue = 0;

    if( line.size() == 4 ) { // not cut of at border

      int startValue = 100;
      int lengthValue = startValue;

      for( int i = 0; i < 4; i++ ) {

        Field field = line.get( i );
        if( field.getStatus() == player ) {
          totalValue += lengthValue;
          lengthValue *= 4;
        }
        else if( field.getStatus() == Field.Status.EMPTY ) {
          totalValue += startValue;
        }
        else { // opponent
          totalValue = 0; // too short
          break;
        }

        if( i > 0 ) {
          totalValue -= 10 * calculateEmptyFieldsBelow(
            board, field.getColumn(), field.getRow() );
        }

      }

    }

    return totalValue;

  }

  int calculateEmptyFieldsBelow( Board board, int column, int row ) {

    int count = 0;
    for( int r = row - 1; r >= 0 && board.get( column, r ).isEmpty(); r-- ) {
      count++;
    }
    return count;

  }

  List<Field> getLine( Board board, int column, int row, int[] direction ) {

    List<Field> line = new ArrayList<>();
    while( line.size() < 4 && board.isOnBoard( column, row ) ) {
      line.add( board.get( column, row ) );
      column += direction[ 0 ];
      row += direction[ 1 ];
    }
    return line;

  }

  class Heuristic implements Comparable<Heuristic> {

    private final int columnIndex;
    private final int value;
    private final Field.Status player;
    private final int depth;

    public Heuristic( int columnIndex, int value, Field.Status player, int depth ) {
      this.columnIndex = columnIndex;
      this.value = value;
      this.player = player;
      this.depth = depth;
    }

    @Override
    public int compareTo( Heuristic o ) {
      return value - o.value; // highest first
    }

    public int getValue() {
      return value;
    }

    public String toString() {
      return "H C" + columnIndex + " PLY:" + player
        + " DEP:" + depth + " >> " + value ;
    }

  }

}
