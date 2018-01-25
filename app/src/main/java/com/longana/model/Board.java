/**
***********************************************************
* Name:  Janila Khan                                      *
* Project : Project 2 / Longana Game			          *
* Class : CMPS 366 Organization of Programming Languages  *
* Date : December 5, 2017                                *
***********************************************************
*/

package com.longana.model;

import java.util.ArrayDeque;

/* *********************************************************
This class creates the board and places tiles onto the board
********************************************************** */

public class Board {
    // Class Variables
    private ArrayDeque<Tile> board;

    public boolean humanPass;
    public boolean computerPass;

    /**
     Constructor initialize the board
     */
    public Board(){
        board = new ArrayDeque<>(28);
    }

    /**
     A Pointer to the board
     @return return a pointer to the board
     */
    public ArrayDeque<Tile> getBoard(){ return board; }

    /**
     Initialize the board for a saved game
     @param temp, an array deque of tiles that holds the temporary tiles of the saved game board
     */
    public void setBoard(ArrayDeque<Tile> temp){ board = temp; }

    /**
     Places a tile on the left side of the board
     @param t, a tile object which is the tile that would be placed on the board.
     */
    public void placeTileLeft(Tile t) { board.addFirst(t); }

    /**
     Places a tile on the right side of the board
     @param t, a tile object which is the tile that would be placed on the board.
     */
    public void placeTileRight(Tile t) { board.addLast(t); }

    /**
     Determines if the computer pass
     @return a boolean value determining if the computer passed or not
     */
    public boolean didComputerPass(){
        return computerPass;
    }

    /**
     Determines if the human pass
     @return a boolean value determining if the human passed or not
     */
    public boolean didHumanPass(){
        return humanPass;
    }

    /**
     Get the first tile from the left side on the board
     @return the first tile on the left side of the board
     */
    public Tile returnTopTile(){return board.getFirst();}

    /**
     Get the first tile from the right side on the board
     @return the first tile on the right side of the board
     */
    public Tile returnBottomTile(){ return board.getLast(); }
}
