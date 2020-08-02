package com.zockerwatte.j4w;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

  @Test(expected = ArrayIndexOutOfBoundsException.class)
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
      .drop( 3, true )
      .drop( 3, true );

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
      .drop( 2, false )
      .drop( 1, false );

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
      .drop( 4, false )
      .drop( 4, false );

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

}