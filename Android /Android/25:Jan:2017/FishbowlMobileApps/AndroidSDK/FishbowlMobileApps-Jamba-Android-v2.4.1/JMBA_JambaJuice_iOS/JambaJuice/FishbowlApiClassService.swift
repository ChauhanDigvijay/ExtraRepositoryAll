import FishBowlLibrary
import SwiftyJSON
import OloSDK

enum PointingServer:String{
    case Staging = "Staging"
    case QA = "QA"
    case Production = "Production"
}
public typealias FishbowlCallback = (_ response: AnyObject?, _ error: NSError?) -> Void
public typealias AccessTokenCallback = (_ response: AnyObject?, _ error: DataManagerError?) -> Void
public typealias FishbowlCompleteCallback = () -> Void
public typealias FishbowlCredentialValidationCallback = (_ status:Bool) -> Void

class FishbowlApiClassService: NSObject{
    var fishbowlStoreList:[FishbowlStore] = []
    var guestRequest:FishbowlJSONDictionary = FishbowlJSONDictionary()
    
    var fishbowlAppEventsCallBack:FishbowlCallback?
    
    var forceUpdateViewController:ForceUpdateViewController?
    var forceUpdateBuildVersion = "iOS_BUILD_VERSION"
    var forceBuildUpdateMessage = "iOS_BUILD_UPDATE_MESSAGE"
    var forceBuildUpdate        = "iOS_BUILD_FORCE_UPDATE"
    var forceUpdateBuildCode    = "iOS_BUILD_CODE"
    
    var forceUpdateBuildVersionSetting:[FishbowlMobileSetting] = []
    var forceBuildUpdateMessageSetting:[FishbowlMobileSetting] = []
    var forceBuildUpdateSetting:[FishbowlMobileSetting] = []
    var forceUpdateBuildCodeSetting:[FishbowlMobileSetting] = []
    
    static let sharedInstance=FishbowlApiClassService();
    override init() {
        super.init()
    }
    
    func fbInitialization(pointingServer: String){
        
        switch pointingServer{
        case PointingServer.Staging.rawValue :
            AppConstant.sharedInstance.AppPointingURL(PointingServer: "https://stg-jamba.fishbowlcloud.com/clpapi/")
            AppConstant.sharedInstance.AppPointingClientID(clientId: "201969E1BFD242E189FE7B6297B1B5A9")
            AppConstant.sharedInstance.AppPointingClientSecret(clientSecret: "C65A0DC0F28C469FB7376F972DEFBCB7")
            AppConstant.sharedInstance.AppPointingTanentID(tanentId: "1173")
            AppConstant.sharedInstance.AppPointingSpendgoBaseUrl(spendgoBaseUrl: "https://my.skuped.com")
            
            
            
        case PointingServer.QA.rawValue :
            AppConstant.sharedInstance.AppPointingURL(PointingServer: "https://qa-jamba.fishbowlcloud.com/clpapi/")
            AppConstant.sharedInstance.AppPointingClientID(clientId: "201969E1BFD242E189FE7B6297B1B5A6")
            AppConstant.sharedInstance.AppPointingClientSecret(clientSecret: "C65A0DC0F28C469FB7376F972DEFBCB7")
            AppConstant.sharedInstance.AppPointingTanentID(tanentId: "1173")
            AppConstant.sharedInstance.AppPointingSpendgoBaseUrl(spendgoBaseUrl: "https://my.skuped.com")
            
            guestRequest["clientId"] = "201969E1BFD242E189FE7B6297B1B5A6" as AnyObject
            guestRequest["clientSecret"] = "C65A0DC0F28C469FB7376F972DEFBCB7" as AnyObject
            guestRequest["deviceId"] = self.getDeviceId() as AnyObject
            guestRequest["tenantId"] = "1173" as AnyObject
            
            
        case PointingServer.Production.rawValue :
            AppConstant.sharedInstance.AppPointingURL(PointingServer: "https://jamba.fishbowlcloud.com/clpapi/")
            AppConstant.sharedInstance.AppPointingClientID(clientId: "201969E1BFD242E189FE7B6297B1B5A5")
            AppConstant.sharedInstance.AppPointingClientSecret(clientSecret: "C65A0DC0F28C469FB7376F972DEFBCB8")
            AppConstant.sharedInstance.AppPointingTanentID(tanentId: "1173")
            AppConstant.sharedInstance.AppPointingSpendgoBaseUrl(spendgoBaseUrl: "https://my.spendgo.com")
            
            guestRequest["clientId"] = "201969E1BFD242E189FE7B6297B1B5A5" as AnyObject
            guestRequest["clientSecret"] = "C65A0DC0F28C469FB7376F972DEFBCB8" as AnyObject
            guestRequest["deviceId"] = self.getDeviceId() as AnyObject
            guestRequest["tenantId"] = "1173" as AnyObject
            
        default :
            AppConstant.sharedInstance.AppPointingURL(PointingServer: "https://stg-jamba.fishbowlcloud.com/clpapi")
            AppConstant.sharedInstance.AppPointingClientID(clientId: "201969E1BFD242E189FE7B6297B1B5A9")
            AppConstant.sharedInstance.AppPointingClientSecret(clientSecret: "C65A0DC0F28C469FB7376F972DEFBCB7")
            AppConstant.sharedInstance.AppPointingTanentID(tanentId: "1173")
            AppConstant.sharedInstance.AppPointingSpendgoBaseUrl(spendgoBaseUrl: "https://my.skuped.com")
        }
        
    }
    
    
    
    
    //user registration for clp platform
    func registerUser(_ user:SignUpUserInfo) {
        // Get guest token
        setTokenBeforeFishbowlApiCallForGuest()
        var favStoreCode =  ""
        if (user.favoriteStoreCode != nil) && (user.favoriteStoreCode!.isEmpty){
            favStoreCode = user.favoriteStoreCode!
        }
        
        let param = FishbowlCustomerModalClass(firstName: user.firstName ?? "", lastName: user.lastName ?? "", email: user.emailAddress ?? "", phone: user.phoneNumber?.stringByRemovingNonNumericCharacters() ?? "", smsOptIn: self.convertBoolToString(user.enrollForTextUpdates), emailOptIn: self.convertBoolToString(user.enrollForEmailUpdates), addressStreet: "", addressCity: "", addressZipCode: "", favoriteStore: favStoreCode, dob: (user.birthdate?.ISOString())!, gender: "", username: user.emailAddress ?? "", deviceId: self.getDeviceId(), loginID: "", storeCode: favStoreCode, pushToken: "", pushOptIn: self.convertBoolToString(user.enrollForPushUpdates))
        let objectDataManager = DataManager()
        objectDataManager.signUpSDKApi(dictBody: param.serializeAsJSONDictionary() as [NSObject : AnyObject]) { (result, error) in
            if error != nil{
                if error == DataManagerError.TokenExpiration{
                    // When token expired
                    self.guestAccessToken{
                        let objectDataManager = DataManager()
                        objectDataManager.signUpSDKApi(dictBody: param.serializeAsJSONDictionary() as [NSObject : AnyObject], completion: { (response, error) in
                        })
                    }
                }
            }
            // we are not handle respone
        }
    }
    
    //user registration for clp platform
    func updateUserDetails() {
        // set token for logged in user
        setTokenBeforeFishbowlApiCallForLogin()
        validateFishbowlCredentials(callback: { (status) in
            if !status{
                return
            }else{
                //if the user is not signed in user then return
                guard let user = UserService.sharedUser else {
                    return
                }
                
                let defaults=UserDefaults.standard;
                let userId = defaults.string(forKey: "fishbowl_customer_id") ?? ""
                
                
                //get push token
                let pushToken = defaults.string(forKey: "pushToken") ?? ""
                
                var storeCode = ""
                if let favStore = user.favoriteStore {
                    storeCode = favStore.storeCode
                }
                
                // Inital push option
                var pushOptIn = false
                
                // update push option from user service
                if let user = UserService.sharedUser{
                    pushOptIn = user.pushOptIn
                }
                
                //reasy the params
                let param = FishbowlCustomerModalClass(firstName: user.firstName ?? "", lastName: user.lastName ?? "", email: user.emailAddress ?? "", phone: user.phoneNumber, smsOptIn: convertBoolToString(user.smsOptIn), emailOptIn: convertBoolToString(user.emailOptIn), addressStreet: "", addressCity: "", addressZipCode: "", favoriteStore: storeCode, dob: "", gender: "", username: user.emailAddress ?? "", deviceId: self.getDeviceId(), loginID: userId, storeCode: storeCode, pushToken: pushToken , pushOptIn: convertBoolToString(pushOptIn))
                let objectDataManager = DataManager()
                objectDataManager.updateMemberAPI(dictBody: param.serializeAsJSONDictionary() as [NSObject : AnyObject]) { (result, error) in
                    if error != nil{
                        if error == DataManagerError.TokenExpiration{
                            let objectDataManager = DataManager()
                            self.getLoggedInUserAccessToken {
                                objectDataManager.updateMemberAPI(dictBody: param.serializeAsJSONDictionary() as [NSObject : AnyObject]) { (result, error) in
                                    self.userProfileUpdateCallback(result, error: error)
                                }
                            }
                        }else{
                            self.userProfileUpdateCallback(result, error: error)
                        }
                    }else{
                        self.userProfileUpdateCallback(result, error: error)
                    }
                }
                
            }
        })
        
    }
    
    func userProfileUpdateCallback(_ response: AnyObject?, error: DataManagerError?) {
        // before user updated option
        if response == nil {
            NSLog("push token registration failed")
            UserService.sharedUser?.updateUserFromFishbowl(!((UserService.sharedUser?.pushOptIn)!))
            return
        }
        if let result = response!.value(forKey: "successFlag") as? Bool {
            if result {
                NSLog("push token registered")
                UserService.sharedUser?.updateUserFromFishbowl(UserService.sharedUser!.pushOptIn)
                return
            }else{
                NSLog("push token registration failed")
                UserService.sharedUser?.updateUserFromFishbowl(!((UserService.sharedUser?.pushOptIn)!))
                return
            }
        }else{
            NSLog("push token registration failed")
            UserService.sharedUser?.updateUserFromFishbowl(!((UserService.sharedUser?.pushOptIn)!))
            return
        }
    }
    
    //update user favourite store
    func updateUserFavouriteStore(_ user: User) {
        setTokenBeforeFishbowlApiCallForLogin()
        validateFishbowlCredentials { (status) in
            if !status{
                return
            }else{
                if let favStore = user.favoriteStore{
                    let defaults = UserDefaults.standard
                    let userId = defaults.string(forKey: "fishbowl_customer_id")
                    
                    // Covert favStore as integer and then string  to remove leading zero
                    let favStoreIntValue = Int(favStore.storeCode)!
                    let favStoreStringValue = String(describing: favStoreIntValue)
                    
                    let param = FishbowlUpdateFavouriteStore(memberid: userId ?? "", storeCode: favStoreStringValue)
                    let objectDataManager = DataManager()
                    objectDataManager.favouriteStoreApi(dictBody: param.serializeAsJSONDictionary() as [NSObject : AnyObject]) { (response, error) in
                        // Response not handled
                        if error != nil{
                            if error == DataManagerError.TokenExpiration{
                                let objectDataManager = DataManager()
                                objectDataManager.favouriteStoreApi(dictBody: param.serializeAsJSONDictionary() as [NSObject : AnyObject]) { (response, error) in
                                    return
                                }
                            }else{
                                return
                            }
                        }else{
                            return
                        }
                    }
                }
            }
        }
    }
    
    //update user device
    func updateUserDevice(complete: @escaping FishbowlCompleteCallback) {
        setTokenBeforeFishbowlApiCallForLogin()
        validateFishbowlCredentials { (status) in
            if !status{
                return complete()
            }else{
                let defaults = UserDefaults.standard
                let userId = defaults.string(forKey: "fishbowl_customer_id")
                var pushToken = ""
                if defaults.string(forKey: "pushToken") != nil {
                    pushToken = defaults.string(forKey: "pushToken")!
                }
                
                let param = FishbowlUpdateUserDevice(deviceId: self.getDeviceId(), memberid: userId ?? "", pushToken: pushToken)
                
                NSLog("update device param %@",param.serializeAsJSONDictionary())
                let objectDataManager = DataManager()
                objectDataManager.updateDeviceSDKApi(dictBody: (param.serializeAsJSONDictionary() as [NSObject : AnyObject])) { (result, error) in
                    // Response not handled
                    if error != nil{
                        if error == DataManagerError.TokenExpiration{
                            self.getLoggedInUserAccessToken {
                                let objectDataManager = DataManager()
                                objectDataManager.updateDeviceSDKApi(dictBody: (param.serializeAsJSONDictionary() as [NSObject : AnyObject]), completion: { (response, error) in
                                    return complete()
                                })
                            }
                        }else{
                            return complete()
                        }
                    }else{
                        return complete()
                    }
                }
            }
        }
    }
    
    // Convert bool to string
    func convertBoolToString(_ boolValue:Bool)-> String{
        if(boolValue){
            return "true";
        }
        else{
            return "false";
        }
    }
    
    
    func getDeviceId() -> String {
        return DataManager().deviceID()
    }
    
    func getAllStores(){
        setTokenBeforeFishbowlApiCallForLogin()
        validateFishbowlCredentials { (status) in
            if !status{
                return
            }
            else{
                let objectDataManager = DataManager()
                objectDataManager.getAllStoresAPI() { (result, error) in
                    if error != nil{
                        if error == DataManagerError.TokenExpiration{
                            self.getLoggedInUserAccessToken {
                                let objectDataManager = DataManager()
                                objectDataManager.getAllStoresAPI() { (result, error) in
                                    self.getAllStoreCallback(result, error: error)
                                }
                            }
                        } else{
                            self.getAllStoreCallback(result, error: error)
                        }
                    }else{
                        self.getAllStoreCallback(result, error: error)
                    }
                }
            }
        }
    }
    
    func getAllStoreCallback(_ response:AnyObject?, error: DataManagerError?){
        if response != nil{
            let json = JSON(response!)
            let storeList = FishbowlStoreList.init(json: json)
            if storeList.successFlag != nil && storeList.successFlag!{
                if storeList.storeList != nil{
                    fishbowlStoreList = storeList.storeList!
                }
            }
        }
    }
    
    func getFishbowlStoreId(_ storeCode:String) -> String{
        let favouriteStore = fishbowlStoreList.filter({Int($0.storeNumber!) == Int(storeCode)})
        if favouriteStore.count > 0 {
            return "\(favouriteStore[0].storeID!)"
        } else {
            return ""
        }
    }
    
    func getFishbowlStoreDetails(_ storeCode:String) -> FishbowlStore? {
        let favouriteStore = fishbowlStoreList.filter({Int($0.storeNumber!) == Int(storeCode)})
        if favouriteStore.count > 0 {
            return favouriteStore[0]
        } else {
            return nil
        }
    }
    
    //compare olo restaurentsn & get olo store details
    func getOloStoreDetails(_ storeCode:String, oloRestaurent:OloRestaurantList) -> OloRestaurant? {
        let oloStore = oloRestaurent.filter({Int($0.storeCode) == Int(storeCode)})
        if oloStore.count > 0 {
            return oloStore[0]
        } else {
            return nil
        }
    }
    
    
    
    func promoCodeFormat(_ promo:String) -> String{
        let strArray = Array(promo.characters)
        var promoString = ""
        for (index,char) in strArray.enumerated(){
            if (index%4) == 3{
                promoString = promoString+"-"
            }
            promoString = promoString + String(char)
        }
        return String(promoString)
    }
    
    func validateFishbowlCredentials(callback:FishbowlCredentialValidationCallback){
        if UserService.sharedUser == nil{
            return callback(false)
        }
        var flag:Bool = false
        let defaults = UserDefaults.standard
        if (defaults.string(forKey: "fishbowl_login_access_token") == nil) || (defaults.string(forKey: "fishbowl_access_token") == nil) || (defaults.string(forKey: "fishbowl_customer_id") == nil){
            fishbowlLogin {
                if (defaults.string(forKey: "fishbowl_login_access_token") == nil) || (defaults.string(forKey: "fishbowl_access_token") == nil) || (defaults.string(forKey: "fishbowl_customer_id") == nil){
                    flag = false
                }else{
                    flag = true
                }
            }
            return callback(flag)
        }else{
            return callback(true)
        }
    }
    
    
    func receivedFishbowlMobileSettings(_ response: AnyObject?, error: DataManagerError?) {
        var fishbowlMobileSettingList:[FishbowlMobileSetting] = []
        if response == nil{
            return
        }
        if let result = response!.value(forKey: "successFlag") as? Bool {
            if result {
                let fishbowlMobileSettings = FishbowlMobileSettingResponse(json: JSON(response!))
                if let mobileSettingList = fishbowlMobileSettings.mobSettingList{
                    if (mobileSettingList.successFlag != nil && mobileSettingList.successFlag == true){
                        fishbowlMobileSettingList = mobileSettingList.mobileSettings
                    }
                }
            }
        }
        
        if fishbowlMobileSettingList.count > 0{
            forceUpdateBuildVersionSetting = fishbowlMobileSettingList.filter({$0.settingName == forceUpdateBuildVersion})
            forceUpdateBuildCodeSetting =  fishbowlMobileSettingList.filter({$0.settingName == forceUpdateBuildCode})
            forceBuildUpdateSetting =  fishbowlMobileSettingList.filter({$0.settingName == forceBuildUpdate})
            forceBuildUpdateMessageSetting = fishbowlMobileSettingList.filter({$0.settingName == forceBuildUpdateMessage})
            forceUpdateValidation()
        }
    }
    
    func validateBuildVersionNumber() -> Bool{
        
        if forceUpdateBuildVersionSetting.count > 0{
            if let forceUpdateBuildVersionSettingObj = forceUpdateBuildVersionSetting.first{
                if (forceUpdateBuildVersionSettingObj.status != nil  && forceUpdateBuildVersionSettingObj.status == true){
                    
                    let buildVersionNumber = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
                    let buildVersionNumberOnly = buildVersionNumber?.stringByRemovingNonNumericCharacters()
                    let buildVersionNumberInteger = Int(buildVersionNumberOnly!)
                    let forceUpdateBuildVersionSettingObjValue = forceUpdateBuildVersionSettingObj.settingValue?.stringByRemovingNonNumericCharacters()
                    if Int(forceUpdateBuildVersionSettingObjValue!)! > buildVersionNumberInteger! {
                        return true
                    }else{
                        return false
                    }
                }else{
                    return false
                }
            } else{
                return false
            }
        } else{
            return false
        }
    }
    
    
    func validateForceUpdate() -> Bool{
        if forceBuildUpdateSetting.count > 0 {
            if let forceUpdateBuildSetting = forceBuildUpdateSetting.first{
                if (forceUpdateBuildSetting.status != nil && forceUpdateBuildSetting.status == true){
                    if forceUpdateBuildSetting.settingValue == "1" || forceUpdateBuildSetting.settingValue == "0"{
                        return true
                    }else{
                        return false
                    }
                }else{
                    return false
                }
            }else{
                return false
            }
        } else{
            return false
        }
    }
    
    func forceUpdateMessage() -> String{
        if forceBuildUpdateMessageSetting.count > 0{
            if let forceBuildUpdateMessageSettingObj = forceBuildUpdateMessageSetting.first{
                if forceBuildUpdateMessageSettingObj.status != nil &&  forceBuildUpdateMessageSettingObj.status == true{
                    return forceBuildUpdateMessageSettingObj.settingValue ?? ""
                }else{
                    return ""
                }
            }else{
                return ""
            }
        }else{
            return ""
        }
    }
    
    func forceUpdateValidation(){
        if validateBuildVersionNumber(){
            if validateForceUpdate(){
                if forceUpdateMessage() == ""{
                    return
                }else{
                    if forceUpdateScreenValidation(){
                        showForceUpdateScreen()
                    }else{
                        return
                    }
                }
            }  else{
                return
            }
        }
        else{
            return
        }
    }
    
    func forceUpdate(){
        // submitMobileAppEvents!.serializeJSONDictionary() as [String : AnyObject]
        let objectDataManager = DataManager()
        objectDataManager.mobileSettingAPI() { (result, error) in
            self.receivedFishbowlMobileSettings(result, error: error)
        }
    }
    
    func getAppStoreBuildNumber() -> String{
        
        if forceUpdateBuildCodeSetting.count > 0{
            let forceUpdateBuildVersionSettingObj = forceUpdateBuildCodeSetting.first
            if forceUpdateBuildVersionSettingObj != nil{
                return forceUpdateBuildVersionSettingObj!.settingValue ?? ""
            } else{
                return ""
            }
        } else{
            return ""
        }
    }
    
    func getAppStoreVersionNumber() -> String{
        if forceUpdateBuildVersionSetting.count > 0{
            let forceUpdateBuildVersionSettingObj = forceUpdateBuildVersionSetting.first
            if forceUpdateBuildVersionSettingObj != nil{
                return forceUpdateBuildVersionSettingObj!.settingValue ?? ""
            } else{
                return ""
            }
        } else {
            return ""
        }
    }
    
    func showForceUpdateScreen(){
        let window = UIApplication.shared.delegate!.window!!
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        self.forceUpdateViewController = storyboard.instantiateViewController(withIdentifier: "ForceUpdateViewController") as? ForceUpdateViewController
        if forceBuildUpdateSetting.count > 0{
            let forceUpdateValue = forceBuildUpdateSetting.first?.settingValue
            if forceUpdateValue == "1"{
                forceUpdateViewController?.forceUpdate = true
            }else{
                forceUpdateViewController?.forceUpdate = false
            }
        }
        let forceUpdateMessageValue = forceUpdateMessage()
        forceUpdateViewController?.updateMessageString = forceUpdateMessageValue
        
        window.addSubview((self.forceUpdateViewController?.view)!)
    }
    
    func forceUpdatValidateRemindmeLater() -> Bool{
        let forceUpdateDefaults=UserDefaults.standard
        
        if forceUpdateDefaults.object(forKey: "forceUpdateRemaindMeLater") != nil {
            let  status = forceUpdateDefaults.object(forKey: "forceUpdateRemaindMeLater") as? String
            if status != nil && status == "true"{
                return true
            }
            else {
                return false
            }
        }
        else{
            return false
        }
    }
    
    func forceUpdateValidateDimiss() -> Bool{
        let forceUpdateDefaults=UserDefaults.standard
        if forceUpdateDefaults.object(forKey: "forceUpdateDismiss") != nil{
            let  status = forceUpdateDefaults.object(forKey: "forceUpdateDismiss") as? String
            if status != nil && status == "true" {
                if (forceUpdateDefaults.object(forKey: "forceUpdateDismissVersionNumber") != nil){
                    let dimissVersionNumber = forceUpdateDefaults.object(forKey: "forceUpdateDismissVersionNumber") as! String
                    let appStoreVersionNumber = getAppStoreVersionNumber()
                    if (dimissVersionNumber.isEmpty == false && dimissVersionNumber != "" &&  appStoreVersionNumber.isEmpty == false && appStoreVersionNumber != "") {
                        
                        if dimissVersionNumber != appStoreVersionNumber{
                            return true
                        } else{
                            return false
                        }
                    } else{
                        return false
                    }
                } else{
                    return false
                }
            } else{
                return false
            }
        }
        else{
            return false
        }
    }
    
    func forceUpdateScreenValidation() -> Bool{
        let forceUpdateDefaults=UserDefaults.standard
        if forceUpdateDefaults.object(forKey: "forceUpdateDismiss") == nil && forceUpdateDefaults.object(forKey: "forceUpdateRemaindMeLater") == nil{
            forceUpdateDefaults.set("false", forKey: "forceUpdateDismiss")
            forceUpdateDefaults.set("false", forKey: "forceUpdateRemaindMeLater")
            forceUpdateDefaults.set("", forKey: "forceUpdateDismissVersionNumber")
            forceUpdateDefaults.synchronize()
            return true
        }else if forceUpdatValidateRemindmeLater() == true{
            return true
        } else if forceUpdateValidateDimiss() == true{
            return true
        } else if forceBuildUpdateSetting.first?.settingValue == "1"{
            return true
        }
        else{
            return false
        }
    }
    
    // Get Fishbowl Offers
    func getFishbowlOffer(callback: @escaping FishbowlCallback){
        setTokenBeforeFishbowlApiCallForLogin()
        _ = self.getDeviceId()
        let defaults = UserDefaults.standard
        let userId = defaults.string(forKey: "fishbowl_customer_id") ?? ""
        var urldict = FishbowlJSONDictionary()
        urldict["customerid"] = userId
        let objectDataManager = DataManager()
        objectDataManager.getOffersAPI(URLBody: urldict as [String : AnyObject]) { (result, error) in
            if error != nil{
                if error == DataManagerError.TokenExpiration{
                    self.getLoggedInUserAccessToken {
                        let objectDataManager = DataManager()
                        objectDataManager.getOffersAPI(URLBody: urldict as [String : AnyObject]) { (result, error) in
                            self.handlingResponse(response: result, error: error, callback: callback)
                        }
                    }
                }else{
                    self.handlingResponse(response: result, error: error, callback: callback)
                }
            }else{
                self.handlingResponse(response: result, error: error, callback: callback)
            }
        }
    }
    
    func validatePushCustomerId(_ pushCustomerId:String?) -> Bool{
        if pushCustomerId == nil{
            return false
        }else{
            let defaults = UserDefaults.standard
            if let fbCustomerId = defaults.string(forKey: "fishbowl_customer_id"){
                if fbCustomerId == pushCustomerId{
                    return true
                }else{
                    return false
                }
            }else{
                return false
            }
        }
    }
    
    
    func submitMobileAppEvent(_ item_id:String?, item_name:String?, event_name:String){
        //created a local instance for Api classes to prevent callback switch error
        let event = FishbowlEvent.init(item_id: item_id, item_name: item_name!, event_name: event_name)
        var events:[FishbowlEvent] = []
        events.append(event)
        let submitFishbowlEvents = FishbowlEvents.init(mobileAppEvents: events)
        submitMobileAppEvents(mobileAppEvents: submitFishbowlEvents)
    }
    
    func submitMobileAppEvent(_ event_name:String) {
        let event = FishbowlEvent.init(item_id: "", item_name: "", event_name: event_name)
        var events:[FishbowlEvent] = []
        events.append(event)
        let submitFishbowlEvents = FishbowlEvents.init(mobileAppEvents: events)
        submitMobileAppEvents(mobileAppEvents: submitFishbowlEvents)
    }
    
    func submitMobileAppEvents(_ events:[FishbowlEvent]){
        let submitFishbowlEvents = FishbowlEvents.init(mobileAppEvents: events)
        submitMobileAppEvents(mobileAppEvents: submitFishbowlEvents)
    }
    
    // Submit mobile app events
    func submitMobileAppEvents(mobileAppEvents:FishbowlEvents){
        // Check events to submit or not
        // Load Jamba configuration
        let config = Configuration.sharedConfiguration
        if !config.FBEvents{
            return
        }
        if UserService.sharedUser == nil {
            guestEvents(mobileAppEvents: mobileAppEvents, callback: { (response, error) in
            })
        }else{
            setTokenBeforeFishbowlApiCallForLogin()
            let objectDataManager = DataManager()
            objectDataManager.mobileAppEventsAPI(dictBody: mobileAppEvents.serializeJSONDictionary() as [String : AnyObject], completion: { (result, error) in
                if error != nil{
                    if error == DataManagerError.TokenExpiration{
                        let objectDataManager = DataManager()
                        self.getLoggedInUserAccessToken{
                            objectDataManager.mobileAppEventsAPI(dictBody: mobileAppEvents.serializeJSONDictionary() as [String : AnyObject], completion: { (result, error) in
                            })
                        }
                    }
                }
            })
        }
    }
    
    func guestEvents(mobileAppEvents:FishbowlEvents!, callback: @escaping FishbowlCallback){
        setTokenBeforeFishbowlApiCallForGuest()
        let submitMobileAppEvents = mobileAppEvents
        let objectDataManager = DataManager()
        objectDataManager.GuestUserMobileAppEventsAPI(dictBody: (submitMobileAppEvents!.serializeJSONDictionary() as [String : AnyObject] ) , completion: { (result, error) in
            if error != nil{
                if error == DataManagerError.TokenExpiration{
                    self.guestAccessToken{
                        let objectDataManager = DataManager()
                        objectDataManager.GuestUserMobileAppEventsAPI(dictBody: submitMobileAppEvents!.serializeJSONDictionary() as [String : AnyObject] , completion: { (result, error) in
                        })
                    }
                }
            }
        })
    }
    
    func guestAccessToken(complete: @escaping FishbowlCompleteCallback){
        _ = self.getDeviceId()
        let objectDataManager = DataManager()
        objectDataManager.getTokenApi(dictBody: guestRequest as [NSObject : AnyObject]) { (response, error) in
            let defaults = UserDefaults.standard
            if response != nil {
                if let result = response!.value(forKey: "successFlag") as? Bool {
                    if result {
                        NSLog("Access token received successfully")
                        //store the access in local storage & then get the member details
                        defaults.set(response!.value(forKey: "message"), forKey: "fishbowl_guest_access_token")
                        defaults.set(response!.value(forKey: "message"), forKey: "fishbowl_access_token")
                        defaults.synchronize()
                        return complete()
                    }else{
                        return complete()
                    }
                }else{
                    return complete()
                }
            } else{
                return complete()
            }
        }
    }
    func handlingResponse(response:AnyObject?, error: DataManagerError?, callback: FishbowlCallback){
        var callbackError: NSError?
        var callbackResponse: AnyObject?
        // When error not equal and response is nil
        if error == nil && response == nil{
            callbackError = NSError.init(description: Constants.genericErrorMessage)
        }else if error == nil && response != nil{
            if response != nil{
                if let result = response!.value(forKey: "successFlag") as? Bool{
                    if result{
                        callbackResponse = response
                    }else{
                        callbackError = NSError.init(description:Constants.genericErrorMessage)
                    }
                }else{
                    callbackError = NSError.init(description:Constants.genericErrorMessage)
                }
            }else{
                callbackError = NSError.init(description:Constants.genericErrorMessage)
            }
            
        }else if error != nil && response == nil{
            callbackError = NSError.init(description: Constants.genericErrorMessage)
        }else if error != nil && response != nil{
            if response != nil{
                if let result = response!.value(forKey: "successFlag") as? Bool{
                    if result{
                        callbackResponse = response
                    }else{
                        callbackError = NSError.init(description:Constants.genericErrorMessage)
                    }
                }else{
                    callbackError = NSError.init(description:Constants.genericErrorMessage)
                }
            }else{
                callbackError = NSError.init(description:Constants.genericErrorMessage)
            }
        }else{
            callbackError = NSError.init(description: Constants.genericErrorMessage)
        }
        
        
        return callback(callbackResponse, callbackError)
    }
    
    func getLoggedInUserAccessToken(complete:@escaping FishbowlCompleteCallback){
        if let user = UserService.sharedUser {
            //get spendgo info & store the values in NSUserDefaults
            let defaults = UserDefaults.standard
            defaults.set("jambamobile",forKey: "externalkey")
            defaults.set(user.spendgoAuthToken, forKey: "external_access_token")
            defaults.set(user.id, forKey: "external_customer_id")
            let spendGOSignature:String? = UserService.getSpendGoSignature()
            defaults.set(spendGOSignature ?? "", forKey: "signature")
            defaults.synchronize()
            
            //login Parameter
            let param = FishbowlLogin(username: user.emailAddress ?? "", deviceId: self.getDeviceId())
            let objectDataManager = DataManager()
            objectDataManager.loginSDKApi(dictBody: (param.serializeAsJSONDictionary() as [NSObject : AnyObject] ), completion: { (response,error) in
                if response != nil{
                    if let result = response!.value(forKey: "successFlag") as? Bool {
                        if result {
                            NSLog("fishbowl login successfull")
                            //store the access in local storage & then get the member details
                            defaults.set(response!.value(forKey: "accessToken"), forKey: "fishbowl_login_access_token")
                            defaults.set(response!.value(forKey: "accessToken"), forKey: "fishbowl_access_token")
                            defaults.synchronize()
                            let userId = defaults.string(forKey: "fishbowl_customer_id")
                            if userId == nil{
                                self.fishbowlUserDetails {
                                    self.setTokenBeforeFishbowlApiCallForLogin()
                                }
                            }else{
                                self.setTokenBeforeFishbowlApiCallForLogin()
                            }
                        }
                    }
                }
                return complete()
            })
        }else{
            return complete()
        }
    }
    
    
    func fishbowlLogin(complete:@escaping FishbowlCompleteCallback){
        if let user = UserService.sharedUser {
            //get spendgo info & store the values in NSUserDefaults
            let defaults = UserDefaults.standard
            defaults.set("jambamobile",forKey: "externalkey")
            defaults.set(user.spendgoAuthToken, forKey: "external_access_token")
            defaults.set(user.id, forKey: "external_customer_id")
            let spendGOSignature:String? = UserService.getSpendGoSignature()
            defaults.set(spendGOSignature ?? "", forKey: "signature")
            defaults.synchronize()
            
            //login Parameter
            let objectDataManager = DataManager()
            let param = FishbowlLogin(username: user.emailAddress ?? "", deviceId: self.getDeviceId())
            
            objectDataManager.loginSDKApi(dictBody: (param.serializeAsJSONDictionary() as [NSObject : AnyObject] ), completion: { (response,error) in
                self.fishbowlLoginCallBack(response, error: error, complete: {
                    if UserDefaults.standard.string(forKey: "fishbowl_customer_id") != nil{
                        return complete()
                    }
                    self.fishbowlUserDetails {
                        self.fishbowlUserDetailsCallback(response, error: error, complete: {
                            return complete()
                        })
                    }
                })
            })
        }
    }
    
    func fishbowlLoginCallBack(_ response: AnyObject?, error:DataManagerError?,complete:FishbowlCompleteCallback) {
        if response == nil || error != nil{
            return complete()
        }
        else if let result = response!.value(forKey: "successFlag") as? Bool {
            if result {
                NSLog("fishbowl login successfull")
                //store the access in local storage & then get the member details
                UserDefaults.standard.set(response!.value(forKey: "accessToken"), forKey: "fishbowl_login_access_token")
                UserDefaults.standard.set(response!.value(forKey: "accessToken"), forKey: "fishbowl_access_token")
                UserDefaults.standard.synchronize()
            }
        }
        return complete()
    }
    
    
    
    func fishbowlUserDetails(complete:@escaping FishbowlCompleteCallback) {
        setTokenBeforeFishbowlApiCallForLogin()
        //if the user is not signed in user then do nothing
        if UserService.sharedUser != nil  {
            let objectDataManager = DataManager()
            objectDataManager.getMemberSDKApi() { (response, error) in
                if error == DataManagerError.TokenExpiration{
                    self.getLoggedInUserAccessToken {
                        let objectDataManager = DataManager()
                        objectDataManager.getMemberSDKApi(){ (response, error) in
                            self.fishbowlUserDetailsCallback(response, error: error, complete: {
                                return complete()
                            })
                        }
                    }
                }else{
                    self.fishbowlUserDetailsCallback(response, error: error, complete: {
                        return complete()
                    })
                }
            }
        }else{
            return complete()
        }
    }
    
    func fishbowlUserDetailsCallback(_ response: AnyObject?, error: DataManagerError?,complete:FishbowlCompleteCallback) {
        let defaults = UserDefaults.standard
        if response == nil || error != nil {
            return complete()
        }
        else if let result = response!.value(forKey: "successFlag") as? Bool {
            if result {
                if let customerId = response!.value(forKey: "customerID") {
                    //store customer id in local storage & then call the update device in background
                    defaults.set("\(customerId)", forKey: "fishbowl_customer_id")
                    defaults.synchronize()
                    //self.updateUserDevice()
                    
                    // get fishbowl offer push notification status
                    if let fbPushnotificationStatus = response!.value(forKey: "pushOpted") as? Bool{
                        if UserService.sharedUser != nil{
                            UserService.sharedUser?.updateUserFromFishbowl(fbPushnotificationStatus)
                        }
                    }
                }
            }
        }
        return complete()
    }
    
    
    func fishbowlLogout(complete:@escaping FishbowlCompleteCallback){
        logoutEvent {
            self.deviceUpdateAfterLogout {
                self.fishbowlLogoutAPI{
                    self.deleteFishbowlDetailsForLogin()
                    self.guestAccessToken {
                        return complete()
                    }
                }
            }
        }
    }
    
    func logoutEvent(complete:@escaping FishbowlCompleteCallback){
        let event = FishbowlEvent.init(item_id: "", item_name: "", event_name: "LOGOUT")
        var events:[FishbowlEvent] = []
        events.append(event)
        let submitFishbowlEvents = FishbowlEvents.init(mobileAppEvents: events)
        let objectDataManager = DataManager()
        objectDataManager.mobileAppEventsAPI(dictBody: submitFishbowlEvents.serializeJSONDictionary() as [String : AnyObject], completion: { (result, error) in
            if error != nil{
                if error == DataManagerError.TokenExpiration{
                    let objectDataManager = DataManager()
                    self.getLoggedInUserAccessToken{
                        objectDataManager.mobileAppEventsAPI(dictBody: submitFishbowlEvents.serializeJSONDictionary() as [String : AnyObject], completion: { (result, error) in
                            return complete()
                        })
                    }
                }else{
                    return complete()
                }
            } else{
                return complete()
            }
        })
    }
    
    func deviceUpdateAfterLogout(complete:@escaping FishbowlCompleteCallback){
        let defaults = UserDefaults.standard
        // Remove push token
        if defaults.string(forKey: "pushToken") != nil {
            defaults.removeObject(forKey: "pushToken")
        }
        defaults.synchronize()
        let userId = defaults.string(forKey: "fishbowl_customer_id")
        let param = FishbowlUpdateUserDevice(deviceId: self.getDeviceId(), memberid: userId ?? "", pushToken: "")
        NSLog("update device param %@",param.serializeAsJSONDictionary())
        let objectDataManager = DataManager()
        objectDataManager.updateDeviceSDKApi(dictBody: (param.serializeAsJSONDictionary() as [NSObject : AnyObject])) { (result, error) in
             // Response not handled
            return complete()
        }

    }
    func fishbowlLogoutAPI(complete:@escaping FishbowlCompleteCallback){
        let param  = [NSObject: AnyObject]()
        let objectDataManager = DataManager()
        objectDataManager.logoutAPI(dictBody:param) { (response, error) in
            if error != nil{
                if error == DataManagerError.TokenExpiration{
                    self.getLoggedInUserAccessToken {
                        let objectDataManager = DataManager()
                        objectDataManager.logoutAPI(dictBody:param, completion: { (response, error) in
                            return complete()
                        })
                    }
                }else{
                    return complete()
                }
            }else{
                return complete()
            }
        }
    }
    
    func setTokenBeforeFishbowlApiCallForGuest(){
        let fbGuestToken = UserDefaults.standard.string(forKey: "fishbowl_guest_access_token")
        let fbAccessToken = UserDefaults.standard.string(forKey: "fishbowl_access_token")
        if fbGuestToken == nil && fbAccessToken != nil{
            UserDefaults.standard.set(fbAccessToken, forKey: "fishbowl_access_token")
        }else{
            UserDefaults.standard.set(fbGuestToken, forKey: "fishbowl_access_token")
        }
        UserDefaults.standard.synchronize()
        
    }
    func setTokenBeforeFishbowlApiCallForLogin(){
        let fbLoginToken = UserDefaults.standard.string(forKey: "fishbowl_login_access_token")
        let fbAccessToken = UserDefaults.standard.string(forKey: "fishbowl_access_token")
        if fbLoginToken == nil && fbAccessToken != nil{
            UserDefaults.standard.set(fbAccessToken, forKey: "fishbowl_access_token")
        }else{
            UserDefaults.standard.set(fbLoginToken, forKey: "fishbowl_access_token")
        }
        UserDefaults.standard.synchronize()
    }
    func deleteFishbowlDetailsForLogin(){
        // Delete customer id and fishbowl access token precaution for failure
        let defaults = UserDefaults.standard
        defaults.removeObject(forKey: "fishbowl_login_access_token")
        defaults.removeObject(forKey: "fishbowl_access_token")
        defaults.removeObject(forKey: "fishbowl_customer_id")
        defaults.removeObject(forKey: "pushToken")
        defaults.synchronize()
    }
}
