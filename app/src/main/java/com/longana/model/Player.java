/**
***********************************************************
* Name:  Janila Khan                                      *
* Project : Project 2 / Longana Game			          *
* Class : CMPS 366 Organization of Programming Languages  *
* Date : December 5, 2017                                 *
***********************************************************
*/

package com.longana.model;

import java.util.Vector;

/* *****************************************
This class holds the actions a player can do
***************************************** */

public class Player {
    public String message;

    // Class Variables
    protected int draw = 1;

    // Vectors of pairs that hold the hand of a user
    protected Vector<Tile> playerHand;

    protected Hand userHand;

    /**
     Constructor initialize the hand and the playerHand
     */
    public Player(){
       playerHand = new Vector<>(28);
       userHand = new Hand();
    }

    /**
     A pointer to a player's hand
     @return return a pointer to a player hand
     */
    public Vector<Tile> getHand() { return playerHand; }

    /**
     Initialize a player's hand when they choose to play a saved game
     @param temp, a vector of tiles that holds the temporary values of a player's hand from a saved game file
     */
    public void setUserHand(Vector<Tile> temp){playerHand = temp;}

    /**
     Where all the plays will be made
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     */
    public void play(Board board, Stock boneyard) { System.out.println(); }

    /**
     To choose a tile to place on the board
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     */
    public void chooseTile(Board  board, Stock boneyard) { System.out.println(); }

    /**
     What a player would do when they decide to pass
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     */
    public void passTurn(Board board,Stock boneyard) { System.out.println(); }

    /**
     Places the engine to the board
     @param board, a pointer to the Board class
     @param engine, an integer that holds the value of the engine
     */
    public void placeEngine(Board board, int engine){
        // Initialize temp with the engine
        Tile temp = new Tile(engine, engine);

        // Search through the vector for the tile
        int index = userHand.getIndex(playerHand, temp);

        // Place the tile on the board and remove the tile from the hand
        board.placeTileLeft(temp);
        playerHand.remove(index);
    }

    /**
     Add a Tile to the right side of the board
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     @param tile, a tile object which holds the value of the tile we would like to place on the right side of the board
     */
    public void addToRight(Board board, Stock boneyard, Tile tile){
        String addToRightMessage = null;

        // Initialize bottom to the first tile on the right side of the board
        Tile bottom = board.returnBottomTile();

        // Check if the tile is legal to place on the right side of the board
        if (!isTileLegalRight(tile, bottom)) {
            addToRightMessage = "This Move Is Not Legal";
            play(board, boneyard);
        }
        else {
            // Get the index of where the tile is in a player's hand
            int index = userHand.getIndex(playerHand, tile);

            if(index != -1) {
                // Add the tile to the right side of the board and erase the tile from the vector
                board.placeTileRight(tile);
                playerHand.remove(index);
            }
            else{
                message = "The Tile Was Not Found";
            }
        }

        message = addToRightMessage;
    }

    /**
     Add a Tile to the left side of the board
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     @param tile, a tile object which holds the value of the tile we would like to place on the left side of the board
     */
    public void addToLeft(Board board, Stock boneyard, Tile tile){
        String addToLeftMessage = null;

        // Initialize top to the first tile on the left side of the board
        Tile top = board.returnTopTile();

        // Check if the tile is able to be played on the left side of the board
        if (!isTileLegalLeft(tile, top)) {
            addToLeftMessage = "This Move Is Not Legal";
            play(board, boneyard);
        }
        else {
            // Get the index of where the tile is in a player's hand
            int index = userHand.getIndex(playerHand, tile);

            if (index != -1) {
                // Add the tile to the left side of the board and erase the tile from the vector
                board.placeTileLeft(tile);
                playerHand.remove(index);
            }
            else{
                addToLeftMessage = "The Tile Was Not Found";
            }
        }

        message = addToLeftMessage;
    }

    /**
     Check if a tile can be placed on the right side of the board
     @param tile1, a tile object that holds the first tile value
     @param tile2, a tile object that holds the second tile value
     @return true if it can be placed, false otherwise
     */
    public boolean isTileLegalRight(Tile tile1, Tile tile2){
        if (tile1.getFirst() == tile2.getSecond()) {
            return true;
        }
        else if (tile1.getSecond() == tile2.getSecond()) {
            tile1.swapTiles();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     Check if a tile can be placed on the left side of the board
     @param tile1, a tile object that holds the first tile value
     @param tile2, a tile object that holds the second tile value
     @return true if it can be placed, false otherwise
     */
    public boolean isTileLegalLeft(Tile tile1, Tile tile2){
        if (tile1.getSecond() == tile2.getFirst()) {
            return true;
        }
        else if (tile1.getFirst() == tile2.getFirst()) {
            tile1.swapTiles();
            return true;
        }
        else {
            return false;
        }
    }

    // Count the pips of a tile
    /**
     Count the pips of a tile
     @param tile, a tile object that holds the values that will be counted
     @return an integer value that holds the sum of the pips
     */
    public int countTilePips(Tile tile){
        int countPips;

        countPips = tile.getFirst() + tile.getSecond();

        return countPips;
    }

    /**
     Get the string that holds the message of what the player has done during their move
     @return a string that holds what the player has done during their move
     */
    public String getMessage(){
        return message;
    }
}
