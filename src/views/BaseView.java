package views;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BaseView {
	protected static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public static void successMsg(String details) {
		System.out.println("SUCCESS: " + details);
	}
}