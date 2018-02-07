#!/usr/bin/env bash
set -e

VERSION=`sh version.sh`
git tag -a $VERSION -m "Version $VERSION"
git push --tags
git push
