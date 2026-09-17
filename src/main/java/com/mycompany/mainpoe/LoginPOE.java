/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mainpoe;

/**
 *
 * @author Student
 */
public class LoginPOE {

    // Stores the details of the currently registered user.
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellNumber;
    private boolean registered = false;

    // Username must contain an underscore and be no more than 16 characters long.
    
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 16;
    }

    
    //Password must be at least 8 characters, and contain a capital letter, a number, and a special character.
 
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    
    //Cell number must start with the South African international code (+27), followed by 9 digits, e.g. +27838968976.

    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber != null && cellNumber.matches("\\+27\\d{9}");
    }

    
    // Validates all three fields and registers the user if they pass. Returns a message for every field that was checked.

    public String registerUser(String firstName, String lastName, String username,
                                String password, String cellNumber) {

        StringBuilder result = new StringBuilder();

        if (checkUserName(username)) {
            result.append("Username successfully captured. ");
        } else {
            result.append("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than 15 characters in length. ");
        }

        if (checkPasswordComplexity(password)) {
            result.append("Password successfully captured. ");
        } else {
            result.append("Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character. ");
        }

        if (checkCellPhoneNumber(cellNumber)) {
            result.append("Cell number successfully captured.");
        } else {
            result.append("Cell number is incorrectly formatted or does not contain an international code. Correct the number and try again.");
        }

        // Only save the account if every check passed
        if (checkUserName(username) && checkPasswordComplexity(password) && checkCellPhoneNumber(cellNumber)) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.password = password;
            this.cellNumber = cellNumber;
            this.registered = true;
        }

        return result.toString().trim();
    }

    
    // Checks the entered username and password against the registered account.
    
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return registered
                && username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    
    // Returns the correct message for a login attempt.
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}