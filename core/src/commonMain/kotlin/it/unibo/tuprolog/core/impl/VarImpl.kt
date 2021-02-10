package it.unibo.tuprolog.core.impl

import it.unibo.tuprolog.core.Scope
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.Terms.VAR_NAME_PATTERN
import it.unibo.tuprolog.core.Var
import it.unibo.tuprolog.utils.setTags
import kotlin.jvm.Synchronized

@Suppress("EqualsOrHashCode")
internal class VarImpl(
    override val name: String,
    private val identifier: Long = instanceId(name),
    tags: Map<String, Any> = emptyMap()
) : TermImpl(tags), Var {

    companion object {
        private val nameToInstanceCount = mutableMapOf<String, Long>()

        @Synchronized
        private fun instanceId(name: String): Long {
            val count = nameToInstanceCount[name]?.let { it + 1 } ?: 0
            nameToInstanceCount[name] = count
            return count
        }
    }

    override val completeName: String by lazy { "${name}_$identifier" }

    override val isAnonymous: Boolean = super.isAnonymous

    override val isNameWellFormed: Boolean by lazy { VAR_NAME_PATTERN.matches(name) }

    override fun structurallyEquals(other: Term): Boolean = other is VarImpl

    override fun copyWithTags(tags: Map<String, Any>): Var = VarImpl(name, identifier, tags)

    override fun freshCopy(): Var = VarImpl(name, tags = tags)

    override fun freshCopy(scope: Scope): Var =
        when {
            isAnonymous -> scope.anonymous()
            else -> scope.varOf(name)
        }.setTags(tags)

    override fun toString(): String = if (isNameWellFormed) completeName else Var.escapeName(completeName)

    override val id: String get() = identifier.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Var) return false

        return equalsByCompleteName(other)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun equalsByCompleteName(other: Var) = completeName == other.completeName

    private fun equalsToVar(other: Var, useVarCompleteName: Boolean) =
        if (useVarCompleteName) {
            completeName == other.completeName
        } else {
            name == other.name
        }

    override fun equals(other: Term, useVarCompleteName: Boolean): Boolean =
        other is Var && equalsToVar(other, useVarCompleteName)

    override val hashCodeCache: Int by lazy { completeName.hashCode() }
}
