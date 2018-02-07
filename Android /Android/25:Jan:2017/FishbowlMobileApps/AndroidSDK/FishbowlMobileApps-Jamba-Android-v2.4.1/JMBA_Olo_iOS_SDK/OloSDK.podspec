Pod::Spec.new do |s|
  s.name         = "OloSDK"
  s.version      = "0.0.5"
  s.summary      = "A short description of Olo-iOS-SDK."
  s.homepage     = "https://github.com/hathway/Olo-iOS-SDK"
  s.license      = "Copyright (c) 2015 Hathway, Inc."
  s.author       = { "eneko" => "eneko@wearehathway.com" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/hathway/Olo-iOS-SDK", :tag => s.version }
  s.source_files  = "Olo", "Olo/**/*.swift"

  s.dependency "SwiftyJSON"
  s.dependency "Alamofire"
  s.dependency "XCGLogger"
end
