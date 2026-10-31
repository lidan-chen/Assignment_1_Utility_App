package au.edu.jcu.assessment.utilityapp

import au.edu.jcu.assessment.utilityapp.data.PokemonRepository

/**
 * Provides application dependencies.
 *
 * This lightweight dependency injection container
 * keeps dependency creation outside the UI layer.
 */
class AppContainer {

    val pokemonRepository: PokemonRepository by lazy {
        PokemonRepository.create()
    }
}
