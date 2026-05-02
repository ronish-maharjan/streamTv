StreamTV/
├── app/
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml   ✅
│           ├── java/
│           │   └── com/
│           │       └── streamtv/
│           │           └── app/
│           │               ├── MainActivity.kt
│           │               ├── data/
│           │               │   ├── api/
│           │               │   │   ├── ApiClient.kt
│           │               │   │   └── ApiService.kt
│           │               │   ├── model/
│           │               │   │   └── Movie.kt
│           │               │   └── prefs/
│           │               │       └── AppPrefs.kt
│           │               └── ui/
│           │                   ├── setup/
│           │                   │   └── SetupActivity.kt
│           │                   ├── home/
│           │                   │   ├── HomeActivity.kt
│           │                   │   ├── HomeFragment.kt
│           │                   │   ├── HomeViewModel.kt
│           │                   │   └── MovieCardPresenter.kt
│           │                   ├── detail/
│           │                   │   ├── DetailActivity.kt
│           │                   │   └── DetailFragment.kt
│           │                   └── player/
│           │                       └── PlayerActivity.kt
│           └── res/
│               ├── drawable/
│               │   └── bg_card.xml
│               ├── layout/
│               │   ├── activity_setup.xml
│               │   ├── activity_home.xml
│               │   ├── activity_detail.xml
│               │   ├── activity_player.xml
│               │   └── item_movie_card.xml
│               ├── mipmap-hdpi/
│               │   └── ic_launcher.png
│               └── values/
│                   ├── colors.xml
│                   ├── strings.xml
│                   └── themes.xml
│
├── app/
│   └── build.gradle        ← app level build.gradle
│
├── .github/
│   └── workflows/
│       └── build.yml
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── build.gradle             ← root level build.gradle
├── gradle.properties
├── gradlew
└── settings.gradle
