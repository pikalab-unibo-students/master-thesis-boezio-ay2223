package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term

internal fun Term.applyWithLabel(substitution: Substitution): Term =
    this.accept(LabelledVisitor(substitution))