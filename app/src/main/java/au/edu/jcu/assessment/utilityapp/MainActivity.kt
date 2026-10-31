package au.edu.jcu.assessment.utilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import au.edu.jcu.assessment.utilityapp.ui.theme.MobileTech_UtilityAppTheme

/**
 * Entry point for the Pokemon Companion application.
 *
 * Provides dependencies and connects the ViewModel
 * with the Jetpack Compose user interface.
 */
class MainActivity : ComponentActivity() {

    private val appContainer = AppContainer()

    private val pokemonViewModel: PokemonViewModel by viewModels {
        PokemonViewModel.factory(
            appContainer.pokemonRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PokemonCompanionApp(
                viewModel = pokemonViewModel
            )
        }
    }
}

/**
 * Main application composable.
 *
 * Holds user settings and the bottom navigation state.
 * Settings are intentionally not persisted between launches.
 */
@Composable
fun PokemonCompanionApp(
    viewModel: PokemonViewModel
) {
    var selectedTab by rememberSaveable {
        mutableStateOf("Pokemon")
    }

    var darkMode by remember {
        mutableStateOf(false)
    }

    var showStats by remember {
        mutableStateOf(true)
    }

    var showWeaknesses by remember {
        mutableStateOf(true)
    }

    MobileTech_UtilityAppTheme(
        darkTheme = darkMode,
        dynamicColor = false
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {

                    NavigationBarItem(
                        selected = selectedTab == "Pokemon",
                        onClick = {
                            selectedTab = "Pokemon"
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Pokemon"
                            )
                        },
                        label = {
                            Text("Pokémon")
                        }
                    )

                    NavigationBarItem(
                        selected = selectedTab == "Settings",
                        onClick = {
                            selectedTab = "Settings"
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        },
                        label = {
                            Text("Settings")
                        }
                    )
                }
            }
        ) { innerPadding ->

            when (selectedTab) {

                "Pokemon" -> {
                    PokemonScreen(
                        viewModel = viewModel,
                        showStats = showStats,
                        showWeaknesses = showWeaknesses,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                "Settings" -> {
                    SettingsScreen(
                        darkMode = darkMode,
                        onDarkModeChange = {
                            darkMode = it
                        },
                        showStats = showStats,
                        onShowStatsChange = {
                            showStats = it
                        },
                        showWeaknesses = showWeaknesses,
                        onShowWeaknessesChange = {
                            showWeaknesses = it
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
