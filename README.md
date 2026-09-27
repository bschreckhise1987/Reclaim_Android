# Reclaim Android

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Backend](https://img.shields.io/badge/Supabase-Auth%20%7C%20DB%20%7C%20Storage-3FCF8E?style=flat&logo=supabase&logoColor=white)](https://supabase.com/)
[![MinSDK](https://img.shields.io/badge/Min%20SDK-26%2B-green?style=flat&logo=android)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Reclaim** is a modern, privacy-focused recovery and wellness companion built natively for Android. Designed to empower individuals on their sobriety journey, Reclaim provides intuitive tools to log daily experiences, track milestones, gain deep emotional insights, and manage personalized coping strategies.

This codebase represents the production-ready Android application, crafted with **Kotlin**, **Jetpack Compose (Material 3)**, and **Supabase**, offering feature-and-design parity with the iOS counterpart.

---

## 📱 Executive Overview & Core Features

### 🏡 Home & Sobriety Dashboard
* **Sober Time Counter**: Dynamic calculation of years, months, and total days since the user's sober start date.
* **Streak & Milestones**: Live streak tracking with unlocked milestone badges (`30d`, `60d`, `90d`, `180d`, `365d`).
* **Smart Recommendations**: Smart engine recommending up to 5 coping strategies tailored to current time-of-day and effectiveness history.
* **Quick Actions & Weekly Progress**: 1-tap access to Daily Check-Ins, Daily Logs, and Strategy guides with a 7-day progress indicator.

### ✍️ Daily Check-Ins & Logging
* **Emoji Mood Picker**: Contextual mood selection mapping to numerical emotional health scores
* **Craving Intensity**: Precise `0–10` slider controls for tracking urges and triggers.
* **Reflections & Triggers**: Structured input fields for identifying root causes and personal reflections.
* **Duplicate Prevention**: Automated duplicate check-in detection per calendar day.

### 📊 Recovery Analytics & Insights
* **Interactive Canvas Charts**: Custom Jetpack Compose canvas-rendered line, bar, and donut charts.
  * **Mood & Craving Trends**: 7-day and 30-day historical trend graphs.
  * **Trigger Frequency**: Bar distribution of top recovery triggers.
  * **Mood Distribution**: Donut chart visualizing emotional balance.
  * **Strategy Effectiveness**: 30-day top-rated strategies and rising trigger frequency alerts.

### 💡 Coping Strategy Manager
* **Categorized Library**: Categorized into *Breathing*, *Distraction*, *Mindfulness*, *Physical*, *Social*, *Creative*, *Emotional*, and *Spiritual*.
* **Usage & Effectiveness Logging**: Track when strategies are used and rate their real-world impact.
* **Interactive Guide**: Integrated educational coping guide for acute craving management.

### 👤 Profile & Customization
* **Avatar & Storage**: Profile photo upload via Android System Photo Picker backed by Supabase Storage.
* **Sobriety Management**: Native date picker to update sober start date and auto-recalculate streak milestones.
* **Bio & Customization**: Personalized bio and display name fields.

### ⚙️ Account, Privacy & Security
* **Authentication**: Supabase Auth integration supporting Sign In, Account Creation, Sign Out, and Account Deletion.
* **Robust Error Handling**: Differentiates invalid credentials from offline network failures (e.g., Airplane mode) with clear, actionable user feedback.
* **Account Controls**: Re-authentication workflows for changing email or password securely.
* **Customization Preferences**: Appearance settings (Light/Dark/System theme, High Contrast, Reduce Motion) and notification reminders.

---

## 🛠 Tech Stack & Architecture

Reclaim follows modern Android development best practices, leveraging a reactive **MVVM (Model-View-ViewModel)** architecture with a clean Repository pattern.

| Layer | Technology / Library |
| :--- | :--- |
| **Language** | [Kotlin 2.2.10](https://kotlinlang.org/) |
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 Design |
| **Navigation** | [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation) |
| **State Management** | StateFlow, `mutableStateOf`, Lifecycle ViewModel Compose |
| **Backend & Auth** | [Supabase Kotlin SDK 3.1.1](https://github.com/supabase-community/supabase-kt) (Auth, Postgrest, Storage) |
| **Networking** | [Ktor Client 3.1.1](https://ktor.io/) (OkHttp engine) |
| **Serialization** | `kotlinx.serialization` (JSON) |
| **Image Loading** | [Coil Compose 2.7.0](https://coil-kt.github.io/coil/) |

```
com.android.reclaim/
├── config/             # Supabase Client & API Credentials Configuration
├── data/
│   ├── model/          # Data transfer objects & Domain entities (@Serializable)
│   └── repository/     # Repositories abstracting Supabase Postgrest & Storage calls
├── util/               # Date parsers, SoberTime calculator, Preferences, Mood options
└── ui/
    ├── auth/           # Login & Registration Screens & AuthViewModel
    ├── components/     # Shared Compose UI components & Canvas Charts
    ├── checkin/        # Daily Check-In bottom sheet & ViewModel
    ├── dailylog/       # Daily Log entry sheet & ViewModel
    ├── history/        # Timeline Event History & ViewModel
    ├── home/           # Main Dashboard & HomeViewModel
    ├── insights/       # Analytics charts & InsightsViewModel
    ├── profile/        # Profile, Profile Editing & ProfileViewModel
    ├── strategy/       # Strategy List, Details, Add & Guide screens
    └── settings/       # Settings & Sub-screens (Account, Email, Password, Appearance, Support)
```

---

## 🚀 Getting Started

### Prerequisites
* **Android Studio**: Studio Ladybug (2024.2.1+) or newer.
* **JDK**: Java Development Kit 17+.
* **Android SDK**: API Level 35+ (Compile SDK 37, Min SDK 26).

### Building & Running
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-org/reclaim-android.git
   cd reclaim-android
   ```
2. **Open in Android Studio**: Open the root directory in Android Studio.
3. **Gradle Sync**: Let Gradle synchronize dependencies (`libs.versions.toml`).
4. **Run Application**: Connect an Android device or emulator (Android 8.0 / API 26+) and click **Run 'app'**.

---

## 🧪 Development & Test Account

For testing and verification during development, use the pre-configured credentials below:

> [!IMPORTANT]
> **Test Login Credentials**
> * **Email**: `test@reclaim.com`
> * **Password**: `12345678`

---

## 📋 Quality Assurance & Verification Checklist

When performing release verification, run through the following test suite:

- [x] **Authentication**: Validate login with valid credentials, incorrect password error, and Airplane Mode network failure.
- [x] **Check-In**: Submit a daily check-in and verify duplicate check-in prevention on the same calendar day.
- [x] **Daily Log**: Add a log with custom trigger, craving slider value, and reflection notes.
- [x] **History**: Refresh timeline and expand history cards to verify detail fields.
- [x] **Insights**: Verify line charts, bar graphs, and donut charts load data from Supabase RPC functions.
- [x] **Strategies**: Create a coping strategy, filter by category, log usage, and rate effectiveness.
- [x] **Profile**: Edit display name, bio, sober start date, and upload a profile photo via system photo picker.
- [x] **Settings**: Test theme toggles, account management, email/password updates, and sign out.

---

## 📝 Changelog

Detailed release history and feature updates are documented in [CHANGELOG.md](CHANGELOG.md).

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
