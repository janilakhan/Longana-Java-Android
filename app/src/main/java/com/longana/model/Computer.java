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

/* *************************************************
This class holds the actions of the Computer Player
************************************************* */

public class Computer extends Player{
    // Class Variables
    // A vector of pairs that will hold the possible tiles to play
    private Vector<Tile> computerTemp;

    /**
     Constructor intializes the class variables
     */
    public Computer(){
        computerTemp = new Vector<>(28);
    }

    /**
     This function will choose what action the computer plays
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     */
    @Override
    public void play(Board board, Stock boneyard){
        // Initialize local Variables
        Tile top = board.returnTopTile();
        Tile bottom = board.returnBottomTile();

        // Search for possible tiles
        searchForTile(board, top, bottom);

        // If the vector is empty pass, if not choose a tile
        if (computerTemp.isEmpty()) {
            passTurn(board, boneyard);
        }
        else {
            chooseTile(board, boneyard);
            computerTemp.clear();
            board.computerPass = false;
        }
    }

    /**
     This function will choose what the best tile to play
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     */
    @Override
    public void chooseTile(Board board, Stock boneyard){
        String playMessage = null;

        // Initialize Local Variables
        Tile top = board.returnTopTile();
        Tile bottom = board.returnBottomTile();

        // This value will be overwritten for now this is a temp value
        Tile tempPair = new Tile(0,0);

        int countPips;
        int largestTile = 0;

        // Check if human passed
        if (board.didHumanPass()) {
            for (int a = 0; a < computerTemp.size(); a++) {
                // check if a double
                if (computerTemp.get(a).isDouble()) {
                    playMessage = "This is the first occurrence of a double within the computer hand. ";
                    tempPair = computerTemp.get(a);
                    break;
                }
                else {
                    // Search for the tile with the greatest sum
                    countPips = countTilePips(computerTemp.get(a));
                    if (largestTile < countPips) {
                        playMessage = "There are no possible doubles that can be placed on the board so, the tile with the greatest sum was chosen.";
                        largestTile = countPips;
                        tempPair = computerTemp.get(a);
                    }
                }
            }

            // Check if the tile can be placed on the right side
            if (isTileLegalLeft(tempPair, top)) {
                addToLeft(board, boneyard, tempPair);
                playMessage += "The computer placed " + tempPair.toString() + " and place it on the left side.";
            }
            else {
                // Check if the tile can be placed on the left side
                if (isTileLegalRight(tempPair, bottom)) {
                    addToRight(board, boneyard, tempPair);
                    playMessage += "The computer placed " + tempPair.toString() + " and place it on the right side.";
                }
            }
        }
        else {
            for (int a = 0; a < computerTemp.size(); a++) {
                // Check if a double
                if (computerTemp.get(a).isDouble()) {
                    playMessage = "This is the first occurrence of a double within the computer hand. ";
                    tempPair = computerTemp.get(a);
                    break;
                } else {
                    // Search for the tile with the greatest sum
                    countPips = countTilePips(computerTemp.get(a));
                    if (largestTile < countPips) {
                        playMessage = "There are no possible doubles that can be placed on the board so, the tile with the greatest sum was chosen.";
                        largestTile = countPips;
                        tempPair = computerTemp.get(a);
                    }
                }
            }

            // If there is a double check if it can be placed on the left side
            if (tempPair.isDouble() && isTileLegalLeft(tempPair, top)) {
                addToLeft(board, boneyard, tempPair);
                playMessage += "The computer placed " + tempPair.toString() + " and put it to the left side.";
            } else {
                // Check if it can be placed on the right side
                if (isTileLegalRight(tempPair, bottom)) {
                    addToRight(board, boneyard, tempPair);
                    playMessage += "The computer placed " + tempPair.toString() + " and put it to the right side.";
                }
            }
        }

        message = playMessage;
    }

    /**
     This function will pass a turn if there are no possible tiles to play
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     */
    @Override
    public void passTurn(Board board, Stock boneyard){
        String passMessage = null;

        // Check if the boneyard is empty
        if (!boneyard.getStock().isEmpty()) {
            Tile bottom = board.returnBottomTile();
            Tile top = board.returnTopTile();

            // Draw a tile
            boneyard.drawTiles(playerHand, draw);

            // Placed the tile if it legal and set computerPass to false
            if (playerHand.lastElement().isDouble() && isTileLegalLeft(playerHand.lastElement(), top)) {
                passMessage = "The computer draw a tile " + playerHand.lastElement().toString() + " and placed it on the left since, it was a double.";
                addToLeft(board, boneyard, playerHand.lastElement());
                board.computerPass = false;
            }
            else if (isTileLegalRight(playerHand.lastElement(), bottom)) {
                passMessage = "The computer draw a tile " + playerHand.lastElement().toString() + " and placed it on the right.";
                addToRight(board, boneyard, playerHand.lastElement());
                board.computerPass = false;
            }
            else {
                // computerPass is set to true
                passMessage = "The computer draw a tile " + playerHand.lastElement().toString() + " and was not able to play so they had to pass.";
                board.computerPass = true;
            }
        }
	    else {
            // computerPass is set to true
            passMessage = "The boneyard is empty so the computer can't draw a tile and must pass.";
            board.computerPass = true;
        }

        message = passMessage;
    }

    /**
     Searches for the possible tile the computer can play
     @param board, a pointer to the Board class
     @param tile1, a tile object that holds a value that will be compared
     @param tile2, a tile object that holds a value that will be compared
     */
    private void searchForTile(Board board, Tile tile1, Tile tile2){
        for (int i = 0; i < playerHand.size(); i++)
        {
            if (playerHand.get(i).isDouble() && isTileLegalLeft(playerHand.get(i), tile1)) {
                computerTemp.addElement(playerHand.get(i));
            }
		    else if (isTileLegalRight(playerHand.get(i), tile2)){
                computerTemp.addElement(playerHand.get(i));
            }
		    else {
                if (board.didHumanPass() && isTileLegalLeft(playerHand.get(i), tile1)) {
                    computerTemp.addElement(playerHand.get(i));
                }
            }
        }
    }
}
