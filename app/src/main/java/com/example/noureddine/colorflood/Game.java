package com.example.noureddine.colorflood;

import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

/**
 * One individual game : ie the size of the board, the number of colors, number of points, timer,
 * etc...
 */
public class Game {

    private Cell[][] board;
    private int boardSize;//the number of cells in a line/column of the board
    private int colorNumber;//number of different colors
    private Paint[] paint;
    private int selectedColor;
    private boolean gamedEnded;//whether the game is finished or not

    //the color clicked by the player

    /**A default game if nothing is specified*/
    public Game(){
        this.board = new Cell[5][5]; //we initialize a 2D 5*5 board
        this.colorNumber = 4; //we set it to 4 different colors
    }

    /**Build the game with the given size, number of colors, and said colors (p)*/
    public Game(int size, int colors, Paint[] p){
        this.boardSize = size;
        this.colorNumber = colors;
        this.paint = p;

        initBoard();
    }

    /**
     * Initialize the board's data (size, cell's color, etc...)*/
    public void initBoard(){

        int i, j;

        /*create a new board with the given dimension*/
        board = new Cell[boardSize][boardSize];

        /*create a new cell and give it a random color*/
        for (j = 0; j < boardSize; j++) {
            for (i = 0; i < boardSize; i++) {
                this.board[j][i] = new Cell(paint[getRand(colorNumber)]);
            }
        }
        /*Initialize the starting cell (the top left cell [0;0] will be the starting point)*/
        this.board[0][0].setState(true);
        /*We check its neighbors upon the start*/
        checkNeighbor(this.board[0][0].getCellColor());
    }

    /**
     * Check a cell's neighbors and change its color
     * @param pickedColor : the color that was clicked by the player*/
    public void checkNeighbor(int pickedColor) {

        setSelectedColor(pickedColor);

        int bLen = board.length; //the board's length

        checkIfWon(board,bLen);

        for (int j = 0; j < bLen; j++) {
            for (int i = 0; i < bLen; i++) {
                //if the cell is part of the flood
                if (board[j][i].getState()){
                    /*then check if its neighbors (bottom one, top one, right one, left one)
                    are out of the flood and have the same color*/
                    if(!isOutOfBound(j+1)) {
                        if ((!board[j + 1][i].getState()) && compareColors(board[j + 1][i], selectedColor)) {
                            /*if they do, the we change their color and state*/
                            changeColor(board[j][i], selectedColor);
                            board[j + 1][i].setState(true);
                            /*recursive call to check the neighbor's neighbors, etc...*/
                            checkNeighbor(selectedColor);
                        }
                    }
                    //Make sure that the cell we are about to check is inside the board
                    if(!isOutOfBound(j-1)) {
                        if ((!board[j-1][i].getState()) && compareColors(board[j-1][i], selectedColor)){
                            changeColor(board[j][i], selectedColor);
                            board[j-1][i].setState(true);
                            checkNeighbor(selectedColor);
                        }
                    }
                    if(!isOutOfBound(i+1)) {
                        if ((!board[j][i + 1].getState()) && compareColors(board[j][i + 1], selectedColor)) {
                            changeColor(board[j][i], selectedColor);
                            board[j][i + 1].setState(true);
                            checkNeighbor(selectedColor);
                        }
                    }
                    if(!isOutOfBound(i-1)) {
                        if ((!board[j][i - 1].getState()) && compareColors(board[j][i - 1], selectedColor)) {
                            changeColor(board[j][i], selectedColor);
                            board[j][i - 1].setState(true);
                            checkNeighbor(selectedColor);
                        }
                    }
                    //not an actual error
                    else Log.e("Error","Color problem : checkNeighbor func");

                    /*TODO : remove quick fix*/
                    changeColor(board[j][i], selectedColor);
                }
            }
        }
    }

    /**Change a cell's color and its state*/
    public void changeColor(Cell cell, int newColor){

        /*We set the cell's new color*/
        Paint tmp = new Paint();
        tmp.setColor(newColor);
        cell.setCellColor(tmp);
        /*We set the cell's new state*/
        cell.setState(true);
    }

    /**Compare 2 cells' colors
     * @return true if the colors match ; false if they don't*/
    public boolean compareColors(Cell c1, int colorPicked){

        return(c1.getCellColor() == colorPicked);
    }

    /**return true if the selected cell is not in the board (ie outside of the board)*/
    public boolean isOutOfBound(int n){

        return (n < 0 || n >= boardSize);
    }

    /**set the color selected by the player*/
    public void setSelectedColor(int color){
        this.selectedColor = color;
    }

    /**Check if the game is won (all the cells have the same color), by checking all the cell's
     * state*/
    public boolean checkIfWon(Cell[][] cell, int size){

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                if (!cell[j][i].getState()) {
                    return false;
                }
            }
        }
        Log.i("End Game","You Won");
        gamedEnded=true;

        return true;
    }
    /**check if the game if finished*/
    public boolean getGameStatus(){
        return gamedEnded;
    }

    /**
     * generate a random int between 0 and n-1
     * */
    public int getRand(int n){
        Random rand = new Random();
        return rand.nextInt(n);
    }

    /**return the board used in the current game*/
    public Cell[][] getBoard(){
        return board;
    }


}

