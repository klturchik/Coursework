/***************************************************************
* file: Project2.java
* author: Kyle Turchik
* class: CS 445 - Computer Graphics
*
* assignment: program 2
* date last modified: 10/27/2016
*
* purpose: This program reads in instructions and coordinates from 
* a file in order to draw polygons, fill them using the scanline 
* algorithm, and perform specified transformations on the shapes.
*
****************************************************************/ 

import java.io.File;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.Scanner;

public class Project2 {

    // Main Window settings
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final String WINDOW_TITLE = "Kyle Turchik - Project 2";
    
    ArrayList pInstr = new ArrayList();
    ArrayList tInstr = new ArrayList();
    public static int counter = 0;

    // method: main
    // purpose: basic driver method that starts the program
    public static void main(String[] args) throws Exception {
        Project2 proj = new Project2();
        proj.start();
    }
    
    // method: start
    // purpose: This method calls the methods which initialize the canvas and
    // sequentially read the data from the coordinates file.
    private void start() throws Exception {
        try {
            createWindow();
            initGL();
            
            File file = new File("coordinates.txt");

            while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){   
                //render
                readFile(file); 
                Display.update();
                Display.sync(60);
            }
            Display.destroy();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // method: readFile
    // purpose: sequentially reads in data line by line from a premade file
    private void readFile(File file) throws Exception {
        pInstr.clear();
        String type, set[];
        
        try (Scanner in = new Scanner(file)) {
            while (in.hasNext()) {
                pInstr.add(in.nextLine());
            }
            
            while (counter < pInstr.size()) {
                type = (String) pInstr.get(counter);
                counter++;
                set = type.split(" ");
                if (set[0].equals("P")) {
                    createPoly(set);
                }
            }
            counter = 0;
        }
    }

    // method: createWindow
    // purpose: initializes the window
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT));
        Display.setTitle(WINDOW_TITLE);
        Display.create();
    }

    // method: initGL
    // purpose: initializes original GL settings
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-320, 320, -240, 240, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    // method: createPoly
    // purpose: initializes a new polygon object with colors and vertex coordinates
    private void createPoly(String[] set) {
        polygon shape = new polygon();
        String type, type2;
        shape.colors[0] = Float.parseFloat(set[1]);
        shape.colors[1] = Float.parseFloat(set[2]);
        shape.colors[2] = Float.parseFloat(set[3]);
        
        glPushMatrix();
        while (counter < pInstr.size()) {
            type = (String) pInstr.get(counter);
            set = (type.split(" "));
            counter++;
            
            if (set[0].equals("T") == false) {
                shape.coords.add(Integer.parseInt(set[0]));
                shape.coords.add(Integer.parseInt(set[1]));
            } else {
                while (set[0].equals("P") == false && counter < pInstr.size()) {
                    type = (String) pInstr.get(counter);
                    set = (type.split(" "));
                    counter++;
                    
                    if (set[0].equals("P") == false && counter < pInstr.size()) {
                        tInstr.add(set);
                        if (counter == pInstr.size()-1){
                            type2= (String)pInstr.get(counter);
                            tInstr.add(type2.split(" "));
                        }
                    } else {
                        break;
                    }
                }      
                calculateTables(shape);
                glPopMatrix();
                tInstr.clear();

                if (set[0].equals("P")) {
                    createPoly(set);
                }
            }
        }
    }

    // method: calculateTables
    // purpose: calculates and updates the global, active, and all edge tables
    private void calculateTables(polygon shape) {
        int[] xCol = new int[shape.coords.size() / 2];
        int[] yCol = new int[shape.coords.size() / 2];
        
        //ALL EDGE TABLE
        edges[] all_edges = new edges[xCol.length];
        
        for (int i = 0; i < (shape.coords.size()) / 2; i++) {
            all_edges[i] = new edges();
            xCol[i] = (int) shape.coords.get(2 * i);
            yCol[i] = (int) shape.coords.get((2 * i) + 1);
        }
        
        for (int i = 0; i < xCol.length; i++) {
            if (i == xCol.length - 1) {
                if (yCol[i] >= yCol[0]) {
                    all_edges[i].ymax = yCol[i];
                    all_edges[i].xval = xCol[i];
                    all_edges[i].ymin = yCol[i];
                } else {
                    all_edges[i].ymax = yCol[i + 1];
                    all_edges[i].xval = xCol[i];
                    all_edges[i].ymin = yCol[i];
                }
            } else if (yCol[i] >= yCol[i + 1]) {
                all_edges[i].ymax = yCol[i];
                all_edges[i].xval = xCol[i + 1];
                all_edges[i].ymin = yCol[i + 1];
            } else {
                all_edges[i].ymax = yCol[i + 1];
                all_edges[i].xval = xCol[i];
                all_edges[i].ymin = yCol[i];
            }
            if (i == xCol.length - 1) {
                if (yCol[0] > yCol[i]) {
                    all_edges[i].m = (float) (xCol[0] - xCol[i]) / (yCol[0] - yCol[i]);
                } else if (yCol[0] < yCol[i]) {
                    all_edges[i].m = (float) (xCol[i] - xCol[0]) / (yCol[i] - yCol[0]);
                } else {
                    all_edges[i].m = -9999;
                }
            } else if (yCol[i + 1] > yCol[i]) {
                all_edges[i].m = (float) (xCol[i + 1] - xCol[i]) / (yCol[i + 1] - yCol[i]);
            } else if (yCol[i + 1] < yCol[i]) {
                all_edges[i].m = (float) (xCol[i] - xCol[i + 1]) / (yCol[i] - yCol[i + 1]);
            } else {
                all_edges[i].m = -9999;
            }
        }
        
        int allCount = 0;
        for (edges all_edge : all_edges) {
            if (all_edge.m != -9999) {
                allCount++;
            }
        }
        
        //GLOBAL EDGE TABLE
        edges[] global_edges = new edges[allCount];
        int countx = 0;
        
        for (int i = 0; i < allCount; i++) {
            global_edges[i] = new edges();
            if (all_edges[i].m != -9999) {
                global_edges[countx] = all_edges[i];
                countx++;
            }
        }
        
        global_edges = sortY(global_edges);
        double ysetter = global_edges[0].ymin;
        int activeCount = 0, x = 0;
        
        for (edges global_edge : global_edges) {
            if (global_edge.ymin == ysetter) {
                activeCount++;
            }
        }
        
        //ACTIVE EDGE TABLE
        edges[] active_edges = new edges[activeCount];
        
        for (edges global_edge : global_edges) {
            if (global_edge.ymin == ysetter) {
                active_edges[x] = new edges();
                active_edges[x] = global_edge;
                x++;
            }
        }
        drawPoly(active_edges, global_edges, shape.colors);
    }

    // method: transformPoly
    // purpose: performs transformations on the polygon 
    private void transformPoly() {
        String[] trans;
           
        for (int i = tInstr.size() - 1; i >= 0; i--) {
            trans = (String[]) tInstr.get(i);
            switch (trans[0]) {
                case "t":
                    glTranslatef(Integer.parseInt(trans[1]), Integer.parseInt(trans[2]), 1);
                    break;
                case "r":
                    glRotatef(Integer.parseInt(trans[1]), Integer.parseInt(trans[2]), Integer.parseInt(trans[3]), 1);
                    break;
                default:
                    glScalef(Float.parseFloat(trans[1]), Float.parseFloat(trans[2]), 1);
                    break;
            }
        }
    }
    
    // method: drawPoly
    // purpose: draws/fills the polygon 
    private void drawPoly(edges[] active, edges[] global, float[] color) {
        double scanline = active[0].ymin;
        int min = 0;
        
        glLoadIdentity();
        glColor3f(color[0], color[1], color[2]);
        glPointSize(1);

        transformPoly();
        
        while (active.length != 0) {
            active = sortX(active);
            for (int i = 0; i < active.length; i++) {
                if (i % 2 == 0) {
                    glBegin(GL_LINES);
                    glVertex2f((float) active[i].xval, (float) scanline);
                    glVertex2f((float) active[i + 1].xval, (float) scanline);
                    glEnd();
                }
                active[i].xval += active[i].m;
                if (active[i].ymax == (scanline + 1)) {
                    min++;
                    active[i] = null;
                }

            }
            int max = 0;
            for (edges global1 : global) {
                if (global1.ymin == (scanline + 1)) {
                    max++;
                }
            }

            edges[] transfer = new edges[active.length - min + max];
            min = 0;
            int tran = 0;
            for (edges active1 : active) {
                if (active1 != null) {
                    transfer[tran] = active1;
                    tran++;
                }
            }

            for (edges global1 : global) {
                if (global1.ymin == (scanline + 1)) {
                    transfer[tran] = global1;
                    tran++;
                }
            }
            active = transfer;
            scanline++;
        }
    }
    
    // method: sortY
    // purpose: assists the calculateTables method in sorting the y values
    private edges[] sortY(edges[] sortY) {
        edges[] newSorted = new edges[sortY.length];
        edges temp;
        int x = 0;
        
        for (int i = 0; i < sortY.length; i++) {
            for (int j = i + 1; j < sortY.length; j++) {
                if (sortY[i].ymin > sortY[j].ymin) {
                    temp = sortY[j];
                    sortY[j] = sortY[i];
                    sortY[i] = temp;
                }
                if (sortY[i].ymin == sortY[j].ymin) {
                    if (sortY[i].xval > sortY[j].xval) {
                        temp = sortY[j];
                        sortY[j] = sortY[i];
                        sortY[i] = temp;
                    }
                }
            }
            newSorted[x] = sortY[i];
            x++;
        }
        return newSorted;
    }

    // method: sortY
    // purpose: assists the calculateTables method in sorting the x-min values
    private edges[] sortX(edges[] sortX) {
        edges[] newSorted = new edges[sortX.length];
        edges temp;
        int x = 0;
        
        for (int i = 0; i < sortX.length; i++) {
            for (int j = i + 1; j < sortX.length; j++) {
                if (sortX[i].xval > sortX[j].xval) {
                    temp = sortX[j];
                    sortX[j] = sortX[i];
                    sortX[i] = temp;
                }
            }
            newSorted[x] = sortX[i];
            x++;
        }
        return newSorted;
    }

    //A custom object to store information about each polygon
    public class polygon {

        ArrayList coords = new ArrayList<>();
        float[] colors = {0, 0, 0};
    }

    //A custom object to store edge information
    public class edges {

        double ymax = 1;
        double ymin = 1;
        double xval = 1;
        double m = 1.0;

        public void edges() {
            ymax = 1;
            ymin = 1;
            xval = 1;
            m = 1;
        }
    }
}
