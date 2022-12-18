package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.label.Labels
import it.unibo.tuprolog.core.label.addLabellings
import it.unibo.tuprolog.core.label.labels
import it.unibo.tuprolog.core.label.setLabellings
import it.unibo.tuprolog.unify.AbstractUnificator
import it.unibo.tuprolog.unify.Equation

abstract class AbstractLabelledUnificator(context: Substitution = Substitution.empty()) : AbstractUnificator(context), LabelledUnificator {

    protected data class LabelledMguComputationContext(val labels: MutableMap<Term, Labels>) : MguComputationContext

    override fun createMguComputationContext(): MguComputationContext {
        return LabelledMguComputationContext(mutableMapOf())
    }

    final override fun shortCircuit(computationContext: MguComputationContext, assignment: Equation.Assignment): Boolean {
        val eq = assignment
        if (shouldUnify(eq.lhs, eq.lhs.labels, eq.rhs, eq.rhs.labels)) {
            val newLabels = merge(eq.lhs, eq.lhs.labels, eq.rhs, eq.rhs.labels)
            val labelsMap = (computationContext as LabelledMguComputationContext).labels
            labelsMap[eq.lhs] = newLabels
            labelsMap[eq.rhs] = newLabels
            return false
        }
        return true
    }

    final override fun hijacktMgu(computationContext: MguComputationContext, substitution: Substitution): Substitution {
        val labelsMap = (computationContext as LabelledMguComputationContext).labels
        return substitution.setLabellings(labelsMap)
    }

    override fun shouldUnify(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Boolean = true

    override fun merge(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Labels = emptySet()

    override fun mgu(term1: Term, term2: Term, occurCheckEnabled: Boolean): Substitution {
        if (shouldUnify(term1, term1.labels, term2, term2.labels)) {
            val mgu = super<AbstractUnificator>.mgu(term1, term2, occurCheckEnabled)
            if (mgu.isSuccess) {
                val finalLabels = merge(term1, term1.labels, term2, term2.labels)
                val termsMap = mutableMapOf<Term, Labels>()
                termsMap[term1] = finalLabels
                termsMap[term2] = finalLabels
                return mgu.addLabellings(termsMap)
            }
        }
        return Substitution.failed()
    }

    override fun unify(term1: Term, term2: Term, occurCheckEnabled: Boolean): Term? {
        val mguWithLabels = mgu(term1, term2, occurCheckEnabled)
        if (mguWithLabels.isSuccess) {
            return term1.accept(LabelledVisitor(mguWithLabels.castToUnifier()))
        }
        return null
    }
}
