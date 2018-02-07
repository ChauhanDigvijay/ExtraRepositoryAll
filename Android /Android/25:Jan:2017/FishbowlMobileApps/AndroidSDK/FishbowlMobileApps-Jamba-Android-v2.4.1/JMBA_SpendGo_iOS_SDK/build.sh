#!/usr/bin/env bash
set -e
set -o pipefail && xcodebuild -workspace SpendGoSDK.xcworkspace -scheme SpendGoSDK clean test -sdk iphonesimulator ONLY_ACTIVE_ARCH=NO | xcpretty -c
