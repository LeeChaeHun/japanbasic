package com.example.myapplication;

public class FileSplit0 {
	public static String questionNum[][] = new String[100][6];

	public FileSplit0(String str) {
		String tmp[] = str.split("\n");
		String s;

		for (int i = 0; i < tmp.length; i++) {
	
			s = tmp[i];
			String tmp2[] = s.split(":");
			
			for(int j = 0; j <6  ; j++){
				tmp2[j]=tmp2[j].trim();
			questionNum[i][j]=tmp2[j];
					
			}
		}
	}

}
