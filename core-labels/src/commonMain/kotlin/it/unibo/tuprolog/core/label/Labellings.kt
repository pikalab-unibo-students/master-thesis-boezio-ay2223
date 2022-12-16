package it.unibo.tuprolog.core.label

import it.unibo.tuprolog.core.Term

typealias Labellings = Map<Term, Labels>

fun emptyLabellings(): Labellings = emptyMap()
