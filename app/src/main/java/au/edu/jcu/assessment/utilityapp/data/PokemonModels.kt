package au.edu.jcu.assessment.utilityapp.data

import com.google.gson.annotations.SerializedName

data class NamedResource(
    val name: String
)

data class PokemonTypeSlot(
    val type: NamedResource
)

data class PokemonStatSlot(
    val base_stat: Int,
    val stat: NamedResource
)

data class PokemonSprites(
    val front_default: String?,
    val other: PokemonOtherSprites?
)

data class PokemonOtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: Artwork?
)

data class Artwork(
    val front_default: String?
)

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
        get() = sprites.other?.officialArtwork?.front_default
            ?: sprites.front_default

    fun stat(label: String): Int {
        return stats.firstOrNull {
            it.stat.name == label
        }?.base_stat ?: 0
    }
}

data class DamageRelations(
    val double_damage_from: List<NamedResource>,
    val half_damage_from: List<NamedResource>,
    val no_damage_from: List<NamedResource>
)

data class TypeDto(
    val damage_relations: DamageRelations
)


