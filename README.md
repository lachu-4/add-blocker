# ZeroAds - System-Wide Android Ad Blocker

ZeroAds is a production-ready Android application that blocks advertisements across all installed applications using a local VPN-based ad filtering system.

## 🚀 Key Features
- **Universal Ad Blocking**: Blocks ads in games, browsers, social media, and streaming apps without root.
- **VPN Filtering Engine**: Intercepts network traffic using Android's `VpnService` API.
- **Smart DNS**: Custom DNS filtering with support for Cloudflare, Google, and manual inputs.
- **Real-Time Stats**: Dashboard showing blocked ads, data saved, and tracking requests.
- **App-Level Control**: Whitelist or blacklist specific applications.
- **AI Tracker Detection**: Heuristic detection for suspicious domain patterns.
- **Malware Protection**: Blocks cryptominers, spyware, and phishing domains.

## 🧠 Architecture
- **Language**: Kotlin
- **Pattern**: MVVM + Clean Architecture
- **UI**: Jetpack Compose
- **Database**: Room (for ad domain storage)
- **DI**: Hilt
- **Async**: Coroutines + Flow
- **Background**: WorkManager + Foreground Service

## 📂 Project Structure
- `app/src/main/kotlin/com/zeroads/vpn`: VPN Service and Packet Filtering logic.
- `app/src/main/kotlin/com/zeroads/ui`: Jetpack Compose screens and components.
- `app/src/main/kotlin/com/zeroads/data`: Room database, Repositories, and Ad list providers.
- `app/src/main/kotlin/com/zeroads/di`: Hilt modules for dependency injection.
- `app/src/main/kotlin/com/zeroads/worker`: WorkManager tasks for ad list updates.

## 🛠️ Setup Instructions
1. Open the `/android` directory in **Android Studio**.
2. Sync Project with Gradle Files.
3. Build and Run on an Android device (API 24+).
4. Grant VPN permissions when prompted.

## 🔐 Security & Privacy
- **Local Processing**: All traffic is filtered locally on the device.
- **No Data Logging**: ZeroAds does not log personal browsing history or send data to external servers.
- **Encrypted DB**: Ad lists and settings are stored securely.

---
© 2026 ZeroAds Security Labs
