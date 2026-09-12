Reclaim Android\
Reclaim is a recovery and wellness app designed to help users track 
sobriety progress, complete daily check-ins, record daily experiences,
review history, view insights, and access coping strategies.

The Android version already had the core application functionality before
the current iOS-to-Android visual and functionality parity work began. The 
current development phase focuses on making the Android version match the
existing iOS version as closely as possible.

**Test Login**\
Use this test account to access the app:
* EMAIL: test@reclaim.com
* PASSWORD: 12345678

These credentials are for development and testing purposes only.

**Technology**\
The Android app uses:
* Kotlin
* Jetpack Compose
* Material 3
* Android Studio
* Supabase authentication
* Supabase database
* Android Viewmodels and repositories

**Core Functionality Already Implemented**\
Before beginning the visual parity work, the Android app already
had basic Functionality across the main areas of the application.

**Authentication**
* User login
* User authentication through Supabase
* Authenticated user session handling
* Account-related navigation
* Logout functionality

**Home**
* Home dashboard
* Personalized welcome message
* Sober time tracking
* Streak tracking
* Recommended coping strategies
* Quick actions
* Daily check-in access
* Daily log access
* Coping strategy access
* Weekly check-in information

**Daily Check-In**
* Mood selection
* Craving level tracking
* Optional notes
* Saving check-ins to the database
* Prevention of duplicate check-ins on the same day
* Loading and error states
* Weekly check-in tracking

**Daily Logs**
* Opening the daily log entry flow
* Entering daily log information
* Saving daily log information
* Accessing daily log functionality from the Home Screen

**History**
* Loading saved check-ins and daily logs
* Displaying historical entries
* Viewing previous recovery activity
* Refreshing history data

**Insights**
* Loading insights information
* Displaying recovery-related statistics
* Displaying check-in and progress information
* Access the insights section from the main navigation

**Coping Strategies**
* Viewing coping strategies
* Opening the strategies section
* Viewing strategy information
* Accessing strategy-related screens
* Adding and managing strategy information

**Profile**
* Viewing user profile information
* Accessing profile-related screens
* Editing user profile information

**Settings**
* Opening the settings section
* Viewing account settings
* Viewing notification settings
* Viewing privacy settings
* Viewing backup and sync settings
* Viewing support settings
* Viewing appearance settings

**iOS-to-Android Parity Work Completed**\
The following screens have been updated during the current effort
to match the iOS version more closely.

**Home Screen**\
Completed:
* iOS-style vertical spacing
* Updated top padding
* Welcome message layout
* Recommended strategies card
* Quick action layout
* Sober Time card
* Streak card
* Milestone icons
* iOS-inspired milestone colors
* Daily check-in button
* Daily log button
* Coping strategy button
* Weekly check-in information
* Improved overall screen organization

**Daily Check-In Screen**\
Completed:
* Emoji-based mood selection
* Selected mood state
* Craving level slider 0 to 10
* Optional notes input
* Craving intensity display
* Submit check-in button
* Submit validation
* Loading State
* Duplicate check-in prevention
* Weekly check-in refresh
* iOS-inspired spacing and card

**History Screen**\
Completed:
* History entries displayed in a list
* Newest entries shown first
* Expandable history cards
* Entry summaries
* Additional entry details
* Empty state
* Error state
* Loading state
* Refresh action
* iOS-inspired card styling and spacing

**Screen Still Requiring Parity Work**
* Insights screen
* Coping Strategies list
* Strategy detail screen
* Profile screen
* Settings screen
* Appearance settings
* Notifications settings
* Backup & Sync
* Account settings
* Privacy settings
* Support settings
* Final navigation review
* Final iOS-to_Android visual comparison
* Full Android testing

**Development Process**\
The Android app is being updated one screen at a time using the 
iOS version as the design and behavior reference.

For each screen:

1. Compare the Android screen with the iOS screen.
2. Identify visual and functional differences
3. Update the Android code.
4. Build the project in Android Studio.
5. Test the screens.
6. Fix any build or functionality issues or errors.
7. Move to the next screen.

**Current Status**\
The Android app already contains the basic functionality for the
complete application.

The following screens have also been updated for closer iOS parity
and have been confirmed to build successfully:

1. Home
2. Daily Check-In
3. History

The next screen schedules for parity work is:

**Insights**

**Important Distinction**\
The app's core functionality was implemented before the 
current parity work began.

The current work is focused on:
* Matching the iOS layout
* Matching spacing and sizing
* Matching colors and typography
* Matching navigation behavior
* Matching loading states
* Matching empty states
* Matching error handling
* Matching interaction behavior
* Matching the overall user experience