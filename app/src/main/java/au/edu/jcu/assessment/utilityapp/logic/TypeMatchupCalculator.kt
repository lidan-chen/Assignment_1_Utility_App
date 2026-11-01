package au.edu.jcu.assessment.utilityapp.logic

import au.edu.jcu.assessment.utilityapp.data.TypeDto

/**
 * Represents defensive type matchups.
 */
data class TypeMatchup(
    val weaknesses: List<String>,
    val resistances: List<String>,
    val immunities: List<String>
)

/**
 * Calculates defensive effectiveness for single-type
 * and dual-type Pokemon.
 *
 * Type multipliers are combined multiplicatively.
 */
object TypeMatchupCalculator {

    private val allTypes = listOf(
        "normal", "fire", "water", "electric",
        "grass", "ice", "fighting", "poison",
        "ground", "flying", "psychic", "bug",
        "rock", "ghost", "dragon", "dark",
        "steel", "fairy"
    )

    fun calculate(typeData: List<TypeDto>): TypeMatchup {

        val multipliers = allTypes.associateWith {
            1.0
        }.toMutableMap()

        typeData.forEach { type ->

            val relations = type.damageRelations

            relations.doubleDamageFrom.forEach {
                val name = it.name
                multipliers[name] =
                    (multipliers[name] ?: 1.0) * 2.0
            }

            relations.halfDamageFrom.forEach {
                val name = it.name
                multipliers[name] =
                    (multipliers[name] ?: 1.0) * 0.5
            }

            relations.noDamageFrom.forEach {
                multipliers[it.name] = 0.0
            }
        }

        return TypeMatchup(
            weaknesses = multipliers
                .filterValues { it > 1.0 }
                .keys.sorted(),

            resistances = multipliers
                .filterValues {
                    it > 0.0 && it < 1.0
                }
                .keys.sorted(),

            immunities = multipliers
                .filterValues { it == 0.0 }
                .keys.sorted()
        )
    }
}
