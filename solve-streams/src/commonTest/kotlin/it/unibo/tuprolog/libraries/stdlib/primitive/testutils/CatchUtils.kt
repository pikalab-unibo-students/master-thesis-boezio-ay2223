package it.unibo.tuprolog.libraries.stdlib.primitive.testutils

import it.unibo.tuprolog.dsl.theory.prolog
import it.unibo.tuprolog.libraries.stdlib.primitive.Call
import it.unibo.tuprolog.libraries.stdlib.primitive.Catch
import it.unibo.tuprolog.libraries.stdlib.primitive.Conjunction
import it.unibo.tuprolog.libraries.stdlib.primitive.Throw
import it.unibo.tuprolog.solve.*
import it.unibo.tuprolog.solve.PrologStandardExampleDatabases.catchAndThrowStandardExampleDatabase
import it.unibo.tuprolog.solve.PrologStandardExampleDatabases.catchAndThrowStandardExampleDatabaseNotableGoalToSolution
import it.unibo.tuprolog.solve.TestingClauseDatabases.catchTestingGoalsToSolutions
import it.unibo.tuprolog.solve.exception.HaltException
import it.unibo.tuprolog.solve.exception.PrologError
import it.unibo.tuprolog.solve.exception.prologerror.SystemError
import it.unibo.tuprolog.solve.exception.prologerror.TypeError
import it.unibo.tuprolog.solve.testutils.SolverTestUtils.createSolveRequest
import kotlin.collections.listOf as ktListOf

/**
 * Utils singleton to help testing [Catch]
 *
 * @author Enrico
 */
internal object CatchUtils {

    /**
     * Catch primitive examples, with expected responses
     *
     * Contained requests:
     * - `catch(true, _, fail).` **will result in** `Yes()`
     * - `catch(catch(throw(external(deepBall)), internal(I), fail), external(E), true).` **will result in** `Yes(E -> deepBall)`
     * - `catch(throw(first), X, throw(second)).` **will result in** `Halt()`
     * - `catch(throw(hello), X, true).` **will result in** `Yes(X -> hello)`
     * - `catch((throw(hello), fail), X, true).`  **will result in** `Yes(X -> hello)`
     * - Plus all [CallUtils.requestSolutionMap] in the form `catch(callGoal, _, fail).` with same result as `callGoal`
     * - Plus all [CallUtils.requestSolutionMap] in the form `catch(callGoalArgument, _, fail).` with same result as `callArgumentGoal`
     * - Plus all [CallUtils.requestToErrorSolutionMap] in the form `catch(callGoal, X, true)` resulting in `X` to be bound to the error struct thrown
     */
    internal val requestSolutionMap by lazy {
        mapOf(
            *catchTestingGoalsToSolutions.map { (goal, solutionList) ->
                createSolveRequest(
                    goal,
                    primitives = mapOf(*ktListOf(Call, Catch, Conjunction, Throw).map { it.descriptionPair }.toTypedArray())
                ) to solutionList
            }.toTypedArray(),

            *CallUtils.requestSolutionMap.flatMap { (callRequest, solutions) ->
                ktListOf(
                    prolog { Catch.functor(callRequest.query, `_`, false) },
                    prolog { Catch.functor(callRequest.arguments.single(), `_`, false) }
                ).map {
                    with(callRequest.context.libraries) {
                        createSolveRequest(it, theory, primitives + Catch.descriptionPair)
                    } to solutions.changeQueriesTo(it)
                }
            }.toTypedArray(),

            *CallUtils.requestToErrorSolutionMap.map { (callRequest, solutions) ->
                val updatedPrimitives = callRequest.context.libraries.primitives + Catch.descriptionPair
                prolog {
                    Catch.functor(callRequest.arguments.single(), "X", true).run {
                        createSolveRequest(this, callRequest.context.libraries.theory, updatedPrimitives) to
                                solutions.map {
                                    yes("X" to (it.exception as PrologError).errorStruct)
                                }
                    }
                }
            }.toTypedArray()
        )
    }

    /**
     * Prolog standard examples for `catch/3` primitive
     *
     * Contains those requests against [catchAndThrowStandardExampleDatabase]:
     *
     * - `catch(p, X, true)` **will result in** `Yes(), Yes(X -> b)`
     * - `catch(q, C, true)` **will result in** `Yes(C -> c)`
     * - `catch(throw(exit(1)), exit(X), true).` **will result in** `Yes(X -> 1)`
     * - `catch(throw(true), X, X).` **will result in** `Yes(X -> true)`
     * - `catch(throw(fail), X, X).` **will result in** `No()`
     */
    internal val prologStandardCatchExamples by lazy {
        catchAndThrowStandardExampleDatabaseNotableGoalToSolution
            .filter { (_, solutionList) -> solutionList.none { it is Solution.Halt } }
            .map { (goal, solutionList) ->
                createSolveRequest(
                    goal,
                    primitives = mapOf(
                        *ktListOf(Call, Catch, Conjunction, Throw).map { it.descriptionPair }.toTypedArray()
                    ),
                    database = catchAndThrowStandardExampleDatabase
                ) to solutionList
            }
    }

}
