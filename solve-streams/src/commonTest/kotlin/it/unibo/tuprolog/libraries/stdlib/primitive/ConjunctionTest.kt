package it.unibo.tuprolog.libraries.stdlib.primitive

import it.unibo.tuprolog.dsl.theory.prolog
import it.unibo.tuprolog.libraries.Libraries
import it.unibo.tuprolog.libraries.Library
import it.unibo.tuprolog.libraries.stdlib.primitive.testutils.ConjunctionUtils.failedRequests
import it.unibo.tuprolog.libraries.stdlib.primitive.testutils.ConjunctionUtils.myRequestToSolutions
import it.unibo.tuprolog.libraries.stdlib.primitive.testutils.ConjunctionUtils.trueAndTrueSolveRequest
import it.unibo.tuprolog.libraries.stdlib.primitive.testutils.ConjunctionUtils.twoMatchesDB
import it.unibo.tuprolog.libraries.stdlib.primitive.testutils.PrimitiveUtils.assertOnlyOneSolution
import it.unibo.tuprolog.primitive.PrimitiveWrapper
import it.unibo.tuprolog.primitive.extractSignature
import it.unibo.tuprolog.solve.*
import it.unibo.tuprolog.solve.solver.ExecutionContextImpl
import it.unibo.tuprolog.solve.testutils.SolverTestUtils.createSolveRequest
import kotlin.test.Test
import kotlin.collections.listOf as ktListOf

/**
 * Test class for [Conjunction]
 *
 * @author Enrico
 */
internal class ConjunctionTest {

    @Test
    fun conjunctionOfTrueReturnsTrue() {
        val responses = Conjunction.wrappedImplementation(trueAndTrueSolveRequest)

        assertOnlyOneSolution(trueAndTrueSolveRequest.query.yes(), responses)
    }

    @Test
    fun conjunctionComputesCorrectlyAllSolutions() {
        myRequestToSolutions.forEach { (goal, solutions) ->
            val responses = Conjunction.wrappedImplementation(createSolveRequest(goal, twoMatchesDB)).asIterable()

            assertSolutionEquals(solutions, responses.map { it.solution })
        }
    }

    @Test
    fun conjunctionFailsIfSomePredicateFails() {
        failedRequests.forEach { request ->
            val responses = Conjunction.wrappedImplementation(request)

            assertOnlyOneSolution(request.query.no(), responses)
        }
    }

    @Test
    fun conjunctionExecutesLeftAndRightArgumentsRemovingNotReachableVariablesExceptForRightAdded() {
        prolog {
            val preRequestSubstitution = "PreRequest" to "a"
            val firstSubstitution = "L" to "first"
            val secondSubstitution = "R" to "second"

            val leftPrimitive = PrimitiveWrapper.wrap<ExecutionContext>("left", 0) {
                sequenceOf(it.replySuccess(firstSubstitution))
            }
            val rightPrimitive = PrimitiveWrapper.wrap<ExecutionContext>("right", 0) {
                sequenceOf(it.replySuccess(secondSubstitution))
            }

            val goal = "left" and "right"
            val request = Solve.Request(
                goal.extractSignature(), goal.argsList, ExecutionContextImpl(
                    substitution = preRequestSubstitution,
                    libraries = Libraries(
                        Library.of(
                            alias = "conjunction.test",
                            primitives = mapOf(
                                *ktListOf(Conjunction, leftPrimitive, rightPrimitive)
                                    .map { it.descriptionPair }.toTypedArray()
                            )
                        )
                    )
                )
            )

            val responses = Conjunction.wrappedImplementation(request)

            assertOnlyOneSolution(request.query.yes(secondSubstitution), responses)
        }
    }
}
