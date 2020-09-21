package it.unibo.tuprolog.solve.stdlib

import it.unibo.tuprolog.core.operators.OperatorSet
import it.unibo.tuprolog.solve.library.AliasedLibrary
import it.unibo.tuprolog.solve.library.Library
import it.unibo.tuprolog.solve.primitive.PrimitiveWrapper
import it.unibo.tuprolog.solve.stdlib.function.*
import it.unibo.tuprolog.solve.stdlib.primitive.*
import it.unibo.tuprolog.solve.stdlib.primitive.Float as FloatPrimitive

object CommonBuiltins : AliasedLibrary by Library.aliased(
    alias = "prolog.lang",
    operatorSet = OperatorSet.DEFAULT,
    theory = CommonRules.theory,
    primitives = sequenceOf<PrimitiveWrapper<*>>(
        Abolish,
        Arg,
        ArithmeticEqual,
        ArithmeticGreaterThan,
        ArithmeticGreaterThanOrEqualTo,
        ArithmeticLowerThan,
        ArithmeticLowerThanOrEqualTo,
        ArithmeticNotEqual,
        Assert,
        AssertA,
        AssertZ,
        Atom,
        AtomChars,
        AtomCodes,
        AtomConcat,
        AtomLength,
        Atomic,
        Between,
        Callable,
        CharCode,
        Clause,
        Compound,
        CopyTerm,
        CurrentOp,
        CurrentPrologFlag,
        EnsureExecutable,
        FindAll,
        FloatPrimitive,
        Functor,
        Ground,
        Halt,
        Integer,
        Is,
        Natural,
        NewLine,
        NonVar,
        NotUnifiableWith,
        Number,
        Op,
        Repeat,
        Retract,
        RetractAll,
        Sleep,
        TermGreaterThan,
        TermGreaterThanOrEqualTo,
        TermIdentical,
        TermLowerThan,
        TermLowerThanOrEqualTo,
        TermNotIdentical,
        TermNotSame,
        TermSame,
        UnifiesWith,
        Univ,
        Var,
        Write
    ).map { it.descriptionPair }.toMap(),
    functions = sequenceOf(
        AbsoluteValue,
        Addition,
        ArcTangent,
        BitwiseAnd,
        BitwiseComplement,
        BitwiseLeftShift,
        BitwiseOr,
        BitwiseRightShift,
        Ceiling,
        Cosine,
        Exponential,
        Exponentiation,
        ToFloat,
        FloatFractionalPart,
        FloatIntegerPart,
        FloatingPointDivision,
        Floor,
        IntegerDivision,
        Modulo,
        Multiplication,
        NaturalLogarithm,
        Remainder,
        Round,
        Sign,
        SignReversal,
        Sine,
        SquareRoot,
        Subtraction,
        Truncate
    ).map { it.descriptionPair }.toMap()
)
