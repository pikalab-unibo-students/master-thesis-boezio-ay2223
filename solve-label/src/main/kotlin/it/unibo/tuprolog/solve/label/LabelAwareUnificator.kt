package it.unibo.tuprolog.solve.label

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.unify.Unificator

interface LabelAwareUnificator: Unificator {

    fun shouldUnify(t1: Labelling, t2: Labelling): Boolean
    fun merge(t1: Labelling, t2: Labelling): Labelling

    fun mguWithLabels(t1: Labelling, t2: Labelling): Pair<Substitution, Labellings>
    fun unifyWithLabels(t1: Labelling, t2: Labelling): Labelling

}