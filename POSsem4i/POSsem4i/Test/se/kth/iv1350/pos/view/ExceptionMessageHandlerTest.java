package se.kth.iv1350.pos.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionMessageHandlerTest {
    ExceptionMessageHandler instanceToTest;

    private final ByteArrayOutputStream outputFromTestToArray = new ByteArrayOutputStream();
    private final PrintStream standOut = System.out;

    @BeforeEach
    void setUp() {
        instanceToTest = new ExceptionMessageHandler();
        System.setOut(new PrintStream(outputFromTestToArray));
    }

    @AfterEach
    void tearDown() {
        instanceToTest = null;
        System.setOut(standOut);

    }

    @Test
    void testCorrectMessageInExceptionMessage() {
        String testMessage = "Test message test message.";
        instanceToTest.printExceptionMessage(testMessage);

        boolean expResult = true;
        boolean result = outputFromTestToArray.toString().contains(testMessage);

        assertEquals(expResult, result, "The exception message was not correctly added to the exception message builder.");
    }
}