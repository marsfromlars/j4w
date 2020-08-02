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

}