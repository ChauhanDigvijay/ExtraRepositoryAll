#!/usr/bin/env bash
xcodebuild -workspace Olo.xcworkspace -scheme Olo test -sdk iphonesimulator ONLY_ACTIVE_ARCH=NO
