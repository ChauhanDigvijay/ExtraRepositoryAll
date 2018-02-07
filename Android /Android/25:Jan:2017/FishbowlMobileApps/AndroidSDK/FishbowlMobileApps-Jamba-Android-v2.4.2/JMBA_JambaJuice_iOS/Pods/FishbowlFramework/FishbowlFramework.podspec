Pod::Spec.new do |s|
s.name                = "FishbowlFramework"
s.version             = "1.1.0"
s.license             = "MIT"
s.homepage            = "https://www.fishbowl.com"
s.author              = { "Fishbowl" => "pkathuria_ic@fishbowl.com"  }

s.summary             = "FishBowlLibrary for iOS by Fishbowl"
s.description         = "FishBowlLibrary by Fishbowl helps to get the analytics of users"

s.source              = { :git => "https://github.com/fishbowlinc/FishbowlFramework",
:tag => "0.1.3" }
s.documentation_url   = "https://fishbowl-doc.readme.io/v1.0/reference#swift-sdk-integration"
s.platform            = :ios , "6.0"


s.ios.preserve_paths   = '**'
s.vendored_frameworks = 'FishBowlLibrary.framework'




end
