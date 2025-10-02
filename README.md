# EdTech Android App

An Android application for educational technology, designed to work in both online and offline modes.

## 🚀 Quick Start

### Prerequisites

- Android Studio (latest version)
- Android SDK (API level 19 or higher)
- Java Development Kit (JDK) 8 or higher
- Gradle 6.0 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd edtech-android
   ```

2. **Configure Local Properties**
   
   Copy the example local properties file and configure it with your settings:
   ```bash
   cp local.properties.example local.properties
   ```
   
   Update the `local.properties` file with your actual Android SDK path:
   ```properties
   sdk.dir=/path/to/your/android/sdk
   ```

3. **Configure Firebase**
   
   Copy the Firebase configuration example:
   ```bash
   cp app/google-services.example.json app/google-services.json
   ```
   
   Update `app/google-services.json` with your Firebase project details:
   ```json
   {
     "project_info": {
       "project_number": "your-project-number",
       "project_id": "your-firebase-project-id",
       "storage_bucket": "your-firebase-project-id.appspot.com"
     },
     "client": [
       {
         "client_info": {
           "mobilesdk_app_id": "your-mobile-sdk-app-id",
           "android_client_info": {
             "package_name": "com.fortyk.studentapp"
           }
         },
         "oauth_client": [
           {
             "client_id": "your-oauth-client-id.apps.googleusercontent.com",
             "client_type": 3
           }
         ],
         "api_key": [
           {
             "current_key": "your-firebase-api-key"
           }
         ]
       }
     ]
   }
   ```

4. **Configure API Endpoints**
   
   Update the API endpoints in `app/src/main/java/com/fortyk/studentapp/api/ApiClient.java`:
   ```java
   public static String baseUrlString = "https://your-api-server.com/";
   public static String uploadbaseUrl = "https://your-lms-server.com/";
   public static String fileserverbaseUrl = "https://your-file-server.com/";
   ```

5. **Configure Signing (Optional)**
   
   For release builds, configure your keystore in `app/build.gradle`:
   ```gradle
   signingConfigs {
       config {
           storeFile file('./../your-keystore.jks')
           storePassword 'your-store-password'
           keyPassword 'your-key-password'
           keyAlias = 'your-key-alias'
       }
   }
   ```

6. **Build the Project**
   ```bash
   ./gradlew assembleDebug
   ```

## 🏃‍♂️ Running the App

### Development Mode
```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

### Release Mode
```bash
# Build release APK
./gradlew assembleRelease

# Build release bundle
./gradlew bundleRelease
```

## 🏗️ Building

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### Bundle Build (for Play Store)
```bash
./gradlew bundleRelease
```

## 🚀 Deployment

### AWS CodeBuild
If using AWS CodeBuild, copy the buildspec example:
```bash
cp buildspec.example.yml buildspec.yml
```

Update the environment variables in your build environment:
- `RPIURL`: Your API server URL
- `LMSURL`: Your LMS server URL
- `buildtype`: Build type (debug/release)
- `buildpath`: Build path for artifacts

## 📁 Project Structure

```
edtech-android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/fortyk/studentapp/
│   │   │   │   ├── activities/     # Activity classes
│   │   │   │   ├── adapters/       # RecyclerView adapters
│   │   │   │   ├── api/           # API client and interface
│   │   │   │   ├── fragments/     # Fragment classes
│   │   │   │   ├── models/        # Data models
│   │   │   │   └── utils/         # Utility classes
│   │   │   ├── res/               # Resources (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml
│   │   └── debug/                 # Debug-specific files
│   ├── build.gradle               # App-level build configuration
│   └── google-services.json       # Firebase configuration
├── gradle/                        # Gradle wrapper
├── build.gradle                   # Project-level build configuration
├── gradle.properties              # Gradle properties
├── local.properties.example       # Local properties template
├── buildspec.example.yml          # AWS CodeBuild configuration template
└── README.md                      # This file
```

## 🔧 Configuration

### API Configuration

The app uses three main API endpoints configured in `ApiClient.java`:

| Variable | Description | Example |
|----------|-------------|---------|
| `baseUrlString` | Main API server URL | `https://your-api-server.com/` |
| `uploadbaseUrl` | LMS server URL | `https://your-lms-server.com/` |
| `fileserverbaseUrl` | File server URL | `https://your-file-server.com/` |

### Firebase Configuration

The app uses Firebase for analytics, crash reporting, and configuration. Configure your Firebase project in `google-services.json`:

```json
{
  "project_info": {
    "project_number": "your-project-number",
    "project_id": "your-firebase-project-id",
    "storage_bucket": "your-firebase-project-id.appspot.com"
  }
}
```

### Signing Configuration

For release builds, configure your keystore in `app/build.gradle`:

```gradle
signingConfigs {
    config {
        storeFile file('./../your-keystore.jks')
        storePassword 'your-store-password'
        keyPassword 'your-key-password'
        keyAlias = 'your-key-alias'
    }
}
```

## 🔒 Security Notes

- Never commit `local.properties` files or any files containing sensitive information
- The `.gitignore` file is configured to exclude sensitive files
- Use environment variables for all configuration that varies between environments
- Keep API keys and secrets secure and never expose them in client-side code
- Keystore files and passwords should be kept secure and not committed to version control

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions, please:

1. Check the [Issues](../../issues) page for existing solutions
2. Create a new issue with detailed information about your problem
3. Include your environment details and steps to reproduce the issue

## 🔄 Updates

Keep your dependencies up to date:

```bash
./gradlew dependencies
```

Check for Android Gradle Plugin updates:

```bash
./gradlew wrapper --gradle-version=latest
```