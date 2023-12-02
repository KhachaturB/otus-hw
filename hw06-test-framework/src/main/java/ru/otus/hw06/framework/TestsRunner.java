package ru.otus.hw06.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.otus.hw06.framework.annotations.After;
import ru.otus.hw06.framework.annotations.Before;
import ru.otus.hw06.framework.annotations.Test;

@SuppressWarnings({"java:S106", "java:S112"})
public class TestsRunner {

    record TestResult(int successfulTests, int failedTests) {}

    public static void runTests(String className) {
        Class<?> testClass;
        try {
            testClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не найден заданный класс", e);
        }
        checkClass(testClass);

        var result = runTests(testClass);
        System.out.printf(
                """
                           Выполнение тестов завершено:
                        \tуспешных тестов: %d
                        \tпроваленных тестов: %d
                """,
                result.successfulTests, result.failedTests);
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

    private static TestResult runTests(Class<?> testClass) {
        var beforeMethods = getMethods(testClass, Before.class);
        var testMethods = getMethods(testClass, Test.class);
        var afterMethods = getMethods(testClass, After.class);
        Constructor<?> constructor = null;
        try {
            constructor = testClass.getConstructor();
        } catch (NoSuchMethodException ignore) {
            // проверили, что контсруктор есть в методе checkClass
        }

        var successfulTests = 0;
        var failedTests = 0;
        for (var test : testMethods) {
            Object instance;
            try {
                assert constructor != null;
                instance = constructor.newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            runMethods(beforeMethods, instance);
            try {
                test.invoke(instance);
                successfulTests++;
            } catch (Exception ignore) {
                failedTests++;
            }
            runMethods(afterMethods, instance);
        }

        return new TestResult(successfulTests, failedTests);
    }

    private static List<Method> getMethods(Class<?> testClass, Class<?> annotationClass) {
        var methods = new ArrayList<Method>();
        for (var method : testClass.getMethods()) {
            for (var annotation : method.getAnnotations()) {
                if (annotation.annotationType().equals(annotationClass)) {
                    methods.add(method);
                    break;
                }
            }
        }
        return methods;
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
