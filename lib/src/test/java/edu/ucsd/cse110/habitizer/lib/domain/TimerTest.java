package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class TimerTest {
    private Timer timer;

    @Before
    public void setUp() throws Exception {
        timer = new Timer();
    }

    @After
    public void tearDown() {
        timer = null;
    }


    @Test
    public void testStartTimer() {
        timer.startTimer();
        assertTrue("Elapsed time should be >= 0 after starting timer",
                   timer.getElapsedTime() >= 0);
    }

    @Test
    public void testGetElapsedTime() throws InterruptedException {
        timer.startTimer();

        //sleep for 2 seconds
        Thread.sleep(2000);
        long elapsedTime = timer.getElapsedTime();
        assertTrue("Elapsed time should be at least 2 seconds after sleeping for 2 seconds",
                   elapsedTime >= 2);
    }

    @Test
    public void testGetElapsedTimeWithoutStarting() {
        long elapsedTime = timer.getElapsedTime();
        assertEquals("Elapsed time should be 0 if timer never started",
                     0, elapsedTime);
    }

    @Test
    public void testGetElapsedTimeAfterRestarting() throws InterruptedException {
        timer.startTimer();
        Thread.sleep(1000); //sleep for 1 second
        long firstElapsedTime = timer.getElapsedTime();
        assertTrue("Should be at least 1 after sleeping for 1 second",
                   firstElapsedTime >= 1);

        //Restart timer
        timer.startTimer();
        Thread.sleep(1000); //sleep another second
        long secondElapsedTime = timer.getElapsedTime();
        assertTrue("Should be around 1 after restarting and sleeping a second",
                   secondElapsedTime >= 1 && secondElapsedTime < 2);
    }
}