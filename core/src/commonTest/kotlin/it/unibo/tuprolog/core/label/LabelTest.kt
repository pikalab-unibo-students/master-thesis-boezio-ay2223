package it.unibo.tuprolog.core.label

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.solve.*
import kotlin.test.Test

class LabelTest {

    @Test
    fun labelsExample() {
        val term: Struct = Struct.of("f", Atom.of("a"))

        println(term)
        println(term.labels)
        println(term.format(LabelAwareTermFormatter))

        val term1: Struct = term.addLabel("pippo")
        println(term1)
        println(term1.labels)
        println(term1.format(LabelAwareTermFormatter))

        println(term == term1)
        println(term.equalsWithLabels(term1))

    }

    @Test
    fun testChangeLabelsInplace(){

        val term: Struct = Struct.of("f", Atom.of("a"))
        print("Term without labels: ")
        println(term.format(LabelAwareTermFormatter))

        // add label
        term.addLabel("x")
        print("Term with labels: ")
        println(term.format(LabelAwareTermFormatter))

    }
}