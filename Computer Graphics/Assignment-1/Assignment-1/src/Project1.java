/***************************************************************
* file: Primitive.java
* author: Kyle Turchik
* class: CS 445 - Computer Graphics
*
* assignment: program 1
* date last modified: 10/11/2016
*
* purpose: This program reads in instructions and coordinates from 
* a file and draws primitive shapes based on that information.
*
****************************************************************/ 

import java.util.Scanner;
import java.io.*;
import java.util.concurrent.TimeUnit;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

public class Project1 {
    
    public int colorConfig = 1;
    public int pointSize = 1;

// method: start
// purpose: This method calls the methods which initialize the canvas and
// sequentially reads the data from the coordinates file.  It also polls
// the keyboard for commands.
    public void start() {
        try {
            createWindow();
            initGL();

            File file = new File("coordinates.txt");
            
            //***SPECIAL CONTROLS***
            //Escape - Closes Window
            //Left & Right Arrow - Cycles colors of shapes
            //(-) & (=) Keys - Decrease or increase point size
            while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    if(colorConfig == 3){
                        colorConfig = 1;
                    } else{
                        colorConfig++;
                    }
                    TimeUnit.MILLISECONDS.sleep(200);
                }
                if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    if(colorConfig == 1){
                        colorConfig = 3;
                    } else{
                        colorConfig--;
                    }
                    TimeUnit.MILLISECONDS.sleep(200);
                }
                if(Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
                    if(pointSize < 10)
                        pointSize++;
                }
                if(Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
                    if(pointSize > 1)
                        pointSize--;
                }
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                
                //render
                readFile(file); 
                Display.update();
                Display.sync(60);
            }          
            Display.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // method: createWindow
    // purpose: initializes the window
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("Kyle Turchik - Project 1");
        Display.create();
    }
 
    
    // method: initGL
    // purpose: initializes original GL settings
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 0, 480, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    
    // method: drawLine
    // purpose: accepts 2 sets of (x,y) coordinates to draw a line between them
    private void drawLine(int x1, int y1, int x2, int y2) {
        switch(colorConfig){
            case 2:
                glColor3f(0.0f,1.0f,0.0f);
                break;
            case 3:
                glColor3f(0.0f,0.0f,1.0f);
                break;
            default:
                glColor3f(1.0f,0.0f,0.0f);
                break;
        }
        glPointSize(pointSize);

        //Modified Midpoint Line Algorithm
        glBegin(GL_POINTS);
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int d = 0;
                
        int ix = x1 < x2 ? 1 : -1;
        int iy = y1 < y2 ? 1 : -1;
                
        if(dy <= dx){
            while(x1 != x2){
                glVertex2f (x1, y1);
                x1 += ix;
                d += dy;

                if(d > dx) {
                    y1 += iy;
                    d -= dx;
                } 
            }
        } else {
            while(y1 != y2){
                glVertex2f (x1, y1);
                y1 += iy;
                d += dx;

                if(d > dy) {
                    x1 += ix;
                    d -= dy;
                } 
            }
        }
        glEnd();
    }

  
    // method: drawCircle
    // purpose: accepts 1 pair of (x,y) coordinates for the center of the 
    // circle and a radius to draws a circle
    private void drawCircle(float x, float y, float rad) {
        switch(colorConfig){
            case 2:
                glColor3f(1.0f,0.0f,0.0f);
                break;
            case 3:
                glColor3f(0.0f,1.0f,0.0f);
                break;
            default:
                glColor3f(0.0f,0.0f,1.0f);
                break;
        }
        glPointSize(pointSize);
                
        int lineAmount = 720;

        float doublePi = 2.0f * (float) Math.PI;

        glBegin(GL_POINTS);
            for(int i = 0; i <= lineAmount; i++) { 
                glVertex2f(
                    x + (rad * (float) Math.cos(i *  doublePi / lineAmount)), 
                    y + (rad * (float) Math.sin(i * doublePi / lineAmount))
                );
            }
        glEnd();
    }

    
    // method: drawEllipse
    // purpose: accepts 1 pair of (x,y) coordinates for the center of the 
    // ellipse and a pair of radii to draw an ellipse   
    private void drawEllipse(float x, float y, float radx, float rady) {                 
        switch(colorConfig){
            case 2:
                glColor3f(0.0f,0.0f,1.0f);
                break;
            case 3:
                glColor3f(1.0f,0.0f,0.0f);
                break;
            default:
                glColor3f(0.0f,1.0f,0.0f);
                break;
        }
        glPointSize(pointSize);
                
        int lineAmount = 720; 

        float twicePi = 2.0f * (float) Math.PI;

        glBegin(GL_POINTS);
        for(int i = 0; i <= lineAmount; i++) { 
            glVertex2f(
                x + (radx * (float) Math.cos(i *  twicePi / lineAmount)), 
                y + (rady * (float) Math.sin(i * twicePi / lineAmount))
            );
        }
        glEnd();
    }
 
    
    // method: readFile
    // purpose: sequentially reads in data line by line from a premade file
    private void readFile(File file) throws IOException {
        Scanner reader = new Scanner(file);
        String[] first = new String[2];
        String[] second = new String[2];
            
        while (reader.hasNext()){ 
            String type = reader.next();
            first = reader.next().split(",");
            switch (type) {
                case "l":
                    second = reader.next().split(",");
                    drawLine(Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(second[0]), Integer.parseInt(second[1]));
                    break;
                case "c":
                    drawCircle(Integer.parseInt(first[0]), Integer.parseInt(first[1]), reader.nextInt());
                    break;
                case "e":
                    second = reader.next().split(",");
                    drawEllipse(Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(second[0]), Integer.parseInt(second[1]));
                    break;
            }
        }
    }
 
    
    // method: main
    // purpose: basic driver method that starts the program
    public static void main(String[] args) throws IOException {
        Project1 primitive = new Project1();
        primitive.start();
    }
}
