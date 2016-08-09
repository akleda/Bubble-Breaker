/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bubblebreaker1_0;

import java.util.ArrayList;
import javafx.scene.shape.Line;

/**
 *  Class for creating field of colors represented by integers 1 - NUMBER_OF_COLORS. The class allows to search in 
 *  the field for other positions arund a specified one which contain the same color.
 *  It also does some other operation over the field.
 * @author Adelka
 */
public class Bubbles {
    private int[][] bubbleField;
    private ArrayList<Position> sameColorAround = new ArrayList<>();
    private int col;
    private int row;
    private final static int NUMBER_OF_COLORS = 4;
   
    /**
     * Creates bubble field with the specified number of columns and rows
     * @param col
     * @param row 
     */
    public Bubbles(int col, int row){
        this.col = col;
        this.row = row;
        bubbleField = new int[col][row];
        populateField();
    }
    
    /**
     * Creates bubble field with the default number of columns and rows
     */
    public Bubbles(){
        this(15,10);        
    }
    
    /**
     * Populate field with random colors (represendted by integers 1 - NUMBER_OF_COLORS)
     */
    private void populateField() {
        for (int i = 0; i<getCol(); i++){
            for (int j = 0; j<getRow(); j++ ){
                bubbleField[i][j] = getRandomColor();
                //bubbleField[i][j] = 0;
            }
        }
    }
    
    /**
     * Return random color specified by integer 1 - NUMBER_OF_COLORS
    */
    private int getRandomColor() {
        return (int) (Math.random()*NUMBER_OF_COLORS) + 1;
    }
    
    /**
     pomocná metoda pro zobrazení, nutno přepsat pro vykreslování na plátno
     */
//    public void vypis(){
//        for (int j = 0; j<getRow(); j++){
//            for (int i = 0; i < getCol(); i++) {
//                System.out.print(getBubbleField()[i][j]+" ");
//            }
//            System.out.println(""); 
//        }
//    }
    
    /**
     * Find out if there is any other possible move in the game
     * @return 
     */
    public boolean endOfGame(){
        for (int i = 0; i < getCol(); i++) {
            for (int j = 0; j<getRow(); j++){
                getSameColorAround(i,j);
                if (sameColorAround.size()>1) {
                    sameColorAround.clear();
                    return false;}
            }
        }
        return true;        
    }
    
    /**
     * Null the same colors in the list of colors around the selected position
     */
    public void setColorToNull(){
        if (sameColorAround.size()>1){
        sameColorAround.stream().forEach((sca) -> {
            setColorAt(((Position) sca).x, ((Position) sca).y, 0);
        });
        }
    }
    
    /**
     * Move bubbles down if there is a space between them in a column
     */
    public void moveBubblesDown(){
        for (int i=0; i<getCol(); i++){
            boolean firstColor = false;
            for (int j=0; j<getRow(); j++){
                int bubble = getBubbleField()[i][j];
                if(bubble != 0){
                    firstColor = true;
                } else if (bubble == 0 && firstColor){
                    moveDown(i,j);
                    j=0;
                    firstColor = false;
                }
            }
        }
    }
    
    /**
     * Recursive method helping to move the bubbles down in case there is a space between them.
     * @param i
     * @param j 
     */
    private void moveDown(int i, int j) {
        if (j>0){
        bubbleField[i][j] = getBubbleField() [i][--j];
        moveDown(i, j);
        } 
        else bubbleField[i][0]=0;
    }
    
    /**
     * Move bubbles right if there is a space between them in a column
     */
    public void moveBubblesRight(){
        for (int j=0; j<getRow(); j++){
            boolean firstColor = false;
            for (int i=0; i<getCol(); i++){
                int bubble = getBubbleField()[i][j];
                if(bubble != 0){
                    firstColor = true;
                } else if (bubble == 0 && firstColor){
                    moveRight(i,j);
                    i=0;
                    firstColor = false;
                }
            }
        }
    }
    /**
     * Recursive method for helping move the bubbles right in case there is a space between them
     * @param i
     * @param j 
     */
    private void moveRight(int i, int j) {
        if (i>0){
            bubbleField[i][j] = getBubbleField()[--i][j];
            moveRight(i, j);
        } else
            bubbleField[i][j] = 0;
    }
    
    /**
     * Populate the list of the same colors around selected position
     * @param i
     * @param j
     * @throws ArrayIndexOutOfBoundsException 
     */
    public void getSameColorAround(int i, int j) throws ArrayIndexOutOfBoundsException{
        if (!sameColorAround.isEmpty()){
            sameColorAround.clear();
        }
        
        int selectedColor = getColorAt(i, j);
        if ( selectedColor == 0) return;
        
        sameColorAround.add(new Position(i,j));
        //reformat do jedne metody ? -> pouzito i v search stejnym zpusobem...
        search(i-1,j,selectedColor);
        search(i,j-1,selectedColor);
        search(i+1,j,selectedColor);
        search(i,j+1,selectedColor);
        //pomocnametodapro vypis stejne barvy v okoli
//        vypisStejneBarvy();
    }
  
    /**
     * Look for the same colors around the selected position recursively
     * @param i
     * @param j
     * @param selectedColor 
     */
    private void search(int i, int j, int selectedColor) {
        if (i < 0 || i > getCol()-1 || j< 0 || j> getRow()-1) {
            //System.out.println("Konec jednoho searche.");
            return;
        }
        Position p = new Position(i, j);
        //System.out.println("Prohledava:" + i +":"+j);
        if (sameColorAround.contains(p)) {
            } else 
        if(selectedColor == getBubbleField()[i][j]){
            sameColorAround.add(p);
            search(i-1,j,selectedColor);
            search(i,j-1,selectedColor);
            search(i+1,j,selectedColor);
            search(i,j+1,selectedColor);
        }
    }
    
    //pomocna metoda pro vypis stejne barvy - prepsat
//    private void vypisStejneBarvy() {
//        for (int i = 1; i < sameColorAround.size(); i++){
//            System.out.println(sameColorAround.get(i).x + ":" + sameColorAround.get(i).y);
//        }
//    }
    
    /** 
     * Returns color at the position i, j
     * @param i
     * @param j
     * @return 
     */
    public int getColorAt(int i, int j) {
        return getBubbleField()[i][j];
    }
    
    /**
     * Set color c at the position x, y;
     * @param x
     * @param y
     * @param c 
     */
    private void setColorAt(int x, int y, int c) {
        bubbleField[x][y] = c;
    }
    
    public int getValueOfSelected(int i, int j){
        getSameColorAround(i, j);
        int size = sameColorAround.size();
        //return (int)(size+Math.sqrt(size*0.1+1)-1*(size+Math.sqrt(size*0.1+1)-1));
        if (size < 2) return 0; 
            else
        return (int)((size*size*1.274));
    }

    /**
     * @return the bubbleField
     */
    public int[][] getBubbleField() {
        return bubbleField;
    }

    /**
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }
    
    public ArrayList<Line> linesToDraw(int x, int y){
        getSameColorAround(x, y);
        ArrayList<Line> lines = new ArrayList<>();
        
        for(Position p: sameColorAround){
            if (!hasNeighbourUp(p.x, p.y))lines.add(new Line(p.x, p.y, p.x+1, p.y));
            if (!hasNeighbourDown(p.x, p.y))lines.add(new Line(p.x, p.y+1, p.x+1, p.y+1));
            if (!hasNeighbourLeft(p.x, p.y))lines.add(new Line(p.x, p.y, p.x, p.y+1));
            if (!hasNeighbourRight(p.x, p.y))lines.add(new Line(p.x+1, p.y, p.x+1, p.y+1));
        }
        return lines;
    }

    private boolean hasNeighbourUp(int x, int y) {
        for (Position p: sameColorAround){
            if (p.x==x && p.y+1 == y) return true;
        }
        return false;
    }

    private boolean hasNeighbourDown(int x, int y) {
        for (Position p: sameColorAround){
            if (p.x==x && p.y-1 == y) return true;
        }
        return false;
    }

    private boolean hasNeighbourLeft(int x, int y) {
        for (Position p: sameColorAround){
            if (p.y == y && p.x+1 == x) return true;
        }
        return false;
    }

    private boolean hasNeighbourRight(int x, int y) {
        for (Position p: sameColorAround){
            if (p.y == y && p.x-1 == x) return true;
        }
        return false;
    }
    
}



class Position {
    public int x;
    public int y;
    
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o){
        if (o == this) 
            return true;
        if (!(o instanceof Position))
            return false;
        Position p = (Position) o;
        return (p.x == this.x && p.y == this.y);
    }
}