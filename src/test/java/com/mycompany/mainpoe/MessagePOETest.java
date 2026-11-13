/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.mainpoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ADMIN
 */
public class MessagePOETest {

    // Test data from the brief (Test Case 1 and Test Case 2).
    private final String recipient1 = "+27718693002";
    private final String message1 = "Hi Mike, can you join us for dinner tonight?";
    private final String recipient2 = "08575975889";
    private final String message2 = "Hi Keegan, did you receive the payment?";

    // Start every test with empty message lists.
    @BeforeEach
    void clearLists() {
        MessagePOE.clearMessages();
    }
    
    
    // Part 3 test data (Test Data Messages 1 to 5 from the brief), entered by the developer.
    // Message 1 and 4 are sent, 2 and 5 are stored and 3 is disregarded.
    private void loadTestData() {
        new MessagePOE("1111111111", 1, "+27834557896", "Did you get the cake?").sentMessage(1);
        new MessagePOE("2222222222", 2, "+27838884567", "Where are you? You are late! I have asked you to be on time.").sentMessage(3);
        new MessagePOE("3333333333", 3, "+27834484567", "Yohoooo, I am at your gate.").sentMessage(2);
        new MessagePOE("4444444444", 4, "0838884567", "It is dinner time!").sentMessage(1);
        new MessagePOE("5555555555", 5, "+27838884567", "Ok, I am leaving without you.").sentMessage(3);
    }

    // Message length (success and failure)
    @Test
    void messageLengthSuccess() {
        assertEquals("Message ready to send.", MessagePOE.checkMessageLength(message1));
    }

    @Test
    void messageLengthFailure() {
        String longMessage = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                MessagePOE.checkMessageLength(longMessage));
    }

    // Recipient number (success and failure)
    @Test
    void recipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.", MessagePOE.checkRecipientCell(recipient1));
    }

    @Test
    void recipientIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                MessagePOE.checkRecipientCell(recipient2));
    }

    // Message hash
    @Test
    void messageHashCorrectForTestCase1() {
        MessagePOE message = new MessagePOE("0012345678", 0, recipient1, message1);
        assertEquals("00:0:HITONIGHT", message.getMessageHash());
    }

    // Tests the rest of the hashes in a loop.
    @Test
    void messageHashesInLoop() {
        String[] ids = {"0012345678", "0198765432"};
        String[] recipients = {recipient1, recipient2};
        String[] messages = {message1, message2};
        String[] expectedHashes = {"00:0:HITONIGHT", "01:1:HIPAYMENT"};

        for (int i = 0; i < messages.length; i++) {
            MessagePOE message = new MessagePOE(ids[i], i, recipients[i], messages[i]);
            assertEquals(expectedHashes[i], message.getMessageHash());
        }
    }

    // Message ID
    @Test
    void messageIDIsCreated() {
        String id = MessagePOE.createMessageID();
        MessagePOE message = new MessagePOE(id, 0, recipient1, message1);

        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}"));
        assertTrue(MessagePOE.checkMessageID(id));
        assertEquals("Message ID generated: " + id, message.getMessageIDMessage());
    }

    // Message sent (1 = send, 2 = disregard, 3 = store)
    @Test
    void sendMessageSelected() {
        MessagePOE message = new MessagePOE("0012345678", 0, recipient1, message1);
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    void disregardMessageSelected() {
        MessagePOE message = new MessagePOE("0198765432", 1, recipient2, message2);
        assertEquals("Press 0 to delete the message.", message.sentMessage(2));
    }

    @Test
    void storeMessageSelected() {
        MessagePOE message = new MessagePOE("0012345678", 0, recipient1, message1);
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    // Total number of messages sent (Message 1 sent, Message 2 discarded)
    @Test
    void totalMessagesSent() {
        MessagePOE first = new MessagePOE("0012345678", 0, recipient1, message1);
        MessagePOE second = new MessagePOE("0198765432", 1, recipient2, message2);

        first.sentMessage(1);
        second.sentMessage(2);

        assertEquals(1, MessagePOE.returnTotalMessages());
    }



    // Part 3 tests

    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        loadTestData();
        assertEquals(List.of("Did you get the cake?", "It is dinner time!"), MessagePOE.getSentMessages());
    }

    @Test
    void displayLongestMessage() {
        loadTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.", MessagePOE.displayLongestMessage());
    }

    @Test
    void searchForMessageID() {
        loadTestData();
        assertEquals("Recipient: 0838884567, Message: It is dinner time!", MessagePOE.searchByMessageID("4444444444"));
    }

    @Test
    void searchMessagesForRecipient() {
        loadTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.\nOk, I am leaving without you.",
                MessagePOE.searchByRecipient("+27838884567"));
    }

    @Test
    void deleteMessageUsingHash() {
        loadTestData();
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                MessagePOE.deleteByHash("22:2:WHERETIME"));
        assertEquals(List.of("Ok, I am leaving without you."), MessagePOE.getStoredMessages());
    }

    @Test
    void displayReportOfSentMessages() {
        loadTestData();
        String expected = "Message ID: 1111111111\n"
                + "Message Hash: 11:1:DIDCAKE\n"
                + "Recipient: +27834557896\n"
                + "Message: Did you get the cake?\n\n"
                + "Message ID: 4444444444\n"
                + "Message Hash: 44:4:ITTIME\n"
                + "Recipient: 0838884567\n"
                + "Message: It is dinner time!\n\n";
        assertEquals(expected, MessagePOE.displayReport("Sent"));
    }

    // The stored messages are saved to the JSON file, then read back into the arrays.
    @Test
    void storedMessagesAreReadBackFromJsonFile() {
        loadTestData();
        MessagePOE.clearMessages();
        MessagePOE.loadStoredMessages();

        assertEquals(List.of("Where are you? You are late! I have asked you to be on time.", "Ok, I am leaving without you."),
                MessagePOE.getStoredMessages());
        assertEquals("Recipient: +27838884567, Message: Ok, I am leaving without you.",
                MessagePOE.searchByMessageID("5555555555"));
    }
}
