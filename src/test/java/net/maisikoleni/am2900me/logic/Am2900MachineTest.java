package net.maisikoleni.am2900me.logic;

import static org.testng.Assert.assertThrows;

import org.testng.annotations.Test;

@SuppressWarnings({ "static-method", "javadoc" })
public class Am2900MachineTest {

	@Test(priority = 1)
	public void testNop() {
		Am2900Machine m = new Am2900Machine();
		MicroInstruction mi = new MicroInstruction();
		m.mpm.setInstruction(0, mi);
		m.mpm.setInstruction(1, mi);
		m.mpm.setInstruction(2, mi);
		m.executeNext();
		m.executeNext();
		m.executeNext();
		m.executeNext();
	}

	@Test(priority = 2)
	public void testIFetch() {
		Am2900Machine m = new Am2900Machine();
		MicroInstruction mi1 = new MicroInstruction();
		mi1.bz_ea = _BZ_EA.E;
		MicroInstruction mi2 = new MicroInstruction();
		mi2.bz_inc = _BZ_INC.I;
		mi2.ir_ld = _IR_LD.L;
		MicroInstruction mi3 = new MicroInstruction();
		mi3.am2910_Inst = Am2910_Inst.JMAP;
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
		assertThrows(() -> m.executeNext());
	}
}
