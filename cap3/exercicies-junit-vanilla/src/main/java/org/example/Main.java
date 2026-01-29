package org.example;

import org.example.entities.Financing;

public class Main {
    public static void main(String[] args) {
        try{
            Financing f1 = new Financing(100000.0, 2000.0, 20);
            System.out.println("========== Financing 1 Data ==========");
            System.out.printf("Entry: %.2f%n", f1.entry());
            System.out.printf("Quota: %.2f%n", f1.quota());

        } catch (IllegalArgumentException e) {
            System.err.println("Error of financing 1: " + e.getMessage());
        }

        try{
            Financing f2 = new Financing(100000.0, 2000.0, 80);
            System.out.println("========== Financing 2 Data ==========");
            System.out.printf("Entry: %.2f%n", f2.entry());
            System.out.printf("Quota: %.2f%n", f2.quota());

        } catch (IllegalArgumentException e) {
            System.err.println("Error of financing 2: " + e.getMessage());
        }
    }
}