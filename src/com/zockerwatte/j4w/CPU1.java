package com.zockerwatte.j4w;

public class CPU1 implements CPU {

  @Override
  public int play( Board board, Field.Status player ) {
	  
    boolean isRed = player == Field.Status.RED;
	
	if (winFinder (board, player) != -1)
		return winFinder(board, player);

    int[] values = eval(board, isRed, player, 3);
        
    System.out.println(values[0] + ", " + values[1] + ", " + values[2] + ", " + values[3] + ", " + values[4] + ", " + values[5] + ", " + values[6]);
    
    return selectMove (board, values, player);
    
  }
  
  private int winFinder (Board b, Field.Status p) {
	  
	  if (winPossible(b, p) != -1)
		  return winPossible(b, p);
	    
	  if (winPossible(b, p.opponent()) != -1)
		  return winPossible(b, p.opponent());
	    
	  return -1;
	  
  }
  
  private int[] eval (Board b, boolean isRed, Field.Status p, int recDepth) {
	  
	  int[] values = {0,0,0,0,0,0,0};
	    
	  for (int i = 0; i < 7; i++) {
		  if (b.getColumnHeight(i) < 6)
			  values[i] = evaluateDrop(b.drop(i, isRed), i, p, recDepth);
		  else 
			  values[i] = evaluateDrop(b, i, p, recDepth);
	  }
	  
	  return values;
	  
  }
  
  private int selectMove (Board b, int[] vals, Field.Status p) {
	  
	  boolean isRed = p == Field.Status.RED;
	  
	  for (int i = 0; i < vals.length; i++) {
		  
		  if (b.getColumnHeight(i) >= 6) {
			  vals[i] = 3 * findMin(vals);
		  } else if (contains(vals) && winPossible(b.drop(i, isRed), p.opponent()) != -1) {
			  vals[i] =  findMin(vals);
		  }		  
		  
	  }
	  
	  return choose(vals);
	  
  }
  
  private int findMin (int[] vals) {
	  
	  int t = 0;
	  
	  for (int i = 0; i < vals.length; i++) {
		  
		  t += Math.abs(vals[i]);
		  
	  }
	  
	  return -t;
	  
  }
  
  private boolean contains (int[] vals) {
	  
	  for (int i = 0; i < vals.length; i++) {
		  
		  if (vals[i] == -10)
			  return true;
		  
	  }
	  
	  return false;
	  
  } 
  
  private int choose (int[] array) {	 
	  
	  int tempMax = array[0];
	  int index = 0;
	  for (int i = 0; i < array.length; i++) {
		  
		  if (array[i] > tempMax) {
			  tempMax = array[i];
			  index = i;
		  }
			  		  
	  } 
	  
	  return index;
	  
  }
  
  
  private int evaluateDrop (Board b, int whereDropped, Field.Status p, int recDepth) {
	  
	  int score = 0;
	  boolean debug = false;
	  boolean isRed = p == Field.Status.RED;
	  
	  if (recDepth <= 0) {

		  int w = whereDropped;
		  int h = b.getColumnHeight(whereDropped) - 1;
		  if (h == -1) 
			  h = 0;
		  if (debug)
			  System.out.println(w + " " + h);
		  
		  
		  double exp = 1.0;
		  int i = 1;
		  //horizontal positive edge
		  while (w+i < 7) {
			  if (b.get(w+i, h).getStatus().equals(p)) {
				  score += Math.pow(2.0, exp);
				  exp ++;
			  } else if (b.get(w+i, h).getStatus().equals(Field.Status.EMPTY)) {
				  score ++;
			  } else if (b.get(w+i, h).getStatus().equals(p.opponent())) {
				  break;
			  }
			  i++;
		  }
		  
		  if (debug)
			  System.out.println("score after pos hor: " + score);
		  
		  exp = 1.0;
		  i = 1;
		  //horizontal negative edge
		  while (w-i >= 0) {
			  if (b.get(w-i, h).getStatus().equals(p)) {
				  score += Math.pow(2.0, exp);
				  exp ++;
			  } else if (b.get(w-i, h).getStatus().equals(Field.Status.EMPTY)) {
				  score ++;
			  } else if (b.get(w-i, h).getStatus().equals(p.opponent())) {
				  break;
			  }
			  i++;
			  //System.out.println("test");
		  }
		  
		  if (debug)
			  System.out.println("score after neg hor: " + score);
		  
		  //vertical positive edge
		  score += (5 - h);
		  
		  if (debug)
			  System.out.println("score after pos vert: " + score);
		  
		  exp = 1.0;
		  i = 1;
		  //vertical negative edge
		  while (h-i >= 0) {
			  if (b.get(w, h-i).getStatus().equals(p)) {
				  score += Math.pow(2.0, exp);
				  exp ++;
			  } else if (b.get(w, h-i).getStatus().equals(Field.Status.EMPTY)) {
				  score ++;
			  } else if (b.get(w, h-i).getStatus().equals(p.opponent())) {
				  break;
			  }
			  i++;
		  }
		  
		  if (debug)
			  System.out.println("score after neg vert: " + score);
		  
		  
		  exp = 1.0;
		  i = 1;
		  //diagonal top right
		  while (w+i < 7 && h+i < 6) {
			  if (b.get(w+i, h+i).getStatus().equals(p)) {
				  score += Math.pow(2.0, exp);
				  exp ++;
			  } else if (b.get(w+i, h+i).getStatus().equals(Field.Status.EMPTY)) {
				  score ++;
			  } else if (b.get(w+i, h+i).getStatus().equals(p.opponent())) {
				  break;
			  }
			  i++;  
		  }
		  
		  exp = 1.0;
		  i = 1;
		  //diagonal top left
		  while (w-i >= 0 && h+i < 6) {
			  if (b.get(w-i, h+i).getStatus().equals(p)) {
				  score += Math.pow(2.0, exp);
				  exp ++;
			  } else if (b.get(w-i, h+i).getStatus().equals(Field.Status.EMPTY)) {
				  score ++;
			  } else if (b.get(w-i, h+i).getStatus().equals(p.opponent())) {
				  break;
			  }
			  i++;  
		  }
		  
		  exp = 1.0;
		  i = 1;
		  //diagonal bottom right
		  while (w+i < 7 && h-i >= 0) {
			  if (b.get(w+i, h-i).getStatus().equals(p)) {
				  score += Math.pow(2.0, exp);
				  exp ++;
			  } else if (b.get(w+i, h-i).getStatus().equals(Field.Status.EMPTY)) {
				  score ++;
			  } else if (b.get(w+i, h-i).getStatus().equals(p.opponent())) {
				  break;
			  }
			  i++;  
		  }
		  
		  exp = 1.0;
		  i = 1;
		  //diagonal bottom left
		  while (w-i >= 0 && h-i >= 0) {
			  if (b.get(w-i, h-i).getStatus().equals(p)) {
				  score += Math.pow(2.0, exp);
				  exp ++;
			  } else if (b.get(w-i, h-i).getStatus().equals(Field.Status.EMPTY)) {
				  score ++;
			  } else if (b.get(w-i, h-i).getStatus().equals(p.opponent())) {
				  break;
			  }
			  i++;  
		  }
  	  } else {
  		  
  		  for (int i = 0; i < 7; i++) {
  			  
  			  if (b.getColumnHeight(i) < 6) {
  				  if (!b.hasWinner() && !b.isDraw())
  					  score -= evaluateDrop(b.drop(i, isRed), i, p.opponent(), recDepth--);
  			  }
  			  
  		  } 
  		  
  	  }
	  
	  return score;
	  
  }
  

  private int winPossible (Board b, Field.Status p) {	 
	  
	  for (int i = 0; i < 7; i++) {
		  
		  if (b.getColumnHeight(i) < 6 && b.drop(i, p == Field.Status.RED).hasWinner())
			  return i;
		  
	  }
	  
	  return -1;
	  
  }

}
