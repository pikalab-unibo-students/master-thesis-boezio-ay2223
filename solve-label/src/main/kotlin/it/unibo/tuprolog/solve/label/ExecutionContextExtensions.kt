package it.unibo.tuprolog.solve.label

import it.unibo.tuprolog.core.Clause
import it.unibo.tuprolog.solve.ExecutionContext
import it.unibo.tuprolog.solve.classic.ClassicExecutionContext

// labellings of the execution context

val ClassicExecutionContext.labellings: Labellings
    get() = this.customData.durable["labellings"] as Labellings

fun ClassicExecutionContext.setLabellings(value: Labellings): ExecutionContext =
    copy(
        customData = customData.copy(
            durable = customData.durable + ("labellings" to value)
        )
    )

// custom Unificator for ExecutionContext

val ClassicExecutionContext.customUnificator: LabelAwareUnificator
    get() = this.customData.durable["customUnificator"] as LabelAwareUnificator

fun ClassicExecutionContext.setCustomUnificator(value: LabelAwareUnificator): ExecutionContext =
    copy(
        customData = customData.copy(
            durable = customData.durable + ("customUnificator" to value)
        )
    )

// stillValid callback method to check consistency of clauses

val ClassicExecutionContext.stillValid: (Clause) -> Boolean
    get() = this.customData.durable["stillValid"] as (Clause) -> Boolean

fun ClassicExecutionContext.setStillValid(value: (Clause) -> Boolean): ExecutionContext =
    copy(
        customData = customData.copy(
            durable = customData.durable + ("stillValid" to value)
        )
    )
