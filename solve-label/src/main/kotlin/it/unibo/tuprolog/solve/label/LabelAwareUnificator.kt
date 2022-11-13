package it.unibo.tuprolog.solve.label

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.unify.AbstractUnificator
import it.unibo.tuprolog.unify.Unificator.Companion.matches
import it.unibo.tuprolog.unify.Unificator.Companion.mguWith

abstract class LabelAwareUnificator: AbstractUnificator() {

    abstract fun shouldUnify(t1: Labelling, t2: Labelling): Boolean
    abstract fun merge(t1: Labelling, t2: Labelling): Labelling

    abstract fun mguWithLabels(t1: Labelling, t2: Labelling): Pair<Substitution, Labelling>

    companion object{

        // default means the case where labels are empty
        fun default(): LabelAwareUnificator{

            return object: LabelAwareUnificator(){

                override fun shouldUnify(t1: Labelling, t2: Labelling): Boolean = t1.first matches t2.first

                override fun merge(t1: Labelling, t2: Labelling): Labelling = t1

                override fun mguWithLabels(t1: Labelling, t2: Labelling): Pair<Substitution, Labelling> {
                    val substitution = t1.first mguWith t2.first
                    val labelling = merge(t1,t2)
                    return Pair(substitution,labelling)
                }

                override fun checkTermsEquality(first: Term, second: Term): Boolean = first == second
            }
        }


    }

}