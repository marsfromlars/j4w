package com.zockerwatte.j4w;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

  private final List<List<Field>> columns;
  private boolean hasWinner = false;
  private Field.Status winner;

  public Board() {
    columns = IntStream.range( 0, 7 )
      .mapToObj( i -> createEmptyColumn() )
      .collect( Collectors.toList() );
  }

  private Board( List<List<Field>> columns ) {
    this.columns = columns;
    checkForWinner();
  }

  public static Board load( File file ) throws IOException {
    BufferedReader in = new BufferedReader( new FileReader( file ) );
    StringBuilder data = new StringBuilder();
    String line = in.readLine();
    while( line != null ) {
      data.append( line + "\n" );
      line = in.readLine();
    }
    return load( data.toString() );
  }

  public static Board load( String data ) {
    Board board = new Board();
    List<String> lines = Arrays.stream( data.split( "\n" ) ).collect( Collectors.toList() );
    Collections.reverse( lines );
    for( int row = 0; row < 6; row++ ) {
      String line = lines.get( row );
      for( int column = 0; column < 7; column++ ) {
        char c = line.charAt( column );
        Field.Status status = c == 'r' ? Field.Status.RED : c == 'b' ? Field.Status.BLUE : Field.Status.EMPTY;
        board.columns.get( column ).set( row, new Field( status ) );
      }
    }
    return board;
  }

  private void checkForWinner() {
    for( int r = 0; r < 6; r++ ) {
      for( int c = 0; c < 7; c++ ) {
        Field field = get( c, r );
        Field.Status status = field.getStatus();
        if( status != Field.Status.EMPTY ) {
          if( hasFour( c, r, status ) ) {
            hasWinner = true;
            winner = status;
            return;
          }
        }
      }
    }
  }

  private boolean hasFour( int column, int row, Field.Status status ) {
    if( checkFields( getNorth( column, row ), status ) ) return true;
    if( checkFields( getSouth( column, row ), status ) ) return true;
    if( checkFields( getEast( column, row ), status ) ) return true;
    if( checkFields( getWest( column, row ), status ) ) return true;
    if( checkFields( getNorthWest( column, row ), status ) ) return true;
    if( checkFields( getNorthEast( column, row ), status ) ) return true;
    if( checkFields( getSouthWest( column, row ), status ) ) return true;
    if( checkFields( getSouthEast( column, row ), status ) ) return true;
    return false;
  }

  private boolean checkFields( List<Field> fields, Field.Status status ) {
    if( fields.size() == 4 ) {
      return fields.stream().allMatch( field -> field.getStatus() == status );
    }
    return false;
  }

  private List<Field> getNorth( int column, int row ) {
    return getFields( column, row, 0, 1 );
  }

  private List<Field> getSouth( int column, int row ) {
    return getFields( column, row, 0, -1 );
  }

  private List<Field> getEast( int column, int row ) {
    return getFields( column, row, 1, 0 );
  }

  private List<Field> getWest( int column, int row ) {
    return getFields( column, row, -1, 0 );
  }

  private List<Field> getNorthWest( int column, int row ) {
    return getFields( column, row, -1, 1 );
  }

  private List<Field> getNorthEast( int column, int row ) {
    return getFields( column, row, -1, 1 );
  }

  private List<Field> getSouthWest( int column, int row ) {
    return getFields( column, row, 1, -1 );
  }

  private List<Field> getSouthEast( int column, int row ) {
    return getFields( column, row, -1, -1 );
  }

  private List<Field> getFields( int column, int row, int columnStep, int rowStep ) {
    List<Field> fields = new ArrayList<>();
    while( isOnBoard( column, row ) && fields.size() < 4 ) {
      fields.add( get( column, row ) );
      column += columnStep;
      row += rowStep;
    }
    return fields;

  }

  private boolean isOnBoard( int column, int row ) {
    return column >= 0 && column < 7 && row >= 0 && row < 6;
  }

  private List<Field> createEmptyColumn() {
    return IntStream.range( 0, 6 )
      .mapToObj( i -> new Field( Field.Status.EMPTY ) )
      .collect( Collectors.toList() );
  }

  public boolean isEmpty() {
    return columns.stream()
      .allMatch( column -> isColumnEmpty( column ) );
  }

  private boolean isColumnEmpty( List<Field> column ) {
    return column.stream()
      .allMatch( field -> field.isEmpty() );
  }

  public Board drop( int dropColumnIndex, boolean isRed ) {

    if( hasWinner() ) {
      throw new RuntimeException( "Cannot drop, because there is already a winner." );
    }

    if( dropColumnIndex < 0 || dropColumnIndex > 6 ) {
      throw new ArrayIndexOutOfBoundsException( "Cannot drop in column " + dropColumnIndex );
    }

    return new Board(
      IntStream.range( 0, 7 )
        .mapToObj( i -> dropIfColumnIndexIsDropIndex( dropColumnIndex, i, isRed ) )
        .collect( Collectors.toList() )
    );

  }

  private List<Field> dropIfColumnIndexIsDropIndex( int dropIndex, int columnIndex, boolean isRed ) {
    if( dropIndex == columnIndex ) {
      return dropInColumn( dropIndex, isRed );
    }
    else {
      return columns.get( columnIndex );
    }
  }

  private List<Field> dropInColumn( int dropIndex, boolean isRed ) {

    // all existing fields
    List<Field> newColumn = columns.get( dropIndex ).stream()
      .filter( field -> !field.isEmpty() )
      .collect( Collectors.toList() );

    if( newColumn.size() >= 6 ) {
      throw new ArrayIndexOutOfBoundsException( "Column " + dropIndex + " already full." );
    }

    // the new field
    newColumn.add( new Field( isRed ? Field.Status.RED : Field.Status.BLUE ) );

    // fill the rest with empty
    while( newColumn.size() < 6 ) {
      newColumn.add( new Field( Field.Status.EMPTY ) );
    }

    return newColumn;

  }

  public Field get( int column, int row ) {
    return columns.get( column ).get( row );
  }

  public String toString() {
    return IntStream.range( 0, 6 )
      .mapToObj( i -> toStringRow( 5 - i ) + "\n" )
      .collect( Collectors.joining() )
      + "_______\n"
      + "0123456";
  }

  private String toStringRow( int row ) {
    return IntStream.range( 0, 7 )
      .mapToObj( i -> "" + get( i, row ).getChar() )
      .collect( Collectors.joining() );
  }

  public boolean hasWinner() {
    return hasWinner;
  }

  public boolean winnerIsRed() {
    return winner == Field.Status.RED;
  }

  public boolean winnerIsBlue() {
    return !winnerIsRed();
  }

  public int getColumnHeight( int columnIndex ) {
    return (int) columns.get( columnIndex ).stream().filter( field -> !field.isEmpty() ).count();
  }

  public boolean equals( Object other ) {
    if( other.getClass() != Board.class ) {
      return false;
    }
    Board otherBoard = (Board) other;
    if( hasWinner != otherBoard.hasWinner ) {
      return false;
    }
    if( winner != otherBoard.winner ) {
      return false;
    }
    return IntStream.range( 0, 7 )
      .filter( i -> columnEqual( otherBoard, i ) )
      .count() == 7;
  }

  private boolean columnEqual( Board otherBoard, int columnIndex ) {

    List<Field> thisColumn = columns.get( columnIndex );
    List<Field> otherColumn = columns.get( columnIndex );

    return IntStream.range( 0, 6 )
      .filter( i -> thisColumn.get( i ).getStatus() == thisColumn.get( i ).getStatus() )
      .count() == 6;

  }

  public void save( File file ) throws FileNotFoundException {
    PrintWriter out = new PrintWriter( file );
    out.println( toString() );
    out.close();
  }

}
