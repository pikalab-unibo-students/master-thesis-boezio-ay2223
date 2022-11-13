package it.unibo.tuprolog.solve.label

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.unify.AbstractUnificator

abstract class LabelAwareUnificator: AbstractUnificator() {

    abstract fun shouldUnify(t1: Labelling, t2: Labelling): Boolean
    abstract fun merge(t1: Labelling, t2: Labelling): Labelling

    abstract fun mguWithLabels(t1: Labelling, t2: Labelling): Pair<Substitution, Labellings>

}