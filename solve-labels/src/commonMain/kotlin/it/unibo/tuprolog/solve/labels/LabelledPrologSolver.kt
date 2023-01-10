package it.unibo.tuprolog.solve.labels

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.label.labellings
import it.unibo.tuprolog.core.label.labels
import it.unibo.tuprolog.solve.channel.InputChannel
import it.unibo.tuprolog.solve.channel.InputStore
import it.unibo.tuprolog.solve.channel.OutputChannel
import it.unibo.tuprolog.solve.channel.OutputStore
import it.unibo.tuprolog.solve.classic.AbstractClassicSolver
import it.unibo.tuprolog.solve.classic.ClassicExecutionContext
import it.unibo.tuprolog.solve.classic.SolutionIterator
import it.unibo.tuprolog.solve.classic.fsm.State
import it.unibo.tuprolog.solve.classic.fsm.StateBacktracking
import it.unibo.tuprolog.solve.classic.fsm.StateGoalSelection
import it.unibo.tuprolog.solve.classic.fsm.StatePrimitiveExecution
import it.unibo.tuprolog.solve.classic.fsm.StateRuleExecution
import it.unibo.tuprolog.solve.exception.Warning
import it.unibo.tuprolog.solve.flags.FlagStore
import it.unibo.tuprolog.solve.library.Runtime
import it.unibo.tuprolog.theory.MutableTheory
import it.unibo.tuprolog.theory.Theory
import it.unibo.tuprolog.unify.Unificator
import it.unibo.tuprolog.unify.label.LabelledUnificator

class LabelledPrologSolver : AbstractClassicSolver {

    constructor(
        unificator: Unificator,
        libraries: Runtime = Runtime.empty(),
        flags: FlagStore = FlagStore.empty(),
        initialStaticKb: Theory = Theory.empty(unificator),
        initialDynamicKb: Theory = MutableTheory.empty(unificator),
        inputChannels: InputStore = InputStore.fromStandard(),
        outputChannels: OutputStore = OutputStore.fromStandard(),
        trustKb: Boolean = false
    ) : super(unificator, libraries, flags, initialStaticKb, initialDynamicKb, inputChannels, outputChannels, trustKb)

    constructor(
        unificator: Unificator,
        libraries: Runtime = Runtime.empty(),
        flags: FlagStore = FlagStore.empty(),
        staticKb: Theory = Theory.empty(unificator),
        dynamicKb: Theory = MutableTheory.empty(unificator),
        stdIn: InputChannel<String> = InputChannel.stdIn(),
        stdOut: OutputChannel<String> = OutputChannel.stdOut(),
        stdErr: OutputChannel<String> = OutputChannel.stdErr(),
        warnings: OutputChannel<Warning> = OutputChannel.warn(),
        trustKb: Boolean = false
    ) : super(unificator, libraries, flags, staticKb, dynamicKb, stdIn, stdOut, stdErr, warnings, trustKb)

    init {
        require(unificator is LabelledUnificator)
    }

    override fun solutionIterator(
        initialState: State,
        onStateTransition: (State, State, Long) -> Unit
    ): SolutionIterator = SolutionIterator.of(initialState, onStateTransition, this::hijackStateTransition)

    @Suppress("UNUSED_PARAMETER")
    private fun hijackStateTransition(source: State, destination: State, step: Long): State {
        if (destination is StateGoalSelection && source.let { it is StateRuleExecution || it is StatePrimitiveExecution }) {
            if (!stillValid(destination.context)) {
                return source.failureState
            }
        }
        return destination
    }

    private val State.failureState: StateBacktracking
        get() = when (this) {
            is StateRuleExecution -> failureState
            is StatePrimitiveExecution -> failureState
            else -> error("Illegal state: $this")
        }

    private fun stillValid(context: ClassicExecutionContext): Boolean {
        val labellings = context.substitution.labellings
        val structuresMap = labellings.filter { (key, _) -> key is Struct && key !is Atom }
        for ((key, value) in structuresMap) {
            // in this particular example labels of the struct must be equals to one of its arguments' ones
            val struct = key.castToStruct()
            val argsLabels = struct.args.map { it.labels }
            val compatible = argsLabels.contains(value)
            if (!compatible) {
                return false
            }
        }
        return true
    }

    override fun copy(
        unificator: Unificator,
        libraries: Runtime,
        flags: FlagStore,
        staticKb: Theory,
        dynamicKb: Theory,
        stdIn: InputChannel<String>,
        stdOut: OutputChannel<String>,
        stdErr: OutputChannel<String>,
        warnings: OutputChannel<Warning>
    ) = LabelledPrologSolver(unificator, libraries, flags)

    override fun clone() = copy()
}
