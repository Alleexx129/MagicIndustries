@echo off
setlocal
where java >nul 2>nul || (echo ERROR: Java is not on PATH. Install/use JDK 8.& exit /b 1)
java -version 2>&1 | findstr /r /c:"\"1\.8\." >nul || (echo ERROR: This Forge 1.12.2/FG3 project is configured for JDK 8. Set JAVA_HOME and the IntelliJ Gradle JVM to JDK 8.& java -version & exit /b 1)
if exist gradlew.bat (
  call gradlew.bat --no-daemon clean build --stacktrace || exit /b 1
) else (
  echo ERROR: gradlew.bat is missing.
  echo Copy gradlew.bat, gradlew, and gradle\wrapper from the official Forge 1.12.2-14.23.5.2859 MDK into this folder.
  exit /b 1
)
echo Build complete. Check build\libs\ for magicindustries-1.5.2-1.12.2.jar
