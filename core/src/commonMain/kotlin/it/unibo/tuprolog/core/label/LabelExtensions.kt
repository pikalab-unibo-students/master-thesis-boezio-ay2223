package it.unibo.tuprolog.core.label

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.Formatter
import it.unibo.tuprolog.utils.setTag

@Suppress("UNCHECKED_CAST")
val Term.labels: Labels
    get() = this.tags["it.unibo.tuprolog.labels"] as Labels? ?: emptySet()

fun <T : Term> T.setLabels(labels: Labels): T =
    this.setTag("it.unibo.tuprolog.labels", labels)

fun <T : Term> T.addLabel(label: Label): T =
    this.setLabels(labels + label)

@Suppress("UNCHECKED_CAST")
val Substitution.labels: Labels
    get() = (this.tags["it.unibo.tuprolog.labels"] as Labels? ?: emptySet())

fun <T : Substitution> T.setLabels(labels: Labels): T =
    this.setTag("it.unibo.tuprolog.labels", labels)

fun <T : Substitution> T.addLabel(label: Label): T =
    this.setLabels(labels + label)

fun <T : Term> T.addLabel(label: Any): T = addLabel(Label.of(label))

object LabelAwareTermFormatter : Formatter<Term> {
    override fun format(value: Term): String {
        return value.accept(LabelFormatter())
    }
}

fun Term.equalsWithLabels(other: Any?) =
    this == other && this.labels == (other as Term).labels
