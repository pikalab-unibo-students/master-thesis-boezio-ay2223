package it.unibo.tuprolog.solve.fsm

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.primitive.Signature
import it.unibo.tuprolog.solve.ExecutionContextImpl
import it.unibo.tuprolog.solve.Solve
import it.unibo.tuprolog.solve.appendPrimitives
import it.unibo.tuprolog.solve.appendRules
import it.unibo.tuprolog.utils.Cursor
import it.unibo.tuprolog.utils.EmptyCursor
import it.unibo.tuprolog.utils.cursor


fun Sequence<Clause>.ensureRules(): Cursor<out Rule> =
    map { require(it is Rule); it as Rule }.cursor()

fun Term.unfoldGoals(): Sequence<Term> =
    when (this) {
        is Tuple -> toSequence().flatMap { it.unfoldGoals() }
        else -> sequenceOf(this)
    }

fun Term.toGoals(): Cursor<out Term> =
    unfoldGoals().map {
        when (it) {
            is Var -> Struct.of("call", it)
            else -> it
        }
    }.cursor()

fun ExecutionContextImpl.createTempChild(inferProcedureFromGoals: Boolean = true): ExecutionContextImpl {
    val currentGoal = this.currentGoal as Struct

    return copy(
        goals = currentGoal.toGoals(),
        procedure = if (inferProcedureFromGoals) currentGoal else procedure,
        parent = this,
        depth = depth + 1,
        step = step + 1
    )
}

fun ExecutionContextImpl.createChildAppendingRulesAndChoicePoints(
    rules: Cursor<out Rule>,
    inferProcedureFromGoals: Boolean = true
): ExecutionContextImpl {
    val tempExecutionContext = createTempChild(inferProcedureFromGoals)

    val newChoicePointContext = if (rules.hasNext) {
        choicePoints.appendRules(rules.next, tempExecutionContext)
    } else {
        choicePoints.appendRules(EmptyCursor, tempExecutionContext)
    }

    return tempExecutionContext.copy(
        rules = rules,
        choicePoints = newChoicePointContext
    )
}

fun ExecutionContextImpl.createChildAppendingPrimitivesAndChoicePoints(
    primitiveExecutions: Cursor<out Solve.Response>,
    inferProcedureFromGoals: Boolean = true
): ExecutionContextImpl {
    val tempExecutionContext = createTempChild(inferProcedureFromGoals)

    val newChoicePointContext = if (primitiveExecutions.hasNext) {
        choicePoints.appendPrimitives(primitiveExecutions.next, tempExecutionContext)
    } else {
        choicePoints.appendPrimitives(EmptyCursor, tempExecutionContext)
    }

    return tempExecutionContext.copy(
        primitives = primitiveExecutions,
        choicePoints = newChoicePointContext
    )
}

// TODO Giovanni's review needed!! with Git > Show History
fun ExecutionContextImpl.toRequest(
    signature: Signature,
    arguments: kotlin.collections.List<Term>
): Solve.Request<ExecutionContextImpl> =
    Solve.Request(
        signature,
        arguments,
        copy(
            libraries = libraries,
            flags = flags,
            staticKB = staticKB,
            dynamicKB = dynamicKB,
            substitution = substitution
        ),
        executionMaxDuration = maxDuration
    )