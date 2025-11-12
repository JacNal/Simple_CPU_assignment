FIRRTL version 1.2.0
circuit ProgramCounter :
  module ProgramCounter :
    input clock : Clock
    input reset : UInt<1>
    input io_stop : UInt<1> @[src/main/scala/ProgramCounter.scala 4:14]
    input io_jump : UInt<1> @[src/main/scala/ProgramCounter.scala 4:14]
    input io_run : UInt<1> @[src/main/scala/ProgramCounter.scala 4:14]
    input io_programCounterJump : UInt<16> @[src/main/scala/ProgramCounter.scala 4:14]
    output io_programCounter : UInt<16> @[src/main/scala/ProgramCounter.scala 4:14]

    reg programCounterReg : UInt<16>, clock with :
      reset => (UInt<1>("h0"), programCounterReg) @[src/main/scala/ProgramCounter.scala 12:42]
    node _T = eq(io_run, UInt<1>("h0")) @[src/main/scala/ProgramCounter.scala 16:15]
    node _T_1 = eq(io_stop, UInt<1>("h1")) @[src/main/scala/ProgramCounter.scala 16:38]
    node _T_2 = or(_T, _T_1) @[src/main/scala/ProgramCounter.scala 16:27]
    node _T_3 = eq(io_jump, UInt<1>("h0")) @[src/main/scala/ProgramCounter.scala 19:18]
    node _programCounterReg_T = add(programCounterReg, UInt<16>("h1")) @[src/main/scala/ProgramCounter.scala 20:46]
    node _programCounterReg_T_1 = tail(_programCounterReg_T, 1) @[src/main/scala/ProgramCounter.scala 20:46]
    node _GEN_0 = mux(_T_3, _programCounterReg_T_1, io_programCounterJump) @[src/main/scala/ProgramCounter.scala 19:31 20:25 22:25]
    node _GEN_1 = mux(_T_2, programCounterReg, _GEN_0) @[src/main/scala/ProgramCounter.scala 16:50 17:23]
    io_programCounter <= programCounterReg @[src/main/scala/ProgramCounter.scala 14:21]
    programCounterReg <= mux(reset, UInt<16>("h0"), _GEN_1) @[src/main/scala/ProgramCounter.scala 12:{42,42}]
