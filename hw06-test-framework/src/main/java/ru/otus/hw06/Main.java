package ru.otus.hw06;

import ru.otus.hw06.framework.TestsRunner;

@SuppressWarnings("java:S106")
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Не задано имя класса с тестами");
        } else {
            System.out.println("Класс с тестами: " + args[0]);
            TestsRunner.runTests(args[0]);
        }
    }
}
