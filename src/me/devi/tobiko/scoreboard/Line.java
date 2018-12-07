package me.devi.tobiko.scoreboard;

public class Line {

    String text = "";

    String main = "";

    String prefix = "";

    String sufix = "";

    int score;

    boolean usedTeam = false;

    String playerName;


    public Line(int score ,String text) {
        this.score = score;
        this.text = text;
        int length = text.length();

        if(length <= 16) {

            main = text;


        } else if(length > 16 && length <= 32) {

            prefix = text.substring(0, length-16);
            main = text.substring(length-16);
            usedTeam = true;


        } else if(length > 32 && length <= 48) {

            prefix = text.substring(0, length-32);
            main = text.substring(length-32,length-16);
            sufix = text.substring(length-16);
            usedTeam = true;

        } else {
            System.out.println("Text cant be longer than 48 chars even with such awsome API like this one :P");
            System.out.println(text);
        }


    }

    public String getLinetext() {
        return text;
    }

    public void update(String text) {
        this.text = text;
        int length = text.length();

        if(length <= 16) {

            main = text;

        } else if(length > 16 && length <= 32) {

            prefix = text.substring(0, length-16);
            main = text.substring(length-16);
            usedTeam = true;

        } else if(length > 32 && length <= 48) {

            prefix = text.substring(0, length-16);
            main = text.substring(length-16,32);
            sufix = text.substring(32);
            usedTeam = true;

        } else
            System.out.println("Text cant be longer than 48 chars even with such awsome API like this one :P");


    }

}