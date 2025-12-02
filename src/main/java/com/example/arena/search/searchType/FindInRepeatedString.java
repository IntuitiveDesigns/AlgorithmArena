package com.example.arena.search.searchType;

public class FindInRepeatedString {
	String sample1 = "kmretasscityylpdhuwjirnqimlkcgxubxmsxpypgzxtenweirknjtasxtvxemtwxuarabssvqdnktqadhyktagjxoanknhgilnm"; 
	String sample2 = "ababa";
	
	public void runTests() {
		System.out.println(this.repeatedString(sample1, 736778906400L));
		System.out.println(this.repeatedString(sample2, 4L));
	}
	
	public long repeatedString(String s, long n) {
		long numA = 0;

		if (s.length() == 1 && s.charAt(0) == 'a') {
			numA = n;
		} else {
			if (n > s.length()) {
				for (int x = 0; x < s.length(); x++) {
					if (s.charAt(x) == 'a')
						numA++;
				}
				numA = n / s.length() * numA;

				for (int x = 0; x < n % s.length(); x++) {
					if (s.charAt(x) == 'a')
						numA++;
				}
			} else {
				for (int x = 0; x < n; x++) {
					if (s.charAt(x) == 'a')
						numA++;
				}
			}
		}
		return numA;
	}
}
