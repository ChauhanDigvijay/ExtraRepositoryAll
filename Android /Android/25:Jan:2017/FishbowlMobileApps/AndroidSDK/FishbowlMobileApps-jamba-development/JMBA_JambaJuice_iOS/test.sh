#!/usr/bin/env bash
xcodebuild -workspace JambaJuice.xcworkspace -scheme JambaJuice clean test -sdk iphonesimulator ONLY_ACTIVE_ARCH=NO 

