plugins {
    id("io.freefair.lombok")
}

dependencies {
    implementation ("ch.qos.logback:logback-classic")
    implementation("com.fasterxml.jackson.core:jackson-core")
    compileOnly("org.projectlombok:lombok")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.otus.hw10.Main"
    }
}
