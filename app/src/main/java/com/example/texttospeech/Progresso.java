package com.example.texttospeech;

public class Progresso {

    private int livello;
    private int totAcc;

    public Progresso(int livello, int totAcc) {
        this.livello = livello;
        this.totAcc = totAcc;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public int getTotAcc() {
        return totAcc;
    }

    public void setTotAcc(int totAcc) {
        this.totAcc = totAcc;
    }
}
