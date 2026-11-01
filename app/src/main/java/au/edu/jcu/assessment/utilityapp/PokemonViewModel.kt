package au.edu.jcu.assessment.utilityapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import au.edu.jcu.assessment.utilityapp.data.PokemonDto
import au.edu.jcu.assessment.utilityapp.data.PokemonRepository
import au.edu.jcu.assessment.utilityapp.logic.TypeMatchup
import au.edu.jcu.assessment.utilityapp.logic.TypeMatchupCalculator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.random.Random

/**
 * Represents the current state of the Pokemon screen.
 */
data class PokemonUiState(
    val pokemon: PokemonDto? = null,
    val matchup: TypeMatchup? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = ""
)

/**
 * Manages Pokemon data and user interactions.
 * Keeps business logic separate from the Compose UI.
 */
class PokemonViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonUiState())

    val uiState: StateFlow<PokemonUiState> =
        _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        searchPokemon("pikachu")
    }

    /**
     * Updates the search field without sending a request.
     */
    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    /**
     * Downloads Pokemon details and defensive type data.
     */
    fun searchPokemon(query: String) {
        val normalizedQuery = query.trim()

        if (normalizedQuery.isEmpty()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Please enter a Pokemon name or number."
                )
            }
            return
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    searchQuery = normalizedQuery
                )
            }

            try {
                val pokemon = repository.getPokemon(normalizedQuery)
                val typeData = repository.getPokemonTypes(pokemon)

                val matchup = TypeMatchupCalculator.calculate(
                    typeData
                )

                _uiState.update {
                    it.copy(
                        pokemon = pokemon,
                        matchup = matchup,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: HttpException) {
                val message = if (exception.code() == 404) {
                    "Pokemon not found. Check the name or number."
                } else {
                    "Server error (${exception.code()}). Please retry."
                }

                showError(message)
            } catch (_: IOException) {
                showError(
                    "Network unavailable. Check your connection."
                )
            } catch (_: Exception) {
                showError(
                    "Unable to load Pokemon. Please try again."
                )
            }
        }
    }

    /**
     * Fetches a random Pokemon from the original 151.
     */
    fun loadRandomPokemon() {
        val randomId = Random.nextInt(1, 152)
        searchPokemon(randomId.toString())
    }


    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = message
            )
        }
    }

    companion object {

        /**
         * Injects the repository through a ViewModel factory.
         */
        fun factory(
            repository: PokemonRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    require(
                        modelClass.isAssignableFrom(
                            PokemonViewModel::class.java
                        )
                    )
                    return PokemonViewModel(repository) as T
                }
            }
        }
    }
}
