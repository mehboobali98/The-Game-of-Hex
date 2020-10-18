package AiHex.players;

import AiHex.gameMechanics.Move;

// class to store a move with its respective value
class Pair{
    Move m;
    int value;

    //constructor
    Pair(Move m, int value)
    {
        this.m = new Move(m.getColour(),m.getX(),m.getY());
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
