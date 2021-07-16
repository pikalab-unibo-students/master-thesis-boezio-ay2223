package it.unibo.tuprolog.solve.classic.fsm

import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.solve.classic.ClassicExecutionContext
import it.unibo.tuprolog.solve.classic.stdlib.rule.Catch
import it.unibo.tuprolog.solve.exception.PrologError
import it.unibo.tuprolog.solve.exception.ResolutionException
import it.unibo.tuprolog.solve.exception.error.MessageError
import it.unibo.tuprolog.solve.exception.error.SystemError
import it.unibo.tuprolog.unify.Unificator.Companion.mguWith
import it.unibo.tuprolog.utils.Cursor
import it.unibo.tuprolog.utils.plus

data class StateException(
    override val exception: ResolutionException,
    override val context: ClassicExecutionContext
) : ExceptionalState, AbstractState(context) {

    private fun Struct.isCatch(): Boolean =
        arity == 3 && functor == Catch.functor

    private fun PrologError.getExceptionContent(): Term {
        return when (this) {
            is MessageError -> content
            else -> errorStruct
        }
    }

    private fun ResolutionException.toPublicException(): ResolutionException =
        when (this) {
            is MessageError -> SystemError.forUncaughtError(this)
            else -> this
        }

    private val finalState: EndState
        get() = StateHalt(
            exception.toPublicException(),
            context.copy(step = nextStep())
        )

    private val handleExceptionInParentContext: StateException
        get() = StateException(
            exception,
            context.parent!!.copy(step = nextStep())
        )

    private fun handleStruct(catchGoal: Struct, error: PrologError): State =
        when {
            catchGoal.isCatch() -> {
                val catcher = catchGoal[1] mguWith error.getExceptionContent()
                handleCatch(catchGoal, catcher)
            }
            context.isRoot -> finalState
            else -> handleExceptionInParentContext
        }

    private fun handleCatch(catchGoal: Struct, catcher: Substitution) =
        when {
            catcher.isSuccess -> {
                val newSubstitution =
                    (context.substitution + catcher).filter(context.interestingVariables)
                val subGoals = catchGoal[2][newSubstitution]
                val newGoals = subGoals.toGoals() + context.goals.next

                StateGoalSelection(
                    context.copy(
                        goals = newGoals,
                        rules = Cursor.empty(),
                        primitives = Cursor.empty(),
                        substitution = newSubstitution.castToUnifier(),
                        step = nextStep()
                    )
                )
            }
            context.isRoot -> finalState
            else -> handleExceptionInParentContext
        }

    override fun computeNext(): State {
        return when (exception) {
            is PrologError -> {
                val catchGoal = context.currentGoal!!
                when {
                    catchGoal.isStruct -> handleStruct(catchGoal.castToStruct(), exception)
                    context.isRoot -> finalState
                    else -> handleExceptionInParentContext
                }
            }
            else -> finalState
        }
    }

    override fun clone(context: ClassicExecutionContext): StateException =
        copy(exception = exception, context = context)
}
