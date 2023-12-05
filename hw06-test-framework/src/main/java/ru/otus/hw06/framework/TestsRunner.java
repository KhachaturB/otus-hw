package ru.otus.hw06.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import ru.otus.hw06.framework.annotations.After;
import ru.otus.hw06.framework.annotations.Before;
import ru.otus.hw06.framework.annotations.Test;

@SuppressWarnings({"java:S106", "java:S112"})
public class TestsRunner {

    record TestResult(int successfulTests, int failedTests) {}

    record TestMethods(Constructor<?> constructor, List<Method> before, List<Method> tests, List<Method> after) {}

    public static void runTests(String className) {
        Class<?> testClass;
        try {
            testClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не найден заданный класс", e);
        }
        checkClass(testClass);

        var testMethods = findTestMethods(testClass);
        var testResults = runTests(testMethods);
        System.out.printf(
                """
                                   Выполнение тестов завершено:
                                \tуспешных тестов: %d
                                \tпроваленных тестов: %d
                        """,
                testResults.successfulTests, testResults.failedTests);
    }

    private static void checkClass(Class<?> testClass) {
        var testMethods = getMethods(testClass, Test.class);
        if (testMethods.isEmpty()) {
            throw new RuntimeException("У класса нет тестовых методов");
        }
        var hasNoOpenNoArgsConstructor = Arrays.stream(testClass.getConstructors())
                .noneMatch(constructor ->
                        constructor.getParameterCount() == 0 && Modifier.isPublic(constructor.getModifiers()));
        if (hasNoOpenNoArgsConstructor) {
            throw new RuntimeException("У класса нет открытого конструктора без аргументов");
        }
    }

    private static TestMethods findTestMethods(Class<?> testClass) {
        var beforeMethods = getMethods(testClass, Before.class);
        var testMethods = getMethods(testClass, Test.class);
        var afterMethods = getMethods(testClass, After.class);

        Constructor<?> constructor;
        try {
            constructor = testClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return new TestMethods(constructor, beforeMethods, testMethods, afterMethods);
    }

    private static TestResult runTests(TestMethods methods) {
        var successfulTests = 0;
        var failedTests = 0;
        for (var test : methods.tests) {
            Object instance;
            try {
                instance = methods.constructor.newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            runMethods(methods.before, instance);
            try {
                test.invoke(instance);
                successfulTests++;
            } catch (Exception ignore) {
                failedTests++;
            }
            runMethods(methods.after, instance);
        }

        return new TestResult(successfulTests, failedTests);
    }

    private static List<Method> getMethods(Class<?> testClass, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(testClass.getMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .toList();
    }

    private static void runMethods(List<Method> methods, Object instance) {
        try {
            for (var method : methods) {
                method.invoke(instance);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
