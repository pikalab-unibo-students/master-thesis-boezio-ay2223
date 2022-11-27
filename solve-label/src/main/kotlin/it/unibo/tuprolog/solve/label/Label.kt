package it.unibo.tuprolog.solve.label

interface Label {
    val value: Any
    fun equals(other: Label): Boolean = this.value.equals(other.value)
}