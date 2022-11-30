package it.unibo.tuprolog.core.label

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.solve.*
import kotlin.test.Test

class LabelTest {

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