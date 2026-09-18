# Reclaim Android

Reclaim is a recovery and wellness app designed to help users track
sobriety progress, complete daily check-ins, record daily experiences,
review history, view insights, and access coping strategies.

The Android version already had the core application functionality before
the current iOS-to-Android visual and functionality parity work began.

The current development phase focuses on making the Android version match
the existing iOS version as closely as possible in appearance, behavior,
navigation, spacing, states, and overall user experience.

---

# Test Login

Use this test account to access the app:

- **EMAIL:** test@reclaim.com
- **PASSWORD:** 12345678

These credentials are for development and testing purposes only.

---

# Technology

The Android app uses:

- Kotlin
- Jetpack Compose
- Material 3
- Android Studio
- Supabase Authentication
- Supabase Database
- Android ViewModels
- Repository-based data access

---

# Core Functionality Already Implemented

Before beginning the visual parity work, the Android app already had
basic functionality across the main areas of the application.

## Authentication

- User login
- User authentication through Supabase
- Authenticated user session handling
- Account-related navigation
- Logout functionality

## Home

- Home dashboard
- Personalized welcome message
- Sober time tracking
- Streak tracking
- Recommended coping strategies
- Quick actions
- Daily check-in access
- Daily log access
- Coping strategy access
- Weekly check-in information

## Daily Check-In

- Mood selection
- Craving level tracking
- Optional notes
- Saving check-ins to the database
- Prevention of duplicate check-ins on the same day
- Loading and error states
- Weekly check-in tracking

## Daily Logs

- Opening the daily log entry flow
- Entering daily log information
- Saving daily log information
- Accessing daily log functionality from the Home Screen

## History

- Loading saved check-ins and daily logs
- Displaying historical entries
- Viewing previous recovery activity
- Refreshing history data

## Insights

- Loading insights information
- Displaying recovery-related statistics
- Displaying check-in and progress information
- Accessing the Insights section from the main navigation

## Coping Strategies

- Viewing coping strategies
- Opening the strategies section
- Viewing strategy information
- Accessing strategy-related screens
- Adding and managing strategy information

## Profile

- Viewing user profile information
- Accessing profile-related screens
- Editing user profile information

## Settings

- Opening the settings section
- Viewing account settings
- Viewing notification settings
- Viewing privacy settings
- Viewing backup and sync settings
- Viewing support settings
- Viewing appearance settings

---

# iOS-to-Android Parity Work

The following screens and areas have been updated during the current
iOS-to-Android parity effort.

The goal is not to recreate basic functionality. The Android application
already had its core functionality before this effort began.

The current goal is to bring the Android implementation as close as
possible to the existing iOS implementation.

---

# Completed Parity Work

## 1. Home Screen

### Completed

- iOS-style vertical spacing
- Updated top padding
- Welcome message layout
- Recommended strategies card
- Quick action layout
- Sober Time card
- Streak card
- Milestone icons
- iOS-inspired milestone colors
- Daily Check-In button
- Daily Log button
- Coping Strategy button
- Weekly check-in information
- Improved overall screen organization

---

## 2. Daily Check-In Screen

### Completed

- Emoji-based mood selection
- Selected mood state
- Craving level slider from 0 to 10
- Optional notes input
- Craving intensity display
- Submit check-in button
- Submit validation
- Loading state
- Duplicate check-in prevention
- Weekly check-in refresh
- iOS-inspired spacing
- iOS-inspired card styling

---

## 3. History Screen

### Completed

- History entries displayed in a list
- Newest entries shown first
- Expandable history cards
- Entry summaries
- Additional entry details
- Empty state
- Error state
- Loading state
- Refresh action
- iOS-inspired card styling
- iOS-inspired spacing

---

## 4. Insights Screen

### Completed

- Updated Insights screen for closer iOS parity
- Recovery-related statistics
- Check-in information
- Progress information
- Chart presentation
- Craving trend chart
- Daily usage chart
- Mood donut chart
- Mood trend chart
- Trigger chart
- Improved screen organization
- iOS-inspired spacing and presentation

### Build Status

The Insights changes were corrected after resolving build errors involving
`count` and `usageCount`.

The completed Insights screen was confirmed to build and run successfully.

---

## 5. Coping Strategies List

### Completed

- Updated Coping Strategies list for closer iOS parity
- Strategy categories
- Category organization
- Strategy cards
- Strategy type indicators
- Strategy ratings
- Navigation to strategy details
- Empty state
- Add Strategy action
- Coping Guide access
- Improved category ordering
- Explicit category handling
- Improved empty-state placement
- Improved overall organization
- iOS-inspired spacing and presentation

### Category Organization

The strategy categories were organized in the following order:

1. Breathing
2. Distraction
3. Mindfulness
4. Physical
5. Emotional
6. Social

### Build Status

The Coping Strategies list changes were confirmed to build and run
successfully.

---

## 6. Add Daily Log

### Completed

The Add Daily Log screen was updated to more closely match the iOS
implementation.

Changes included:

- Updated Daily Log presentation
- iOS-style mood selection
- Emoji-based mood selection
- Mood selection state
- Mood category presentation
- Updated trigger input
- Updated trigger wording
- Optional trigger entry
- Craving intensity section
- 0-to-10 craving slider
- Craving intensity display
- Reflection Notes section
- Larger reflection notes input
- Updated Save Daily Log button
- Loading state while saving
- Saved confirmation behavior
- Error message presentation
- iOS-inspired spacing
- iOS-inspired card sections
- Improved overall screen organization

### ViewModels Reviewed

The following Daily Log components were reviewed during this work:

- `DailyLogEntrySheet`
- `DailyLogEntryViewModel`
- `DailyLogViewModel`

### Build Status

The Add Daily Log changes were completed successfully.

---

# Login / Authentication Error Fix

A login error-handling issue was identified during testing.

When the device had no internet connection or was in Airplane Mode,
the login screen could display:

> Invalid email or password.

This message was misleading because the problem could be network
connectivity rather than incorrect credentials.

## Problem

The authentication error handling was treating different types of
failures as the same error.

This created a particularly confusing situation on the login screen
because two common problems could produce the same message:

- Incorrect email or password
- No internet connection / Airplane Mode

## Fix

The authentication error handling was updated to distinguish network
and timeout failures from authentication failures.

### Invalid Credentials

The user receives:

> Invalid email or password.

### Network Connectivity

The user receives:

> Unable to connect. Check your internet connection or turn off Airplane Mode and try again.

### Missing Login Information

The user receives:

> Please enter email and password.

### Timeout / Connection Failure

Network request timeout and connection failures are also reported using
the connectivity message rather than incorrectly reporting invalid
credentials.

## Additional Error-Handling Improvement

Coroutine cancellation is preserved rather than being incorrectly
converted into a login failure.

This prevents cancellation from being presented to the user as an
authentication problem.

---

# Current Screen Status

The current iOS-to-Android parity work has progressed through the
following screens:

| Screen | Status |
|---|---|
| Home | Completed |
| Daily Check-In | Completed |
| History | Completed |
| Insights | Completed |
| Coping Strategies List | Completed |
| Add Daily Log | Completed |

### Additional Completed Fix

- Login network/connectivity error handling

---

# Screens Still Requiring Parity Work

The following areas still require additional iOS-to-Android parity work:

- Strategy Detail Screen
- Add Strategy Screen
- Coping Strategy Guide
- Profile Screen
- Settings Screen
- Appearance Settings
- Notifications Settings
- Backup & Sync
- Account Settings
- Privacy Settings
- Support Settings
- Final Navigation Review
- Final iOS-to-Android Visual Comparison
- Full Android Testing

Some of these screens already contain functional implementations.
Additional parity work is required to compare their Android presentation
and behavior against the iOS version.

---

# Known Bugs / Issues

## Sober Time Dark Theme

The Sober Time card has been identified as displaying an incorrect
value or presentation when using the dark theme.

This remains an item for investigation and testing.

---

# Development Process

The Android app is being updated **one screen at a time** using the iOS
version as the design and behavior reference.

For each screen:

1. Compare the Android screen with the iOS screen.
2. Identify visual and functional differences.
3. Update the Android code.
4. Build the project in Android Studio.
5. Run and test the screen.
6. Fix build errors and functionality issues.
7. Confirm the change works.
8. Move to the next screen.

This process helps prevent multiple unfinished screens from being changed
at the same time and makes it easier to identify problems introduced by
each parity update.

---

# Changelog

## September 18, 2026

### Add Daily Log

- Completed the Add Daily Log iOS-to-Android parity work.
- Reviewed the existing `DailyLogEntrySheet`.
- Reviewed the existing `DailyLogEntryViewModel`.
- Reviewed the existing `DailyLogViewModel`.
- Compared the Android implementation against the iOS Daily Log
  implementation.
- Updated mood selection to use an iOS-style mood selection layout.
- Removed the previous mood dropdown approach from the Daily Log entry
  presentation.
- Updated Daily Log section layout and spacing.
- Updated trigger input presentation.
- Updated craving intensity controls.
- Added 0-to-10 slider presentation.
- Updated craving intensity display.
- Updated reflection notes input.
- Updated Save Daily Log button presentation.
- Added saving/loading feedback.
- Added Saved confirmation behavior.
- Improved error presentation.
- Updated card sections and spacing to more closely match iOS.
- Completed the Daily Log parity work.

### Login / Authentication

- Investigated the login error shown when the device was in Airplane Mode
  or otherwise unable to connect to the authentication service.
- Identified that network failures could be reported as:
  `Invalid email or password.`
- Updated authentication error handling to distinguish network failures
  from invalid credentials.
- Added a specific connectivity error message:
  `Unable to connect. Check your internet connection or turn off Airplane Mode and try again.`
- Preserved the invalid-credentials message for authentication failures.
- Added handling for missing login credentials.
- Added handling for network request timeouts.
- Preserved coroutine cancellation instead of converting cancellation into
  a login error.

---

## September 9, 2026

### Insights

- Continued iOS-to-Android parity work with the Insights screen.
- Updated the Insights implementation and chart presentation.
- Worked through build errors involving:
    - `count`
    - `usageCount`
- Corrected the Insights implementation.
- Updated the presentation of recovery-related statistics.
- Updated chart-related components.
- Confirmed the Insights screen builds and runs successfully.

### Coping Strategies

- Began iOS-to-Android parity work on the Coping Strategies area.
- Reviewed:
    - `AddStrategyScreen`
    - `CopingStrategyGuideScreen`
    - `StrategyDetailScreen`
    - `StrategyListScreen`
    - `StrategyViewModel`
- Updated the Strategy List implementation.
- Improved strategy category organization.
- Added explicit category handling.
- Corrected category ordering.
- Improved the empty-state implementation.
- Updated strategy card presentation.
- Confirmed the Strategy List builds and runs successfully.

---

# Important Distinction

The app's core functionality was implemented before the current
iOS-to-Android parity work began.

The current work is focused on:

- Matching the iOS layout
- Matching spacing and sizing
- Matching colors
- Matching typography
- Matching navigation behavior
- Matching loading states
- Matching empty states
- Matching error handling
- Matching success states
- Matching interaction behavior
- Matching screen organization
- Matching the overall user experience
- Verifying Android behavior against the existing iOS implementation

The purpose of this phase is to make the Android version as close as
possible to the existing iOS version while preserving the functionality
that was already present in the Android application.

---

# Next Development Step

The next screen scheduled for iOS-to-Android parity work is:

**Strategy Detail Screen**

After the remaining screens are completed, the project will move into
the final navigation review, full iOS-to-Android comparison, and complete
Android testing phase.