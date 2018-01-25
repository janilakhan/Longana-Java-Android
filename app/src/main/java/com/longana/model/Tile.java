/**
***********************************************************
* Name:  Janila Khan                                      *
* Project : Project 2 / Longana Game			          *
* Class : CMPS 366 Organization of Programming Languages  *
* Date : December 5, 2017                                 *
***********************************************************
*/

package com.longana.model;

/*************************************
This class will act like a domino tile
*************************************/

public class Tile {
    // Class Variables
    private int first;
    private int second;

    /**
     Default Constructor
     */
    public Tile(){
        first = 0;
        second = 0;
    }

    /**
     Constructor
     @param first, this integer holds the left side value of the tile
     @param second, this integer holds the right side value of the tile
     */
    // Constructor
    public Tile(int first, int second){
        this.first = first;
        this.second = second;
    }

    /**
     Get the first or left side value of the tile
     @return the first value of the tile
     */
    public int getFirst() { return first; }


    /**
     Get the second or right side value of the tile
     @return the second value of the tile
     */
    public int getSecond(){ return second; }


    /**
     Checks to see if the tile is a double
     @return true if the tile is a double, false otherwise
     */
    public boolean isDouble(){
        return this.first == this.second;
    }

    /**
     Swap the values of the tile
     */
    public void swapTiles() {
        int temp;

        temp = first;
        this.first = second;
        this.second = temp;
    }

    /**
     Checks to see if the tiles are equal
     @param t, is a tile object
     @return true if the tiles are equal, false otherwise
     */
    // Check if the tiles are equal
    public boolean equals(Tile t) {
        if(t.first == first && t.second == second){
            return true;
        }
        else if(t.first == second && t.second == first){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     Returns the String value of the tile
     @return the tile is return as a string
     */
    @Override
    public String toString(){
        return first + "-" + second;
    }
}
