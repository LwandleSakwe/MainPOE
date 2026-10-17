/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mainpoe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ADMIN
 */
public class MessagePOE {

    // The details of one message.
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // These lists are shared by every message while the program is running.
    private static ArrayList<MessagePOE> sentMessages = new ArrayList<>();
    private static ArrayList<MessagePOE> storedMessages = new ArrayList<>();

    // Creates a message and automatically creates its hash.
    public MessagePOE(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash(messageID, messageNumber, messageText);
    }

    public String getMessageHash() {
        return messageHash;
    }

    // Creates a random ten-digit message ID, one digit at a time.
    public static String createMessageID() {
        Random random = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + random.nextInt(10);
        }
        return id;
    }

    // The message ID must not be more than ten characters.
    public static boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    // The message must not be more than 250 characters.
    public static String checkMessageLength(String messageText) {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int extra = messageText.length() - 250;
            return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
        }
    }

    // Reuses the cell number check from LoginPOE (same method as Part 1).
    public static String checkRecipientCell(String recipient) {
        LoginPOE login = new LoginPOE();
        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Hash = first two digits of the ID : message number : first word + last word, in capitals.
    // Example: 00:0:HITONIGHT
    public static String createMessageHash(String messageID, int messageNumber, String messageText) {
        String[] words = messageText.trim().split("\\s+");

        // Remove punctuation such as "?" or "," so only letters and numbers are left.
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");

        String hash = messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // Choice 1 = send, 2 = disregard, 3 = store.
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else {
            storeMessage();
            return "Message successfully stored.";
        }
    }

    public String getMessageIDMessage() {
        return "Message ID generated: " + messageID;
    }

    // All the details of one message, in the order from the brief's test data.
    public String getDetails() {
        return "Message Number: " + messageNumber + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Message ID: " + messageID + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n";
    }

    // Returns all the messages sent while the program is running.
    public static String printMessages() {
        StringBuilder result = new StringBuilder();
        for (MessagePOE message : sentMessages) {
            result.append(message.getDetails()).append("\n");
        }
        return result.toString();
    }

    // Returns how many messages have been sent.
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Clears both lists (used by the unit tests so each test starts fresh).
    public static void clearMessages() {
        sentMessages.clear();
        storedMessages.clear();
    }

    // Stores every stored message in messages.json as a JSON array.
    // References: JSON format from https://www.json.org/json-en.html
    // Writing to a file with FileWriter from https://docs.oracle.com/javase/tutorial/essential/io/index.html
    public void storeMessage() {
        storedMessages.add(this);

        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write("[\n");

            for (int i = 0; i < storedMessages.size(); i++) {
                MessagePOE m = storedMessages.get(i);

                writer.write("  {\n");
                writer.write("    \"messageID\": \"" + escapeJson(m.messageID) + "\",\n");
                writer.write("    \"messageNumber\": " + m.messageNumber + ",\n");
                writer.write("    \"messageHash\": \"" + escapeJson(m.messageHash) + "\",\n");
                writer.write("    \"recipient\": \"" + escapeJson(m.recipient) + "\",\n");
                writer.write("    \"message\": \"" + escapeJson(m.messageText) + "\"\n");
                writer.write("  }");

                // No comma after the last message.
                if (i < storedMessages.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("]\n");
        } catch (IOException e) {
            System.out.println("Could not store the message: " + e.getMessage());
        }
    }

    // Quotes and backslashes inside text must be escaped to keep the JSON valid.
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}