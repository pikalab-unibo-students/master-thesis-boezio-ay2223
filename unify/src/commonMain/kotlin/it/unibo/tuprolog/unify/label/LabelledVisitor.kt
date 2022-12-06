package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Integer
import it.unibo.tuprolog.core.label.Labels
import it.unibo.tuprolog.core.label.setLabels
import it.unibo.tuprolog.core.Numeric
import it.unibo.tuprolog.core.Real
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.Var
import it.unibo.tuprolog.core.visitors.DefaultTermVisitor

class LabelledVisitor(substitution: Substitution) : DefaultTermVisitor<Term>() {

    private val unifier: Substitution.Unifier = substitution.castToUnifier()

    @Suppress("UNCHECKED_CAST")
    override fun defaultValue(term: Term): Term {
        val labels: Labels = unifier.tags[term.toString()] as Labels
        return unifier[term]?.setLabels(labels) ?: term.setLabels(labels)
    }

    @Suppress("UNCHECKED_CAST")
    override fun visitVar(term: Var): Term{
        val labels: Labels = unifier.tags[term.completeName] as Labels
        return unifier[term]?.setLabels(labels) ?: term.setLabels(labels)
    }

    override fun visitInteger(term: Integer): Term = visitNumber(term)

    override fun visitReal(term: Real): Term = visitNumber(term)

    @Suppress("UNCHECKED_CAST")
    override fun visitAtom(term: Atom): Term {
        val labels: Labels = unifier.tags[term.toString()] as Labels
        return term.setLabels(labels)
    }

    @Suppress("UNCHECKED_CAST")
    override fun visitStruct(term: Struct): Term {
        val newArgs = term.args.map { it.accept(this) }
        return Struct.of(
            term.functor,
            newArgs
        ).setLabels(unifier.tags[term.toString()] as Labels)
    }

    @Suppress("UNCHECKED_CAST")
    private fun visitNumber(term : Numeric) : Term {
        val labels: Labels = unifier.tags[term.toString()] as Labels
        return term.setLabels(labels)
    }
}
