# HWAY_HDK_iOS
HDK SDK library to include in iOS mobile applications.

[![travis][travis_img]][travis]

[travis]: https://magnum.travis-ci.com/hathway/HWAY_HDK_iOS
[travis_img]: https://api.travis-ci.com/hathway/HWAY_HDK_iOS.svg?token=pU7bnLhdpXaHcypnAMqR

## Installation
### Via Pods
HDK Framework can be added to any iOS project via Pods by directly referencing the private repository. Specific versions can be targeted with tags, `develop` branch will always contain the latest version.

```
source 'https://github.com/CocoaPods/Specs.git'
platform :ios, '8.0'
use_frameworks!

pod 'HDK', :git => 'git@github.com:hathway/HWAY_HDK_iOS.git', :branch => 'develop'
```

### Via Framework
1. Checkout the HDK project: `git clone git@github.com:hathway/HWAY_HDK_iOS.git`
2. Open the HDK.xcodeproj and build the project
3. Drag the HDK.framework file to your iOS project


