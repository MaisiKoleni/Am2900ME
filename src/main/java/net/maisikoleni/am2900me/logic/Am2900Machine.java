package net.maisikoleni.am2900me.logic;

import net.maisikoleni.am2900me.logic.microinstr.ASEL;
import net.maisikoleni.am2900me.logic.microinstr.Am2910_Inst;
import net.maisikoleni.am2900me.logic.microinstr.BSEL;
import net.maisikoleni.am2900me.logic.microinstr.KMUX;
import net.maisikoleni.am2900me.logic.microinstr.MicroInstruction;
import net.maisikoleni.am2900me.logic.microinstr._ABUS;
import net.maisikoleni.am2900me.logic.microinstr._DBUS;
import net.maisikoleni.am2900me.util.BitUtil;

/**
 * A simple, basic Am2900 machine that does not support interrupts at the
 * moment.
 *
 * @author MaisiKoleni
 */
public class Am2900Machine {
	final ProgramCounter pc = new ProgramCounter();
	final InstructionRegister ir = new InstructionRegister();
	final MappingPROM mProm = new MappingPROM();
	final Am2910 am2910 = new Am2910();
	final Am2904_01x4 am2904_01x4 = new Am2904_01x4();
	final MicroprogramMemory mpm = new MicroprogramMemory();
	final MachineRAM machineRam = new MachineRAM();

	private MicroInstruction mi;
	private int currentMI = -1;
	private int dataBus;
	private int addrBus;

	/**
	 * Creates a new basic Am2900 machine; Am2910's _OE, CI and _RLD are fix.
	 * 
	 * @author MaisiKoleni
	 */
	public Am2900Machine() {
		mi = MicroInstruction.DEFAULT.withAm2910_Inst(Am2910_Inst.JZ);
		am2904_01x4.input._OEY = 1;
		am2904_01x4.input._OECT = 0;
		am2910.input._OE = 0;
		am2910.input.CI = 1;
		am2910.input._RLD = 1;
	}

	/**
	 * Executes the next microinstruction
	 * 
	 * @author MaisiKoleni
	 */
	public void executeNext() {
		dataBus = BitUtil.TRI_STATE_OFF;
		addrBus = BitUtil.TRI_STATE_OFF;
		// memory
		machineRam.input.data = dataBus;
		machineRam.input.addr = addrBus;
		machineRam.input._MWE = mi.mwe;
		machineRam.outputData();
		setData(machineRam.output.data);
		// program counter
		pc.input._BZ_EA = mi.bz_ea;
		pc.input._BZ_ED = mi.bz_ed;
		pc.input._BZ_INC = mi.bz_inc;
		pc.input._BZ_LD = mi.bz_ld;
		pc.input.data = dataBus;
		pc.process();
		setData(pc.output.data);
		setAddr(pc.output.addr);
		// instruction register
		ir.input._IR_LD = mi.ir_ld;
		ir.input.data = dataBus;
		ir.process();
		int ra_addr = (mi.asel == ASEL.IR) ? ir.output.regAAddr : mi.ra_addr.value;
		int rb_addr = (mi.bsel == BSEL.IR) ? ir.output.regBAddr : mi.rb_addr.value;
		int d = mi.kmux == KMUX.D ? dataBus : mi.k.value;
		if (d == BitUtil.TRI_STATE_OFF)
			throw new IllegalStateException("no data bus signal to use in K-MUX");
		// Am2904 and Am2901
		am2904_01x4.input._CEM = mi.ce_m;
		am2904_01x4.input._CEµ = mi.ce_µ;
		am2904_01x4.input.D = d;
		am2904_01x4.input.regA_addr = ra_addr;
		am2904_01x4.input.regB_addr = rb_addr;
		am2904_01x4.input.mi_src = mi.am2901_Src;
		am2904_01x4.input.mi_func = mi.am2901_Func;
		am2904_01x4.input.mi_dest = mi.am2901_Dest;
		am2904_01x4.input.mi_carry = mi.am2904_Carry;
		am2904_01x4.input.mi_inst = mi.am2904_Inst;
		am2904_01x4.input.mi_shift = mi.am2904_Shift;
		am2904_01x4.process();
		int y = am2904_01x4.output.Y;
		if (mi.abus == _ABUS.AB)
			setAddr(y);
		if (mi.dbus == _DBUS.DB)
			setData(y);
		// Am2910 - D source
		am2910.input.mi_inst = mi.am2910_Inst;
		am2910.processStep1();
		// Mapping PROM
		mProm.input.opCode = ir.output.opCode;
		mProm.input._OE = am2910.output._MAP;
		mProm.process();
		// Am2910 - next MI address
		am2910.input._CCEN = mi.ccen;
		am2910.input._CC = am2904_01x4.output.CT;
		if (am2910.output._PL == 0)
			am2910.input.D = mi.bar.value;
		else if (am2910.output._MAP == 0)
			am2910.input.D = mProm.output.miAddress;
		else // no support for _VECT / interrupts jet
			throw new IllegalStateException("interrupts are currently not supported");
		am2910.processStep2();
		// Microprogram memory
		currentMI = am2910.output.Y;
		mi = mpm.getInstruction(currentMI);
		// memory save
		machineRam.input.data = dataBus;
		machineRam.input.addr = addrBus;
		machineRam.saveData();
	}

	private void setData(int signal) {
		if (signal == BitUtil.TRI_STATE_OFF)
			return;
		if (dataBus != BitUtil.TRI_STATE_OFF)
			throw new IllegalStateException("data bus already in use");
		dataBus = signal;
	}

	private void setAddr(int signal) {
		if (signal == BitUtil.TRI_STATE_OFF)
			return;
		if (addrBus != BitUtil.TRI_STATE_OFF)
			throw new IllegalStateException("address bus already in use");
		addrBus = signal;
	}

	public final ProgramCounter getPc() {
		return pc;
	}

	public final InstructionRegister getIr() {
		return ir;
	}

	public final MappingPROM getmProm() {
		return mProm;
	}

	public final Am2910 getAm2910() {
		return am2910;
	}

	public final Am2904_01x4 getAm2904_01x4() {
		return am2904_01x4;
	}

	public final MicroprogramMemory getMpm() {
		return mpm;
	}

	public final MachineRAM getMachineRam() {
		return machineRam;
	}

	public final int getCurrentMicroInstruction() {
		return currentMI;
	}
}
