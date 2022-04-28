package dk.au.mad22spring.appproject.group22.ideanator.model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<Round> rounds;
//    private ArrayList<String> problems;
//    private ArrayList<String> options;
    //private int RoundCounter??

    public enum gameState {
        LOBBY,
        ROUND,
        VOTE,
        FINAL
    }

    private gameState state = gameState.LOBBY;

    public Game() {
        players = new ArrayList<>();
       // problems = new ArrayList<>();
       // options = new ArrayList<>();
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


   /* public ArrayList<String> getProblems() {
        return problems;
    }

    public void setProblems(ArrayList<String> problems) {
        this.problems = problems;
    }*/

    /*public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }*/

    public gameState getState() {
        return state;
    }

    public void setState(gameState state) {
        this.state = state;
    }
}