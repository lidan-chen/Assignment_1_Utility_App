package au.edu.jcu.assessment.utilityapp.data

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Defines API endpoints for retrieving Pokemon information
 * from the public PokeAPI service.
 */
interface PokemonApi {

    /**
     * Retrieves a Pokemon by its name or Pokedex number.
     */
    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") name: String
    ): PokemonDto

    /**
     * Retrieves type information, including defensive
     * weaknesses, resistances, and immunities.
     */
    @GET("type/{name}")
    suspend fun getType(
        @Path("name") name: String
    ): TypeDto
}
