package Co.Cleveland.KryptoChatServer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Lehmer {
	
	public int[] numberToFactoradic(int num) {
		int dividend = num;
		int[] result = new int[6];
		int divisor = 1;
		int i = 5;
		while(dividend != 0) {
			final int remainder = dividend % divisor;
			dividend /= divisor;
			result[i] = remainder;
			divisor++;
			i--;
		}
		return result;
	}
	
	public byte[] factoradicToPermutation(int[] fact, byte[] b) {
		byte[] ret = new byte[6];
		Byte[] temp = new Byte[6];
		for(int i = 0; i < 6; i++) {
			temp[i] = b[i];
		}
		List<Byte> l = new LinkedList<Byte>(Arrays.asList(temp));
		for(int i = 0; i < 6; i++) {
			ret[i] = l.get(fact[i]);
			l.remove(fact[i]);
		}
		return ret;
	}

}

