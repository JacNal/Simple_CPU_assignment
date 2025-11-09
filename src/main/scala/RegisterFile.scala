import chisel3._

class RegisterFile extends Module {
  val io = IO(new Bundle {
    val aSel = Input(UInt(4.W))
    val bSel = Input(UInt(4.W))
    val dataToWrite = Input(UInt(32.W))
    val writeSel = Input(UInt(4.W))
    val writeEnable = Input(Bool())
    val outA = Output(UInt(32.W))
    val outB = Output(UInt(32.W))
    //Define the module interface here (inputs/outputs)
  })

  val registers = Reg(Vec(16, UInt(32.W)))

  io.outA := registers(io.aSel)
  io.outB := registers(io.bSel)

  when (io.writeEnable) {
    registers(io.writeSel) := io.dataToWrite
  }
  //Implement this module here

}