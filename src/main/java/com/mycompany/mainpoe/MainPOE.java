/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mainpoe;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class MainPOE {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LoginPOE login = new LoginPOE();

        System.out.println("Registration");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username (must contain '_' and be max 16 characters): ");
        String username = scanner.nextLine();

        System.out.print("Enter password (min 8 characters, 1 capital, 1 number, 1 special character): ");
        String password = scanner.nextLine();

        System.out.print("Enter cell number (e.g. +27838968976): ");
        String cellNumber = scanner.nextLine();

        String registrationResult = login.registerUser(firstName, lastName, username, password, cellNumber);
        System.out.println(registrationResult);

        System.out.println();
        System.out.println("Login");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccessful = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loginSuccessful));

        scanner.close();
    }
}