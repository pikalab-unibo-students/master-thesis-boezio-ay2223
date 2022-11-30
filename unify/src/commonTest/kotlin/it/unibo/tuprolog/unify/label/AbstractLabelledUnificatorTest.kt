package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.core.label.Labels
import it.unibo.tuprolog.solve.addLabel
import it.unibo.tuprolog.unify.AbstractUnificator
import kotlin.test.Test
import kotlin.test.assertEquals

class AbstractLabelledUnificatorTest {

    // custom Unificator
    val customAbstractUnificator = object : AbstractUnificator(){

        override fun checkTermsEquality(first: Term, second: Term): Boolean {
            TODO("Not yet implemented")
        }

        override fun shouldUnify(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Boolean {
            return labels1.any { it in labels2 } || (labels1.isEmpty() && labels2.isEmpty())
        }

        override fun merge(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Labels {
            return (labels1.filter { it in labels2 }.toSet() + labels2.filter { it in labels1 }.toSet())
        }

    }
    private val myUnificator = object : AbstractLabelledUnificator(customAbstractUnificator){}

    @Test
    fun testExampleWithoutLabels(){

        val f1 = Struct.of("f", Atom.of("a"), Var.of("B"))
        val f2 = Struct.of("f", Var.of("A"), Atom.of("b"))

        val unified = myUnificator.unify(f1, f2)
        val expected = Struct.of("f", Atom.of("a"), Atom.of("b"))

        assertEquals(expected, unified)

    }

    @Test
    fun testExampleWithLabelledArguments(){

        val f1 = Struct.of(
            "f",
            Atom.of("a").addLabel("x").addLabel("y"),
            Var.of("B")
        )
        val f2 = Struct.of(
            "f",
            Var.of("A").addLabel("x").addLabel("z"),
            Atom.of("b")
        )

        val unified = myUnificator.unify(f1, f2)
        val expected = Struct.of(
            "f",
            Atom.of("a").addLabel("x"),
            Atom.of("b")
        )

        assertEquals(expected, unified)

    }

    @Test
    fun testExampleWithLabelledArgsAndStruct(){

        val f1 = Struct.of(
            "f",
            Atom.of("a").addLabel("x").addLabel("y"),
            Var.of("B")
        ).addLabel("w").addLabel("v")
        val f2 = Struct.of(
            "f",
            Var.of("A").addLabel("x").addLabel("z"),
            Atom.of("b")
        ).addLabel("w")

        val unified = myUnificator.unify(f1, f2)
        val expected = Struct.of(
            "f",
            Atom.of("a").addLabel("x"),
            Atom.of("b")
        ).addLabel("w")

        assertEquals(expected, unified)
    }

    @Test
    fun testExampleWithComplexStruct(){

        val f1 = Struct.of(
            "f",
            Integer.of(1),
            Var.of("B").addLabel("x").addLabel("y"),
        ).addLabel("x").addLabel("y")
        val f2 = Struct.of(
            "f",
            Var.of("A"),
            Struct.of("g", Atom.of("b")).addLabel("x"),
        ).addLabel("y")

        val unified = myUnificator.unify(f1, f2)
        val expected = Struct.of(
            "f",
            Integer.of("1"),
            Struct.of("g", Atom.of("b")).addLabel("x")
        ).addLabel("y")

        assertEquals(expected, unified)

    }
}