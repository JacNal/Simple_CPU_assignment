import chisel3._
import chisel3.util._

class ControlUnit extends Module {
  val io = IO(new Bundle {
    val opCode = Input(UInt(4.W))
    val controlBits = Input(UInt(3.W))
    val nzp = Input(UInt(3.W))

    //OUTPUTS = SELECTIONS AND FLAGS
    val ALUSel = Output(UInt(2.W))
    val registerWriteEnable = Output(Bool())
    val memoryWriteEnable = Output(Bool())
    val pcJump = Output(Bool())
    val useIMM = Output(Bool())
    val halt = Output(Bool())
    //Define the module interface here (inputs/outputs)
  })

  //Wire bundle all outs
  val default = Wire(new Bundle {
    val ALUSel = UInt(2.W)
    val registerWriteEnable = Bool()
    val memoryWriteEnable = Bool()
    val pcJump = Bool()
    val useIMM = Bool()
    val halt = Bool()
  })

  //set safe defaults
  default.ALUSel := "b00".U(2.W)
  default.registerWriteEnable := false.B
  default.memoryWriteEnable := false.B
  default.pcJump := false.B
  default.useIMM := false.B
  default.halt := false.B


  val baseline = default

  switch(io.opCode) {
    is("b0001".U) {
      baseline.ALUSel := "b00".U(2.W)
      baseline.registerWriteEnable := true.B
      baseline.useIMM := io.controlBits(2)
    }
    is("b1001".U) {
      baseline.ALUSel := "b01".U(2.W)
      baseline.registerWriteEnable := true.B
      baseline.useIMM := true.B
    }
    is("b0010".U) {
      baseline.ALUSel := "b10".U(2.W)
      baseline.registerWriteEnable := true.B
    }
    is("b0100".U) {
      baseline.registerWriteEnable := true.B
      baseline.useIMM := io.controlBits(2)
    }
    is("b1100".U) {
      baseline.memoryWriteEnable := true.B
    }
    is("b0101".U) {
      baseline.memoryWriteEnable := true.B
      baseline.ALUSel := "b00".U(2.W)
      baseline.useIMM := true.B
    }
    is("b0110".U) {
      baseline.pcJump := true.B
    }
    is("b0111".U) {
      val take = (io.controlBits & io.nzp).orR
      baseline.pcJump := take
    }
    is("b0000".U) {
      baseline.halt := true.B
    }
  }

  //Set outs
  io.ALUSel := baseline.ALUSel
  io.registerWriteEnable := baseline.registerWriteEnable
  io.memoryWriteEnable := baseline.memoryWriteEnable
  io.pcJump := baseline.pcJump
  io.useIMM := baseline.useIMM
  io.halt := baseline.halt

  //Implement this module here

}