package ru.otus.hw10.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

public class Ioc {

    private Ioc() {}

    public static <I, T extends I> I createInstance(Class<I> interfaceClass, Class<T> instanceClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = instanceClass.getDeclaredConstructor();
        var handler = new LoggingInvocationHandler<I>(constructor.newInstance());

        @SuppressWarnings("unchecked")
        var proxy = (I) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {interfaceClass}, handler);
        return proxy;
    }

    @Slf4j
    static class LoggingInvocationHandler<I> implements InvocationHandler {

        private final I instance;
        private final List<String> methodsToLog;

        LoggingInvocationHandler(I instance) {
            this.instance = instance;
            methodsToLog = Arrays.stream(instance.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .map(this::getMethodShortName)
                    .toList();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            var methodShortName = getMethodShortName(method);
            if (methodsToLog.stream().anyMatch(mt -> mt.equals(methodShortName))) {
                log.info(
                        "executed method: {}, params: {}",
                        method.getName(),
                        Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", ")));
            }
            return method.invoke(instance, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + instance + '}';
        }

        private String getMethodShortName(Method method) {
            return method.getName()
                    + " "
                    + Arrays.stream(method.getParameterTypes())
                            .map(Class::getTypeName)
                            .collect(Collectors.joining(":"));
        }
    }
}
