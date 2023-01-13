package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.label.Labellings
import it.unibo.tuprolog.core.label.Labels
import it.unibo.tuprolog.unify.Unificator

interface LabelledUnificator : Unificator {
    fun shouldUnify(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Boolean
    fun merge(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Labels
    fun stillValid(struct: Struct, labellings: Labellings): Boolean

    companion object {
        fun strict(
            shouldUnify: (Term, Labels, Term, Labels) -> Boolean,
            merge: (Term, Labels, Term, Labels) -> Labels,
            stillValid: (Struct, Labellings) -> Boolean
        ): LabelledUnificator =
            object : AbstractLabelledUnificator() {
                override fun checkTermsEquality(first: Term, second: Term): Boolean = first == second

                override fun shouldUnify(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Boolean {
                    return shouldUnify(term1, labels1, term2, labels2)
                }

                override fun merge(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Labels {
                    return merge(term1, labels1, term2, labels2)
                }

                override fun stillValid(struct: Struct, labellings: Labellings): Boolean {
                    return stillValid(struct, labellings)
                }
            }
    }
}
