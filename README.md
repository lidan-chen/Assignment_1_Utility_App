# Pokémon Companion

**CP3406 / CP5307 — Assessment 1: Utility App**

## 1. Project Overview

Pokémon Companion is an Android utility application designed to help users quickly access essential Pokémon information and defensive type matchups.

The application focuses on presenting useful information at a glance, allowing users to search for Pokémon, view base statistics, and identify weaknesses, resistances, and immunities.

## 2. Core Features

- **Pokémon Search:** Search for a Pokémon using its name or Pokédex number.
- **Random Discovery:** Select a random Pokémon from the original 151 Pokémon.
- **Pokémon Details:** Display Pokémon artwork, name, Pokédex number, types, height, and weight.
- **Base Stats Dashboard:** Visualise HP, Attack, Defense, Special Attack, Special Defense, and Speed using progress indicators.
- **Battle Matchup Analysis:** Calculate defensive weaknesses, resistances, and immunities, including dual-type combinations.
- **Customisable Settings:** Toggle dark mode, base statistics, and battle matchup information.
- **Network Error Handling:** Display feedback when Pokémon data cannot be retrieved.

## 3. Technologies Used

| Technology | Purpose |
|---|---|
| Kotlin | Android application development |
| Jetpack Compose | Declarative user interface |
| Material Design 3 | UI components and visual consistency |
| ViewModel | UI state and lifecycle management |
| StateFlow | Reactive state updates |
| Repository Pattern | Separation of data access from UI logic |
| Dependency Injection | Providing dependencies through an application container |
| Retrofit | REST API communication |
| Gson | JSON deserialisation |
| Coil | Pokémon artwork loading |

## 4. Application Architecture

The application separates presentation, state management, data access, and business logic.

```text
Jetpack Compose UI
        |
PokemonViewModel
        |
PokemonRepository
        |
PokemonApi (Retrofit)
        |
PokéAPI
```

The `PokemonViewModel` manages loading, search results, and errors. The `PokemonRepository` communicates with PokéAPI, while `TypeMatchupCalculator` calculates defensive type effectiveness.

A lightweight `AppContainer` provides repository dependencies.

## 5. Project Structure

```text
app/src/main/java/au/edu/jcu/assessment/utilityapp/
├── MainActivity.kt
├── PokemonScreen.kt
├── SettingsScreen.kt
├── PokemonViewModel.kt
├── AppContainer.kt
├── data/
│   ├── PokemonApi.kt
│   ├── PokemonModels.kt
│   └── PokemonRepository.kt
├── logic/
│   └── TypeMatchupCalculator.kt
└── ui/theme/
```

## 6. Getting Started

1. Clone or download this GitHub repository.
2. Open the project root folder in Android Studio.
3. Allow Gradle to synchronise and download dependencies.
4. Select an Android emulator or physical device.
5. Run the `app` configuration.
6. Enter a Pokémon name or Pokédex number, then tap **Search**.

An active internet connection is required to retrieve data from PokéAPI.

## 7. Settings

The Settings screen provides three controls:

- Dark Mode
- Show Base Stats
- Show Battle Matchups

Settings affect the main interface immediately and are not persisted between app launches.

## 8. Testing and Known Limitations

The project has been compiled and launched successfully using Android Studio.

The main interface, search field, navigation controls, and error-message display have been observed in the Android emulator.

During testing, the emulator reported that it was connected to Wi-Fi but had no internet access. Consequently, successful retrieval of Pokémon data, artwork, and type matchups has not yet been verified in that environment.

The application currently selects random Pokémon from the original 151 and provides defensive, rather than offensive, type matchup analysis.

## 9. Data Source

Pokémon information is retrieved through the public [PokéAPI](https://pokeapi.co/).

Pokémon artwork is loaded using the image URLs supplied by the API.

## 10. Development Context

This application was developed for CP3406 / CP5307 Assessment 1 using the provided Android Starter Template, with additional functionality and architecture implemented for the Pokémon Companion utility.

The project was developed with generative AI assistance, subject to the accompanying academic integrity declaration.
