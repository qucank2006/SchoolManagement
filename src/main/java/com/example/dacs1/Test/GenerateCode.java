package com.example.dacs1.Test;

import java.util.Random;

public class GenerateCode {
    public static String generateCode() {

        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public static void main(String[] args){
        String code = generateCode();
        System.out.println(code);
    }
}
