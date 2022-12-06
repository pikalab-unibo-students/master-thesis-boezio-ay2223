package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.label.Labels
import it.unibo.tuprolog.core.label.labels
import it.unibo.tuprolog.unify.AbstractUnificator
import it.unibo.tuprolog.unify.Unificator
import it.unibo.tuprolog.utils.setTag

abstract class AbstractLabelledUnificator(
    private val delegate: AbstractUnificator = Unificator.default as AbstractUnificator
) : LabelledAwareUnificator {

    override fun shouldUnify(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Boolean =
        delegate.shouldUnify(term1, labels1, term2, labels2)

    override fun merge(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Labels =
        delegate.merge(term1, labels1, term2, labels2)

    override fun merge(
        substitution1: Substitution,
        substitution2: Substitution,
        occurCheckEnabled: Boolean
    ): Substitution {
        TODO("Not yet implemented")
    }

    override val context: Substitution
        get() = delegate.context

    override fun mgu(term1: Term, term2: Term, occurCheckEnabled: Boolean): Substitution {
        if (shouldUnify(term1, term1.labels, term2, term2.labels)) {
            val mgu = delegate.mgu(term1, term2, occurCheckEnabled)
            if (mgu.isSuccess) {
                val finalLabels = merge(term1, term1.labels, term2, term2.labels)
                val newMgu = mgu.setTag(term1.toString(), finalLabels)
                return newMgu.setTag(term2.toString(), finalLabels)
            }
        }
        return Substitution.failed()
    }

    override fun unify(term1: Term, term2: Term, occurCheckEnabled: Boolean): Term? {
        val mguWithLabels = mgu(term1, term2)
        if (mguWithLabels.isSuccess) {
            return term1.accept(LabelledVisitor(mguWithLabels))
        }
        return null
    }
}