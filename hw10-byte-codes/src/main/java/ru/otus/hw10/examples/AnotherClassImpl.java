package ru.otus.hw10.examples;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hw10.framework.Log;

@Slf4j
public class AnotherClassImpl implements AnotherClassInterface {

    @Override
    public void someMethod(Long param1) {
        log.info("someMethod, param1:{}", param1);
    }

    @Override
    @Log
    public void someMethod(String param1) {
        log.info("someMethod, param1:{}", param1);
    }

    @Override
    @Log
    public void someMethod(Long param1, String param2) {
        log.info("someMethod, param1:{}, param2:{}", param1, param2);
    }
}
