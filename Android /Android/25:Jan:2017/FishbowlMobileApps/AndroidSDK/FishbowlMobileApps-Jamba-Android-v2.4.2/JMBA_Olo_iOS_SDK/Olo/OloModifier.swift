//
//  OloModifier.swift
//  JambaJuice
//
//  Created by Taha Samad on 23/04/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloModifier {
    
    public var id: Int64
    public var desc: String
    public var mandatory: Bool
    public var options: [OloOption]
    public var parentChoiceId: String
    public var minSelects: String
    public var maxSelects: String
    public var minChoiceQuantity: String
    public var maxChoiceQuantity: String
    public var maxAggregateQuantity: String
    public var supportsChoiceQuantities: Bool?
    public var choiceQuantityIncrement: String
    
    public init(json: JSON) {
        id                          = json["id"].int64Value
        desc                        = json["description"].stringValue
        mandatory                   = json["mandatory"].boolValue
        parentChoiceId              = json["parentchoiceid"].stringValue
        minSelects                  = json["minselects"].stringValue
        maxSelects                  = json["maxselects"].stringValue
        minChoiceQuantity           = json["minchoicequantity"].stringValue
        maxChoiceQuantity           = json["maxchoicequantity"].stringValue
        maxAggregateQuantity        = json["maxaggregatequantity"].stringValue
        choiceQuantityIncrement     = json["choicequantityincrement"].stringValue
        supportsChoiceQuantities    = json["supportschoicequantities"].boolValue
        options                     = json["options"].arrayValue.map { item in OloOption(json: item) }
    }
    
}
