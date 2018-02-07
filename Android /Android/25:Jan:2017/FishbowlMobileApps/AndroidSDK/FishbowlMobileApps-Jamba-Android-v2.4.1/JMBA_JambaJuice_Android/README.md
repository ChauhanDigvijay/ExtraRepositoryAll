# JMBA_JambaJuice_Android

![Build Status](https://magnum.travis-ci.com/hathway/JMBA_JambaJuice_Android.svg?token=jpeuNsUzrDsqyzi1XgkF&branch=release/2.1.2)

## Setup Instructions

1. Fork the GitHub repo to your own account.

2. Clone the forked repo
```git clone git@github.com:[youruser]/JMBA_JambaJuice_Android.git```

3. Import project in Android studio.

4. Create a feature branch for the JIRA ticket you are working on
`git checkout -b feature/JMBAAN-1-my-branch-name`

5. Commit your changes.

6. Push to your remote forked repo.

7. Submit a pull request to `hathway:develop`

8. Clone the repo
```git clone git@github.com:[youruser]/Olo-Android-SDK.git ```

9. Clone the repo 
```git clone git@github.com:[youruser]/SpendGo-Android-SDK.git```

10. Place both repositories adjacent to JMBA_JambaJuice_Android repository.

11. After making changes to Olo or SpendGo SDK upload archive to gradle repository and update dependency in JMBA_JambaJuice_Android build.gradle.

12. Now sync project with gradle files.


## Dependencies

- Facebook Android SDK
- Google Play Services 7.3.0
- OloAndroidSDK
- SpendGoAndroidSDK
- Crashlytics
    1. Download crashlytics android studio plugin.
    2. Go to Preference.
    3. Select Plugins.
    4. Click "Install plugin from disk…" and choose the downloaded zip file.
    5. Click "OK" and restart Android Studio to load the plugin.
