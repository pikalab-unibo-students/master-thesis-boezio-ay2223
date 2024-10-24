package it.unibo.tuprolog.core.label

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.visitors.DefaultTermVisitor

class LabelFormatter() : DefaultTermVisitor<String>() {

    override fun defaultValue(term: Term) = getFormattedOutput(term)

    override fun visitStruct(term: Struct): String {
        return if (term !is Atom) {
            val prefix = "${term.functor}("
            var arguments = ""
            for (arg in term.args) {
                arguments = arguments + arg.accept(this) + ", "
            }
            arguments = arguments.dropLast(2)
            arguments += ")"
            val postfix = term.labels.joinToString(", ", "<", ">")
            prefix + arguments + postfix
        } else {
            getFormattedOutput(term)
        }
    }

    private fun <T : Term> getFormattedOutput(term: T): String =
        term.labels.joinToString(",", "$term<", ">")
}
