package it.unibo.tuprolog.unify.label

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.core.label.Labels
import it.unibo.tuprolog.solve.LabelAwareTermFormatter
import it.unibo.tuprolog.solve.addLabel
import it.unibo.tuprolog.solve.applyWithLabel
import it.unibo.tuprolog.solve.labels
import it.unibo.tuprolog.unify.AbstractUnificator
import kotlin.test.Test

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
    private val myUnificator = object : AbstractLabelledUnificator(customAbstractUnificator) {
        override fun shouldUnify(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Boolean {
            return labels1.any { it in labels2 } || (labels1.isEmpty() && labels2.isEmpty())
        }

        override fun merge(term1: Term, labels1: Labels, term2: Term, labels2: Labels): Labels {
            return (labels1.filter { it in labels2 }.toSet() + labels2.filter { it in labels1 }.toSet())
        }
    }

    @Test
    fun labelsExampleWithUnification() {


        val f1: Struct = Struct.of("f", Integer.of(1)).addLabel("x").addLabel("y")
        val f2: Struct = Struct.of("f", Var.of("A")).addLabel("z").addLabel("x")

        print("First Term: ")
        println(f1.format(LabelAwareTermFormatter))

        print("Second Term: ")
        println(f2.format(LabelAwareTermFormatter))

        val mgu = myUnificator.mgu(f1, f2)
        println(mgu.labels)

        val unified = myUnificator.unify(f1, f2)
        println(unified?.format(LabelAwareTermFormatter))

        val unified2 = f2.applyWithLabel(mgu)
        println(unified2.format(LabelAwareTermFormatter))
    }

    @Test
    fun labelsExampleWithLabelledVars() {

        val f1: Struct = Struct.of(
            "f",
            Atom.of("a"),
            Struct.of("g", Atom.of("b")).addLabel("x").addLabel("y"),
        )
        val f2: Struct = Struct.of(
            "f",
            Var.of("A"),
            Var.of("B").addLabel("x")
        )

        println("Formatted terms")
        println(f1.format(LabelAwareTermFormatter))
        println(f2.format(LabelAwareTermFormatter))

        val mgu = myUnificator.mgu(f1, f2)
        println(mgu.labels)

        val unified = myUnificator.unify(f2, f1)
        println(unified?.format(LabelAwareTermFormatter))

        val unified2 = f2.applyWithLabel(mgu)
        println(unified2.format(LabelAwareTermFormatter))
    }

    @Test
    fun labelsExampleWithLabelledVarsAndStructs() {

        val f1: Struct = Struct.of(
            "f",
            Atom.of("a"),
            Struct.of("g", Atom.of("b").addLabel("x").addLabel("y")),
        ).addLabel("x").addLabel("y")

        val f2: Struct = Struct.of(
            "f",
            Var.of("A"),
            Var.of("B").addLabel("x")
        ).addLabel("x").addLabel("y").addLabel("z")

        println("Formatted terms")
        println(f1.format(LabelAwareTermFormatter))
        println(f2.format(LabelAwareTermFormatter))

        val mgu = myUnificator.mgu(f1, f2)
        println(mgu.labels)

        val unified = myUnificator.unify(f1, f2)
        println(unified?.format(LabelAwareTermFormatter))

        val unified2 = f2.applyWithLabel(mgu)
        println(unified2.format(LabelAwareTermFormatter))
    }

    @Test
    fun testVariableMgu(){

        val variable = Var.of("X")
        val integer = Integer.of(2)

        val mgu = myUnificator.mgu(variable, integer)
        print(mgu)
    }
}