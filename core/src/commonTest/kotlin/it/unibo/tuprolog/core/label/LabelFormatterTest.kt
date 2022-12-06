package it.unibo.tuprolog.core.label


import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.format
import kotlin.test.Test

class LabelFormatterTest {

    @Test
    fun testFormatterLabelledStruct() {
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
    fun testFormatterLabelledStructAndLabelledArgs() {
        val term: Struct = Struct.of(
            "f",
            Atom.of("a").addLabel("x")
        ).addLabel("y")

        println(term)
        println(term.labels)
        println(term.format(LabelAwareTermFormatter))
    }
}
