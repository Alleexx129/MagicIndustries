# IntelliJ IDEA setup — NeoForge 1.21.1

## Requirements

- IntelliJ IDEA (Community or Ultimate)
- A Java 21 JDK
- Internet access for the first Gradle sync

## First import

1. Extract this project somewhere **outside** your old broken Forge worktree.
2. In IntelliJ IDEA, choose **Open** and select the project folder containing `build.gradle`.
3. Trust the project when IntelliJ asks.
4. In **Settings > Build, Execution, Deployment > Build Tools > Gradle**, set **Gradle JVM** to Java 21.
5. Refresh/sync Gradle.
6. In the Gradle tool window, run `build`.

Windows terminal equivalent:

```powershell
.\gradlew.bat build
```

Linux/macOS terminal equivalent:

```bash
./gradlew build
```

The built mod JAR should appear under `build/libs/`.

## Running Minecraft from IntelliJ

ModDevGradle provides the development runs. After the Gradle sync, use `runClient` for the client and `runServer` for a dedicated server. If IntelliJ looks stale, refresh the Gradle project first; do not use ForgeGradle's old `genIntellijRuns` workflow.

## If Gradle import gets weird

Try these in order:

```powershell
.\gradlew.bat clean
.\gradlew.bat --refresh-dependencies
.\gradlew.bat build
```

Then use **File > Invalidate Caches** only if IntelliJ itself is still showing stale classes after Gradle succeeds.

## JEI

The old JEI integration sources are retained but fully commented out, so JEI is not required to compile this port. If you re-enable the integration later, add the NeoForge JEI artifacts in `build.gradle` rather than the old Forge API package.
