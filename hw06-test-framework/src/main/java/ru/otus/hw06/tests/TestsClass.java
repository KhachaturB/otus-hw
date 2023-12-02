package ru.otus.hw06.tests;

import ru.otus.hw06.framework.annotations.After;
import ru.otus.hw06.framework.annotations.Before;
import ru.otus.hw06.framework.annotations.Test;

@SuppressWarnings({"java:S106", "java:S112"})
public class TestsClass {

    @Before
    public void init() {
        System.out.println("Init (@Before) method called");
    }

    @Test
    public void test1() {
        System.out.println("Test1 method called");
    }

    @Test
    public void test2() {
        System.out.println("Test2 method called");
        throw new RuntimeException("Test2 threw new exception");
    }

    @Test
    public void test3() {
        System.out.println("Test3 method called");
    }

    @After
    public void teardown() {
        System.out.println("Teardown (@After) method called");
    }
}
