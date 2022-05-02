package dk.au.mad22spring.appproject.group22.ideanator.model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<Round> rounds;
    private int RoundCounter;

    public enum gameState {
        LOBBY,
        ROUND,
        VOTE,
        FINAL
    }

    private gameState state = gameState.LOBBY;

    public Game() {
        players = new ArrayList<>();
        setRoundCounter(1);
        rounds = new ArrayList<Round>();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }


    public int getRoundCounter() {
        return RoundCounter;
    }

    public void setRoundCounter(int roundCounter) {
        RoundCounter = roundCounter;
    }




    public gameState getState() {
        return state;
    }

    public void setState(gameState state) {
        this.state = state;
    }
}
