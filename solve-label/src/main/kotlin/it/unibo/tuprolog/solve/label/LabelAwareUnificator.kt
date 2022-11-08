package it.unibo.tuprolog.solve.label

interface LabelAwareUnificator: Unificator {

    fun shouldUnify(t1: Labelling, t2: Labelling): Boolean
    fun merge(t1: Labelling, t2: Labelling): Labelling

    fun mguWithLabels(t1: Labelling, t2: Labelling): Pair<Substitution, Labellings>
    fun unifyWithLabels(t1: Labelling, t2: Labelling): Labelling

}