package org.rustem.utils;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class DateUtilsTest {

    @Test
    public void testStringDate() {
        LocalDate localDate = LocalDate.of(2017, 3, 10);
        String stringDate = DateUtils.stringDate(localDate);

        assertNotNull(stringDate);
        assertEquals(stringDate, "10.03.2017");
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "Date is not must be null")
    public void testStringDateWithNull() {
        DateUtils.stringDate(null);
    }

    @Test
    public void testToDate() {
        String date1 = "10.03.2017";
        String date2 = "06.11.2018 23:23";
        LocalDate localDate1 = DateUtils.toDate(date1);
        LocalDate localDate2 = DateUtils.toDate(date2);
        assertNotNull(localDate1);
        assertEquals(localDate1.getYear(), 2017);
        assertEquals(localDate1.getMonth().getValue(), 3);
        assertEquals(localDate1.getDayOfMonth(), 10);

        assertNotNull(localDate2);
        assertEquals(localDate2.getYear(), 2018);
        assertEquals(localDate2.getMonth().getValue(), 11);
        assertEquals(localDate2.getDayOfMonth(), 6);
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "Date is not must be null")
    public void testToDateWithNull() {
        DateUtils.toDate(null);
    }
}