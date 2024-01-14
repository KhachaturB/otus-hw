package ru.otus.hw10.examples;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hw10.framework.Log;

@Slf4j
public class MyClassImpl implements MyClassInterface {

    @Override
    @Log
    public void secureAccess(String param) {
        log.info("secureAccess, param:{}", param);
    }

    @Override
    public void secureAccess(String param, int param2) {
        log.info("secureAccess, param:{}, param2: {}", param, param2);
    }

    @Override
    @Log
    public void secureAccess(String param, int param2, String param3) {
        log.info("secureAccess, param:{}, param2: {}, param3: {}", param, param2, param3);
    }

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
