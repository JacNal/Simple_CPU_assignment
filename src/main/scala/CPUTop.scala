import chisel3._
import chisel3.util._

class CPUTop extends Module {
  val io = IO(new Bundle {
    val done = Output(Bool ())
    val run = Input(Bool ())
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerDataMemEnable = Input(Bool ())
    val testerDataMemAddress = Input(UInt (16.W))
    val testerDataMemDataRead = Output(UInt (32.W))
    val testerDataMemWriteEnable = Input(Bool ())
    val testerDataMemDataWrite = Input(UInt (32.W))
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerProgMemEnable = Input(Bool ())
    val testerProgMemAddress = Input(UInt (16.W))
    val testerProgMemDataRead = Output(UInt (32.W))
    val testerProgMemWriteEnable = Input(Bool ())
    val testerProgMemDataWrite = Input(UInt (32.W))
  })

  //Creating components
  val programCounter = Module(new ProgramCounter())
  val dataMemory = Module(new DataMemory())
  val programMemory = Module(new ProgramMemory())
  val registerFile = Module(new RegisterFile())
  val controlUnit = Module(new ControlUnit())
  val alu = Module(new ALU())

  //Connecting the modules
  //programCounter.io.run := io.run
  //programMemory.io.address := programCounter.io.programCounter
  
  ////////////////////////////////////////////
  when (controlUnit.io.useIMM) {
    dataMemory.io.address := alu.io.result
  }.otherwise{
    dataMemory.io.address := registerFile.io.outA
  }
  ////////////////////////////////////////////
  val haltReg = RegInit(false.B)
  when (controlUnit.io.halt) {
    haltReg := true.B
  }

  io.done := haltReg

  programCounter.io.run := io.run && !haltReg
  programCounter.io.stop := haltReg
  programCounter.io.jump := controlUnit.io.pcJump

  val instr = programMemory.io.instructionRead
  val immPC = instr(24,9)
  programCounter.io.programCounterJump := immPC

  programMemory.io.address := programCounter.io.programCounter
  ////////////////////////////////////////////
  ////////////////////////////////////////////
  val opCode = instr(31,28)
  val controlBits = instr(27,25)

  controlUnit.io.opCode := opCode
  controlUnit.io.controlBits := controlBits
  controlUnit.io.nzp := alu.io.nzp
  ////////////////////////////////////////////
  ////////////////////////////////////////////
  val regPosOne = instr(24,21)
  val regPosTwo = instr(20,17)
  val regPosThree = instr(16,13)
  ////////////////////////////////////////////
  ////////////////////////////////////////////
  val aSel = Wire(UInt(4.W))
  val bSel = Wire(UInt(4.W))
  val writeSel = Wire(UInt(4.W))

  writeSel := regPosOne
  aSel := regPosTwo
  bSel := regPosThree

  registerFile.io.aSel := aSel
  registerFile.io.bSel := bSel
  registerFile.io.writeSel := writeSel
  registerFile.io.writeEnable := controlUnit.io.registerWriteEnable

  val immALU = instr(16,1)
  val immALUExtended = Cat(Fill(16,immALU(15)), immALU)

  val ALUOp1 = registerFile.io.outA
  val ALUOp2 = Mux(controlUnit.io.useIMM, immALUExtended, registerFile.io.outB)

  alu.io.operand1 := ALUOp1
  alu.io.operand2 := ALUOp2
  alu.io.sel := controlUnit.io.ALUSel
  //Continue here with your connections
  ////////////////////////////////////////////

  //This signals are used by the tester for loading the program to the program memory, do not touch
  programMemory.io.testerAddress := io.testerProgMemAddress
  io.testerProgMemDataRead := programMemory.io.testerDataRead
  programMemory.io.testerDataWrite := io.testerProgMemDataWrite
  programMemory.io.testerEnable := io.testerProgMemEnable
  programMemory.io.testerWriteEnable := io.testerProgMemWriteEnable
  //This signals are used by the tester for loading and dumping the data memory content, do not touch
  dataMemory.io.testerAddress := io.testerDataMemAddress
  io.testerDataMemDataRead := dataMemory.io.testerDataRead
  dataMemory.io.testerDataWrite := io.testerDataMemDataWrite
  dataMemory.io.testerEnable := io.testerDataMemEnable
  dataMemory.io.testerWriteEnable := io.testerDataMemWriteEnable
}
