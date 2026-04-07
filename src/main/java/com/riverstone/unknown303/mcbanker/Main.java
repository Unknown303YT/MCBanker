package com.riverstone.unknown303.mcbanker;

public class Main {
    public static void main(String[] args) {
        System.out.println("Unknown_303YT: ");
        BankCard unknown303 = new BankCard("Unknown_303YT");
        System.out.printf("Card Number: %d, Salt: %s%n", unknown303.getCardNumber(), new String(unknown303.getCardNumSalt()));

        BankCard redcreeper1 = new BankCard("redcreeper1");
        System.out.printf("Card Number: %d, Salt: %s%n", redcreeper1.getCardNumber(), new String(redcreeper1.getCardNumSalt()));
    }
}
