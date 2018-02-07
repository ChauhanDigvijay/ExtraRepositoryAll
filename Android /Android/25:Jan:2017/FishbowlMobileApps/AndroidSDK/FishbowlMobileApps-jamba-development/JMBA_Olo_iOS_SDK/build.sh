#!/usr/bin/env bash
set -e
set -o pipefail && xcodebuild -workspace Olo.xcworkspace -scheme Olo clean test -sdk iphonesimulator ONLY_ACTIVE_ARCH=NO | xcpretty -c
