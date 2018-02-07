Pod::Spec.new do |s|
  s.name                    = "SpendGoSDK"
  s.version                 = "0.0.3"
  s.summary                 = "A short description of SpendGo-iOS-SDK."
  s.homepage                = "https://github.com/hathway/SpendGo-iOS-SDK"
  s.license                 = "Copyright (c) 2015 Hathway, Inc."
  s.author                  = { "eneko" => "eneko@wearehathway.com" }
  s.platform                = :ios, "8.0"
  s.source                  = { :git => "git@github.com:hathway/SpendGo-iOS-SDK.git", :tag => s.version }
  s.source_files            = "SpendGoSDK", "SpendGoSDK/**/*.swift"

  s.dependency "SwiftyJSON"
  s.dependency "Alamofire"
  s.dependency "CryptoSwift"
  s.dependency "XCGLogger"
end
