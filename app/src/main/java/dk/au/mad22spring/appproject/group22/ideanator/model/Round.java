package dk.au.mad22spring.appproject.group22.ideanator.model;

import org.checkerframework.checker.nullness.Opt;

import java.util.ArrayList;

public class Round {
    private String problem;
    private OptionCard Winner;
    private ArrayList<OptionCard> playedOptions;

    public Round(String theProblem){
        problem = theProblem;
        playedOptions=new ArrayList<OptionCard>();
    }

    public Round (){

    }

    public String getProblems() {
        return problem;
    }

    public void setProblems(String theProblem) {
        this.problem = theProblem;
    }

    public OptionCard getWinner() {
        return Winner;
    }

    public void setWinner(OptionCard winner) {
        Winner = winner;
    }

    public ArrayList<OptionCard> getPlayedOptions() {
        return playedOptions;
    }

    public void setPlayedOptions(ArrayList<OptionCard> playedOptions) {
        this.playedOptions = playedOptions;
    }
}
