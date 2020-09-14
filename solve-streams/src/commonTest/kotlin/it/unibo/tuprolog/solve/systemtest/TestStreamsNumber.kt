package it.unibo.tuprolog.solve.systemtest

import it.unibo.tuprolog.solve.SolverFactory
import it.unibo.tuprolog.solve.StreamsSolverFactory
import it.unibo.tuprolog.solve.TestNumber
import kotlin.test.Test

class TestStreamsNumber : TestNumber, SolverFactory by StreamsSolverFactory {

    private val prototype = TestNumber.prototype(this)

    @Test
    override fun testBasicNum() {
        prototype.testBasicNum()
    }

    @Test
    override fun testDecNum() {
        prototype.testDecNum()
    }

    @Test
    override fun testNegNum() {
        prototype.testNegNum()
    }

    @Test
    override fun testLetterNum() {
        prototype.testLetterNum()
    }

    @Test
    override fun testXNum() {
        prototype.testXNum()
    }
}
