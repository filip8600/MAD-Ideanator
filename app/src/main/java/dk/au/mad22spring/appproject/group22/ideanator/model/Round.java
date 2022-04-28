package dk.au.mad22spring.appproject.group22.ideanator.model;

import java.util.ArrayList;

public class Round {
    private String problem;
    private OptionCard Winner;

    public Round(String theProblem){
        problem = theProblem;
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

}
