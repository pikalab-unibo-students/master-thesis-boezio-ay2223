package it.unibo.tuprolog.solve.classic

import it.unibo.tuprolog.solve.channel.InputStore
import it.unibo.tuprolog.solve.channel.OutputStore
import it.unibo.tuprolog.solve.flags.FlagStore
import it.unibo.tuprolog.solve.library.Runtime
import it.unibo.tuprolog.theory.MutableTheory
import it.unibo.tuprolog.theory.Theory

internal fun ClassicSolver.constructor(
    libraries: Runtime = Runtime.empty(),
    flags: FlagStore = FlagStore.empty(),
    initialStaticKb: Theory = Theory.empty(),
    initialDynamicKb: Theory = MutableTheory.empty(),
    inputChannels: InputStore = InputStore.fromStandard(),
    outputChannels: OutputStore = OutputStore.fromStandard(),
    trustKb: Boolean = false,
    labellings: Labellings = mapOf<Term,Labels>(),
    customUnificator: LabelAwareUnificator = LabelAwareUnificator.default(),
    stillValid: (Clause) -> Boolean = { true }
){
    super(libraries, flags, initialStaticKb, initialDynamicKb, inputChannels, outputChannels, trustKb)
    currentContext.setLabellings(labellings)
    currentContext.setCustomUnificator(customUnificator)
    currentContext.setStillValid(stillValid)
}

// same for other constructors, maybe it is better to modify the original class