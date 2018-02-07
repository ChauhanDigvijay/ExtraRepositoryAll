//
//  String+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 12/22/14.
//  Copyright (c) 2014 hathway. All rights reserved.
//

import Foundation

extension String {

    /// Trim any leading and trailing whitespace, including newlines
    /// - returns: The trimmed string
    public func trim() -> String {
        return trimmingCharacters(in: .whitespacesAndNewlines)
    }

    /// Verifies if the string is a valid email address
    /// - returns: Bool
    public func isEmail() -> Bool {
        return matchesRegex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    }

    /// Verifies if the string is a valid 10 digit phone number
    /// (805) 555-1234, 8055551234, 805-555-1234, 805.555.1234 are all valid
    /// - returns: Bool
    public func isTenDigitPhone() -> Bool {
        let cleanNumber = stringByRemovingNonNumericCharacters()
        return cleanNumber.matchesRegex("^\\d{10}$")
    }
    
    /// Verifies if the string is a valid 12-19 digit card number
    /// http://en.wikipedia.org/wiki/Bank_card_number
    /// - returns: Bool
    public func isCreditCardNumber() -> Bool {
        let cleanNumber = stringByRemovingNonNumericCharacters()
        return (12...19).contains(cleanNumber.characters.count)
    }
    
    /// Verifies if the string is a valid 5 digit zip code
    /// http://en.wikipedia.org/wiki/ZIP_code
    /// - returns: Bool
    public func isZipCode() -> Bool {
        return matchesRegex("^\\d{5}$")
    }
    
    /// Verifies if the string is a valid 3-4 digit cvv
    /// https://www.cvvnumber.com/
    /// - returns: Bool
    public func isSecurityCode() -> Bool {
        return matchesRegex("^(\\d{3}|\\d{4})$")
    }

    /// Pretty-formats ten digit phone numbers (1234567890 --> (123) 456-7890)
    /// - returns: String
    public func prettyFormattedPhoneNumber() -> String {
        let cleanNumber = stringByRemovingNonNumericCharacters()
        if !cleanNumber.isTenDigitPhone() {
            return self
        }
        let areaCode = String(cleanNumber[cleanNumber.startIndex ..< cleanNumber.index(cleanNumber.startIndex, offsetBy: 3)])
        let middleComponent = String(cleanNumber[cleanNumber.index(cleanNumber.startIndex, offsetBy: 3) ..< cleanNumber.characters.index(cleanNumber.startIndex, offsetBy: 6)])
        let lastComponent = String(cleanNumber[cleanNumber.index(cleanNumber.startIndex, offsetBy: 6) ..< cleanNumber.endIndex])
        return "(\(areaCode)) \(middleComponent)-\(lastComponent)"
    }
    
    /// Remove non-numeric characters
    /// - returns: String
    public func stringByRemovingNonNumericCharacters() -> String {
        return components(separatedBy: CharacterSet.decimalDigits.inverted).joined(separator: "")
    }

    /// Matches the string against a regex string
    /// - returns: Bool
    public func matchesRegex(_ regexString: String) -> Bool {
        let regexTest = NSPredicate(format: "SELF MATCHES %@", regexString)
        return regexTest.evaluate(with: self)
    }
    
    /// Return NSDate from ISO string
    public func dateFromISOString() -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter.date(from: self)
    }

    //Formats accepted mm/dd/yyyy or mm-dd-yyyy or mm.dd.yyyy format
    //http://stackoverflow.com/a/8768241/3332383
    public func isValidDateOfBirth() -> Bool {
        let regexString = "^(?:(?:(?:0?[13578]|1[02])(\\/|-|\\.)31)\\1|(?:(?:0?[1,3-9]|1[0-2])(\\/|-|\\.)(?:29|30)\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:0?2(\\/|-|\\.)29\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:(?:0?[1-9])|(?:1[0-2]))(\\/|-|\\.)(?:0?[1-9]|1\\d|2[0-8])\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$"
        return matchesRegex(regexString)
    }
    
    //http://stackoverflow.com/a/20430544/3332383
    public func isValidCreditCardExpiry() -> Bool {
        let regexString = "^(0?[1-9]|1[0-2])\\/(([0-9]{4})$)"
        return matchesRegex(regexString)
    }

    public func isValidStateOrCountryCode() -> Bool {
        let regexString = "^([a-z]{2}|[A-Z]{2})$"
        return matchesRegex(regexString)
    }

}
