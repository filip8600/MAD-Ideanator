package dk.au.mad22spring.appproject.group22.ideanator;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<String> questions;
    private ArrayList<String> options;

    enum gameState {
        LOBBY,
        ROUND,
        VOTE,
        FINAL
    }

    private gameState state = gameState.LOBBY;

    public Game() {
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public gameState getState() {
        return state;
    }

    public void setState(gameState state) {
        this.state = state;
    }
}
