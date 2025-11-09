import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ALUTester extends AnyFlatSpec with ChiselScalatestTester {

  "ALU" should "do ADD, SUB and AND correctly" in {
    test(new ALU)
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

        val add = "b00".U(2.W)
        val sub = "b01".U(2.W)
        val and = "b10".U(2.W)

        def check(sel: UInt, a: BigInt, b: BigInt, expected: BigInt, msg: String = ""): Unit = {
          dut.io.sel.poke(sel)
          dut.io.operand1.poke(a.U)
          dut.io.operand2.poke(b.U)
          dut.clock.step(1)
          dut.io.result.expect(expected.U, s"$msg sel=$sel a=$a b=$b")
        }

        val mask32 = (BigInt(1) << 32) - 1

        check(add, 0, 0, 0, "ADD 0+0")
        check(add, 1, 2, 3, "ADD 1+2")
        check(add, 10, 5, 15, "ADD 10+5")
        check(add, mask32, 1, 0, "ADD wrap")

        check(sub, 5, 3, 2, "SUB 5-3")
        check(sub, 10, 10, 0, "SUB 10-10")
        check(sub, 0, 1, mask32, "SUB underflow")

        check(and, 0xF0F0F0F0L, 0x0FF00FF0L, 0x00F000F0L, "AND pattern")
        check(and, 0xFFFFFFFFL, 0x0L, 0x0L, "AND with zero")
        check(and, 0xAAAAAAAAL, 0x55555555L, 0x0L, "AND disjoint bits")

        val rnd = new scala.util.Random(0)
        for (_ <- 0 until 20) {
          val a = BigInt(32, rnd)
          val b = BigInt(32, rnd)

          dut.io.sel.poke(add)
          dut.io.operand1.poke(a.U)
          dut.io.operand2.poke(b.U)
          dut.clock.step(1)
          val addExp = (a + b) & mask32
          dut.io.result.expect(addExp.U)

          dut.io.sel.poke(sub)
          dut.io.operand1.poke(a.U)
          dut.io.operand2.poke(b.U)
          dut.clock.step(1)
          val subExp = (a - b) & mask32
          dut.io.result.expect(subExp.U)

          dut.io.sel.poke(and)
          dut.io.operand1.poke(a.U)
          dut.io.operand2.poke(b.U)
          dut.clock.step(1)
          val andExp = a & b
          dut.io.result.expect(andExp.U)
        }
      }
  }
}
