package com.example.texttospeech;

public class Livello {
    private int livello;
    private String[] scioglilinguaTesti;

    public Livello(int livello, String[] scioglilingua) {
        this.livello = livello;
        this.scioglilinguaTesti = scioglilingua;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public String[] getScioglilingua() {
        return scioglilinguaTesti;
    }

    public void setScioglilingua(String[] scioglilingua) {
        this.scioglilinguaTesti = scioglilingua;
    }
}
