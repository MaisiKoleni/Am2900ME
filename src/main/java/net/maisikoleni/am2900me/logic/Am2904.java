package net.maisikoleni.am2900me.logic;

import net.maisikoleni.am2900me.logic.microinstr.Am2904_Carry;
import net.maisikoleni.am2900me.logic.microinstr.Am2904_Inst;
import net.maisikoleni.am2900me.logic.microinstr.Am2904_Shift;
import net.maisikoleni.am2900me.logic.microinstr._CE_M;
import net.maisikoleni.am2900me.logic.microinstr._CE_µ;
import net.maisikoleni.am2900me.util.BitUtil;

/**
 * The Am2904 is the "Status and Shift Control Unit". Intended to work closely
 * together with the ALU, see {@link Am2901x4}.<br>
 * <b>For more details and a comprehensive overview, please see the
 * "Am2904_Instruction_Codes" PDF and the Am2900 Data Book page 92ff in the
 * additional_material folder.</b>
 *
 * @author MaisiKoleni
 *
 */
public class Am2904 {
	private int µC;
	private int µN;
	private int µZ;
	private int µOVR;
	private int MC;
	private int MN;
	private int MZ;
	private int MOVR;

	private final int[] srCache = new int[8];

	final Am2904input input = new Am2904input();
	final Am2904output output = new Am2904output();

	/**
	 * Sets only the C0, the ALU carry in.
	 * 
	 * @author MaisiKoleni
	 */
	public Am2904output processStep1() {
		output.C0 = calculateC0();
		return output;
	}

	/**
	 * Writes the status registers, calculates the condition code and does the shift
	 * linkage (what shifts where and how to rotate bits and such).
	 * 
	 * @author MaisiKoleni
	 */
	public Am2904output processStep2() {
		doSROperations();
		output.CT = calculateCT();
		output.Y3 = calculateY3();
		doShiftLinkage();
		return output;
	}

	private int calculateC0() {
		Am2904_Carry carry = input.mi_carry;
		switch (carry) {
		case CI0:
			return 0;
		case CI1:
			return 1;
		case CX:
			return input.CX;
		case USE_SR:
			Am2904_Inst inst = input.mi_inst;
			int instBlock = inst.getBlock();
			if (instBlock <= 1)
				return inst.isLoadCarryInvert() ? 1 - µC : µC;
			return inst.isLoadCarryInvert() ? 1 - MC : MC;
		default:
			throw new IllegalArgumentException("unknown am2904 carry config: " + carry);
		}
	}

	private int calculateY3() {
		if (input._OEY == 1)
			return BitUtil.TRI_STATE_OFF;
		Am2904_Inst inst = input.mi_inst;
		if (inst == Am2904_Inst.LoadM_LoadY_µ_NxorOVRorZ)
			return BitUtil.TRI_STATE_OFF;
		switch (inst.getBlock()) {
		case 0:
		case 1:
			return BitUtil.toBitVector(µZ, µC, µN, µOVR);
		case 2:
			return BitUtil.toBitVector(MZ, MC, MN, MOVR);
		case 3:
			return BitUtil.toBitVector(input.IZ, input.IC, input.IN, input.IOVR);
		default:
			throw new IllegalArgumentException(
					"unknown am2904 instruction block: " + inst + ", block=" + inst.getBlock());
		}
	}

	private int calculateCT() {
		if (input._OECT == 1)
			return BitUtil.TRI_STATE_OFF;
		Am2904_Inst inst = input.mi_inst;
		switch (inst) {
		case LoadM_LoadY_µ_NxorOVRorZ:
			return (µN ^ µOVR) | µZ;
		case Set_Set_µ_NxnorOVRornotZ:
			return 1 - (µN ^ µOVR) & µZ;
		case Swap_Swap_µ_NxorOVR:
			return µN ^ µOVR;
		case Reset_Reset_µ_NxnorOVR:
			return 1 - (µN ^ µOVR);
		case Load_LoadForShiftThroughOvr_µ_Z:
			return µZ;
		case Load_Invert_µ_notZ:
			return 1 - µZ;
		case LoadOvrRetain_Load_µ_OVR:
			return µOVR;
		case LoadOvrRetain_Load_µ_notOVR:
			return 1 - µOVR;
		case ResetZ_LoadCarryInvert_µ_CorZ:
			return µC | µZ;
		case SetZ_LoadCarryInvert_µ_notCandnotZ:
			return 1 - µC & 1 - µZ;
		case ResetC_Load_µ_C:
			return µC;
		case SetC_Load_µ_notC:
			return 1 - µC;
		case ResetN_Load_µ_notCorZ:
			return 1 - µC | µZ;
		case SetN_Load_µ_CandnotZ:
			return µC & 1 - µZ;
		case ResetOvr_Load_IM_NxorN:
			return input.IN ^ MN;
		case SetOvr_Load_IM_NxnorN:
			return 1 - (input.IN ^ MN);
		case Load_Load_µ_NxorOVRorZ:
			return (µN ^ µOVR) | µZ;
		case Load_Load_µ_NxnorOVRornotZ:
			return 1 - (µN ^ µOVR) & µZ;
		case Load_Load_µ_NxorOVR:
			return µN ^ µOVR;
		case Load_Load_µ_NxnorOVR:
			return 1 - (µN ^ µOVR);
		case Load_Load_µ_Z:
			return µZ;
		case Load_Load_µ_notZ:
			return 1 - µZ;
		case Load_Load_µ_OVR:
			return µOVR;
		case Load_Load_µ_notOVR:
			return 1 - µOVR;
		case LoadCarryInvert_LoadCarryInvert_µ_CorZ:
			return µC | µZ;
		case LoadCarryInvert_LoadCarryInvert_µ_notCandnotZ:
			return 1 - µC & 1 - µZ;
		case Load_Load_µ_C:
			return µC;
		case Load_Load_µ_notC:
			return 1 - µC;
		case Load_Load_µ_notCorZ:
			return 1 - µC | µZ;
		case Load_Load_µ_CandnotZ:
			return µC & 1 - µZ;
		case Load_Load_µ_N:
			return µN;
		case Load_Load_µ_notN:
			return 1 - µN;
		case Load_Load_M_NxorOVRorZ:
			return (MN ^ MOVR) | MZ;
		case Load_Load_M_NxnorOVRornotZ:
			return 1 - (MN ^ MOVR) & MZ;
		case Load_Load_M_NxorOVR:
			return MN ^ MOVR;
		case Load_Load_M_NxnorOVR:
			return 1 - (MN ^ MOVR);
		case Load_Load_M_Z:
			return MZ;
		case Load_Load_M_notZ:
			return 1 - MZ;
		case Load_Load_M_OVR:
			return MOVR;
		case Load_Load_M_notOVR:
			return 1 - MOVR;
		case LoadCarryInvert_LoadCarryInvert_M_CorZ:
			return MC | MZ;
		case LoadCarryInvert_LoadCarryInvert_M_notCandnotZ:
			return 1 - MC & 1 - MZ;
		case Load_Load_M_C:
			return MC;
		case Load_Load_M_notC:
			return 1 - MC;
		case Load_Load_M_notCorZ:
			return 1 - MC | MZ;
		case Load_Load_M_CandnotZ:
			return MC & 1 - MZ;
		case Load_Load_M_N:
			return MN;
		case Load_Load_M_notN:
			return 1 - MN;
		case Load_Load_I_NxorOVRorZ:
			return (input.IN ^ input.IOVR) | input.IZ;
		case Load_Load_I_NxnorOVRornotZ:
			return 1 - (input.IN ^ input.IOVR) & input.IZ;
		case Load_Load_I_NxorOVR:
			return input.IN ^ input.IOVR;
		case Load_Load_I_NxnorOVR:
			return 1 - (input.IN ^ input.IOVR);
		case Load_Load_I_Z:
			return input.IZ;
		case Load_Load_I_notZ:
			return 1 - input.IZ;
		case Load_Load_I_OVR:
			return input.IOVR;
		case Load_Load_I_notOVR:
			return 1 - input.IOVR;
		case Load_Load_I_C:
			return input.IC;
		case Load_Load_I_notC:
			return 1 - input.IC;
		case LoadCarryInvert_LoadCarryInvert_I_notCorZ:
		case Load_Load_I_notCorZ:
			return 1 - input.IC | input.IZ;
		case LoadCarryInvert_LoadCarryInvert_I_CandnotZ:
		case Load_Load_I_CandnotZ:
			return input.IC & 1 - input.IZ;
		case Load_Load_I_N:
			return input.IN;
		case Load_Load_I_notN:
			return 1 - input.IN;
		default:
			throw new IllegalArgumentException("unknown am2904 instruction: " + inst);
		}
	}

	private void doSROperations() {
		if (input._CEM == _CE_M.H && input._CEµ == _CE_µ.H)
			return;
		srCache[0] = µZ;
		srCache[1] = µC;
		srCache[2] = µN;
		srCache[3] = µOVR;
		srCache[4] = MZ;
		srCache[5] = MC;
		srCache[6] = MN;
		srCache[7] = MOVR;
		doµSROperations();
		doMSROperations();
		// this is much more readable and less error prone than mixing the calculations
		// with the status register write restrictions
		if (input._CEµ == _CE_µ.H) {
			µZ = srCache[0];
			µC = srCache[1];
			µN = srCache[2];
			µOVR = srCache[3];
		} else if (input._CEM == _CE_M.H) {
			MZ = srCache[4];
			MC = srCache[5];
			MN = srCache[6];
			MOVR = srCache[7];
		}
		if (input._EZ == 1)
			MZ = srCache[4];
		if (input._EC == 1)
			MC = srCache[5];
		if (input._EN == 1)
			MN = srCache[6];
		if (input._EOVR == 1)
			MOVR = srCache[7];
	}

	private void doµSROperations() {
		Am2904_Inst inst = input.mi_inst;
		switch (inst) {
		case LoadM_LoadY_µ_NxorOVRorZ:
			µZ = MZ;
			µC = MC;
			µN = MN;
			µOVR = MOVR;
			break;
		case Set_Set_µ_NxnorOVRornotZ:
			µZ = µC = µN = µOVR = 1;
			break;
		case Swap_Swap_µ_NxorOVR:
			µZ ^= MZ;
			µC ^= MC;
			µN ^= MN;
			µOVR ^= MOVR;
			break;
		case Reset_Reset_µ_NxnorOVR:
			µZ = µC = µN = µOVR = 0;
			break;
		case LoadOvrRetain_Load_µ_OVR:
		case LoadOvrRetain_Load_µ_notOVR:
			µZ = input.IZ;
			µC = input.IC;
			µN = input.IN;
			µOVR |= input.IOVR;
			break;
		case ResetZ_LoadCarryInvert_µ_CorZ:
			µZ = 0;
			break;
		case SetZ_LoadCarryInvert_µ_notCandnotZ:
			µZ = 1;
			break;
		case ResetC_Load_µ_C:
			µC = 0;
			break;
		case SetC_Load_µ_notC:
			µC = 1;
			break;
		case ResetN_Load_µ_notCorZ:
			µN = 0;
			break;
		case SetN_Load_µ_CandnotZ:
			µN = 1;
			break;
		case ResetOvr_Load_IM_NxorN:
			µOVR = 0;
			break;
		case SetOvr_Load_IM_NxnorN:
			µOVR = 1;
			break;
		case LoadCarryInvert_LoadCarryInvert_µ_CorZ:
		case LoadCarryInvert_LoadCarryInvert_µ_notCandnotZ:
		case LoadCarryInvert_LoadCarryInvert_M_CorZ:
		case LoadCarryInvert_LoadCarryInvert_M_notCandnotZ:
		case LoadCarryInvert_LoadCarryInvert_I_notCorZ:
		case LoadCarryInvert_LoadCarryInvert_I_CandnotZ:
			µZ = input.IZ;
			µC = 1 - input.IC;
			µN = input.IN;
			µOVR = input.IOVR;
			break;
		default:
			µZ = input.IZ;
			µC = input.IC;
			µN = input.IN;
			µOVR = input.IOVR;
			break;
		}
	}

	private void doMSROperations() {
		Am2904_Inst inst = input.mi_inst;
		switch (inst) {
		case LoadM_LoadY_µ_NxorOVRorZ:
			loadY3toMSR();
			break;
		case Set_Set_µ_NxnorOVRornotZ:
			MZ = MC = MN = MOVR = 1;
			break;
		case Swap_Swap_µ_NxorOVR:
			MZ ^= µZ;
			µZ ^= MZ;
			MC ^= µC;
			µC ^= MC;
			MN ^= µN;
			µN ^= MN;
			MOVR ^= µOVR;
			µOVR ^= MOVR;
			break;
		case Reset_Reset_µ_NxnorOVR:
			MZ = MC = MN = MOVR = 0;
			break;
		case Load_LoadForShiftThroughOvr_µ_Z:
			MZ = input.IZ;
			MN = input.IN;
			MC ^= MOVR;
			MOVR ^= MC;
			MC ^= MOVR;
			break;
		case Load_Invert_µ_notZ:
			MZ = 1 - MZ;
			MC = 1 - MC;
			MN = 1 - MN;
			MOVR = 1 - MOVR;
			break;
		case ResetZ_LoadCarryInvert_µ_CorZ:
		case SetZ_LoadCarryInvert_µ_notCandnotZ:
		case LoadCarryInvert_LoadCarryInvert_µ_CorZ:
		case LoadCarryInvert_LoadCarryInvert_µ_notCandnotZ:
		case LoadCarryInvert_LoadCarryInvert_M_CorZ:
		case LoadCarryInvert_LoadCarryInvert_M_notCandnotZ:
		case LoadCarryInvert_LoadCarryInvert_I_notCorZ:
		case LoadCarryInvert_LoadCarryInvert_I_CandnotZ:
			MZ = input.IZ;
			MC = 1 - input.IC;
			MN = input.IN;
			MOVR = input.IOVR;
			break;
		default:
			MZ = input.IZ;
			MC = input.IC;
			MN = input.IN;
			MOVR = input.IOVR;
			break;
		}
	}

	private void loadY3toMSR() {
		if (input._CEM == _CE_M.H)
			return;
		if (input.Y3 == BitUtil.TRI_STATE_OFF)
			throw new IllegalStateException("Cannot load Y3 in state " + BitUtil.TRI_STATE_OFF);
		int[] bits = BitUtil.single4Bits(input.Y3);
		MZ = bits[0];
		MC = bits[1];
		MN = bits[2];
		MOVR = bits[3];
	}

	private void doShiftLinkage() {
		output.QIO0 = BitUtil.TRI_STATE_OFF;
		output.QIO3 = BitUtil.TRI_STATE_OFF;
		output.SIO0 = BitUtil.TRI_STATE_OFF;
		output.SIO3 = BitUtil.TRI_STATE_OFF;
		if (input._SE == 1)
			return;
		int code = input.mi_shift.value | (input.I10 << 4);
		switch (code) {
		case 0b0_0000:
			output.SIO3 = output.QIO3 = 0;
			break;
		case 0b0_0001:
			output.SIO3 = output.QIO3 = 1;
			break;
		case 0b0_0010:
			output.SIO3 = 0;
			output.QIO3 = MN;
			MC = input.SIO0;
			break;
		case 0b0_0011:
			output.SIO3 = 1;
			output.QIO3 = input.SIO0;
			break;
		case 0b0_0100:
			output.SIO3 = MC;
			output.QIO3 = input.SIO0;
			break;
		case 0b0_0101:
			output.SIO3 = MN;
			output.QIO3 = input.SIO0;
			break;
		case 0b0_0110:
			output.SIO3 = 0;
			output.QIO3 = input.SIO0;
			break;
		case 0b0_0111:
			output.SIO3 = 0;
			output.QIO3 = input.SIO0;
			MC = input.QIO0;
			break;
		case 0b0_1000:
			MC = output.SIO3 = input.SIO0;
			output.QIO3 = input.QIO0;
			break;
		case 0b0_1001:
			output.SIO3 = MC;
			output.QIO3 = input.QIO0;
			MC = input.SIO0;
			break;
		case 0b0_1010:
			output.SIO3 = input.SIO0;
			output.QIO3 = input.QIO0;
			break;
		case 0b0_1011:
			output.SIO3 = input.IC;
			output.QIO3 = input.SIO0;
			break;
		case 0b0_1100:
			output.SIO3 = MC;
			output.QIO3 = input.SIO0;
			MC = input.QIO0;
			break;
		case 0b0_1101:
			MC = output.SIO3 = input.QIO0;
			output.QIO3 = input.SIO0;
			break;
		case 0b0_1110:
			output.SIO3 = input.IN ^ input.IOVR;
			output.QIO3 = input.SIO0;
			break;
		case 0b0_1111:
			output.SIO3 = input.QIO0;
			output.QIO3 = input.SIO0;
			break;
		case 0b1_0000:
			output.SIO0 = output.QIO0 = 0;
			MC = input.SIO3;
			break;
		case 0b1_0001:
			output.SIO0 = output.QIO0 = 1;
			MC = input.SIO3;
			break;
		case 0b1_0010:
			output.SIO0 = output.QIO0 = 0;
			break;
		case 0b1_0011:
			output.SIO0 = output.QIO0 = 1;
			break;
		case 0b1_0100:
			output.SIO0 = input.QIO3;
			output.QIO0 = 0;
			MC = input.SIO3;
			break;
		case 0b1_0101:
			output.SIO0 = input.QIO3;
			output.QIO0 = 1;
			MC = input.SIO3;
			break;
		case 0b1_0110:
			output.SIO0 = input.QIO3;
			output.QIO0 = 0;
			break;
		case 0b1_0111:
			output.SIO0 = input.QIO3;
			output.QIO0 = 1;
			break;
		case 0b1_1000:
			output.SIO0 = input.SIO3;
			output.QIO0 = input.QIO3;
			MC = input.SIO3;
			break;
		case 0b1_1001:
			output.SIO0 = MC;
			output.QIO0 = input.QIO3;
			MC = input.SIO3;
			break;
		case 0b1_1010:
			output.SIO0 = input.SIO3;
			output.QIO0 = input.QIO3;
			break;
		case 0b1_1011:
			output.SIO0 = MC;
			output.QIO0 = 0;
			break;
		case 0b1_1100:
			output.SIO0 = input.QIO3;
			output.QIO0 = MC;
			MC = input.SIO3;
			break;
		case 0b1_1101:
			output.SIO0 = input.QIO3;
			output.QIO0 = input.SIO3;
			MC = input.SIO3;
			break;
		case 0b1_1110:
			output.SIO0 = input.QIO3;
			output.QIO0 = MC;
			break;
		case 0b1_1111:
			output.SIO0 = input.QIO3;
			output.QIO0 = input.SIO3;
			break;
		default:
			throw new IllegalArgumentException("illegal am2904 shift code:" + code);
		}
	}

	public final int getµC() {
		return µC;
	}

	public final int getµN() {
		return µN;
	}

	public final int getµZ() {
		return µZ;
	}

	public final int getµOVR() {
		return µOVR;
	}

	public final int getMC() {
		return MC;
	}

	public final int getMN() {
		return MN;
	}

	public final int getMZ() {
		return MZ;
	}

	public final int getMOVR() {
		return MOVR;
	}

	public final void setµC(int µC) {
		this.µC = µC & 1;
	}

	public final void setµN(int µN) {
		this.µN = µN & 1;
	}

	public final void setµZ(int µZ) {
		this.µZ = µZ & 1;
	}

	public final void setµOVR(int µOVR) {
		this.µOVR = µOVR & 1;
	}

	public final void setMC(int mC) {
		MC = mC & 1;
	}

	public final void setMN(int mN) {
		MN = mN & 1;
	}

	public final void setMZ(int mZ) {
		MZ = mZ & 1;
	}

	public final void setMOVR(int mOVR) {
		MOVR = mOVR & 1;
	}

	public void reset() {
		setµC(0);
		setµN(0);
		setµZ(0);
		setµOVR(0);
		setMC(0);
		setMN(0);
		setMZ(0);
		setMOVR(0);
	}
}

class Am2904input {
	Am2904_Inst mi_inst;
	Am2904_Carry mi_carry;
	Am2904_Shift mi_shift;
	int I10;
	int CX;
	int Y3;
	int IZ;
	int IC;
	int IOVR;
	int IN;
	_CE_M _CEM;
	_CE_µ _CEµ;
	int _OEY;
	int _OECT;
	int _SE;
	int _EZ;
	int _EC;
	int _EOVR;
	int _EN;
	int SIO0;
	int SIO3;
	int QIO0;
	int QIO3;
}

class Am2904output {
	int C0;
	int CT;
	int Y3;
	int SIO0;
	int SIO3;
	int QIO0;
	int QIO3;
}