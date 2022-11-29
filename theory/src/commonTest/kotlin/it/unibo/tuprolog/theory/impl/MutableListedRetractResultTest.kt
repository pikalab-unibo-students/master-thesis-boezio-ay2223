package it.unibo.tuprolog.theory.impl

import it.unibo.tuprolog.collections.impl.Factories
import it.unibo.tuprolog.theory.PrototypeRetractResultTest
import kotlin.test.Test

internal class MutableListedRetractResultTest {

    private val prototype: PrototypeRetractResultTest =
        PrototypeRetractResultTest(
            Factories::emptyMutableListedTheory,
            Factories::mutableListedTheoryOf
        )

    @Test
    fun successTheoryCorrect() {
        prototype.successTheoryCorrect()
    }

    @Test
    fun successClausesListCorrect() {
        prototype.successClausesListCorrect()
    }

    @Test
    fun successFirstClauseCorrect() {
        prototype.successFirstClauseCorrect()
    }

    @Test
    fun successFirstClauseWithEmptyClauseListThrowsException() {
        prototype.successFirstClauseWithEmptyClauseListThrowsException()
    }

    @Test
    fun failTheoryCorrect() {
        prototype.failTheoryCorrect()
    }
}
