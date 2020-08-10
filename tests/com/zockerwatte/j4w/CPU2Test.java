package com.zockerwatte.j4w;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.zockerwatte.j4w.CPU2.*;
import static org.junit.Assert.*;

public class CPU2Test {

  private CPU2 cpu;
  private Board board;

  @Before
  public void setup() {
    cpu = new CPU2();
    board = new Board();
  }

  @Test
  public void getLineReturnsARow() {

    List<Field> line = cpu.getLine( board, 4, 0, WEST );
    assertEquals( 4, line.size() );

  }

  @Test
  public void getLineReturnsAColumn() {

    List<Field> line = cpu.getLine( board, 4, 5, SOUTH );
    assertEquals( 4, line.size() );

  }

  @Test
  public void getLineCutsOfAtRightBoarder() {

    List<Field> line = cpu.getLine( board, 5, 0, EAST );
    assertEquals( 2, line.size() );

  }

  @Test
  public void getLineCutsOfAtTopRow() {

    List<Field> line = cpu.getLine( board, 5, 3, NORTH );
    assertEquals( 3, line.size() );

  }

  @Test
  public void thereAreNoEmptyFieldsBelowRow0() {

    assertEquals( 0, cpu.calculateEmptyFieldsBelow( board, 3, 0 ) );

  }

  @Test
  public void thereAreNoEmptyFieldsBelowAFieldInATower() {

    board = Board.load(
        ".......\n"
      + ".......\n"
      + ".......\n"
      + "...r...\n"
      + "...r...\n"
      + "...b...\n"
    );

    assertEquals( 0, cpu.calculateEmptyFieldsBelow( board, 3, 2 ) );

  }

  @Test
  public void thereAreNoFiveEmptyFieldsBelowTopRow() {
    assertEquals( 5, cpu.calculateEmptyFieldsBelow( board, 3, 5 ) );
  }

  @Test
  public void singleChipAtBottomIs_1850() {

    board = Board.load(
        ".......\n"
      + ".......\n"
      + ".......\n"
      + ".......\n"
      + ".......\n"
      + ".......\n"
    );

    int valuePerDiagonalLine = 100 + 100 - 10 + 100 - 20 + 100 - 30;
    int valueForNorthLine = 100 + 100 + 100 - 10 + 100 - 20;;
    int valuePerHorizontalLine = 400;
    int totalValue = 2 * valuePerDiagonalLine + valueForNorthLine + 2 * valuePerHorizontalLine;

    assertEquals( totalValue, cpu.calculate( board, Field.Status.BLUE, 3, 0 ).getValue() );

  }

  @Test
  public void bugfix1_wouldNotCheckForFullLines() {

     board = Board.load(
         ".......\n"
       + ".......\n"
       + ".......\n"
       + "....b..\n"
       + "...rr..\n"
       + "...br..\n" );

     cpu.play( board, Field.Status.BLUE ); // should not throw exception

  }

  @Test
  public void bugfix2_doesNotRecognizeWinsWhenGoingDeep() {

    board = Board.load(
      "r..b...\n" +
      "r..b...\n" +
      "b.br...\n" +
      "r.rr...\n" +
      "r.rb.bb\n" +
      "brbrbrb\n" );

    cpu.play( board, Field.Status.BLUE ); // should not throw exception

  }

  @Test
  public void bugfix3_cpuDoesStupidMoveWhichAllowsOpponentsWinInOneMove() {

    board = Board.load(
      ".......\n" +
      "...r...\n" +
      "...b...\n" +
      "...br..\n" +
      "b..rrr.\n" +
      "b.rbrb.\n" );

    assertFalse( 5 == cpu.play( board, Field.Status.BLUE ) );

  }

  @Test
  public void bugfix4_funnyException() {

    board = Board.load(
      "rrr.rb.\n" +
      "bbr.br.\n" +
      "brbbrr.\n" +
      "rbrrbb.\n" +
      "rrbbrrb\n" +
      "bbrrbbb\n" );

    cpu.play( board, Field.Status.BLUE );

  }


  @Test
  public void bugfix5_cpuDoesStupidMoveWhichAllowsOpponentsWinInOneMove() {

    board = Board.load(
      ".......\n" +
      ".......\n" +
      ".......\n" +
      ".......\n" +
      ".......\n" +
      "..rr..b\n"
    );

    // cpu should drop either 1 or 4
    int drop = cpu.play( board, Field.Status.BLUE );

    assertTrue( drop == 1 || drop == 4 );

  }



  
}