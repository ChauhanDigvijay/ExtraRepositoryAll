#!/usr/bin/env bash
set -e 

VERSION=`/usr/libexec/PlistBuddy -c "Print :CFBundleShortVersionString" JambaJuice/Info.plist`
BUILD=`/usr/libexec/PlistBuddy -c "Print :CFBundleVersion" JambaJuice/Info.plist`

echo "$VERSION.$BUILD"
