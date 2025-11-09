import chisel3._
import chisel3.util._

class ControlUnit extends Module {
  val io = IO(new Bundle {
    val opCode = Input(UInt(4.W))
    val controlBits = Input(UInt(3.W))

    //OUTPUTS = SELECTIONS AND FLAGS
    val ALUSel = Output(UInt(2.W))
    val registerWriteEnable = Output(Bool())
    val memoryWriteEnable = Output(Bool())
    val pcJump = Output(Bool())
    val useIMM = Output(Bool())
    //Define the module interface here (inputs/outputs)
  })

  //Wire bundle all outs
  val default = Wire(new Bundle {
    val ALUSel = UInt(2.W)
    val registerWriteEnable = Bool()
    val memoryWriteEnable = Bool()
    val pcJump = Bool()
    val useIMM = Bool()
  })

  //set safe defaults
  default.ALUSel := "b00".U(2.W)
  default.registerWriteEnable := false.B
  default.memoryWriteEnable := false.B
  default.pcJump := false.B
  default.useIMM := false.B

  val baseline = default

  switch(io.opCode) {
    is("b0001".U) {
      baseline.ALUSel := "b00".U(2.W)
      baseline.registerWriteEnable := true.B
      when (io.controlBits(2) === 0.U) {
        baseline.useIMM := false.B
      }.elsewhen (io.controlBits(2) === 1.U) {
        baseline.useIMM := true.B
      }
    }

  }

  //Set outs

  //Implement this module here

}