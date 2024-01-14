package ru.otus.hw10;

import java.lang.reflect.InvocationTargetException;
import ru.otus.hw10.examples.AnotherClassImpl;
import ru.otus.hw10.examples.AnotherClassInterface;
import ru.otus.hw10.examples.MyClassImpl;
import ru.otus.hw10.examples.MyClassInterface;
import ru.otus.hw10.framework.Ioc;

public class Main {
    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var myClass = Ioc.createInstance(MyClassInterface.class, MyClassImpl.class);
        myClass.secureAccess("Security Param1");
        myClass.secureAccess("Security Param2", 111);
        myClass.secureAccess("Security Param3", 111, "I'm happy");
        var anotherClass = Ioc.createInstance(AnotherClassInterface.class, AnotherClassImpl.class);
        anotherClass.someMethod(111L);
        anotherClass.someMethod("Some param 1");
        anotherClass.someMethod(222L, "Some param 2");
    }
}
