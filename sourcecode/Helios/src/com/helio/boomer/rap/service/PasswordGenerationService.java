package com.helio.boomer.rap.service;

import java.util.Random;

public class PasswordGenerationService {

	public String getUserPassword() {
		char[] values1 = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
				'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
				'u', 'v', 'w', 'x', 'y', 'z' };
		char[] values2 = { '@', '!', '$', '#', '%', '*' };
		char[] values3 = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		String out1 = "";
		String out2 = "";
		String out3 = "";
		String out = "";
		Random rand = new Random();

		for (int i = 0; i < 3; i++) {
			int idx = rand.nextInt(values1.length);
			out1 += values1[idx];
		}

		for (int i = 0; i < 3; i++) {
			int idx = rand.nextInt(values3.length);
			out2 += values3[idx];
		}

		for (int i = 0; i < 2; i++) {
			int idx = rand.nextInt(values2.length);
			out3 += values2[idx];
		}

		out = out1.concat(out3).concat(out2);
		return out;
	}

}
