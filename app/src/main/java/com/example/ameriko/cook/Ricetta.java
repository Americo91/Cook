package com.example.ameriko.cook;

/**
 * Created by Ameriko on 09/04/2017.
 */

public class Ricetta {

    private int id;
    private String nome;
    private String img;


    public Ricetta(int id, String nome, String img) {
        this.id = id;
        this.nome = nome;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
