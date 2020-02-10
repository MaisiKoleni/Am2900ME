package net.maisikoleni.am2900me.logic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import net.maisikoleni.am2900me.logic.microinstr.Am2910_Inst;
import net.maisikoleni.am2900me.logic.microinstr.MicroInstruction;
import net.maisikoleni.am2900me.logic.microinstr._BZ_EA;
import net.maisikoleni.am2900me.logic.microinstr._BZ_INC;
import net.maisikoleni.am2900me.logic.microinstr._IR_LD;

@SuppressWarnings({ "static-method" })
public class Am2900MachineTest {

	@Test
	@Order(1)
	public void testNop() {
		Am2900Machine m = new Am2900Machine();
		MicroInstruction mi = MicroInstruction.DEFAULT;
		m.mpm.setInstruction(0, mi);
		m.mpm.setInstruction(1, mi);
		m.mpm.setInstruction(2, mi);
		m.executeNext();
		m.executeNext();
		m.executeNext();
		m.executeNext();
	}

	@Test
	@Order(2)
	public void testIFetch() {
		Am2900Machine m = new Am2900Machine();
		MicroInstruction mi1 = MicroInstruction.DEFAULT.withBz_ea(_BZ_EA.E);
		MicroInstruction mi2 = MicroInstruction.DEFAULT.withBz_inc(_BZ_INC.I).withIr_ld(_IR_LD.L);
		MicroInstruction mi3 = MicroInstruction.DEFAULT.withAm2910_Inst(Am2910_Inst.JMAP);
		m.mpm.setInstruction(0, mi1);
		m.mpm.setInstruction(1, mi2);
		m.mpm.setInstruction(2, mi3);
		m.machineRam.set(0, 0x00_00);
		m.machineRam.set(1, 0x01_00);
		m.executeNext();
		// first
		m.executeNext();
		m.executeNext();
		m.executeNext();
		// second
		m.executeNext();
		m.executeNext();
		m.executeNext();
		// because the is no microprogram at 0xF
		assertThrows(IllegalStateException.class, () -> m.executeNext());
	}

	@Test
	@Order(3)
	public void testSpeed1() {
		final int LARGE_NUM = 15_000_000;
		Am2900Machine m = new Am2900Machine();
		MicroInstruction mi = MicroInstruction.DEFAULT.withAm2910_Inst(Am2910_Inst.JZ);
		m.mpm.setInstruction(0, mi);
		m.executeNext();
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < LARGE_NUM; i++) {
			m.executeNext();
		}
		int instsPms = (LARGE_NUM / (int) (System.currentTimeMillis() - t1));
		System.out.println("Speed test 1 has " + instsPms + " inst./ms. Should be well above 200");
		assertTrue(instsPms > 200);
	}

	@Test
	@Order(3)
	public void testSpeed2() {
		final int LARGE_NUM = 5_000_000;
		Am2900Machine m = new Am2900Machine();
		MicroInstruction mi1 = MicroInstruction.DEFAULT.withBz_ea(_BZ_EA.E);
		MicroInstruction mi2 = MicroInstruction.DEFAULT.withBz_inc(_BZ_INC.I).withIr_ld(_IR_LD.L);
		MicroInstruction mi3 = MicroInstruction.DEFAULT.withAm2910_Inst(Am2910_Inst.JMAP);
		m.mpm.setInstruction(0, mi1);
		m.mpm.setInstruction(1, mi2);
		m.mpm.setInstruction(2, mi3);
		m.executeNext();
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < LARGE_NUM; i++) {
			m.executeNext();
			m.executeNext();
			m.executeNext();
		}
		int instsPms = (3 * LARGE_NUM / (int) (System.currentTimeMillis() - t1));
		System.out.println("Speed test 2 has " + instsPms + " inst./ms. Should be well above 200");
		assertTrue(instsPms > 200);
	}
}
