package it.unibo.tuprolog.solve.label

interface Label {
    val value: Any
    fun equals(other: Label): Bool = this.value.equals(other.value)
}