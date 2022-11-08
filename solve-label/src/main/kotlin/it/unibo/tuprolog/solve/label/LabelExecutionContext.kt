package it.unibo.tuprolog.solve.label

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
    val labelings: Labelings,
    // stillValid is a Callback method
    val stillValid: (clause: Clause) -> Boolean,
    override val startTime: TimeInstant,
    override val maxDuration: TimeDuration = TimeDuration.MAX_VALUE,
    val choicePoints: ChoicePointContext? = null,
    val parent: LabelExecutionContext? = null,
    val depth: Int = 0,
    val step: Long = 0
): ExecutionContext{

}