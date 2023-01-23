package com.example.texttospeech;

public class Livello {
    private int livello;
    private String[] scioglilingua;

    public Livello(int livello, String[] scioglilingua) {
        this.livello = livello;
        this.scioglilingua = scioglilingua;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public String[] getScioglilingua() {
        return scioglilingua;
    }

    public void setScioglilingua(String[] scioglilingua) {
        this.scioglilingua = scioglilingua;
    }
}
