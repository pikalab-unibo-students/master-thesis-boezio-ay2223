package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Integer
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Var
import it.unibo.tuprolog.core.label.addLabel
import kotlin.test.Test
import kotlin.test.assertEquals

class LabelledUnificatorTest {

    private val myUnificator = LabelledUnificator.strict(
        shouldUnify = { _, l1, _, l2 ->
            l1.any { it in l2 } || (l1.isEmpty() && l2.isEmpty())
        },
        merge = { _, l1, _, l2 ->
            l1.filter { it in l2 }.toSet() + l2.filter { it in l1 }.toSet()
        }
    )

    @Test
    fun testExampleWithoutLabels() {
        // f(a<>,B<>)<>
        // f(A<>,b<>)<>

        val f1 = Struct.of("f", Atom.of("a"), Var.of("B"))
        val f2 = Struct.of("f", Var.of("A"), Atom.of("b"))

        val unified = myUnificator.unify(f1, f2)
        // f(a<>,b<>)<>
        val expected = Struct.of("f", Atom.of("a"), Atom.of("b"))

        assertEquals(expected, unified)
    }

    @Test
    fun testExampleWithLabelledArguments() {
        // f(a<@x,@y>,B<>)<>
        // f(A<@x,@z>,b<>)<>

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
        // f(a<@x>,b<>)<>
        val expected = Struct.of(
            "f",
            Atom.of("a").addLabel("x"),
            Atom.of("b")
        )

        assertEquals(expected, unified)
    }

    @Test
    fun testExampleWithLabelledArgsAndStruct() {
        // f(a<@x,@y>,B<>)<@w,@v>
        // f(A<@x,@z>,b<>)<@w>

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
        // f(a<@x>,b<>)<@w>
        val expected = Struct.of(
            "f",
            Atom.of("a").addLabel("x"),
            Atom.of("b")
        ).addLabel("w")

        assertEquals(expected, unified)
    }

    @Test
    fun testExampleWithComplexStruct() {
        // f(1<>,B<@x,@y>)<@x,@y>
        // f(A<>,g(b<>)<@x>)<@x,@y>

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
        // f(1<>,g(b<>)<@x>)<@y>
        val expected = Struct.of(
            "f",
            Integer.of("1"),
            Struct.of("g", Atom.of("b")).addLabel("x")
        ).addLabel("y")

        assertEquals(expected, unified)
    }
}
