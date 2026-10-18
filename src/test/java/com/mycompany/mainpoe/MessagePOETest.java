/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.mainpoe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ADMIN
 */
public class MessagePOETest {

    // Test data from the brief (Test Case 1 and Test Case 2).
    private final String recipient1 = "+27718693002";
    private final String message1 = "Yo Avuyile, can you hop on Fortnite?";
    private final String recipient2 = "08575975889";
    private final String message2 = "Ekse Zuko, when are you paying back my money?";

    // Start every test with empty message lists.
    @BeforeEach
    void clearLists() {
        MessagePOE.clearMessages();
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
}
