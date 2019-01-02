package net.maisikoleni.am2900me.logic;

import static org.testng.Assert.*;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import net.maisikoleni.am2900me.logic.microinstr.Am2901_Dest;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Func;
import net.maisikoleni.am2900me.logic.microinstr.Am2901_Src;
import net.maisikoleni.am2900me.util.BitUtil;

@SuppressWarnings({ "static-method", "javadoc" })
public class Am2901Test {

	@Test
	public void add() throws ReflectiveOperationException {
		Am2901 a = new Am2901();
		Method add = Am2901.class.getDeclaredMethod("add", Integer.TYPE, Integer.TYPE);
		add.setAccessible(true);
		for (int rInt = -8; rInt <= 7; rInt++) {
			for (int sInt = -8; sInt <= 7; sInt++) {
				int r = rInt & 0b1111;
				int s = sInt & 0b1111;
				int javaRes = BitUtil.signed4ToSigned32(r) + BitUtil.signed4ToSigned32(s);
				int rs4bits = BitUtil.signed4ToSigned32(javaRes);
				int res = (Integer) add.invoke(a, r, s);
				assertEquals(res, javaRes & 0b1111, "add");
				assertEquals(a.output.F0 == 1, rs4bits == 0, "Zero");
				assertEquals(a.output.F3 == 1, (javaRes & 0b1000) > 0, "Sign");
				assertEquals(a.output.OVR == 1, rs4bits != javaRes, "Over");
				assertEquals(a.output.Cn4 == 1, ((r + s) & 0b11_0000) != 0, "Carry");
			}
		}
	}

	@Test
	public void alu() throws ReflectiveOperationException {
		Am2901 a = new Am2901();
		Method alu = Am2901.class.getDeclaredMethod("alu", Am2901_Func.class, Integer.TYPE, Integer.TYPE);
		alu.setAccessible(true);
		for (Am2901_Func f : Am2901_Func.values()) {
			a.input.Cn = f.name().toLowerCase().contains("sub") ? 1 : 0;
			int res1 = (Integer) alu.invoke(a, f, -3, 5);
			int res2 = (Integer) alu.invoke(a, f, 1, 3);
			switch (f) {
			case ADD:
				assertEquals(res1, 2, "Func: " + f);
				assertEquals(res2, 4, "Func: " + f);
				break;
			case AND:
				assertEquals(res1, 5, "Func: " + f);
				assertEquals(res2, 1, "Func: " + f);
				break;
			case EXNOR:
				assertEquals(res1, 7, "Func: " + f);
				assertEquals(res2, 0b1101, "Func: " + f);
				break;
			case EXOR:
				assertEquals(res1, 0b1000, "Func: " + f);
				assertEquals(res2, 2, "Func: " + f);
				break;
			case NOTRS:
				assertEquals(res1, 0, "Func: " + f);
				assertEquals(res2, 2, "Func: " + f);
				break;
			case OR:
				assertEquals(res1, 0b1101, "Func: " + f);
				assertEquals(res2, 3, "Func: " + f);
				break;
			case SUBR:
				assertEquals(res1, 0b1000, "Func: " + f);
				assertEquals(res2, 2, "Func: " + f);
				break;
			case SUBS:
				assertEquals(res1, 0b1000, "Func: " + f);
				assertEquals(res2, 0b1110, "Func: " + f);
				break;
			default:
				fail("unkown Am2901_Func: " + f);
			}
		}
	}

	@Test
	public void and() throws ReflectiveOperationException {
		Am2901 a = new Am2901();
		Method and = Am2901.class.getDeclaredMethod("and", Integer.TYPE, Integer.TYPE);
		and.setAccessible(true);
		for (int r = -8; r <= 7; r++) {
			for (int s = -8; s <= 7; s++) {
				int res = (Integer) and.invoke(a, r, s);
				assertEquals((r & s) & 0xF, res);
			}
		}
	}

	@Test
	public void getR() throws ReflectiveOperationException {
		Am2901 a = new Am2901();
		// setting Q
		a.input.D = 4;
		a.input.mi_src = Am2901_Src.DZ;
		a.input.mi_func = Am2901_Func.OR;
		a.input.mi_dest = Am2901_Dest.QREG;
		a.processStep1();
		a.processStep2();
		a.input.D = 2;
		a.input.regA_addr = 16;
		a.input.regB_addr = 16;
		Method getR = Am2901.class.getDeclaredMethod("getR", Am2901_Src.class);
		getR.setAccessible(true);
		for (Am2901_Src src : Am2901_Src.values()) {
			switch (src.name().charAt(0)) {
			case 'A':
				a.input.regA_addr = 16;
				assertThrows(() -> getR.invoke(a, src));
				a.input.regA_addr = 0;
				assertEquals((int) (Integer) getR.invoke(a, src), 0, "Src: " + src);
				a.input.regA_addr = 16;
				break;
			case 'Z':
				assertEquals((int) (Integer) getR.invoke(a, src), 0, "Src: " + src);
				break;
			case 'D':
				assertEquals((int) (Integer) getR.invoke(a, src), 2, "Src: " + src);
				break;
			default:
				fail("unknown source: " + src.name().charAt(0) + " from " + src);

			}
		}
	}

	@Test
	public void getS() throws ReflectiveOperationException {
		Am2901 a = new Am2901();
		// setting Q
		a.input.D = 4;
		a.input.mi_src = Am2901_Src.DZ;
		a.input.mi_func = Am2901_Func.OR;
		a.input.mi_dest = Am2901_Dest.QREG;
		a.processStep1();
		a.processStep2();
		a.input.D = 2;
		a.input.regA_addr = 16;
		a.input.regB_addr = 16;
		Method getS = Am2901.class.getDeclaredMethod("getS", Am2901_Src.class);
		getS.setAccessible(true);
		for (Am2901_Src src : Am2901_Src.values()) {
			switch (src.name().charAt(1)) {
			case 'A':
				a.input.regA_addr = 16;
				assertThrows(() -> getS.invoke(a, src));
				a.input.regA_addr = 0;
				assertEquals((int) (Integer) getS.invoke(a, src), 0, "Src: " + src);
				a.input.regA_addr = 16;
				break;
			case 'B':
				a.input.regB_addr = 16;
				assertThrows(() -> getS.invoke(a, src));
				a.input.regB_addr = 0;
				assertEquals((int) (Integer) getS.invoke(a, src), 0, "Src: " + src);
				a.input.regB_addr = 16;
				break;
			case 'Z':
				assertEquals((int) (Integer) getS.invoke(a, src), 0, "Src: " + src);
				break;
			case 'Q':
				assertEquals((int) (Integer) getS.invoke(a, src), 4, "Src: " + src);
				break;
			default:
				fail("unknown source: " + src.name().charAt(0) + " from " + src);

			}
		}
	}

	@Test
	public void or() throws ReflectiveOperationException {
		Am2901 a = new Am2901();
		Method or = Am2901.class.getDeclaredMethod("or", Integer.TYPE, Integer.TYPE);
		or.setAccessible(true);
		for (int r = -8; r <= 7; r++) {
			for (int s = -8; s <= 7; s++) {
				int res = (Integer) or.invoke(a, r, s);
				assertEquals((r | s) & 0xF, res);
			}
		}
	}

	@Test
	public void process() {
		Am2901 a = new Am2901();
		a.input.D = 4;
		a.input.mi_src = Am2901_Src.DZ;
		a.input.mi_func = Am2901_Func.OR;
		a.input.mi_dest = Am2901_Dest.QREG;
		a.processStep1();
		assertEquals(a.output.F0, 0);
		assertEquals(a.output.Y, 4);
		a.processStep2();
		assertEquals(a.output.F0, 0);
		assertEquals(a.output.Y, 4);
		a.input.D = 5;
		a.input.mi_src = Am2901_Src.DQ;
		a.input.mi_func = Am2901_Func.ADD;
		a.input.mi_dest = Am2901_Dest.RAMF;
		a.processStep1();
		assertEquals(a.output.OVR, 1);
		assertEquals(a.output.Y, -7 & 0xF);
		a.processStep2();
		assertEquals(a.output.OVR, 1);
		assertEquals(a.output.Y, -7 & 0xF);
		a.input.D = -1 & 0xF;
		a.input.Cn = 1;
		a.input.mi_src = Am2901_Src.DA;
		a.input.mi_func = Am2901_Func.SUBR;
		a.input.mi_dest = Am2901_Dest.NOP;
		a.processStep1();
		assertEquals(a.output.F3, 1);
		assertEquals(a.output.Y, -6 & 0xF);
		a.processStep2();
		assertEquals(a.output.F3, 1);
		assertEquals(a.output.Y, -6 & 0xF);
	}

	@Test
	public void sub() throws ReflectiveOperationException {
		Am2901 a = new Am2901();
		a.input.Cn = 1;
		Method sub = Am2901.class.getDeclaredMethod("sub", Integer.TYPE, Integer.TYPE);
		sub.setAccessible(true);
		for (int rInt = -8; rInt <= 7; rInt++) {
			for (int sInt = -8; sInt <= 7; sInt++) {
				int r = rInt & 0b1111;
				int s = sInt & 0b1111;
				int javaRes = BitUtil.signed4ToSigned32(r) - BitUtil.signed4ToSigned32(s);
				int rs4bits = BitUtil.signed4ToSigned32(javaRes);
				int res = (Integer) sub.invoke(a, r, s);
				assertEquals(res, javaRes & 0b1111, "sub");
				assertEquals(a.output.F0 == 1, rs4bits == 0, "Zero");
				assertEquals(a.output.F3 == 1, (javaRes & 0b1000) > 0, "Sign");
				assertEquals(a.output.OVR == 1, rs4bits != javaRes, "Over");
//				assertEquals(a.output.Cn4 == 1, ((r + s) & 0b11_0000) != 0, "Carry"); // TODO
			}
		}
	}

	@Test
	public void xnor() throws ReflectiveOperationException {
		Am2901 a = new Am2901();
		Method xnor = Am2901.class.getDeclaredMethod("xnor", Integer.TYPE, Integer.TYPE);
		xnor.setAccessible(true);
		for (int r = -8; r <= 7; r++) {
			for (int s = -8; s <= 7; s++) {
				int res = (Integer) xnor.invoke(a, r, s);
				assertEquals(~(r ^ s) & 0xF, res);
			}
		}
	}
}
