package net.maisikoleni.am2900me.logic;

import java.util.HashMap;
import java.util.Map;

import net.maisikoleni.am2900me.logic.microinstr._MWE;
import net.maisikoleni.am2900me.util.BitUtil;

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

	private Map<Integer, short[]> pages = new HashMap<>();
	private int lastAddr = BitUtil.TRI_STATE_OFF;
	private _MWE last_MWE = _MWE.R;

	/**
	 * Load data on the data bus output
	 * 
	 * @author MaisiKoleni
	 */
	public void outputData() {
		output.data = BitUtil.TRI_STATE_OFF;
		if (lastAddr != BitUtil.TRI_STATE_OFF && last_MWE == _MWE.R) {
			if (input.data == BitUtil.TRI_STATE_OFF)
				output.data = getPage(lastAddr)[lastAddr & 0x0FFF] & 0xFFFF;
			else
				throw new IllegalStateException("cannot write data on data bus, already in use");
		}
	}

	/**
	 * Saves the data, depending on the last cycle's address and _MWE.
	 * 
	 * @author MaisiKoleni
	 */
	public void saveData() {
		if (lastAddr != BitUtil.TRI_STATE_OFF && last_MWE == _MWE.W) {
			if (input.data != BitUtil.TRI_STATE_OFF)
				setIntern(lastAddr, (short) input.data);
			else
				throw new IllegalStateException("cannot write data from data bus to memory, no data signals");
		}
		last_MWE = input._MWE;
		lastAddr = input.addr;
	}

	private short[] getPage(int addr) {
		int pageAddr = addr >>> 12;
		return pages.computeIfAbsent(pageAddr, x -> new short[4096]);
	}

	/**
	 * Used to program the machine itself, value will be cast to short (16bit).
	 * 
	 * @author MaisiKoleni
	 */
	public void set(int address, int shortValue) {
		setIntern(address, (short) shortValue);
	}

	private void setIntern(int address, short value) {
		getPage(address)[address & 0x0FFF] = value;
	}

	/**
	 * Returns the 16 bit short stored at the given address.
	 * 
	 * @author MaisiKoleni
	 */
	public short get(int address) {
		return getPage(address)[address & 0x0FFF];
	}

	/**
	 * Returns the number of pages the RAM has.
	 * 
	 * @author MaisiKoleni
	 */
	@SuppressWarnings("static-method")
	public int pageCount() {
		return 16;
	}

	/**
	 * Returns the number of memory cells of a single page.
	 * 
	 * @author MaisiKoleni
	 */
	@SuppressWarnings("static-method")
	public int cellCount() {
		return 4096;
	}

	/**
	 * Returns true if the given page is currently in use, that is when values
	 * located in the page are read or written by the user or machine.
	 * 
	 * @author MaisiKoleni
	 */
	public boolean isPageInUse(int page) {
		return pages.containsKey(page);
	}

	/**
	 * Allocates the given page if it is not already allocated.
	 * 
	 * @author MaisiKoleni
	 */
	public void allocatePage(int page) {
		getPage(page * cellCount());
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