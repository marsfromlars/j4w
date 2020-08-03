package com.zockerwatte.j4w;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class BoardTest {

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void newBoardShouldBeEmpty() {
    assertTrue( new Board().isEmpty() );
  }

  @Test
  public void notEmptyAfterFirstDrop() {
    assertFalse( new Board().drop( 0, true ).isEmpty() );
  }

  @Test
  public void dropInEmptyColumnFillsField0() {
    assertFalse( new Board().drop( 0, true ).get( 0, 0 ).isEmpty() );
  }


  @Test
  public void dropRedChipLetsFieldBeRed() {
    assertTrue( new Board().drop( 0, true ).get( 0, 0 ).isRed() );
  }


  @Test
  public void dropBlueChipLetsFieldBeBlue() {
    assertTrue( new Board().drop( 0, false ).get( 0, 0 ).isBlue() );
  }


  @Test
  public void boardNotEmptyAfterDrop() {
    assertFalse( new Board().drop( 0, true ).isEmpty() );
  }

  @Test
  public void dropIncreasesColumnHeight() {

    Board board = new Board();

    assertEquals( 0, board.getColumnHeight( 3 ) );
    assertEquals( 1, board.drop( 3, true ).getColumnHeight( 3 ) );
    assertEquals( 4, board.drop( 3, true ).drop( 3, true ).drop( 3, true ).drop( 3, true ).getColumnHeight( 3 ) );

  }

  @Test( expected = ArrayIndexOutOfBoundsException.class )
  public void throwsExceptionWhenFull() {
    new Board()
      .drop( 0, true )
      .drop( 0, true )
      .drop( 0, true )
      .drop( 0, false )
      .drop( 0, true )
      .drop( 0, true )
      .drop( 0, true );
  }

  @Test( expected = ArrayIndexOutOfBoundsException.class )
  public void throwsExceptionWhenDroppingLessThan0() {
    new Board().drop( -1, true );
  }

  @Test( expected = ArrayIndexOutOfBoundsException.class )
  public void throwsExceptionWhenDroppingGreaterThan6() {
    new Board().drop( 7, true );
  }

  @Test
  public void toStringShowsDebugOutput() {

  assertEquals(
          "......b\n"
        + "......r\n"
        + ".b....r\n"
        + ".r....b\n"
        + "bb.b..b\n"
        + "rb.rbbr\n"
        + "_______\n"
        + "0123456"
      , new Board()
      .drop( 0, true )
      .drop( 0, false )
      .drop( 1, false )
      .drop( 1, false )
      .drop( 1, true )
      .drop( 1, false )
      .drop( 3, true )
      .drop( 3, false )
      .drop( 4, false )
      .drop( 5, false )
      .drop( 6, true )
      .drop( 6, false )
      .drop( 6, false )
      .drop( 6, true )
      .drop( 6, true )
      .drop( 6, false )
      .toString()
    );

  }

  @Test
  public void emptyBoardHasNoWinner() {
    assertFalse( new Board().hasWinner() );
  }

  @Test
  public void fourInColumnIsAWin() {

    Board board = new Board()
      .drop( 3, false )
      .drop( 3, true )
      .drop( 3, true )
      .drop( 3, true );

    assertFalse( board.hasWinner() );

      board = board.drop( 3, true );

    assertTrue( board.hasWinner() && board.winnerIsRed() );

  }

  @Test
  public void fourInRowIsAWin() {

    Board board = new Board()
      .drop( 1, false )
      .drop( 2, true )
      .drop( 3, true )
      .drop( 4, true )
      .drop( 4, false )
      .drop( 3, false )
      .drop( 2, false );

    assertFalse( board.hasWinner() );

      board = board.drop( 1, false );

    assertTrue( board.hasWinner() && board.winnerIsBlue() );

  }

  @Test
  public void fourInDiagonalIsAWin() {

    Board board = new Board()
      .drop( 1, false )
      .drop( 2, true )
      .drop( 3, true )
      .drop( 4, true )
      .drop( 2, false )
      .drop( 3, true )
      .drop( 4, false )
      .drop( 3, false )
      .drop( 4, false );

    assertFalse( board.hasWinner() );

      board = board.drop( 4, false );

    assertTrue( board.hasWinner() && board.winnerIsBlue() );

  }

  @Test( expected = RuntimeException.class )
  public void mayNotDropAfterWin() {

    Board board = new Board()
      .drop( 3, false )
      .drop( 3, true )
      .drop( 3, true )
      .drop( 3, true )
      .drop( 3, true )
      .drop( 3, true );

  }

  @Test
  public void equalDropsIsEqual() {

    Board b1 = new Board();
    Board b2 = new Board();

    b1.drop( 2, false );
    b1.drop( 1, true );
    b1.drop( 3, false );
    b1.drop( 3, true );
    b1.drop( 4, true );
    b1.drop( 5, false );
    b1.drop( 1, true );
    b1.drop( 2, false );
    b1.drop( 6, false );

    b2.drop( 2, false );
    b2.drop( 1, true );
    b2.drop( 3, false );
    b2.drop( 3, true );
    b2.drop( 4, true );
    b2.drop( 5, false );
    b2.drop( 1, true );
    b2.drop( 2, false );
    b2.drop( 6, false );

    assertTrue( b1.equals( b2 ) && b2.equals( b1 ) );

  }

  @Test
  public void saveToFileAndLoadAgainIsEqual() throws IOException {

    Board board = new Board()
      .drop( 0, true )
      .drop( 1, false )
      .drop( 1, true )
      .drop( 1, true )
      .drop( 3, false )
      .drop( 4, true )
      .drop( 4, true )
      .drop( 5, true )
      .drop( 6, false );

    File file = File.createTempFile( "f4w-board-test-", ".txt" );
    try {

      board.save( file );
      Board board2 = Board.load( file );

      assertEquals( board, board2 );

    }
    finally {
      file.delete();
    }

  }


  @Test
  public void initializeFromStringJustWorksFine_empty() {

    Board emptyBoard = Board.load(
        ".......\n"
      + ".......\n"
      + ".......\n"
      + ".......\n"
      + ".......\n"
      + ".......\n"
    );

    assertTrue( emptyBoard.isEmpty() );

  }

  @Test
  public void initializeFromStringJustWorksFine() {

    Board board = Board.load(
          ".......\n"
        + "......r\n"
        + ".r....b\n"
        + ".r....b\n"
        + ".bbb..r\n"
        + ".rbrr.b\n"
    );

    assertTrue( board.get( 1, 3 ).isRed() );
    assertTrue( board.get( 1, 2 ).isRed() );
    assertTrue( board.get( 1, 1 ).isBlue() );
    assertTrue( board.get( 1, 0 ).isRed() );
    assertTrue( board.get( 2, 1 ).isBlue() );
    assertTrue( board.get( 2, 0 ).isBlue() );
    assertTrue( board.get( 3, 1 ).isBlue() );
    assertTrue( board.get( 3, 0 ).isRed() );
    assertTrue( board.get( 4, 0 ).isRed() );
    assertTrue( board.get( 6, 4 ).isRed() );
    assertTrue( board.get( 6, 3 ).isBlue() );
    assertTrue( board.get( 6, 2 ).isBlue() );
    assertTrue( board.get( 6, 1 ).isRed() );
    assertTrue( board.get( 6, 0 ).isBlue() );
    assertTrue( board.getColumnHeight( 0 ) == 0 );
    assertTrue( board.getColumnHeight( 1 ) == 4 );
    assertTrue( board.getColumnHeight( 2 ) == 2 );
    assertTrue( board.getColumnHeight( 3 ) == 2 );
    assertTrue( board.getColumnHeight( 4 ) == 1 );
    assertTrue( board.getColumnHeight( 5 ) == 0 );
    assertTrue( board.getColumnHeight( 6 ) == 5 );

  }

}