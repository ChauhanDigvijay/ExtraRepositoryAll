#!/usr/bin/env bash
xcodebuild -workspace SpendGoSDK.xcworkspace -scheme SpendGoSDK test -sdk iphonesimulator ONLY_ACTIVE_ARCH=NO
