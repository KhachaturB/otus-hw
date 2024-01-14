plugins {
    id("io.freefair.lombok")
}

dependencies {
    implementation ("ch.qos.logback:logback-classic")
    compileOnly("org.projectlombok:lombok")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.otus.hw10.Main"
    }
}
