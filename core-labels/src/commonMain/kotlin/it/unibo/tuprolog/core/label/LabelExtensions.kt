package it.unibo.tuprolog.core.label

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.utils.setTag

private const val LABELS = "it.unibo.tuprolog.labels"
private const val LABELLINGS = "it.unibo.tuprolog.labellings"

@Suppress("UNCHECKED_CAST")
val Term.labels: Labels
    get() = this.tags[LABELS] as Labels? ?: emptySet()

fun <T : Term> T.setLabels(labels: Labels): T =
    this.setTag(LABELS, labels)

fun <T : Term> T.addLabel(label: Label): T =
    this.setLabels(labels + label)

@Suppress("UNCHECKED_CAST")
val Substitution.labels: Labels
    get() = (this.tags[LABELS] as Labels? ?: emptySet())

fun <T : Substitution> T.setLabels(labels: Labels): T =
    this.setTag(LABELS, labels)

fun <T : Substitution> T.addLabel(label: Label): T =
    this.setLabels(labels + label)

fun <T : Term> T.addLabel(label: Any): T = addLabel(Label.of(label))

fun Term.equalsWithLabels(other: Any?) =
    this == other && this.labels == (other as Term).labels


@Suppress("UNCHECKED_CAST")
val Substitution.labellings: Labellings
    get() = this.tags[LABELLINGS] as Labellings? ?: emptyLabellings()

fun <S : Substitution> S.setLabellings(labellings: Labellings): S =
    this.setTag(LABELLINGS, labellings)
