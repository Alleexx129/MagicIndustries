# Magic Industries — NeoForge 1.21.1

Port of Magic Industries 1.5.3 from Forge 1.21.1 to NeoForge 1.21.1.

## Target

- Minecraft 1.21.1
- NeoForge 21.1.248
- Java 21
- ModDevGradle 2.0.144
- Gradle wrapper 9.2.1

## Open in IntelliJ IDEA

1. Install/select a Java 21 JDK.
2. Open this folder in IntelliJ IDEA.
3. Import/sync it as a Gradle project and set the Gradle JVM to Java 21.
4. Run the Gradle `build` task once.
5. Use the generated `runClient` / `runServer` Gradle run configurations for development.

See `INTELLIJ_SETUP.md` for troubleshooting and `GIT_BRANCH_1.21.1.md` for the safe GitHub branch workflow.

## Important verification note

The porting environment could not resolve `services.gradle.org`, so it could not download Gradle/Minecraft/NeoForge dependencies for a real compile. The code and resources were statically migrated and checked, but your first local `gradlew build` is the dependency-resolved compile verification.
