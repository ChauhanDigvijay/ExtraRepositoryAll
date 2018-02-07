Pod::Spec.new do |s|
  s.name         = "InCommSDK"
  s.version      = "0.0.1"
  s.summary      = "A short description of InComm-iOS-SDK."
  s.homepage     = "https://github.com/fishbowl"
  s.license      = "Copyright (c) 2016 Fishbowl, Inc."
  s.author       = { "Anand" => "anand@vthink.co.in" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/fishbowl", :tag => s.version }
  s.source_files  = "InCommSDK", "InCommSDK/**/*.swift"

  s.dependency "SwiftyJSON"
  s.dependency "Alamofire"
  s.dependency "CryptoSwift"
end
