package it.unibo.tuprolog.solve.label

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.core.operators.OperatorSet
import it.unibo.tuprolog.solve.*
import it.unibo.tuprolog.solve.channel.InputStore
import it.unibo.tuprolog.solve.channel.OutputStore
import it.unibo.tuprolog.solve.data.CustomDataStore
import it.unibo.tuprolog.solve.flags.FlagStore
import it.unibo.tuprolog.theory.MutableTheory
import it.unibo.tuprolog.theory.Theory
import it.unibo.tuprolog.solve.library.Runtime
import it.unibo.tuprolog.solve.primitive.Solve
import it.unibo.tuprolog.utils.Cursor

data class LabelExecutionContext(
    override val procedure: Struct? = null,
    override val libraries: Runtime = Runtime.empty(),
    override val flags: FlagStore = FlagStore.empty(),
    override val staticKb: Theory = Theory.empty(),
    override val dynamicKb: MutableTheory = MutableTheory.empty(),
    override val operators: OperatorSet = getAllOperators(libraries, staticKb, dynamicKb).toOperatorSet(),
    override val inputChannels: InputStore = InputStore.fromStandard(),
    override val outputChannels: OutputStore = OutputStore.fromStandard(),
    override val customData: CustomDataStore = CustomDataStore.empty(),
    // custom unifier must be provided as parameter
    val customUnifier: LabelAwareUnificator,
    override val substitution: Substitution.Unifier,
    val query: Struct = Truth.TRUE,
    val goals: Cursor<out Term> = Cursor.empty(),
    val rules: Cursor<out Rule> = Cursor.empty(),
    val primitives: Cursor<out Solve.Response> = Cursor.empty(),
    // labellings to use for the resolution process
    val labellings: Labellings,
    // stillValid is a Callback method
    val stillValid: (clause: Clause) -> Boolean,
    override val startTime: TimeInstant,
    override val maxDuration: TimeDuration = TimeDuration.MAX_VALUE,
    val choicePoints: ChoicePointContext? = null,
    val parent: LabelExecutionContext? = null,
    val depth: Int = 0,
    val step: Long = 0
): ExecutionContext {

}