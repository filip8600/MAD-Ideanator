package dk.au.mad22spring.appproject.group22.ideanator.model;

public class OptionCard {
    private int Votes;
    private String option;

    public OptionCard(){}

    public OptionCard(String theOption){
        option = theOption;
    }

    public int getVotes() {
        return Votes;
    }

    public void setVotes(int votes) {
        Votes = votes;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
