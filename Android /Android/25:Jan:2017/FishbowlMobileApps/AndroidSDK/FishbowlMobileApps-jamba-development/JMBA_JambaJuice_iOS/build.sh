#!/usr/bin/env bash
set -e
set -o pipefail && xcodebuild -workspace JambaJuice.xcworkspace -scheme JambaJuice clean test -sdk iphonesimulator ONLY_ACTIVE_ARCH=NO | xcpretty -c

