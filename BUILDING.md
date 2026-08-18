# Building Magic Industries for Forge 1.12.2

## Exact target
- Java/JDK 8 bytecode
- Minecraft 1.12.2
- Forge **14.23.5.2859**
- ForgeGradle **3.x** (the current official MinecraftForge 1.12.x MDK style)
- MCP mappings **stable_39** (`stable`, `39-1.12` in FG3 syntax)
- Baubles `1.12-1.5.2`
- JEI API baseline `4.16.1.302`; FG3 dev runtime `4.16.1.1013`

Baubles and JEI are required at runtime. Baubles is the 1.12.2 translation of the original Curios charm slot.

## IntelliJ / Windows setup
1. Install/select a **64-bit JDK 8**.
2. Download the official Forge **1.12.2-14.23.5.2859 MDK**.
3. Copy these wrapper files from that MDK into this project root:
   - `gradlew`
   - `gradlew.bat`
   - `gradle/wrapper/gradle-wrapper.jar`
   - `gradle/wrapper/gradle-wrapper.properties`
4. Open this folder as a Gradle project in IntelliJ.
5. Set **Gradle JVM = JDK 8** for this legacy project. Do not let IntelliJ blindly upgrade the wrapper/plugin.
6. Run:

```text
gradlew.bat genIntellijRuns
gradlew.bat clean build --stacktrace
```

`genIntellijRuns` is only for the IDE run configurations; `build` is the actual compile/reobfuscation test. The finished mod JAR should be under `build/libs/`.

You can also run `compile-local.bat`; it checks that Java 8 is active and invokes the build.

## Dependency fallback
The normal build resolves Baubles and JEI from their historical Maven repositories and deobfuscates them through ForgeGradle.

If a historical repository is unavailable, put these exact production JARs in `libs/`:

```text
libs/Baubles-1.12-1.5.2.jar
libs/jei_1.12.2-4.16.1.1013.jar
```

The build detects those files and uses them as local dependencies. The mod still compiles against the 4.16.1.302 API baseline, while runClient uses 4.16.1.1013 to avoid the 4.16.1.302 ForgeGradle-3 development-runtime access crash.

## First runtime test
Use a clean Forge 14.23.5.2859 instance containing only:
- the built Magic Industries JAR
- Baubles 1.12-1.5.2
- a compatible JEI 4.16.x build

Test a client world and a dedicated server. Then add the rest of the modpack to isolate cross-mod conflicts.

## What to send back if the build fails
Send the complete output from:

```text
gradlew.bat clean build --stacktrace
```

Compiler errors with class/method signatures and line numbers let the remaining 1.12 API mismatches be fixed exactly.
