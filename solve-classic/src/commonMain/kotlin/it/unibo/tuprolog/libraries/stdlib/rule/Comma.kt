package it.unibo.tuprolog.libraries.stdlib.rule

import it.unibo.tuprolog.core.Scope
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.rule.RuleWrapper
import it.unibo.tuprolog.solve.ExecutionContextImpl

import kotlin.collections.List as KtList
import kotlin.collections.listOf as ktListOf

object Comma : RuleWrapper<ExecutionContextImpl>(",", 2) {
    override val Scope.head: KtList<Term>
        get() = ktListOf(varOf("A"), varOf("B"))

    override val Scope.body: Term
        get() = tupleOf(varOf("A"), varOf("B"))
}