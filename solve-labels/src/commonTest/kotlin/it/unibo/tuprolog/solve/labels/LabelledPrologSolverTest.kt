package it.unibo.tuprolog.solve.labels

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Fact
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Var
import it.unibo.tuprolog.core.label.addLabel
import it.unibo.tuprolog.theory.Theory
import it.unibo.tuprolog.unify.label.LabelledUnificator
import kotlin.test.Test
import kotlin.test.assertTrue

class LabelledPrologSolverTest {

    // f(a<@2>,b<@5,@6>)<@5>
    private val theory = Theory.of(
        Fact.of(
            Struct.of(
                "f",
                Atom.of("a").addLabel("2"),
                Atom.of("b").addLabel("5").addLabel("6")
            ).addLabel("5")
        )
    )

    private val unificator = LabelledUnificator.strict(
        shouldUnify = { _, l1, _, l2 ->
            l1.any { it in l2 } || (l1.isEmpty() && l2.isEmpty())
        },
        merge = { _, l1, _, l2 ->
            l1.filter { it in l2 }.toSet() + l2.filter { it in l1 }.toSet()
        },
        stillValid = { struct, labellings ->
            val argsLabels = struct.args.map { labellings[it] }
            argsLabels.any { it == labellings[struct] }
        }
    )

    @Test
    fun testBaseExampleYesSolution() {
        // f(A<@2>,B<@5>)<@5,@6>
        val goal = Struct.of(
            "f",
            Var.of("A").addLabel("2"),
            Var.of("B").addLabel("5")
        ).addLabel("5").addLabel("6")

        val solver = LabelledPrologSolverFactory.solverOf(
            unificator = unificator,
            staticKb = theory
        )

        val solution = solver.solveOnce(goal)
        assertTrue(solution.isYes)
    }

    @Test
    fun testBaseExampleNoSolution() {
        // f(A<@2>,B<@6>)<@5,@6>
        val goal = Struct.of(
            "f",
            Var.of("A").addLabel("2"),
            Var.of("B").addLabel("6")
        ).addLabel("5").addLabel("6")

        val solver = LabelledPrologSolverFactory.solverOf(
            unificator = unificator,
            staticKb = theory
        )

        val solution = solver.solveOnce(goal)
        assertTrue(solution.isNo)
    }
}
