package fr.natation.service;

public class MultiLine {

    public static void main(String[] args) {
        String s = "2) Entrer dans l'eau par l'échelle sans s'immerger.";
        String[] ret = s.split(" ");
        System.out.println(ret.length);
    }
}