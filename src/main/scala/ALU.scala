import chisel3._
import chisel3.util._

class ALU extends Module {
  val io = IO(new Bundle {
    val operand1 = Input(UInt(32.W))
    val operand2 = Input(UInt(32.W))
    val sel = Input(UInt(2.W))
    val result = Output(UInt(32.W))
    val nzp = Output(UInt(3.W))
  })

  object ALUOp {
    val add = "b00".U(2.W)
    val sub = "b01".U(2.W)
    val and = "b10".U(2.W)
  }


  val ALUResult = WireDefault(0.U(32.W))

  switch(io.sel) {
    is(ALUOp.add) { ALUResult := io.operand1 + io.operand2}
    is(ALUOp.sub) { ALUResult := io.operand1 - io.operand2}
    is(ALUOp.and) { ALUResult := io.operand1 & io.operand2}
  }

  io.result := ALUResult

  val nzpReg = RegInit(0.U(3.W))

  val isZero = ALUResult === 0.U
  val isNeg  = ALUResult(31)
  val isPos  = !isNeg & !isZero

  when(io.sel === ALUOp.add || io.sel === ALUOp.sub) {
    nzpReg := Cat(isNeg, isZero, isPos)
  }

  io.nzp := nzpReg

  //Implement this module here

}