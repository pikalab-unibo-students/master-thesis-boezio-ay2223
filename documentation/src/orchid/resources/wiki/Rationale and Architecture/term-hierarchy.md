---
---

Data and Knowledge are represented in 2P through _logic terms_.
Logic terms are essentially tree-based data structures coming with no predefined semantics.
More information on logic terms can be found on [Wikipedia](https://en.wikipedia.org/wiki/Term_(logic)).

Logic terms can be recursively defined by means of the following context-free grammar:

|-----------:|:----:|----------------------------------------------------------|
|     `Term` | `:=` | `Constant | Var | Struct`                                |
| `Constant` | `:=` | `Atom | Numeric`                                         |
|      `Var` | `:=` | all possible strings over an alphabet for variables      |
|     `Atom` | `:=` | all possible strings over an alphabet for atoms          |
|  `Numeric` | `:=` | `Integer | Real`                                         |
|  `Integer` | `:=` | all possible integer numbers                             |
|     `Real` | `:=` | all possible real numbers                                |
|   `Struct` | `:=` | `Functor(Argument)`                                      |
| `Argument` | `:=` | `Term | Term, Argument`                                  |
|  `Functor` | `:=` | all possible strings over an alphabet for function names |

where non-terminal symbols are uppercase.
There, `Var` is a shortcut for "variable" and `Struct` is a shortcut for "structure".
Structure are also known as "compound terms", whereas constants are also knwon as "atomic terms". 

Often, for practical reasons, the alphabets for atoms and variables both coincide with (some sub-set of) the [UTF-8](https://en.wikipedia.org/wiki/UTF-8) or [UTF-16](https://en.wikipedia.org/wiki/UTF-16) charsets. 
When this is the case, some convention is needed to distinguish among variables and atoms.

In 2P-Kt we adopt the Prolog syntactical convention which states that:
- variables are UTF-16 strings matching the regular expression `[_A-Z](_A-Za-z0-9)*`, i.e., strings of at least 1 alphanumeric character, possibly containing underscores, and starting by either an uppercase letter or an underscore
- atoms are UTF-16 _double/single_-quoted strings or strings matching the regular expression `[a-z](_A-Za-z0-9)*`, i.e., strings of at least 1 alphanumeric character, possibly containing underscores, and starting by either a lowercase letter
- function names are either _non-double_-quoted atoms or UTF-16 strings matching the regular expression `[+*/\^<>=~:.,;?!@#$&-]+`

Thus, for instance, the term: `f(X, y, g(1, 2.3), h(_, 'j'("k")))` must be interpreted as a `Struct`, whose functor is `f`, and having 4 arguments:
- the variable `X`
- the atom `y`
- the structure `g(1, 2.3)`, whose functor is `g`, which in turn has 2 arguments:
    * the integer number `1`
    * the real number `2.3` 
- the structure `h(_, 'j'("k"))`, whose functor is `h`, which in turn has 2 arguments:
    * the variable  `_`
    * the structure `'j'("k")`, which is equal to `j(k)`
    
## Logic terms in 2P-Kt

The base type for logic terms in 2P-Kt is [`Term`](/kotlindoc/it/unibo/tuprolog/core/term). 

As shown in the following diagram, the `Term` interface is the root of an articulate hierarchy of term types: 

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/terms-nofields.puml') | raw }}
<!--div-->

### Terms 

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/term.puml') | raw }}
<!--div-->

Terms is 2P-Kt are __immutable__ data structures.
This is why all subtypes of `Term` come with no `var` public property nor any public method provoking side effects.
This is deliberate as it simplify design and implementation.
Plus, this enables optimization through caching.
From the user-side, this means that once one you have checked a property (say `isGround`, for example) on a term, you are sure it will never change (for that term).

### Main functionalities of terms

Given a `Term` it is possible to:

- retrieve the [sequence](https://kotlinlang.org/docs/reference/sequences.html) of `Var`iables contained within the term through the `variables: Sequence<Var>` read-only property 

- check whether it is _ground_ (i.e., it does _not_ contain any variable) through the the `isGround: Boolean` read-only property

- graphically represent it as a `String`, through the `toString()` method

- clone it, in order to _refresh_ its variables, through the `freshCopy(): Term` method

- check if it is _identical_ to another term, through the `equals(other: Any): Boolean` method
    + of course, the semantics of `hashCode(): Int` is consistent w.r.t. `equals`

- check if it is **structurally** _equal_ to another term, through the `structurallyEquals(other: Term): Boolean` method

#### About terms representation as `String`

The `Term.toString()` method prodeces a raw, debug-friendly representation of terms where:

- all compound term are in their canonical form, e.g., `'functor'(arg1.toString(), ..., argN.toString())`
    * where apexes may be omitted in simpler cases

- except lists which are represented in square brackets, e.g., `[item1.toString(), ..., itemN.toString()]`

- except sets which are represented within braces, e.g., `{item1.toString(), ..., itemN.toString()}`

- except tuples which are represented within parentheses, e.g., `(item1.toString(), ..., itemN.toString())`

- except clauses which are represented in their canonical form, e.g. `OptionalHead :- Body`
    * where the `OptionalHead` is omitted in case of directives

- all atoms are represented are bare strings, e.g., `'atom'`
    * where apexes may be omitted in simpler cases
   
- all numbers are represented though their natural string representation as decimal integers/reals, e.g., `1` or `2.3`

- all variables are represented though their _complete_ name, e.g., `VariableName_<some unique index here>`

More details about the actual string representations of subtypes are provided in the following sections.

The takeaway here is that the `Term.toString()` is __not__ aimed at presenting terms to teh users.

#### About terms identity

Terms identity is recursively defined.
Roughly speaking the identity among two terms holds if and only if:

1. the type of the two terms is equal, and

0. they are both variables, and they have the same _complete_ name, or

0. they are both integers, and they have the same value, or

0. they are both real numbers, and they have the same value, or

0. they are both structures, and they have the same functor and arity, and their arguments are recursively equal.

So, for instance, the terms `f(x)` and `f(x)` are identical, whereas the terms `f(X_1)` and `f(X_2)` are different.
Similarly, the terms `f(1)` and `f(1.0)` are different as well.

However, more details are provided in the following sections.

#### About terms _structural_ equality

Terms _structural_ equality is recursively defined.
Roughly speaking the _structural_ equality among two terms holds if and only if:

1. they are both variables, or

0. they are both numbers and their numeric value is equal, or

0. they are both structures, and they have the same functor and arity, and their arguments are recursively _structurally_ equal.

So, for instance, the terms `f(x)` and `f(x)` are _structurally_ equal, as well as the terms `f(X_1)` and `f(X_2)`.
Similarly, the terms `f(1)` and `f(1.0)` are _structurally_ equal as well.

More details are provided in the following sections.

#### About fresh copies of terms

A common use case in 2P-Kt is to produce a fresh copy of a term.

Let `t` be a `Term`, then a fresh copy of `t` is another term `t'` which is structurally equal to `t` but differs in the variables it contains.
In particular, the two terms differ because each variable `V` possibly occurring in `t` is replaced in `t'` by another variable `V'` whose name is equal to `V` but whose _complete_ name is different than `V`'s.
Notice that if variable `V` occurs several times in `t`, than variable `V'` occurs as many times in `t'`.
Of course, if `t` is ground, then `t'` is identical to `t`.

So, for instance, by refreshing the term `f(X_1, g(X_1), Y_1)` one may obtain the a term like `f(X_2, g(X_2), Y_2)`.

### Instantiation of terms

By default, terms are instantiated through static __factory__ methods in the form `<Subtype>.<factoryMethod>(<args>)`, which are described in details in the following sections.
This is necessary since the actual implementation classes of the aforementioned interfaces are __not__ part of the public API of 2P-Kt.

Of course, there is no way to create an instance of `Term` since this is just a super type for all terms.
Only subtypes can actually be instantiated. 
So, for instance, one may create 
- a structure through the `Struct.of(functor: String, varargs args: Term)` method, 
- a variable, through the `Var.of(name: String)` or `Var.anonymous()` methods,
- an atom through the `Atom.of(value: String)` method
- a number through the `Numeric.of(value: String)` method (or the `Integer.of`, or `Real.of` ones)
- etc.

Details about such factory methods are provided below. 

Static factories are not the only means to instantiate terms. 
The notion of [`Scope`](./variables-and-scopes.md) is introduced to serve a similar -- yet more articulated -- use case.

### Variables 

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/var.puml') | raw }}
<!--div-->

[`Var`iables](/kotlindoc/it/unibo/tuprolog/core/var/) are `Term`s acting as placeholders of other `Term`s.
They are identified by a _complete_ name, that is, a string in the form:
```
<Name>_<Suffix> 
``` 
(with no angular parenthesis).
The first section the variable (simple) name, whereas the last section is the variable suffix.
The suffix is guaranteed to make the variable complete name unique.
Furthermore, the suffix is guaranteed to be a string matching the `[_A-Z](_A-Za-z0-9)*` regex.
Accordingly, two variables may have the same `name`, but they are considered identical only if their suffixes are equal as well.

If a variable simple name is `"_"`, then the variable is said to be _anonymous_.
Furthermore, if a variable simple name matches the `[_A-Z](_A-Za-z0-9)*` regex, then the variable is said to be _well-formed_.

Non-well-formed variables can be instantiated in 2P-Kt.
However, they are graphically represented as strings through the ad-hoc notation exemplified below:
```
`<Non-well-formed Name>_<Suffix>`
```

### Main functionalities of variables

Given a `Var`iable it is possible to:

- retrieve its unique, _complete_ name through the `completeName: String` property

- retrieve its simple name through the `name: String` property

- check if it is _anonymous_, through the `isAnonymous: Boolean` property

- check if its name is _well-formed_, through the `isWellFormed: Boolean` property  

### Instantiation of variables

Variables can only be instantiated through two static factory methods, namely `Var.of` and `Var.anonymous`.
In both cases, the instantiating code has no way to setup the suffix of the to-be-created variable.
In other words, the aforementioned factory methods are guaranteed to produce a fresh variable which is different (w.r.t. `Term.equals`)
than any other variable which was created before it.

In particular:
- `Var.of(name: String)` creates a new instance of `Var` whose name is `name` and whose suffix is non-deterministically chosen by 2P-Kt
- `Var.anonymous()` creates a new instance of `Var` whose name is `"_"` and whose suffix is non-deterministically chosen by 2P-Kt

In some cases, developers may be in need of re-using previously instantiated variables.
In such situation, they may either store `Var` instances through Kotlin variables or use [`Scope`s](/kotlindoc/it/unibo/tuprolog/core/scope/) instead.

### Constants 

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/constant.puml') | raw }}
<!--div-->

[`Constant`s](/kotlindoc/it/unibo/tuprolog/core/constant/) are ground, non-compound terms characterised by a _value_.
Such value can be retrieved by users through the `value: Any` property.

Constants can be either numbers or atoms (i.e. strings).
They cannot be instantiated directly, as they must be instantiated through their specific interfaces.

### Numbers 

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/numeric.puml') | raw }}
<!--div-->

Instances of the [`Numeric`](/kotlindoc/it/unibo/tuprolog/core/numeric/) interface are a particular sorts of constant
whose value is either an [`Integer`](/kotlindoc/it/unibo/tuprolog/core/integer/) or a [`Real`](/kotlindoc/it/unibo/tuprolog/core/real/) number.

Behind the scenes, 2P-Kt employs `BigInteger`s and `BigDecimal`s to reify numbers.
Briefly speaking, this means:
- integers are virtually unlimited in length, thus no over- or under-flow exception may ever arise
- decimal computations may be performed with arbitrary (and configurable) precision

Of course, both the `BigInteger` and `BigDecimal` classes expose conversion methods to explicitly convert their instances into
ordinary Kotlin numeric types such as `Int`, `Long`, `Short`, `Byte`, `Float`, or `Double`.
Inverse conversion methods are available as well, usually as static factory methods such as `BigInteger.of` or `BigDecimal.of`. 

### Main functionalities of numbers

Given a `Numeric` object it is possible to:
- retrieve the corresponding integer value, through the `intValue: BigInteger`
- retrieve the corresponding real value, through the `decimalValue: BigDecimal`

Of course, the most adequate type is used behind the scenes.
In fact, the `value: Any` property always returns a value having the most adequate type for representing the current number.

Finally, given a couple of `Numeric` objects, it is possible to compare them by value through the `compareTo(other: Numeric): Int` method.
As a side note, it is worth to be mentioned that -- thanks to [Kotlin's Operator Overloading](https://kotlinlang.org/docs/reference/operator-overloading.html) feature -- `Numeric` objects can be
compared in Kotlin through ordinary comparison operators such as `>=`, `>`, `<`, `=<`.

### Instantiation of numbers

`Numeric` terms can be created through the many static, factory methods in the form `Numeric.of`, other than the static factories in 
`Real` and `Integer`.

In particular:
- `Numeric.of(value: TYPE): Integer` where `TYPE` ∈ `{Int, Long, Short, Byte, BigInteger}` creates a novel instance of `Integer`
    + notice that these are just aliases for `Integer.of(value: TYPE): Integer`
- `Numeric.of(value: TYPE): Real` where `TYPE` ∈ `{Float, Double, BigDecimal}` creates a novel instance of `Real`
    + notice that these are just aliases for `Real.of(value: TYPE): Real`
- `Numeric.of(value: String): Numeric` creates a novel instance of `Numeric` by parsing the provided string and by instantiating the most appropriate specific type
    + notice that this method just tries to parse a string through `Integer.of(value: String): Integer` and falls back on
    `Real.of(value: String): Real` in case of failure
- `Integer.of(value: String, radix: Int): Integer` creates a novel instance of `Integer` by parsing the provided string as a number whose base is `radix`

### Structures 

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/struct.puml') | raw }}
<!--div-->

[`Struct`ures](/kotlindoc/it/unibo/tuprolog/core/struct/) are `Term`s _composed_ by other `Term`s.
They are characterised by a _functor_ -- which states thir name -- and an _arity_---which states how many (sub-)terms (or arguments) compose them.

### Atoms 

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/atom.puml') | raw }}
<!--div-->

### Booleans

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/truth.puml') | raw }}
<!--div-->

### Indicators

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/indicator.puml') | raw }}
<!--div-->

### Collections

#### Lists

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/list.puml') | raw }}
<!--div-->

#### Tuples

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/tuple.puml') | raw }}
<!--div-->

#### Sets

<!--div style="width: 100%; overflow: auto; background-color:LightGray" -->
{{ load('assets/diagrams/set.puml') | raw }}
<!--div-->

### Clauses

#### Rules

#### Facts

#### Directives