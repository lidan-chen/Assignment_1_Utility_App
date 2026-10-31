package au.edu.jcu.assessment.utilityapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.Locale

/**
 * Provides Pokemon data to the ViewModel.
 * Networking is handled through Retrofit.
 */
class PokemonRepository(
    private val api: PokemonApi
) {

    /**
     * Fetches a Pokemon using its name or Pokedex number.
     */
    suspend fun getPokemon(query: String): PokemonDto {
        val normalizedQuery = query.trim()
            .lowercase(Locale.ROOT)

        require(normalizedQuery.isNotEmpty()) {
            "Please enter a Pokemon name or number."
        }

        return api.getPokemon(normalizedQuery)
    }

    /**
     * Retrieves defensive type information.
     */
    suspend fun getType(typeName: String): TypeDto {
        return api.getType(
            typeName.trim().lowercase(Locale.ROOT)
        )
    }

    /**
     * Fetches the defensive type data for all of
     * a Pokemon's types.
     */
    suspend fun getPokemonTypes(
        pokemon: PokemonDto
    ): List<TypeDto> {
        return pokemon.types.map { slot ->
            getType(slot.type.name)
        }
    }

    companion object {

        private const val BASE_URL =
            "https://pokeapi.co/api/v2/"

        /**
         * Creates the repository with a Retrofit API client.
         */
        fun create(): PokemonRepository {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()

            val api = retrofit.create(
                PokemonApi::class.java
            )

            return PokemonRepository(api)
        }
    }
}
