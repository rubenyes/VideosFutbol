package com.ruben.videosfutbol;

public class Video{
    private Partido p;
    private String link;
    private String texto;
    
    public Video(Partido p, String link, String texto){
        this.p = p;
        this.link = link;
        this.texto = texto;
    }
}