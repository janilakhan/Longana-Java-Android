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

/* ***********************************************
This class holds the actions of the Human Player
************************************************* */

public class Human extends Player{
    // Class Variables
    private Vector<Tile> humanTemp;

    /**
     Constructor intializes the class variables
     */
    public Human(){
        humanTemp = new Vector<>(28);
    }

    /**
     This function will call ask for help
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     */
    @Override
    public void play(Board board, Stock boneyard){
        askForHelp(board);
    }

    /**
     This function allows the user to pass their turn
     @param board, a pointer to the Board class
     @param boneyard, a pointer to the Stock class
     */
    @Override
    public void passTurn(Board board, Stock boneyard){
        String humanPassMessage;

        // If the Boneyard is not empty, continue
        if (!boneyard.getStock().isEmpty())
        {
            // Initialize top to the first tile on the left side of the board
            Tile top = board.returnTopTile();
            Tile bottom = board.returnBottomTile();

            // Human Hand draws a tile
            boneyard.drawTiles(playerHand, draw);

            // Check if the tile can be placed, if true place the tile ans set humanPass to false
            if (playerHand.lastElement().isDouble() && isTileLegalRight(playerHand.lastElement(), bottom))
            {
                humanPassMessage = "You drawed a tile " + playerHand.lastElement().toString() + " and placed it on the right.";
                addToRight(board, boneyard, playerHand.lastElement());
                board.humanPass = false;
            }
            else if (isTileLegalLeft(playerHand.lastElement(), top))
            {
                humanPassMessage = "You have drawed a tile " + playerHand.lastElement().toString() + " and placed it on the left.";
                addToLeft(board, boneyard, playerHand.lastElement());
                board.humanPass = false;
            }
            else
            {
                // Otherwise, set humanPass to true
                humanPassMessage = "You have drawed a tile " + playerHand.lastElement().toString() + " but was not able to play so you had to pass.";
                board.humanPass = true;
            }
        }
	    else
        {
            humanPassMessage = "The boneyard is empty so you can't draw a tile and must pass.";
            board.humanPass = true;
        }

        message = humanPassMessage;
    }

    /**
     This function helps the user make a decision of what to do
     @param board, a pointer to the Board class
     */
    private void askForHelp(Board board){
        String helpMessage = null;

        // Initialize local variables
        Tile top = board.returnTopTile();
        Tile bottom = board.returnBottomTile();

        Tile tempPair = new Tile(0,0);
        int countPips;
        int largestTile = 0;

        // Search for possible tile
        searchForTile(board, top, bottom);

        // If empty tell the user to pass
        if (humanTemp.isEmpty())
        {
            helpMessage = "There is no tile in your hand that you can play, it is better to pass.";
        }
        else
        {
            // Check if the Computer pass
            if (board.didComputerPass())
            {
                // Chose the best tile
                for (int a = 0; a < humanTemp.size(); a++)
                {
                    // Check for a double
                    if (humanTemp.get(a).isDouble())
                    {
                        helpMessage = "Found the first occurrence of a double within your hand. ";
                        tempPair = humanTemp.get(a);
                        break;
                    }
                    else
                    {
                        // Search for a tile with the greatest sum
                        countPips = countTilePips(humanTemp.get(a));
                        if (largestTile < countPips)
                        {
                            helpMessage = "There were no possible doubles that can be placed on the board so, the tile with the greatest sum was chosen. ";
                            largestTile = countPips;
                            tempPair = humanTemp.get(a);
                        }
                    }
                }

                // Check if the tile can be placed on the right side, if not check on the left side
                if (isTileLegalRight(tempPair, bottom)) {
                    helpMessage += "Choose Tile: " + tempPair.toString() + " and put it to the right side.";
                }
                else {
                    if (isTileLegalLeft(tempPair, top)) {
                        helpMessage += "Choose Tile: " + tempPair.toString() + " and put it to the left side.";
                    }
                }
            }
            else {
                for (int a = 0; a < humanTemp.size(); a++) {
                    // Check if it is a double
                    if (humanTemp.get(a).isDouble()) {
                        helpMessage = "Found the first occurrence of a double within your hand. ";
                        tempPair = humanTemp.get(a);
                        break;
                    }
                    else {
                        // Search for the tile with the greatest sum
                        countPips = countTilePips(humanTemp.get(a));
                        if (largestTile < countPips) {
                            helpMessage = "There were no possible doubles to place on the board so the tile with the greatest sum was chosen. ";
                            largestTile = countPips;
                            tempPair = humanTemp.get(a);
                        }
                    }
                }

                // If the temp is a double check if it can be place on the right
                if (tempPair.isDouble() && isTileLegalRight(tempPair, bottom)) {
                    helpMessage += "Choose Tile: " + tempPair.toString() + " and put it to the right side.";
                }
                else {
                    // If the tile is legal tell the user to place the tile
                    if (isTileLegalLeft(tempPair, top)) {
                        helpMessage += "Choose Tile: " + tempPair.toString() + " and put it to the left side.";
                    }
                }
            }
        }
        humanTemp.clear();

        message = helpMessage;
    }

    /**
     Searches for the possible tile the human user can play
     @param board, a pointer to the Board class
     @param tile1, a tile object that holds a value that will be compared
     @param tile2, a tile object that holds a value that will be compared
     */
    private void searchForTile(Board board, Tile tile1, Tile tile2){
        for (int i = 0; i < playerHand.size(); i++)
        {
            if (playerHand.get(i).isDouble() && isTileLegalRight(playerHand.get(i), tile2))
            {
                humanTemp.addElement(playerHand.get(i));
            }
		    else if (isTileLegalLeft(playerHand.get(i), tile1))
            {
                humanTemp.addElement(playerHand.get(i));
            }
		    else
            {
                if (board.didComputerPass() && isTileLegalRight(playerHand.get(i), tile2))
                {
                    humanTemp.addElement(playerHand.get(i));
                }
            }
        }
    }
}