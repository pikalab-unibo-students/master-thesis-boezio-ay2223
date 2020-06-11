package it.unibo.tuprolog.solve.primitive

import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.solve.ExecutionContext
import it.unibo.tuprolog.solve.Solve

abstract class TypeEnsurer<E : ExecutionContext>(typeName: String) : UnaryPredicate.Predicative<E>(typeName) {
    override fun Solve.Request<E>.compute(first: Term): Boolean {
        ensureType(context, arguments[0][context.substitution])
        return true
    }

    abstract fun ensureType(context: E, term: Term)
}