package it.unibo.tuprolog.core.label

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.format
import it.unibo.tuprolog.core.Struct
import kotlin.test.Test

class LabelTest {

    @Test
    fun testChangeLabelsInplace() {
        val term: Struct = Struct.of("f", Atom.of("a"))
        print("Term without labels: ")
        println(term.format(LabelAwareTermFormatter))

        // add label
        term.addLabel("x")
        print("Term with labels: ")
        println(term.format(LabelAwareTermFormatter))
    }
}
