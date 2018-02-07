#!/usr/bin/env bash
set -e 

VERSION=`/usr/libexec/PlistBuddy -c "Print :CFBundleShortVersionString" JambaKiosk/Info.plist`
BUILD=`/usr/libexec/PlistBuddy -c "Print :CFBundleVersion" JambaKiosk/Info.plist`

echo "$VERSION.$BUILD"
