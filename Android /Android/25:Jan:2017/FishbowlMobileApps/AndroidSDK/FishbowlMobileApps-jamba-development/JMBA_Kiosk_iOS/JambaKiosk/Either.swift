//
//  Either.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/19/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

enum Either<T1, T2> {
    case Left(T1)
    case Right(T2)
}
