package it.unibo.tuprolog.solve.libs.oop.impl

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.solve.libs.oop.NullRef
import it.unibo.tuprolog.solve.libs.oop.Result
import it.unibo.tuprolog.solve.libs.oop.invoke
import kotlin.collections.List

@Suppress("UNCHECKED_CAST")
internal object NullRefImpl : NullRef, Atom by Atom.of(NullRef.NULL_FUNCTOR) {

    override val isConstant: Boolean
        get() = true

    override fun freshCopy(): Atom = this

    override fun freshCopy(scope: Scope): Atom = this

    override fun <T : Term> `as`(): T = this as T

    override fun <T : Term> castTo(): T = this as T

    override fun apply(substitution: Substitution): Term = this

    override fun apply(substitution: Substitution, vararg substitutions: Substitution): Term = this

    override fun get(substitution: Substitution, vararg substitutions: Substitution): Term = this

    override fun <T> accept(visitor: TermVisitor<T>): T =
        visitor.visit(this)
}