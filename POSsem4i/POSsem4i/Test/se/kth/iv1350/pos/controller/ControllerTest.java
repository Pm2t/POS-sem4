package se.kth.iv1350.pos.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.integration.CreateExternalSys;
import se.kth.iv1350.pos.integration.IdentifierDoesNotExistException;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.Printer;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private Controller instanceToTest;
    private ItemDTO itemInfoTest;

    private final ByteArrayOutputStream outputFromTestToArray = new ByteArrayOutputStream();
    private final PrintStream standOut = System.out;

    private static final int VALID_ITEM_ID = 11;

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outputFromTestToArray));
        Printer printerTest = new Printer();
        CreateExternalSys creatorTest = new CreateExternalSys();
        instanceToTest = new Controller(creatorTest, printerTest);
        instanceToTest.startNewSale();
    }

    @AfterEach
    void tearDown() {
        System.setOut(standOut);
        instanceToTest = null;
    }

    @Test
    void testStartNewSaleCreatesAnSaleObject() {

        boolean expResult = true;
        boolean result = instanceToTest.getTotalPrice() == 0;

        assertEquals(expResult, result, "A sale object was not created.");
    }

    @Test
    void testCorrectInformationFromEnterItem(){

        try {
            itemInfoTest = instanceToTest.enterItem(VALID_ITEM_ID);
        }catch (IdentifierDoesNotExistException | SystemOperationFailedException exception){
            fail("Exception was thrown when i should not have been.");
        }
        boolean expResult = true;
        boolean result = itemInfoTest.getItemName().equals("Milk");

        assertEquals(expResult, result, "The correct item name was not returned.");
    }

    @Test
    void testEnterItemWithWrongItemID(){

        int invalidItemId = 123;

        try {
            itemInfoTest = instanceToTest.enterItem(invalidItemId);
        }catch (IdentifierDoesNotExistException idnee){
            assertTrue(idnee.getMessage().contains("does not exist"), "Wrong exception message.");
            assertEquals(idnee.getItemIDThatDoesNotExist(), invalidItemId, "Wrong item id in exception.");
        }catch (SystemOperationFailedException sofe){
            fail("Exception was thrown when i should not have been.");
        }
    }

    @Test
    void testCurrentSaleUpdatesCorrectlyInEnterItem(){

        try {
            itemInfoTest = instanceToTest.enterItem(VALID_ITEM_ID);
        }catch (IdentifierDoesNotExistException | SystemOperationFailedException exception){
            fail("Exception was thrown when i should not have been.");
        }

        boolean expResult = true;
        double correctTotalPrice = itemInfoTest.getItemPrice()*(1 + itemInfoTest.getItemVAT());
        boolean result = correctTotalPrice == instanceToTest.getTotalPrice();

        assertEquals(expResult, result, "Sale was not updated correctly.");
    }

    @Test
    void testEndSaleGivesTheTotalCost(){

        try {
            itemInfoTest = instanceToTest.enterItem(VALID_ITEM_ID);
            itemInfoTest = instanceToTest.enterItem(VALID_ITEM_ID);
        }catch (IdentifierDoesNotExistException | SystemOperationFailedException exception){
            fail("Exception was thrown when i should not have been.");
        }
        double totalPriceTest = instanceToTest.endSale();

        boolean expResult = true;
        boolean result = totalPriceTest == itemInfoTest.getItemPrice()*(1 + itemInfoTest.getItemVAT())*2;

        assertEquals(expResult, result, "The correct total cost was not returned.");
    }

    @Test
    void testPaymentReturnsCorrectChangeAmount(){

        try {
            itemInfoTest = instanceToTest.enterItem(VALID_ITEM_ID);
        }catch (IdentifierDoesNotExistException | SystemOperationFailedException exception){
            fail("Exception was thrown when i should not have been.");
        }
        double paidAmountTest = 100;
        double correctChange = paidAmountTest - itemInfoTest.getItemPrice()*(1 + itemInfoTest.getItemVAT());

        boolean expResult = true;
        boolean result = instanceToTest.payment(paidAmountTest) == correctChange;

        assertEquals(expResult, result, "The change amount returned from payment is incorrect.");
    }

    @Test
    void testExceptionNotThrownWhenPassedValidItemID(){
        try {
            itemInfoTest = instanceToTest.enterItem(VALID_ITEM_ID);
        }catch (IdentifierDoesNotExistException | SystemOperationFailedException exception){
            fail("Exception was thrown when it should not have.");
        }
    }

    @Test
    void testEnterItemWithItemIDthatTriggersDatabaseException(){
        int itemIdThatTriggersDataBaseException = 14;

        try {
            instanceToTest.enterItem(itemIdThatTriggersDataBaseException);
        }catch (IdentifierDoesNotExistException idnee){
            fail("Exception was thrown when it should not have.");
        }catch (SystemOperationFailedException sofe){
            assertEquals("Item could not be entered.", sofe.getMessage(), "Wrong message in exception.");
            assertTrue(sofe.getCause().getMessage().contains("inventory system could not be reached"), "The cause of the " +
                    "exception is not correct.");
        }
    }

    @Test
    void testExceptionMessagePrintedToLog(){
        int itemIdThatTriggersDataBaseException = 14;

        try {
            instanceToTest.enterItem(itemIdThatTriggersDataBaseException);
        }catch (IdentifierDoesNotExistException idnee){
            fail("Exception was thrown when it should not have.");
        }catch (SystemOperationFailedException sofe){
            try {
                File exceptionLogFile = new File("ExceptionLog.txt");
                Scanner scanner = new Scanner(exceptionLogFile);
                StringBuilder builder = new StringBuilder();
                while(scanner.hasNext()){
                    builder.append(scanner.nextLine());
                }
                scanner.close();
                assertTrue(builder.toString().contains("The external inventory system could not be reached."), "The text in the log file is incorrect.");
            }catch (FileNotFoundException fnfe){
                fail("Could not find file for the test: " + fnfe.getMessage());
            }
        }
    }
}