package it.unibo.tuprolog.core.label

import it.unibo.tuprolog.core.Formatter
import it.unibo.tuprolog.core.Term

object LabelAwareTermFormatter : Formatter<Term> {
    override fun format(value: Term): String {
        return value.accept(LabelFormatter())
    }
}
