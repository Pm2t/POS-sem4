package se.kth.iv1350.pos.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtInvSysTest {
    private ExtInvSys instanceToTest;
    private ItemDTO itemInfoTest;

    @BeforeEach
    void setUp() {
        instanceToTest = new ExtInvSys();
    }

    @AfterEach
    void tearDown() {
        instanceToTest = null;
    }

    @Test
    void testGetItemInfoWithValidItemID(){
        int validItemID = 11;

        try {
            itemInfoTest = instanceToTest.getItemInfo(validItemID);
        }catch (IdentifierDoesNotExistException idnee){
            fail("Exception was thrown when it should not have been.");
        }

        boolean expResult = true;
        boolean result = itemInfoTest.getItemName().equals("Milk");

        assertEquals(expResult, result, "The method getItemInfo returned wrong item info.");
    }

    @Test
    void testGetItemInfoWithInvalidID(){
        int invalidItemID = 40;

        try {
            itemInfoTest = instanceToTest.getItemInfo(invalidItemID);
        }catch (IdentifierDoesNotExistException idnee){
            assertTrue(idnee.getMessage().contains("does not exist"), "Wrong exception message.");
            assertEquals(idnee.getItemIDThatDoesNotExist(), invalidItemID, "Wrong item id in exception.");
        }
    }

    @Test
    void testItemIDThatTriggersDatabaseException(){
        int itemIdThatTriggersDataBaseException = 14;

        try{
            itemInfoTest = instanceToTest.getItemInfo(itemIdThatTriggersDataBaseException);
        }catch (DatabaseAccessUnavailableException daue){
            assertTrue(daue.getMessage().contains("inventory system could not be reached"), "The exception message is incorrect.");
        }catch (IdentifierDoesNotExistException idnee){
            fail("Exception was thrown when it should not have been.");
        }
    }






}