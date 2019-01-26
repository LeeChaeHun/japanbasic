package com.example.myapplication;


import java.util.Arrays;
import java.util.Collections;

public class FileSplit1 {
    public static String questionNum2[][] = new String[100][10];
    public static String questionNum[][] = new String[100][10];
    int count =  0 ;


    public FileSplit1(String str) {
        String tmp[] = str.split("\n");
        String s;

        for (int i = 0; i < tmp.length; i++) {
            s = tmp[i];
            String tmp2[] = s.split(":");
            for (int j = 0; j < 6; j++) {
                tmp2[j] = tmp2[j].trim();
                questionNum2[i][j] = tmp2[j];
            }

        }

        makeThousand();
    }

    public void makeThousand() {
        int selectedQuestion;
        double randomNum;

        for (int i = 0; i < 100; i++) {
            questionNum2[i][5] = "";

        }
        for (int i = 0; i < 100; i++) {

            do {
                randomNum = Math.random();
                selectedQuestion = (int) ((randomNum * (100)));
            } while (questionNum2[selectedQuestion][5] == "yes");

            for (int j = 0; j < 6; j++) {
                FileSplit1.questionNum[i][j] = FileSplit1.questionNum2[selectedQuestion][j];
                FileSplit1.questionNum[i][0] = Integer.toString(i + 1);
            }
            questionNum2[selectedQuestion][5] = "yes";
                for (int j = 0; j < 6; j++) {
                    FileSplit1.questionNum[i][j] = FileSplit1.questionNum2[selectedQuestion][j];
                   FileSplit1.questionNum[i][0] = Integer.toString(i + 1);
           }
                            }
        }
    }



