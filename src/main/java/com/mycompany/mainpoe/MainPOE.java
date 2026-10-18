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
        
        // Users can only send messages if they have logged in successfully.
        if (!loginSuccessful) {
            System.out.println("You must log in successfully before you can send messages.");
            scanner.close();
            return;
        }

        System.out.println();
        System.out.println("Welcome to QuickChat.");

        // The user decides how many messages they want to enter.
        System.out.print("How many messages do you want to enter? ");
        int numberOfMessages = readNumber(scanner);
        while (numberOfMessages < 1) {
            System.out.print("Please enter a number greater than 0: ");
            numberOfMessages = readNumber(scanner);
        }

        // The menu keeps running until the user chooses 3 (Quit).
        boolean messagesEntered = false;
        int choice = 0;

        while (choice != 3) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            choice = readNumber(scanner);

            if (choice == 1) {
                if (messagesEntered) {
                    System.out.println("You have already entered all " + numberOfMessages + " of your messages.");
                } else {
                    sendMessages(scanner, numberOfMessages);
                    messagesEntered = true;
                }
            } else if (choice == 2) {
                System.out.println("Coming Soon.");
            } else if (choice == 3) {
                System.out.println("Goodbye.");
            } else {
                System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }
        scanner.close();
    }
    
        // Lets the user enter exactly the number of messages they chose at the start.
    public static void sendMessages(Scanner scanner, int numberOfMessages) {
        LoginPOE cellChecker = new LoginPOE();

        // The loop counter (i) is used as the message number.
        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println();
            System.out.println("Enter the details for your next message (" + (numberOfMessages - i) + " left to enter).");

            String messageID = MessagePOE.createMessageID();

            // Recipient: keep asking until the cell number is valid.
            String recipient = "";
            boolean recipientValid = false;
            while (!recipientValid) {
                System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                recipient = scanner.nextLine();
                System.out.println(MessagePOE.checkRecipientCell(recipient));
                recipientValid = cellChecker.checkCellPhoneNumber(recipient);
            }

            // Message: keep asking until it is 250 characters or less.
            String messageText = "";
            boolean messageValid = false;
            while (!messageValid) {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = scanner.nextLine();
                System.out.println(MessagePOE.checkMessageLength(messageText));
                messageValid = messageText.length() <= 250;
            }

            MessagePOE message = new MessagePOE(messageID, i, recipient, messageText);

            System.out.println();
            System.out.println(message.getMessageIDMessage());
            System.out.println(message.getDetails());

            // Send, disregard or store the message.
            int sendChoice = 0;
            while (sendChoice < 1 || sendChoice > 3) {
                System.out.println("1) Send Message");
                System.out.println("2) Disregard Message");
                System.out.println("3) Store Message to send later");
                System.out.print("Choose an option: ");
                sendChoice = readNumber(scanner);
            }

            System.out.println(message.sentMessage(sendChoice));

            if (sendChoice == 2) {
                String press = scanner.nextLine();
                if (press.trim().equals("0")) {
                    System.out.println("Message deleted.");
                }
            }
        }

        System.out.println();
        System.out.println("Total messages sent: " + MessagePOE.returnTotalMessages());
        System.out.println(MessagePOE.printMessages());
    }

    // Keeps asking until the user types a whole number, so the program never crashes on bad input.
    public static int readNumber(Scanner scanner) {
        int number = 0;
        boolean valid = false;

        while (!valid) {
            try {
                number = Integer.parseInt(scanner.nextLine().trim());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number: ");
            }
        }
        return number;
    }
}