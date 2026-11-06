#!/usr/bin/env bash
set -euo pipefail

# Find Java 21/17
if [ -d "$HOME/.gradle/jdks/jetbrains_s_r_o_-21-amd64-linux.2" ]; then
    export JAVA_HOME="$HOME/.gradle/jdks/jetbrains_s_r_o_-21-amd64-linux.2"
    export PATH="$JAVA_HOME/bin:$PATH"
fi

if [ -z "${ANDROID_HOME:-}" ]; then
    if [ -d "$HOME/android-sdk" ]; then
        export ANDROID_HOME="$HOME/android-sdk"
    fi
fi

echo "Using Java: $(java -version 2>&1 | head -n 1)"
echo "Building Gravity Defied Release APK..."

chmod +x gradlew
./gradlew assembleRelease

APK_SRC="app/build/outputs/apk/release/app-release.apk"
APK_OUT="gravitydefied.apk"

if [ -f "$APK_SRC" ]; then
    cp "$APK_SRC" "$APK_OUT"
    echo "Successfully built: $APK_OUT ($(du -h "$APK_OUT" | cut -f1))"
else
    echo "Error: Output APK not found at $APK_SRC"
    exit 1
fi
