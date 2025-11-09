import chisel3._
import chisel3.util._

class ALU extends Module {
  val io = IO(new Bundle {
    val operand1 = Input(UInt(32.W))
    val operand2 = Input(UInt(32.W))
    val sel = Input(UInt(2.W))
    val result = Output(UInt(32.W))
  })

  object ALUOp {
    val add = "b00".U(2.W)
    val sub = "b01".U(2.W)
    val and = "b10".U(2.W)
  }

  io.result := 0.U
  switch(io.sel) {
    is(ALUOp.add) { io.result := io.operand1 + io.operand2}
    is(ALUOp.sub) { io.result := io.operand1 - io.operand2}
    is(ALUOp.and) { io.result := io.operand1 & io.operand2}
  }



  //Implement this module here

}