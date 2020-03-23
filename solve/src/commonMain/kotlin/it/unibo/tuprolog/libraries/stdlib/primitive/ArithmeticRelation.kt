package it.unibo.tuprolog.libraries.stdlib.primitive

import it.unibo.tuprolog.core.Numeric
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.function.ArithmeticEvaluator
import it.unibo.tuprolog.solve.ExecutionContext
import it.unibo.tuprolog.solve.Solve

/** Base class for implementing arithmetic relation between [Numeric] terms */
abstract class ArithmeticRelation<E : ExecutionContext>(operator: String) : BinaryRelation<E>(operator) {

    override fun uncheckedImplementation(request: Solve.Request<E>): Sequence<Solve.Response> =
        sequenceOf(
            request.ensuringAllArgumentsAreInstantiated()
                .computeSingleResponse()
        )

    override fun Solve.Request<E>.computeSingleResponse(): Solve.Response =
        ArithmeticEvaluator(context).let {
            replyWith(relationWithoutSideEffects(arguments[0].accept(it), arguments[1].accept(it)))
        }

    override fun relationWithoutSideEffects(x: Term, y: Term): Boolean =
        arithmeticRelation(x as Numeric, y as Numeric)

    /** Template method that should implement the arithmetic relation between [x] and [y] */
    protected abstract fun arithmeticRelation(x: Numeric, y: Numeric): Boolean
}
