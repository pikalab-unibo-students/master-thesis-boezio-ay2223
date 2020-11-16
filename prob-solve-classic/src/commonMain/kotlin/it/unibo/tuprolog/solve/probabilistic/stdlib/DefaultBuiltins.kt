package it.unibo.tuprolog.solve.probabilistic.stdlib

import it.unibo.tuprolog.solve.Signature
import it.unibo.tuprolog.solve.library.AliasedLibrary
import it.unibo.tuprolog.solve.primitive.Primitive
import it.unibo.tuprolog.solve.probabilistic.stdlib.primitive.Throw
import it.unibo.tuprolog.solve.probabilistic.stdlib.rule.SpecificRules
import it.unibo.tuprolog.solve.stdlib.CommonBuiltins
import it.unibo.tuprolog.theory.Theory

object DefaultBuiltins : AliasedLibrary by CommonBuiltins {

    override val theory: Theory by lazy {
        CommonBuiltins.theory + SpecificRules.theory
    }

    override val primitives: Map<Signature, Primitive> by lazy {
        CommonBuiltins.primitives + sequenceOf(
            Throw
        ).map { it.descriptionPair }.toMap()
    }
}
