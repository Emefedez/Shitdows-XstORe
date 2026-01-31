package com.shitdows.xstore;

public class listedProgram {
    //así la arquitectura ocupa menos espacio pues solo guarda un numero
    /*
    byte x64 = 0;
    byte x86 = 1;
    byte arm = 2;
    */

    private String link;
    private String title;
    private String extension;
    private byte architecture;

    public listedProgram(String title, String link, String extension, byte architecture) {
        this.title = title;
        this.link = link;
        this.extension = extension;
        this.architecture = architecture;
    }

    public String getTitle() { return title; }
    public String getLink() { return link; }
    public String getExtension() { return extension; }
    public byte getArch() {return architecture; }


    

}
