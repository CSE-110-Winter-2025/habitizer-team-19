package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import java.sql.Time;

public class TimeTest {

    @Test
    public void getHours() {
        Time time = new Time(1,2,3);

        assertThat(time.getHours(), is(1));
    }

    @Test
    public void getMinutes() {
        Time time = new Time(1,2,3);

        assertThat(time.getMinutes(), is(2));

    }

    @Test
    public void getSeconds() {
        Time time = new Time(1,2,3);

        assertThat(time.getSeconds(), is(3));
    }

    @Test
    public void setHours() {
        Time time = new Time(1,2,3);
        time.setHours(4);

        assertThat(time.getHours(), is(4));
    }

    @Test
    public void setMinutes() {
        Time time = new Time(1,2,3);
        time.setMinutes(4);

        assertThat(time.getMinutes(), is(4));
    }

    @Test
    public void setSeconds() {
        Time time = new Time(1,2,3);
        time.setSeconds(4);

        assertThat(time.getSeconds(), is(4));
    }
}