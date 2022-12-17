package it.unibo.tuprolog.core.label

interface Label {
    val value: Any
    fun equals(other: Label): Boolean = this.value.equals(other.value)

    companion object {
        fun of(value: Any): Label = LabelImpl(value)
    }
}
