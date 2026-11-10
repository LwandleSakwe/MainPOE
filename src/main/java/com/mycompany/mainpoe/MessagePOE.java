/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mainpoe;

import java.io.BufferedReader;
import java.io.FileReader;
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

        // Parallel arrays: position 0 in every list belongs to the first message,
    // position 1 to the second message, and so on.
    private static ArrayList<String> messageIDs = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageRecipients = new ArrayList<>();
    private static ArrayList<String> messageTexts = new ArrayList<>();
    private static ArrayList<String> messageFlags = new ArrayList<>(); // "Sent", "Disregard" or "Stored"

    // The three message arrays from the brief.
    private static ArrayList<String> sentMessages = new ArrayList<>();
    private static ArrayList<String> disregardedMessages = new ArrayList<>();
    private static ArrayList<String> storedMessages = new ArrayList<>();

    // The file the stored messages are saved in.
    private static final String storageFile = "messages.json";

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

    // Creates a random ten-digit number and turns it into a String with String.valueOf.
    // References: Random.nextLong from https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Random.html
    // String.valueOf from https://www.w3schools.com/java/ref_string_valueof.asp
    public static String createMessageID() {
        Random random = new Random();
        long number = random.nextLong(1000000000L, 10000000000L);
        return String.valueOf(number);
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
            addToArrays("Sent");
            sentMessages.add(messageText);
            return "Message successfully sent.";
        } else if (choice == 2) {
            addToArrays("Disregard");
            disregardedMessages.add(messageText);
            return "Press 0 to delete the message.";
        } else {
            addToArrays("Stored");
            storedMessages.add(messageText);
            writeStoredFile();
            return "Message successfully stored.";
        }
    }

    // Adds this message to every parallel array.
    private void addToArrays(String flag) {
        messageIDs.add(messageID);
        messageHashes.add(messageHash);
        messageRecipients.add(recipient);
        messageTexts.add(messageText);
        messageFlags.add(flag);
    }

    public String getMessageIDMessage() {
        return "Message ID generated: " + messageID;
    }

    // All the details of one message: ID, Hash, Recipient, Message.
    public String getDetails() {
         return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n";
    }


    // Returns how many messages have been sent.
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Getters so the unit tests can check the sent and stored arrays.
    public static ArrayList<String> getSentMessages() {
        return sentMessages;
    }

    public static ArrayList<String> getStoredMessages() {
        return storedMessages;
    }

    // Clears every array (used by the unit tests so each test starts fresh).
    public static void clearMessages() {
        messageIDs.clear();
        messageHashes.clear();
        messageRecipients.clear();
        messageTexts.clear();
        messageFlags.clear();
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
    }

    // Menu option a: the sender and recipient of every stored message.
    public static String displaySenderAndRecipient(String sender) {
        String result = "";
        for (int i = 0; i < messageTexts.size(); i++) {
            if (messageFlags.get(i).equals("Stored")) {
                result = result + "Sender: " + sender + ", Recipient: " + messageRecipients.get(i) + "\n";
            }
        }
        if (result.equals("")) {
            return "There are no stored messages.";
        }
        return result;
    }

    // Menu option b: the longest stored message.
    public static String displayLongestMessage() {
        String longest = "";
        for (String text : storedMessages) {
            if (text.length() > longest.length()) {
                longest = text;
            }
        }
        if (longest.equals("")) {
            return "There are no stored messages.";
        }
        return longest;
    }

    // Menu option c: search for a message ID and return its recipient and message.
    public static String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                return "Recipient: " + messageRecipients.get(i) + ", Message: " + messageTexts.get(i);
            }
        }
        return "Message ID not found.";
    }

    // Menu option d: every sent or stored message for one recipient.
    public static String searchByRecipient(String recipient) {
        String result = "";
        for (int i = 0; i < messageRecipients.size(); i++) {
            boolean notDisregarded = !messageFlags.get(i).equals("Disregard");
            if (messageRecipients.get(i).equals(recipient) && notDisregarded) {
                if (!result.equals("")) {
                    result = result + "\n";
                }
                result = result + messageTexts.get(i);
            }
        }
        if (result.equals("")) {
            return "No messages found for that recipient.";
        }
        return result;
    }

    // Menu option e: delete a message using its hash.
    public static String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String text = messageTexts.get(i);
                String flag = messageFlags.get(i);

                // Remove the message from the matching message array.
                if (flag.equals("Sent")) {
                    sentMessages.remove(text);
                } else if (flag.equals("Disregard")) {
                    disregardedMessages.remove(text);
                } else {
                    storedMessages.remove(text);
                }

                // Remove it from every parallel array.
                messageIDs.remove(i);
                messageHashes.remove(i);
                messageRecipients.remove(i);
                messageTexts.remove(i);
                messageFlags.remove(i);

                writeStoredFile();
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // Menu option f: a report of every message with the given flag ("Sent" or "Stored").
    public static String displayReport(String flag) {
        String report = "";
        for (int i = 0; i < messageTexts.size(); i++) {
            if (messageFlags.get(i).equals(flag)) {
                report = report + "Message ID: " + messageIDs.get(i) + "\n"
                        + "Message Hash: " + messageHashes.get(i) + "\n"
                        + "Recipient: " + messageRecipients.get(i) + "\n"
                        + "Message: " + messageTexts.get(i) + "\n\n";
            }
        }
        if (report.equals("")) {
            return "There are no " + flag.toLowerCase() + " messages to display.\n";
        }
        return report;
    }

    // Saves every stored message in the JSON file as a JSON array.
    // JSON references: the format (an array of objects with "name": "value" pairs) is from
    // W3Schools (n.d.) JSON Introduction https://www.w3schools.com/js/js_json_intro.asp and
    // JSON.org (n.d.) Introducing JSON https://www.json.org/json-en.html
    // File writing references: W3Schools (n.d.) Java Create and Write To Files
    // https://www.w3schools.com/java/java_files_write.asp and Oracle (n.d.) Basic I/O
    // https://docs.oracle.com/javase/tutorial/essential/io/index.html
    private static void writeStoredFile() {
        try (FileWriter writer = new FileWriter(storageFile)) {
            writer.write("[\n");

            boolean firstMessage = true;
            for (int i = 0; i < messageTexts.size(); i++) {
                if (messageFlags.get(i).equals("Stored")) {
                    // A comma goes before every message except the first one.
                    if (!firstMessage) {
                        writer.write(",\n");
                    }
                    firstMessage = false;

                    writer.write("  {\n");
                    writer.write("    \"messageID\": \"" + escapeJson(messageIDs.get(i)) + "\",\n");
                    writer.write("    \"messageHash\": \"" + escapeJson(messageHashes.get(i)) + "\",\n");
                    writer.write("    \"recipient\": \"" + escapeJson(messageRecipients.get(i)) + "\",\n");
                    writer.write("    \"message\": \"" + escapeJson(messageTexts.get(i)) + "\"\n");
                    writer.write("  }");
                }
            }

            writer.write("\n]\n");
        } catch (IOException e) {
            System.out.println("Could not store the message: " + e.getMessage());
        }
    }

    // Reads the JSON file back into the arrays, so stored messages are still there next time the program runs.
    // References: reading a file line by line with BufferedReader from Oracle (n.d.) Basic I/O
    // https://docs.oracle.com/javase/tutorial/essential/io/index.html and the JSON format from
    // JSON.org (n.d.) Introducing JSON https://www.json.org/json-en.html
    // The file was written by writeStoredFile, so each value is on its own line.
    public static void loadStoredMessages() {
        String id = "";
        String hash = "";
        String recipient = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();

                if (line.startsWith("\"messageID\":")) {
                    id = getJsonValue(line);
                } else if (line.startsWith("\"messageHash\":")) {
                    hash = getJsonValue(line);
                } else if (line.startsWith("\"recipient\":")) {
                    recipient = getJsonValue(line);
                } else if (line.startsWith("\"message\":")) {
                    // "message" is the last value of each stored message, so the message is complete now.
                    String text = getJsonValue(line);
                    messageIDs.add(id);
                    messageHashes.add(hash);
                    messageRecipients.add(recipient);
                    messageTexts.add(text);
                    messageFlags.add("Stored");
                    storedMessages.add(text);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            // No file yet means there are no stored messages, so nothing is loaded.
        }
    }

    // Gets the value from a line such as:  "recipient": "+27718693002",
    private static String getJsonValue(String line) {
        String value = line.substring(line.indexOf(":") + 3, line.lastIndexOf("\""));
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    // Quotes and backslashes inside text must be escaped to keep the JSON valid.
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

//commit