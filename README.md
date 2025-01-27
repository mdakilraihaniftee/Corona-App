# Corona App  

An **Android application** developed during the COVID-19 pandemic to serve as a centralized platform for real-time updates on COVID-19 statistics, along with social media and messaging features. This app combines the power of Firebase for backend support and API integration for dynamic content updates. This is an Android Application that can be used to get updates of all kinds of Covid News in Bangladesh and other countries of the world. It can be also used as social media as users can post or share something via News Feed and Chatbox. Fetch data from API in JSON, Used Firebase database, Firebase Cloud Messaging, Firebase Cloud fuction 

---

## Features  

### 1. COVID-19 Information Panel  
- Real-time updates on critical COVID-19 statistics such as:  
  - **Daily detections**: Number of new cases reported.  
  - **Deaths**: Daily and cumulative death tolls.  
  - **Recoveries**: Number of people who have successfully recovered.  
- Data fetched from a **reliable API** and parsed using **JSON** manipulation.  
- Well-organized and visually informative dashboard for users to easily stay updated.  

### 2. Social Media Platform  
- Includes a **built-in social media platform** where users can:  
  - Create posts and share updates.  
  - Like and comment on other users' posts.  
  - Engage with content relevant to the pandemic and community support.  
- **Firebase Realtime Database** powers seamless storage and retrieval of posts and interactions.  

### 3. Chat and Messaging  
- Integrated **real-time chat platform** where users can:  
  - Message other users directly.  
  - Engage in one-on-one or group conversations.  
- Utilizes **Firebase Cloud Messaging (FCM)** for instantaneous delivery of messages.  

### 4. Notifications  
- **Pop-up notifications** to ensure users stay informed about:  
  - New COVID-19 updates.  
  - Activity on their social media posts (likes, comments, etc.).  
  - Incoming messages from the chat platform.  
- Notifications are sent in real-time using **Firebase Cloud Messaging**.  

### 5. Firebase Integration  
- Full integration with Firebase services:  
  - **Firebase Realtime Database**: Stores user data, posts, and messages for live updates.  
  - **Firebase Storage**: Handles media uploads like images and videos shared by users.  
  - **Firebase Cloud Messaging (FCM)**: Powers notifications and chat messages.  

### 6. API Manipulation  
- The app retrieves COVID-19 statistics dynamically using **REST APIs**.  
- **JSON parsing** is used to extract, format, and display information efficiently.  

---

## Tech Stack  

### Frontend  
- **Java/Kotlin**: For developing the Android app.  
- **XML**: For designing the user interface.  

### Backend  
- **Firebase**:  
  - Realtime Database for live data updates.  
  - Cloud Messaging for notifications and chat.  
  - Firebase Storage for media uploads.  

### APIs  
- External API services for COVID-19 statistics.  

### Libraries and Tools  
- **Retrofit**: For making API calls and parsing JSON responses.  
- **Glide/Picasso**: For handling image loading and caching.  
- **Android Studio**: The primary development environment.  

---

## Project Folder Structure  

### Root Folder  
- **`app/`**: Contains the main application source code and resources.  
  - **`src/`**: Contains the core codebase for the app, divided into activities, fragments, and utilities.  
  - **`google-services.json`**: Firebase configuration file for backend integration.  
- **Gradle Files**:  
  - **`build.gradle`**: Configuration for building the app.  
  - **`settings.gradle`**: Global Gradle settings for the project.  
- **`.gitignore`**: Specifies the files to be ignored by Git, such as local configurations.  

### Key Files  
- **`proguard-rules.pro`**: Configuration for code obfuscation and minification in production builds.  
- **`gradlew` & `gradlew.bat`**: Gradle wrapper scripts for building and managing dependencies.  

---

## How to Set Up the Project  

### Prerequisites  
- [Android Studio](https://developer.android.com/studio) installed on your machine.  
- Firebase account with a project set up.  
- Replace the default `google-services.json` with your own Firebase configuration file.  

### Steps to Run  
1. **Clone the Repository**:  
   ```bash
   git clone https://github.com/your-username/Corona-App.git
2. **Open the Project**:
    Open the project in Android Studio.

3. **Sync Gradle**:
    Ensure all dependencies are installed by syncing the Gradle files.

4. **Run the App**:

    Connect an Android device or start an emulator.
    Click on the "Run" button in Android Studio.


