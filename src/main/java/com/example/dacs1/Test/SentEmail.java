package com.example.dacs1.Test;

import com.example.dacs1.Utils.EmailSender;


import java.util.Scanner;

public class SentEmail {

    public static String code = GenerateCode.generateCode();

    public static void testSendEmail(String toEmail) {
        boolean success = EmailSender.sendVerificationEmail(toEmail, code);

        if (success) {
            System.out.println("Email đã được gửi tới: " + toEmail);
            System.out.println("Mã xác nhận là: " + code);
        } else {
            System.out.println("Gửi email thất bại.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập email người nhận: ");
        String email = scanner.nextLine();
        testSendEmail(email);
    }
}
