package com.qsteam.afm.util.math.shapes;

public interface IBooleanFunction {

    IBooleanFunction FALSE         = (a, b) -> false;
    IBooleanFunction NOR           = (a, b) -> !a && !b;
    IBooleanFunction ONLY_SECOND   = (a, b) -> b && !a;
    IBooleanFunction NOT_FIRST     = (a, b) -> !a;
    IBooleanFunction ONLY_FIRST    = (a, b) -> a && !b;
    IBooleanFunction NOT_SECOND    = (a, b) -> !b;
    IBooleanFunction XOR           = (a, b) -> a != b;
    IBooleanFunction NAND          = (a, b) -> !a || !b;
    IBooleanFunction AND           = (a, b) -> a && b;
    IBooleanFunction XNOR          = (a, b) -> a == b;
    IBooleanFunction SECOND        = (a, b) -> b;
    IBooleanFunction IMPLY         = (a, b) -> !a || b; // Материальная импликация (A -> B)
    IBooleanFunction FIRST         = (a, b) -> a;
    IBooleanFunction REVERSE_IMPLY = (a, b) -> a || !b; // Обратная импликация (A <- B)
    IBooleanFunction OR            = (a, b) -> a || b;
    IBooleanFunction TRUE          = (a, b) -> true;

    boolean apply(boolean a, boolean b);

}