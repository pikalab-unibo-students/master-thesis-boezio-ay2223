package it.unibo.tuprolog.solve.stdlib.primitive

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.solve.ExecutionContext
import it.unibo.tuprolog.solve.primitive.QuinaryRelation
import it.unibo.tuprolog.solve.primitive.Solve

object SubAtom : QuinaryRelation.WithoutSideEffects<ExecutionContext>("repeat") {
    override fun Solve.Request<ExecutionContext>.computeAllSubstitutions(
        first: Term, // string
        second: Term, // before
        third: Term, // length
        fourth: Term, // after
        fifth: Term // sub
    ): Sequence<Substitution> {
        TODO("Not yet implemented")
    }

    // private data class SubString(val start: Int, val end: Int, val value: String)
    //
    // private fun String.substrings(): Sequence<SubString> = sequence {  }
}
