package au.edu.jcu.assessment.utilityapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.edu.jcu.assessment.utilityapp.data.PokemonDto
import au.edu.jcu.assessment.utilityapp.logic.TypeMatchup
import coil.compose.AsyncImage
import java.util.Locale

/**
 * Main screen for searching and analysing Pokemon.
 */
@Composable
fun PokemonScreen(
    viewModel: PokemonViewModel,
    showStats: Boolean,
    showWeaknesses: Boolean,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Pokémon Companion",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Your quick Pokémon battle reference",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Pokémon name or Pokédex number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    viewModel.searchPokemon(state.searchQuery)
                },
                enabled = !state.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Search")
            }

            OutlinedButton(
                onClick = viewModel::loadRandomPokemon,
                enabled = !state.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Surprise Me")
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage?.let { message ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        state.pokemon?.let { pokemon ->
            PokemonDetailsCard(pokemon)

            if (showStats) {
                PokemonStatsCard(pokemon)
            }

            if (showWeaknesses) {
                state.matchup?.let { matchup ->
                    PokemonMatchupCard(matchup)
                }
            }
        }
    }
}

/**
 * Displays the selected Pokemon's key information.
 */
@Composable
private fun PokemonDetailsCard(pokemon: PokemonDto) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "#${pokemon.id}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = "${pokemon.name} artwork",
                modifier = Modifier.size(180.dp)
            )

            Text(
                text = pokemon.name.replaceFirstChar {
                    it.titlecase(Locale.ROOT)
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = pokemon.types.joinToString("  •  ") {
                    it.type.name.uppercase(Locale.ROOT)
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Height: ${pokemon.height / 10.0} m    " +
                        "Weight: ${pokemon.weight / 10.0} kg",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Displays base stats using progress indicators.
 */
@Composable
private fun PokemonStatsCard(pokemon: PokemonDto) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Base Stats",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            StatRow("HP", pokemon.stat("hp"))
            StatRow("Attack", pokemon.stat("attack"))
            StatRow("Defense", pokemon.stat("defense"))
            StatRow("Sp. Attack", pokemon.stat("special-attack"))
            StatRow("Sp. Defense", pokemon.stat("special-defense"))
            StatRow("Speed", pokemon.stat("speed"))
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = value.toString(),
                fontWeight = FontWeight.SemiBold
            )
        }

        LinearProgressIndicator(
            progress = { (value / 255f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

/**
 * Displays defensive weaknesses, resistances and immunities.
 */
@Composable
private fun PokemonMatchupCard(matchup: TypeMatchup) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Battle Matchups",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            MatchupSection(
                title = "Weak Against",
                types = matchup.weaknesses,
                color = MaterialTheme.colorScheme.error
            )

            MatchupSection(
                title = "Resistant To",
                types = matchup.resistances,
                color = MaterialTheme.colorScheme.primary
            )

            MatchupSection(
                title = "Immune To",
                types = matchup.immunities,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun MatchupSection(
    title: String,
    types: List<String>,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = color
        )

        Text(
            text = if (types.isEmpty()) {
                "None"
            } else {
                types.joinToString("  •  ") {
                    it.replaceFirstChar { char ->
                        char.titlecase(Locale.ROOT)
                    }
                }
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
