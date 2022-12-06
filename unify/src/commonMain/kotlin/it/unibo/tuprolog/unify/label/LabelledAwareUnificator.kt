package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.label.Labels
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.unify.Unificator

interface LabelledAwareUnificator : Unificator {

    fun shouldUnify(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Boolean
    fun merge(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Labels

}
