package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class StudyView extends SurfaceView implements Callback {
    static Bitmap background;


    static int soundOk = 1;
    int questionNumber = 0;
    int numberOfquestion = 99;

    int textSizeForG4 = 0;
    int textSizeChanging = 0;
    int textSizeChanging2 = 0;


    int starIng = 0;
    int starIndex = 0;
    int starX, starY;


    //값이 1이면 [단어장등록] 아이콘이 화면에 제시된다.


    double rand;
    int btnPressed=0;

    String[] wordForDelete = {"", "", "", "", ""};
    String wordToDelete = "";

    // SurfaceView
    static StudyThread mThread;
    SurfaceHolder mHolder;
    static Context mContext;

    FileTable mFile;

    //db
    MyDBHelper m_helper;

    Cursor cursor;
    int dicOk = 0;
    int movePosition = 0;


    MyButton1 btnPrevious;    //btnPrevious : next
    MyButton1 btnNext;   //btnPrevious : previous
    MyButton1 btnWordSelection;
    MyButton1 btnMyNote;
    MyButton1 btnExit;
    MyButton1 btnRandom;   //btn : random
    MyButton1 btnNum1;   //btn : number1
    MyButton1 btnNum2;   //btn : number2
    MyButton1 btnNum3;  //btn : number3
    MyButton1 btnNum4;  //btn : number4
    MyButton1 btnPreviousQuestion;
    MyButton1 btnSolveAgain;

    String whichSubject="선택단어 1";

    //sub menu
    MyButton1 btnSub1;
    MyButton1 btnSub2;
    MyButton1 btnSub3;
    MyButton1 btnSub4;
    MyButton1 btnSub5;
    MyButton1 btnSub6;
    MyButton1 btnSub7;
    MyButton1 btnSub8;

    MyButton1 btnWordSave;



    MyButton1 btnLeftArrow;   //left
    MyButton1 btnRightArrow;   //right
    MyButton1 btnClose;   //close button in circle

    MyButton1 btnForDictionary[];

    //단어 전체 삭제 버튼: 여기서는 사용안함
    MyButton1 btnAllDelete;




    int btnPreCount = 0;
    int btnPreviousCount = 0;
    int btnSelectCount = 0;
    int btnMyNoteCount = 0;
    int btnRanCount = 0 ;

    int btnNum1Count = 0;
    int btnNum2Count = 0;
    int btnNum3Count = 0;
    int btnNum4Count = 0;



    static int Width, Height;                    // View
    int subNumber=1;

    Bitmap answerx;
    Bitmap answero;

    Bitmap cap;
    Bitmap explain;
    Bitmap star[] = new Bitmap[4];   //1,2,3,4 버튼 클릭시 원이 나옴.
    static SoundPool sdPool;
    static int dingdongdaeng, taeng;


    public StudyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mHolder = holder;
        mContext = context;
        mThread = new StudyThread(holder, context);


        initAll();
        makeQuestion(subNumber);

        setFocusable(true);
    }


    private void initAll() {
        m_helper = new MyDBHelper(mContext, "testforeng.db", null, 1);
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Width = display.getWidth();
        Height = display.getHeight();
        textSizeChanging = (int) (Width * 64 / 1280);
        if (Width > 1700) textSizeForG4 = 120;

        mFile = new FileTable();

        btnPrevious = new MyButton1(30, 34, 0);  //previous
        btnNext = new MyButton1(btnPrevious.x + btnPrevious.w * 2, 34, 1);  //next
        btnWordSelection = new MyButton1(btnNext.x + btnPrevious.w * 2, 34, 2);  //단어선택

        btnMyNote = new MyButton1(btnWordSelection.x + btnPrevious.w * 2, 34, 4);  //내노트
        btnExit = new MyButton1(Width - btnPrevious.w * 2 - btnPrevious.w / 3, 34, 5);  //exit
        btnRandom = new MyButton1(btnMyNote.x + btnPrevious.w * 2, 34, 6); // random button

        btnNum1 = new MyButton1(btnPrevious.x + 70 + 50, btnPrevious.y + btnPrevious.h * 2 + 93, 7); // number 1
        btnNum2 = new MyButton1(btnPrevious.x + 70 + 50, btnNum1.y + btnNum1.h * 2 + 8, 8); // number 2
        btnNum3 = new MyButton1(btnPrevious.x + 70 + 50, btnNum2.y + btnNum2.h * 2 + 8, 9); // number 3
        btnNum4 = new MyButton1(btnPrevious.x + 70 + 50, btnNum3.y + btnNum3.h * 2 + 8, 10); // number 4


        btnPreviousQuestion = new MyButton1(Width - btnWordSelection.w * 6, btnNum1.y + btnNum1.h * 2 + 1, 12); //다음문제
        btnSolveAgain = new MyButton1(Width - btnWordSelection.w * 6, btnPreviousQuestion.y + btnPreviousQuestion.h * 2 + 1, 13); //다시풀기

        btnWordSave = new MyButton1(Width - btnWordSelection.w * 6, btnSolveAgain.y + btnSolveAgain.h * 2 + 1, 23); //단어장등록



        // sub menu 단어 선택 메뉴
        btnSub1 = new MyButton1(btnNext.x + 10, btnWordSelection.y + btnWordSelection.h * 2 + 5, 15);
        btnSub2 = new MyButton1(btnSub1.x + btnSub1.w * 2, btnSub1.y, 16);
        btnSub3 = new MyButton1(btnSub2.x + btnSub2.w * 2, btnSub1.y, 17);
        btnSub4 = new MyButton1(btnSub3.x + btnSub3.w * 2, btnSub1.y, 18);
        btnSub5 = new MyButton1(btnSub4.x + btnSub4.w * 2, btnSub1.y, 19);
        btnSub6 = new MyButton1(btnNext.x + 10, btnSub1.y + btnSub1.h * 2, 20);
        btnSub7 = new MyButton1(btnSub1.x + btnSub1.w * 2, btnSub1.y + btnSub1.h * 2, 21);
        btnSub8 = new MyButton1(btnSub2.x + btnSub2.w * 2, btnSub1.y + btnSub1.h * 2, 22);

        // 내사전에서 왼쪽, 오른쪽 버튼
        btnLeftArrow = new MyButton1(btnNext.x, Height - btnPrevious.h * 2, 26);    // left arrow
        btnRightArrow = new MyButton1(btnNext.x + 150, Height - btnPrevious.h * 2, 27);  //right arrow

        // 닫기 버튼
        btnClose = new MyButton1(Width - btnPrevious.w *2,  btnPrevious.h , 28); //close button in dic
        btnForDictionary = new MyButton1[5];


        // 삭제버튼
        for (int i = 0; i < 5; i++)
            btnForDictionary[i] = new MyButton1(btnExit.w * 20, btnExit.h * 3 + btnExit.w * 3 / 2 * i, 29);
}


    public void makeQuestion(int x) {
        mFile.loadFile(x);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.setRunning(true);
        try {

            if (mThread.getState() == Thread.State.TERMINATED) {

                mThread = new StudyThread(getHolder(), mContext);
                mThread.setRunning(true);
                setFocusable(true);
                mThread.start();
            } else {
                mThread.start();
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        StopStudy();

        boolean retry = true;
        mThread.setRunning(false);
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (Exception e) {
            }
        }

    }

    public static void StopStudy() {
        mThread.StopThread();
    }

    class StudyThread extends Thread {
        boolean canRun = true;
        boolean isWait = false;
        Paint paint = new Paint();
        Paint paint2 = new Paint();
     
        Paint paint3 = new Paint();


        public StudyThread(SurfaceHolder holder, Context context) {

            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.create("", Typeface.BOLD));

            paint2.setColor(Color.GRAY);
            paint2.setAntiAlias(true);
            paint2.setTypeface(Typeface.create("", Typeface.BOLD));

            paint3.setColor(Color.BLACK);
            paint3.setAntiAlias(true);
            paint3.setTypeface(Typeface.create("", Typeface.BOLD));

            paint.setTextSize(TypedValue.COMPLEX_UNIT_DIP);
            paint2.setTextSize(35);
            paint3.setTextSize(40);

        }

        public void setRunning(boolean b) {
            // TODO Auto-generated method stub

        }

        public void DrawAll(Canvas canvas) {


            Paint pp = new Paint();
            Paint frame = new Paint();
            background=  BitmapFactory.decodeResource(getResources(),R.drawable.background2);
            background= Bitmap.createScaledBitmap(background,Width,Height,true);

            canvas.drawBitmap(background,0 ,0,null);
            textSizeChanging = (int) (Width * 58 / 1280);  // aha here I have to adjust
            textSizeChanging2 = (int) (Width * 40 / 1280);

            //텍스트 크기 설정
            paint.setTextSize(Width/23); // 큰 검정글씨
            paint2.setTextSize(Width/40);  //작은 회색글씨
            paint3.setTextSize(Width/34);  //작은 검정글씨


            if (dicOk==0) {

                if (questionNumber > numberOfquestion) questionNumber = numberOfquestion;
                if (questionNumber < 0) questionNumber = 0;

                canvas.drawText(FileSplit0.questionNum[questionNumber][0],
                        btnPrevious.x + btnNum1.w, btnPrevious.y + btnPrevious.h * 3, paint); //번호
                canvas.drawText(FileSplit0.questionNum[questionNumber][1],
                        btnPrevious.x + btnNum1.w *3, btnPrevious.y + btnPrevious.h * 3, paint); //일본어
                canvas.drawText(FileSplit0.questionNum[questionNumber][2],
                        btnPrevious.x+btnNum1.w*3,btnPrevious.y+btnPrevious.h*4,paint);
                canvas.drawText(FileSplit0.questionNum[questionNumber][3],
                        btnPrevious.x + btnNum2.w * 0, btnNum2.y + btnNum2.w + btnNum2.w*2, paint2); //1
                canvas.drawText(FileSplit0.questionNum[questionNumber][4],
                        btnPrevious.x + btnNum3.w * 0, btnNum3.y + btnNum3.w + btnNum3.w/2, paint3); //2


            }


            if (dicOk == 0) {
                canvas.drawBitmap(btnPrevious.button_img, btnPrevious.x, btnPrevious.y, null);
                canvas.drawBitmap(btnNext.button_img, btnNext.x, btnNext.y, null);
                canvas.drawBitmap(btnWordSelection.button_img, btnWordSelection.x, btnWordSelection.y, null);

                canvas.drawBitmap(btnMyNote.button_img, btnMyNote.x, btnMyNote.y, null);
                canvas.drawBitmap(btnExit.button_img, btnExit.x, btnExit.y, null);
                canvas.drawBitmap(btnRandom.button_img, btnRandom.x, btnRandom.y, null);
            }

            int imsy;






            if (starIng == 1) {

                starIndex += 1;
                if (starIndex >= 15) {
                    starIng = 0;
                    starIndex = 0;
                } else
                    canvas.drawBitmap(star[starIndex / 4], starX - starIndex / 4, starY - starIndex / 4, null);
            }



            if (btnPreCount == 15) {
                btnPreCount = 0;
                btnNext.btn_released();
            }

            if (btnPreviousCount == 15) {
                btnPreviousCount = 0;
                btnPrevious.btn_released();
            }

            if (btnSelectCount == 15) {
                btnSelectCount = 0;
                btnWordSelection.btn_released();
            }

            if (btnMyNoteCount == 15) {
                btnMyNoteCount = 0;
                btnMyNote.btn_released();
            }

            if (btnRanCount == 15) {
                btnRanCount = 0;
                btnRandom.btn_released();
            }

            if (btnNum1Count == 15) {
                btnNum1Count = 0;
                btnNum1.btn_released();
            }
            if (btnNum2Count == 15) {
                btnNum2Count = 0;
                btnNum2.btn_released();
            }
            if (btnNum3Count == 15) {
                btnNum3Count = 0;
                btnNum3.btn_released();
            }
            if (btnNum4Count == 15) {
                btnNum4Count = 0;
                btnNum4.btn_released();
            }

            // 내노트 나타나기
            if (dicOk == 1) {

                pp.setColor(Color.rgb(243,201,202));
                canvas.drawRect(0,0,Width,Height,pp);



                SQLiteDatabase db = m_helper.getReadableDatabase();

                cursor = db.query("englishWordTable", null, null, null, null, null, null);

                int numofdb = cursor.getCount();

                if (movePosition > numofdb) movePosition -= 5;
                else if (movePosition == numofdb) movePosition -= 5;

                if (movePosition <= 0) movePosition = 0;

                for (int i = 0; i < 5; i++) {
                    if (cursor.moveToPosition(movePosition + i) == false) break;
                    canvas.drawText((movePosition + i + 1) + " " + cursor.getString(1) + " : "
                            + cursor.getString(2), btnExit.w * 3, btnExit.h * 4 + btnExit.w * 3 / 2 * i, paint);
                    //현재 내노트에 있는 단어 5개 대한 영어단어를 삭제를 위해 wordForDelete[]에담는다.
                    wordForDelete[i] = cursor.getString(1);
                }

                //reft, right arrow  and close button in circle format
                canvas.drawBitmap(btnLeftArrow.button_img, btnLeftArrow.x, btnLeftArrow.y, null);
                canvas.drawBitmap(btnRightArrow.button_img, btnRightArrow.x, btnRightArrow.y, null);
                canvas.drawBitmap(btnClose.button_img, btnClose.x, btnClose.y, null);

                int x = 0;
                     for (int i = 0; i < 5; i++) {
                    canvas.drawText("저장된 단어수 : " + Integer.toString(numofdb), 100, 100, paint2);
                    imsy = 0;
                    if (numofdb == 0) {
                        canvas.drawText("단어가 없습니다!", 70, 180 + 90 * i, paint);
                        break;
                    }

                    canvas.drawBitmap(btnForDictionary[i].button_img, btnExit.w*20, btnExit.h * 3 + btnExit.w * 3/ 2 * i, null);
                    x = (numofdb - 1) / 5;
                    if ((movePosition) / 5 < x) imsy = 1;
                    else imsy = 0;

                    if (imsy == 0) {
                        if (numofdb % 5 == 4 && i == 3) break;
                        if (numofdb % 5 == 3 && i == 2) break;
                        if (numofdb % 5 == 2 && i == 1) break;
                        if (numofdb % 5 == 1 && i == 0) break;
                    }
                }
                cursor.close();
                db.close();
            }
        }               // end of drawall


        public void run() {
            Canvas canvas = null;
            while (canRun) {
                canvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        DrawAll(canvas);
                    } // sync
                } finally {
                    if (canvas != null)
                        mHolder.unlockCanvasAndPost(canvas);
                } // try


                synchronized (this) {
                    if (isWait)
                        try {
                            wait();
                        } catch (Exception e) {
                            // nothing
                        }
                } // sync

            } // while
        } // run


        public void StopThread() {
            canRun = false;
            synchronized (this) {
                this.notify();
            }
        }


    } // Thread

    // keykey
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = 0, y = 0;

    //    synchronized (mHolder) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                x = (int) event.getX();
                y = (int) event.getY();

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

            }
    //    }   // end of sync
        //이전 버튼 클릭시
        if (dicOk==0 &&x > btnPrevious.x && x < (btnPrevious.x + btnPrevious.w * 2) && y > btnPrevious.y && y < (btnPrevious.y + btnPrevious.h * 2)) {
            questionNumber -= 1;                        //문제 번호 -1
            btnPreCount = 0;
            btnPressed=1;
            btnPrevious.btn_press();

            dicOk = 0;
        }

        //다음  버튼 클릭시
        if (dicOk==0&& x > btnNext.x && x < (btnNext.x + btnNext.w * 2) && y > btnNext.y && y < (btnNext.y + btnNext.h * 2)) {
            questionNumber += 1;
            btnPreCount = 0;
            btnPressed=1;
            btnNext.btn_press();

            dicOk = 0;
        }


        if (dicOk==0&& x > btnWordSelection.x && x < (btnWordSelection.x + btnWordSelection.w * 2) && y > btnWordSelection.y && y < (btnWordSelection.y + btnWordSelection.h * 2)) {
            btnWordSelection.btn_press();

            SQLiteDatabase db = m_helper.getWritableDatabase();
            String sql = String.format("INSERT INTO englishWordTable VALUES(NULL, '%s', '%s');",
                    // sss+1 은 정답에 해당되는 영어단어
                    //questionNum[questionNumber][1] 에서 첨자 1에 들어가는 것은 단어뜻이다.(한글)
                    FileSplit0.questionNum[questionNumber][1], FileSplit0.questionNum[questionNumber][2]);
            db.execSQL(sql);
            db.close();
            Toast toast2 = Toast.makeText(mContext, "노트에 저장되었습니다.", Toast.LENGTH_SHORT);
            toast2.show();
        }

        if (dicOk==0&& x > btnMyNote.x && x < (btnMyNote.x + btnMyNote.w * 2) && y > btnMyNote.y && y < (btnMyNote.y + btnMyNote.h * 2)) {
            btnMyNote.btn_press();
            btnMyNoteCount = 0;
            btnPressed=1;
            dicOk = 1;

        }

        //exit button
        if (dicOk==0&& dicOk != 1)
            if (x > btnExit.x && x < (btnExit.x + btnExit.w * 2) && y > btnExit.y && y < (btnExit.y + btnExit.h * 2)) {
                btnPressed=1;
                System.exit(0);

                dicOk = 0;
            }


        if (dicOk==0&& x > btnRandom.x && x < (btnRandom.x + btnRandom.w * 2) && y > btnRandom.y && y < (btnRandom.y + btnRandom.h * 2)) {
            rand = Math.random();
            questionNumber = (int) ((rand * (numberOfquestion + 2)));

            btnRanCount = 0;
            btnPressed=1;
            btnRandom.btn_press();

            dicOk = 0;
        }

        // left arrow button in circle
        if (dicOk == 1)
            if (x > btnLeftArrow.x && x < (btnLeftArrow.x + btnLeftArrow.w * 2) && y > btnLeftArrow.y && y < (btnLeftArrow.y + btnLeftArrow.h * 2)) {
                btnLeftArrow.btn_press();

                dicOk = 1;
                movePosition -= 5;

                if (movePosition < 0) movePosition = 0;
            }

        // right arrow button in circle
        if (dicOk == 1)
            if (x > btnRightArrow.x && x < (btnRightArrow.x + btnRightArrow.w * 2) && y > btnRightArrow.y && y < (btnRightArrow.y + btnRightArrow.h * 2)) {
                btnRightArrow.btn_press();

                movePosition += 5;
            }

        if (dicOk == 1 )
            if (x > btnClose.x && x < (btnClose.x + btnClose.w * 2) && y > btnClose.y && y < (btnClose.y + btnClose.h * 2)) {

                try {
                    Thread.sleep(120);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                btnClose.btn_press();

                dicOk = 0;
            }


        if (dicOk == 1) {
            if (x > btnForDictionary[0].x && x < (btnForDictionary[0].x + btnForDictionary[0].w * 2) && y > btnForDictionary[0].y && y < (btnForDictionary[0].y + btnForDictionary[0].h * 2)) {
                if (wordForDelete[0] != null) wordToDelete = wordForDelete[0];
                Toast t1  = Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT);
                t1.show();
            }
            if (x > btnForDictionary[1].x && x < (btnForDictionary[1].x + btnForDictionary[1].w * 2) && y > btnForDictionary[1].y && y < (btnForDictionary[1].y + btnForDictionary[1].h * 2)) {
                if (wordForDelete[1] != null) wordToDelete = wordForDelete[1];
                Toast t2  = Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT);
                t2.show();
            }
            if (x > btnForDictionary[2].x && x < (btnForDictionary[2].x + btnForDictionary[2].w * 2) && y > btnForDictionary[2].y && y < (btnForDictionary[2].y + btnForDictionary[2].h * 2)) {
                if (wordForDelete[2] != null) wordToDelete = wordForDelete[2];
                Toast t3  = Toast.makeText(getContext(),"3",Toast.LENGTH_SHORT);
                t3.show();
            }
            if (x > btnForDictionary[3].x && x < (btnForDictionary[3].x + btnForDictionary[3].w * 2) && y > btnForDictionary[3].y && y < (btnForDictionary[3].y + btnForDictionary[3].h * 2)) {
                if (wordForDelete[3] != null) wordToDelete = wordForDelete[3];
                Toast t4  = Toast.makeText(getContext(),"4",Toast.LENGTH_SHORT);
                t4.show();
            }
            if (x > btnForDictionary[4].x && x < (btnForDictionary[4].x + btnForDictionary[4].w * 2) && y > btnForDictionary[4].y && y < (btnForDictionary[4].y + btnForDictionary[4].h * 2)) {
                if (wordForDelete[4] != null) wordToDelete = wordForDelete[4];
                Toast t5  = Toast.makeText(getContext(),"5",Toast.LENGTH_SHORT);
                t5.show();
            }

            SQLiteDatabase db = m_helper.getWritableDatabase();

            String sql = String.format("DELETE FROM englishWordTable WHERE eWord = '%s'", wordToDelete);
            db.execSQL(sql);

            try {
                Thread.sleep(130);
            } catch (InterruptedException e) {

            }
            db.close();
        }



        return false;
    }  //End of onTouchEvent



    //-------------------------------------
    //  onKeyDown
    //-------------------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        synchronized (mHolder) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    break;
                default:

            }
        }
        return false;
    }

    class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE englishWordTable (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " eWord TEXT, kWord TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS englishWordTable");
            onCreate(db);
        }
    }          //end of MyDBHelper



} // End of SurfaceView
