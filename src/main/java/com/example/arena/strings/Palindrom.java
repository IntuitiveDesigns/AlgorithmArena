package com.example.arena.strings;

/**
 * A palindrome number is a number that is exactly the same number when written backwards.
 * Example: 12021
 * @author sa1lo
 *
 */
public class Palindrom {

	int r, t;
	
	public boolean isPalindrome(int num) {
		boolean isPalindrome = false;
		t = num;
		
		while (t != 0) {
			r = r * 10;
			r = r + t%10;
			t = t/10;
		}
		
		if (num == r) {
			isPalindrome = true;
		} 
		
		return isPalindrome;
	}
}
