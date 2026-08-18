#!/usr/bin/env sh
set -eu
if ! command -v java >/dev/null 2>&1; then
  echo "ERROR: Java is not on PATH. Install/use JDK 8." >&2
  exit 1
fi
if ! java -version 2>&1 | grep -Eq 'version "1\.8\.'; then
  echo "ERROR: This Forge 1.12.2/FG3 project is configured for JDK 8. Set JAVA_HOME and the IDE Gradle JVM to JDK 8." >&2
  java -version >&2
  exit 1
fi
if [ ! -f ./gradlew ]; then
  echo "ERROR: ./gradlew is missing. Copy gradlew, gradlew.bat, and gradle/wrapper from the official Forge 1.12.2-14.23.5.2859 MDK into this folder." >&2
  exit 1
fi
chmod +x ./gradlew
./gradlew --no-daemon clean build --stacktrace
echo "Build complete. Check build/libs/ for magicindustries-1.5.2-1.12.2.jar"
