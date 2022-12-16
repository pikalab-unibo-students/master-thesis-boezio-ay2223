package it.unibo.tuprolog.core.label

internal data class LabelImpl(override val value: Any) : Label {
    override fun toString(): String {
        return "@$value"
    }
}
