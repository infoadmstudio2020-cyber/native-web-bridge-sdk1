#!/bin/bash
# Build script for NativeRuntimeSDK
set -e

echo "Building NativeRuntimeSDK v1.0.1..."
chmod +x gradlew
./gradlew clean :java-library:assembleRelease 2>/dev/null || ./gradlew clean assembleRelease || ./gradlew clean build
echo "Build complete: build/outputs/aar/"
