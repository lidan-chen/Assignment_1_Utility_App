package au.edu.jcu.assessment.utilityapp.data

import com.google.gson.annotations.SerializedName

/**
 * Represents a named resource returned by PokeAPI.
 */
data class NamedResource(
    val name: String
)

/**
 * Represents one of a Pokemon's types.
 */
data class PokemonTypeSlot(
    val type: NamedResource
)

/**
 * Represents a Pokemon's base stat.
 */
data class PokemonStatSlot(
    @SerializedName("base_stat")
    val baseStat: Int,

    val stat: NamedResource
)

/**
 * Contains the Pokemon's image URLs.
 */
data class PokemonSprites(
    @SerializedName("front_default")
    val frontDefault: String?,

    val other: PokemonOtherSprites?
)

data class PokemonOtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: Artwork?
)

data class Artwork(
    @SerializedName("front_default")
    val frontDefault: String?
)

/**
 * Main Pokemon data model.
 */
data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeSlot>,
    val stats: List<PokemonStatSlot>,
    val sprites: PokemonSprites
) {
    val imageUrl: String?
        get() = sprites.other?.officialArtwork?.frontDefault
            ?: sprites.frontDefault

    /**
     * Returns a base stat by its API name.
     */
    fun stat(label: String): Int {
        return stats.firstOrNull {
            it.stat.name == label
        }?.baseStat ?: 0
    }
}

/**
 * Describes how attacking types affect a defending type.
 */
data class DamageRelations(
    @SerializedName("double_damage_from")
    val doubleDamageFrom: List<NamedResource>,

    @SerializedName("half_damage_from")
    val halfDamageFrom: List<NamedResource>,

    @SerializedName("no_damage_from")
    val noDamageFrom: List<NamedResource>
)

data class TypeDto(
    @SerializedName("damage_relations")
    val damageRelations: DamageRelations
)



