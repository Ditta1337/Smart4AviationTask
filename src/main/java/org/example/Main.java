package org.example;


import org.example.util.InputParsingException;

public class Main {
    public static void main(String[] args) {
        try {
            Solver solver = new Solver();
            solver.Solve();
        } catch (InputParsingException e) {
            System.out.println("There was an error parsing the input: " + e.getMessage());
        }
    }
}