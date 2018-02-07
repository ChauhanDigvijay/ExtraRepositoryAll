Pod::Spec.new do |s|
  s.name             = "HDK"
  s.version          = "0.0.8"
  s.summary          = "HDK"      
  s.homepage         = "http://github.com/fishbowl"
  s.license          = "Copyright (c) 2016 Fishbowl, Inc."
  s.author           = { "Anand" => "anand@vthink.co.in" }
  s.source           = { :git => "https://github.com.fishbowl", :tag => s.version }  
  s.source_files     = "HDK", "HWAY_HDK_iOS/**/*.swift"
  s.resources = ''

  s.frameworks       = "UIKit"  
end