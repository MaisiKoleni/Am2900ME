package net.maisikoleni.am2900me.logic;

import java.util.HashMap;

/**
 * Quite rudimentary implementation of the machine's main memory; memory pages
 * (4096 x 16bit) are only created when used.
 *
 * @author MaisiKoleni
 *
 */
public class MainMachineRAM {

	final MainMachineRAMinput input = new MainMachineRAMinput();
	final MainMachineRAMoutput output = new MainMachineRAMoutput();

	private HashMap<Integer, short[]> pages = new HashMap<>();
	private int lastAddr;
	private _MWE last_MWE;

	/**
	 * Saves the data or outputs data, depending on the last cycle's address and
	 * _MWE.
	 * 
	 * @author MaisiKoleni
	 */
	public void process() {
		if (lastAddr != BitUtil.TRI_STATE_OFF) {
			boolean dataOff = input.data == BitUtil.TRI_STATE_OFF;
			if (last_MWE == _MWE.R) {
				if (dataOff)
					output.data = getPage()[input.addr & 0x0FFF] & 0xFFFF;
				else
					throw new IllegalStateException("cannot write data on data bus, already in use");
			} else {
				if (!dataOff)
					getPage()[input.addr & 0x0FFF] = (short) input.data;
				else
					throw new IllegalStateException("cannot write data from data bus to memory, no data signals");
			}
		}
		last_MWE = input._MWE;
		lastAddr = input.addr;
	}

	private short[] getPage() {
		int pageAddr = input.addr >>> 12;
		short[] page = pages.get(pageAddr);
		if (page == null) {
			page = new short[4096];
			pages.put(pageAddr, page);
		}
		return page;
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