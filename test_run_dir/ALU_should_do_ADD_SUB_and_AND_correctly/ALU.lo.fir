FIRRTL version 1.2.0
circuit ALU :
  module ALU :
    input clock : Clock
    input reset : UInt<1>
    input io_operand1 : UInt<32> @[src/main/scala/ALU.scala 5:14]
    input io_operand2 : UInt<32> @[src/main/scala/ALU.scala 5:14]
    input io_sel : UInt<2> @[src/main/scala/ALU.scala 5:14]
    output io_result : UInt<32> @[src/main/scala/ALU.scala 5:14]
    output io_nzp : UInt<3> @[src/main/scala/ALU.scala 5:14]

    node _T = eq(UInt<2>("h0"), io_sel) @[src/main/scala/ALU.scala 22:18]
    node _ALUResult_T = add(io_operand1, io_operand2) @[src/main/scala/ALU.scala 23:46]
    node _ALUResult_T_1 = tail(_ALUResult_T, 1) @[src/main/scala/ALU.scala 23:46]
    node _T_1 = eq(UInt<2>("h1"), io_sel) @[src/main/scala/ALU.scala 22:18]
    node _ALUResult_T_2 = sub(io_operand1, io_operand2) @[src/main/scala/ALU.scala 24:46]
    node _ALUResult_T_3 = tail(_ALUResult_T_2, 1) @[src/main/scala/ALU.scala 24:46]
    node _T_2 = eq(UInt<2>("h2"), io_sel) @[src/main/scala/ALU.scala 22:18]
    node _ALUResult_T_4 = and(io_operand1, io_operand2) @[src/main/scala/ALU.scala 25:46]
    node _GEN_0 = mux(_T_2, _ALUResult_T_4, UInt<32>("h0")) @[src/main/scala/ALU.scala 22:18 20:30 25:31]
    node _GEN_1 = mux(_T_1, _ALUResult_T_3, _GEN_0) @[src/main/scala/ALU.scala 22:18 24:31]
    node _GEN_2 = mux(_T, _ALUResult_T_1, _GEN_1) @[src/main/scala/ALU.scala 22:18 23:31]
    reg nzpReg : UInt<3>, clock with :
      reset => (UInt<1>("h0"), nzpReg) @[src/main/scala/ALU.scala 30:23]
    node ALUResult = _GEN_2 @[src/main/scala/ALU.scala 20:30]
    node isZero = eq(ALUResult, UInt<1>("h0")) @[src/main/scala/ALU.scala 32:26]
    node isNeg = bits(ALUResult, 31, 31) @[src/main/scala/ALU.scala 33:25]
    node _isPos_T = eq(isNeg, UInt<1>("h0")) @[src/main/scala/ALU.scala 34:16]
    node _isPos_T_1 = eq(isZero, UInt<1>("h0")) @[src/main/scala/ALU.scala 34:25]
    node isPos = and(_isPos_T, _isPos_T_1) @[src/main/scala/ALU.scala 34:23]
    node _T_3 = eq(io_sel, UInt<2>("h0")) @[src/main/scala/ALU.scala 36:15]
    node _T_4 = eq(io_sel, UInt<2>("h1")) @[src/main/scala/ALU.scala 36:39]
    node _T_5 = or(_T_3, _T_4) @[src/main/scala/ALU.scala 36:29]
    node nzpReg_hi = cat(isNeg, isZero) @[src/main/scala/ALU.scala 37:18]
    node _nzpReg_T = cat(nzpReg_hi, isPos) @[src/main/scala/ALU.scala 37:18]
    node _GEN_3 = mux(_T_5, _nzpReg_T, nzpReg) @[src/main/scala/ALU.scala 36:54 37:12 30:23]
    io_result <= ALUResult @[src/main/scala/ALU.scala 28:13]
    io_nzp <= nzpReg @[src/main/scala/ALU.scala 40:10]
    nzpReg <= mux(reset, UInt<3>("h0"), _GEN_3) @[src/main/scala/ALU.scala 30:{23,23}]
