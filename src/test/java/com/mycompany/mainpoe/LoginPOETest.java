/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.mainpoe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class LoginPOETest {

    // checkUserName

    @Test
    void usernameCorrectlyFormatted() {
        LoginPOE login = new LoginPOE();
        assertTrue(login.checkUserName("Lwandle_S"));
    }

    @Test
    void usernameIncorrectlyFormatted() {
        LoginPOE login = new LoginPOE();
        assertFalse(login.checkUserName("Lwandlesssss!"));
    }

    // checkPasswordComplexity 

    @Test
    void passwordMeetsComplexityRequirements() {
        LoginPOE login = new LoginPOE();
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void passwordDoesNotMeetComplexityRequirements() {
        LoginPOE login = new LoginPOE();
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // checkCellPhoneNumber 

    @Test
    void cellPhoneNumberCorrectlyFormatted() {
        LoginPOE login = new LoginPOE();
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void cellPhoneNumberIncorrectlyFormatted() {
        LoginPOE login = new LoginPOE();
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // registerUser messages 

    @Test
    void registerUserReturnsAllSuccessMessages() {
        LoginPOE login = new LoginPOE();
        String result = login.registerUser("Lwandle", "Sakwe", "Lwandle_S", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(result.contains("Username successfully captured."));
        assertTrue(result.contains("Password successfully captured."));
        assertTrue(result.contains("Cell number successfully captured."));
    }

    @Test
    void registerUserReturnsUsernameFailureMessage() {
        LoginPOE login = new LoginPOE();
        String result = login.registerUser("Lwandle", "Sakwe", "lwandlessss", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(result.contains(
                "Username is not correctly formatted; please ensure that your username " +
                "contains an underscore and is no more than 16 characters in length."));
    }

    // loginUser (assertTrue / assertFalse)

    @Test
    void loginSuccessful() {
        LoginPOE login = new LoginPOE();
        login.registerUser("Lwandle", "Sakwe", "Lwandle_S", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("Lwandle_S", "Ch&&sec@ke99!"));
    }

    @Test
    void loginFailed() {
        LoginPOE login = new LoginPOE();
        login.registerUser("Lwandle", "Sakwe", "Lwandle_S", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("Lwandle_S", "wrongPassword"));
    }

    // returnLoginStatus

    @Test
    void returnLoginStatusForSuccessfulLogin() {
        LoginPOE login = new LoginPOE();
        login.registerUser("Lwandle", "Sakwe", "Lwandle_S", "Ch&&sec@ke99!", "+27838968976");
        boolean loggedIn = login.loginUser("Lwandle_S", "Ch&&sec@ke99!");
        assertEquals("Welcome Lwandle, it is great to see you.", login.returnLoginStatus(loggedIn));
    }

    @Test
    void returnLoginStatusForFailedLogin() {
        LoginPOE login = new LoginPOE();
        login.registerUser("Lwandle", "Sakwe", "Lwandle_S", "Ch&&sec@ke99!", "+27838968976");
        boolean loggedIn = login.loginUser("Lwandle_S", "wrongPassword");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(loggedIn));
    }
}
