package net.maisikoleni.am2900me.logic;

import java.util.HashMap;

/**
 * Quite rudimentary implementation of the machine's main memory; memory pages
 * (4096 x 16bit) are only created when used.
 *
 * @author MaisiKoleni
 *
 */
public class MachineRAM {

	final MainMachineRAMinput input = new MainMachineRAMinput();
	final MainMachineRAMoutput output = new MainMachineRAMoutput();

	private HashMap<Integer, short[]> pages = new HashMap<>();
	private int lastAddr = BitUtil.TRI_STATE_OFF;
	private _MWE last_MWE = _MWE.R;

	/**
	 * Load data on the data bus output
	 * 
	 * @author MaisiKoleni
	 */
	public void outputData() {
		output.data = BitUtil.TRI_STATE_OFF;
		if (lastAddr != BitUtil.TRI_STATE_OFF) {
			if (last_MWE == _MWE.R) {
				if (input.data == BitUtil.TRI_STATE_OFF)
					output.data = getPage(lastAddr)[lastAddr & 0x0FFF] & 0xFFFF;
				else
					throw new IllegalStateException("cannot write data on data bus, already in use");
			}
		}
	}

	/**
	 * Saves the data, depending on the last cycle's address and _MWE.
	 * 
	 * @author MaisiKoleni
	 */
	public void saveData() {
		if (lastAddr != BitUtil.TRI_STATE_OFF) {
			if (last_MWE == _MWE.W) {
				if (input.data != BitUtil.TRI_STATE_OFF)
					getPage(lastAddr)[lastAddr & 0x0FFF] = (short) input.data;
				else
					throw new IllegalStateException("cannot write data from data bus to memory, no data signals");
			}
		}
		last_MWE = input._MWE;
		lastAddr = input.addr;
	}

	private short[] getPage(int addr) {
		int pageAddr = addr >>> 12;
		short[] page = pages.get(pageAddr);
		if (page == null) {
			page = new short[4096];
			pages.put(pageAddr, page);
		}
		return page;
	}

	/**
	 * Used to program the machine itself, value will be cast to short (16bit).
	 * 
	 * @author MaisiKoleni
	 */
	public void set(int address, int shortValue) {
		getPage(address)[address & 0x0FFF] = (short) shortValue;
	}
}

class MainMachineRAMinput {
	_MWE _MWE;
	int addr;
	int data;
}

class MainMachineRAMoutput {
	int data;
}