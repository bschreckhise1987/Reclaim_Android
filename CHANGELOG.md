# Changelog

All notable changes to the **Reclaim Android** application will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-09-26

### Added
- **Complete Android Application Port**: Ported the Reclaim application from Swift/SwiftUI to native Kotlin + Jetpack Compose with full Supabase integration.
- **Brand Assets**: Added the official Reclaim tree logo as adaptive vector launcher resources (`@mipmap/ic_launcher` and `@mipmap/ic_launcher_round`) across all Android density buckets.
- **Home Dashboard**:
  - Sober time counter calculating years, months, and total days.
  - Current & longest streak tracking with unlockable milestone badges (`30d`, `60d`, `90d`, `180d`, `365d`).
  - Recommended coping strategies engine blending time-of-day relevance and historical effectiveness scores.
  - Quick action buttons for Daily Check-Ins, Daily Logs, and Strategy Management.
  - 7-day weekly check-in progress indicator.
- **Daily Check-In & Daily Logs**:
  - Modal bottom sheet entry flows with emoji-based mood selection.
  - Interactive craving intensity slider (`0–10`).
  - Trigger entry and reflection notes fields.
  - Automated same-day duplicate check-in prevention.
- **History Timeline**:
  - Paginated recovery timeline with newest entries first
  - Expandable history cards detailing mood, craving levels, triggers, and reflection notes.
  - Pull-to-refresh support.
- **Insights & Recovery Analytics**:
  - Custom Jetpack Compose Canvas chart components (`MoodTrendChart`, `CravingTrendChart`, `TriggerBarChart`, `DailyUsageBarChart`, `MoodDonutChart`).
  - 7-day and 30-day view toggles for mood and craving trends.
  - Most effective strategies (30 days) and triggers with rising frequency reports.
- **Coping Strategies Engine**:
  - Categorized strategy library (*Breathing*, *Distraction*, *Mindfulness*, *Physical*, *Social*, *Creative*, *Emotional*, *Spiritual*).
  - Strategy detail view, add strategy form, and interactive Coping Guide.
  - Logging strategy usage and 5-star effectiveness ratings.
- **User Profile**:
  - Hero profile avatar with Supabase Storage upload via Android System Photo Picker.
  - Full name, bio, and sober start date editing with native date picker.
  - Dynamic recalculation of streak milestones upon sober start date updates.
- **Settings & Account Management**:
  - Supabase Auth workflows: Sign in, Sign up, Sign out, Change Email, Change Password, and Account Deletion.
  - Appearance settings (Light/Dark/System theme, High Contrast, Reduce Motion).
  - Backup & Sync status indicator and Support/Feedback mail intents.

### Fixed
- **Network Error Differentiation**: Fixed misleading `"Invalid email or password"` error during connectivity drops (e.g., Airplane mode). Added dedicated network timeout and connection error messaging.
- **Type Reference Resolutions**:
  - Resolved `usageCount` unresolved reference in `InsightsScreen.kt` by mapping to `TopStrategyItem.uses`.
  - Resolved `currentState` unresolved reference in `ProfileEditScreen.kt`.
- **Category Sorting & Grouping**: Corrected category order and normalized coping strategy names.

### Security
- Added secure re-authentication checks before email or password modifications.
- Integrated Supabase Row-Level Security (RLS) policies for user data isolation.
