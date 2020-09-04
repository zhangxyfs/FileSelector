package com.z7dream.lib.selector.utils.callback

object Callback {
    interface Callback<IN1>{
        fun event(var1: IN1)
    }

    interface Callback2<IN1, IN2> {
        fun event(var1: IN1, var2: IN2)
    }

    interface Callback10<IN1, IN2, IN3> : Callback2<IN1, IN2> {
        fun event1(var1: IN3)
    }
}