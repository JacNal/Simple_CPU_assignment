import chisel3._

class ProgramCounter extends Module {
  val io = IO(new Bundle {
    val stop = Input(Bool())
    val jump = Input(Bool())
    val run = Input(Bool())
    val programCounterJump = Input(UInt(16.W))
    val programCounter = Output(UInt(16.W))
  })

  private val programCounterReg = RegInit(0.U(16.W))

  io.programCounter := programCounterReg

  when(io.run === false.B || io.stop === true.B) {
    programCounterReg := programCounterReg
  }.otherwise {
    when(io.jump === false.B) {
      programCounterReg := programCounterReg + 1.U(16.W)
    }.otherwise{
      programCounterReg := io.programCounterJump
    }
  }

  //Implement this module here (respect the provided interface, since it used by the tester)

}