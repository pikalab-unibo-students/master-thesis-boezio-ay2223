package it.unibo.tuprolog.solve.labels

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Fact
import it.unibo.tuprolog.core.Integer
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Var
import it.unibo.tuprolog.core.label.Label
import it.unibo.tuprolog.core.label.addLabel
import it.unibo.tuprolog.core.label.labels
import it.unibo.tuprolog.core.parsing.TermParser
import it.unibo.tuprolog.theory.Theory
import it.unibo.tuprolog.unify.label.LabelledUnificator
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertTrue

class DressSelectionTest {

    private fun checkSimilarity(referenceColor: Struct, dressColor: Struct, threshold: Integer): Boolean {
        val argsReference = referenceColor.args.map { it.castToInteger().value.toDouble() }
        val argsDress = dressColor.args.map { it.castToInteger().value.toDouble() }
        val intThreshold = threshold.value.toDouble()
        val componentDistances = argsReference.zip(argsDress).map { (lhs, rhs) -> (lhs - rhs).pow(2) }
        val euclideanDistance = sqrt(componentDistances.sum())
        return euclideanDistance <= intThreshold
    }

    @Test
    fun testDressSelectionCaseStudy() {
        // Knowledge Base
        val theory = Theory.of(
            // dress ( sweater , rgb (255 , 240 , 245) ) <@winter ,@fall >.
            Fact.of(
                Struct.of(
                    "dress",
                    Atom.of("sweater"),
                    Struct.of(
                        "rgb",
                        Integer.of(255),
                        Integer.of(240),
                        Integer.of(245)
                    )
                ).addLabel("winter").addLabel("fall")
            ),
            // dress ( t-shirt , rgb (255 , 222 , 173) ) <@summer , @spring >.
            Fact.of(
                Struct.of(
                    "dress",
                    Atom.of("t-shirt"),
                    Struct.of(
                        "rgb",
                        Integer.of(255),
                        Integer.of(222),
                        Integer.of(173)
                    )
                ).addLabel("summer").addLabel("spring")
            ),
            // dress ( t-shirt , rgb (119 , 136 , 153) ) <@summer , @spring >.
            Fact.of(
                Struct.of(
                    "dress",
                    Atom.of("t-shirt"),
                    Struct.of(
                        "rgb",
                        Integer.of(119),
                        Integer.of(136),
                        Integer.of(153)
                    )
                ).addLabel("summer").addLabel("spring")
            ),
            // dress (jeans , rgb (188 , 143 , 143) ) <@winter ,@fall , @summer , @spring >.
            Fact.of(
                Struct.of(
                    "dress",
                    Atom.of("jeans"),
                    Struct.of(
                        "rgb",
                        Integer.of(188),
                        Integer.of(143),
                        Integer.of(143)
                    )
                ).addLabel("winter").addLabel("fall").addLabel("summer").addLabel("spring")
            ),
        )
        // query of the user
        val goal = Struct.of(
            "dress",
            Var.of("Name"),
            Var.of("Color").addLabel("rgb(255,239,213)").addLabel("30")
        ).addLabel("winter")

        // custom unificator for this problem
        val unificator = LabelledUnificator.strict(
            shouldUnify = { term1, l1, term2, l2 ->
                if (term1.let { it is Struct && it.functor == "dress" && it.arity == 2 }
                    && term2.let { it is Struct && it.functor == "dress" && it.arity == 2 }) {
                    if (l1 == emptySet<Label>()) {
                        true
                    } else {
                        l1.all { it in l2 }
                    }
                } else if (term1.labels.size == 2 && term2 is Struct) {
                    val termParser = TermParser.withDefaultOperators()
                    val rgbReference = termParser.parseStruct(l2.elementAt(0).toString())
                    val threshold = termParser.parseInteger(l2.elementAt(1).toString())
                    val isSimilar = checkSimilarity(rgbReference, term2, threshold)
                    isSimilar
                } else {
                    true
                }
            },
            merge = { term1, l1, term2, _ ->
                if (term1.let { it is Struct && it.functor == "dress" && it.arity == 2 }
                    && term2.let { it is Struct && it.functor == "dress" && it.arity == 2 }) {
                    if (l1 == emptySet<Label>()) {
                        emptySet()
                    } else {
                        l1
                    }
                } else if (term1.labels.size == 2 && term2 is Struct) {
                    l1
                } else {
                    emptySet()
                }
            },
            stillValid = { _, _ -> true }
        )

        val solver = LabelledPrologSolverFactory.solverOf(
            unificator = unificator,
            staticKb = theory
        )

        val solution = solver.solveOnce(goal)
        assertTrue(solution.isYes)
    }
}
