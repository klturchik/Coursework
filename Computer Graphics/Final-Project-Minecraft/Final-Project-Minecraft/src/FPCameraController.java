/**********************************************************************
 * file: FPCameraController.java
 * author: Kyle Turchik, Vu Dao, Marco Roman
 * class: CS 445 - Computer Graphics
 *
 * assignment: Quarter Project CP#3
 * date last modified: 11/29/2016
 *
 * purpose: This class allows the player to control the camera through
 *          keyboard commands and renders objects in the player's view.
 * 
 * controls:
 *          W/A/S/D:    Forward/Left/Backward/Right
 *          Space Bar:  Move Up
 *          Left Shift: Move Down
 *          Mouse:      Look
 *          Escape:     Exit Game
 * 
 * extra:
 *          Tab:        Changes Texture
 *          Enter:      Regenerate Terrain
 *          0:          Creates Day/Night cycle
 *
 **********************************************************************/

import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class FPCameraController {
    //stores the camera's position
    private Vector3Float position = null;
    private Vector3Float lPosition = null;
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
    //Ensures the chunks are only randomized once
    private boolean firstGen;
    private Chunk chunk;
    private int tex =0 ;

    //Constructor
    //instantiate position Vector3f to the x y z params.
    public FPCameraController(float x, float y, float z) {
        position = new Vector3Float(x, y, z);
        lPosition = new Vector3Float(x, y, z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
        
        firstGen = true;
    }

    //increment the camera's current yaw rotation
    public void yaw(float amount) {
        //increment the yaw by the amount param
        yaw += amount;
    }
    
    //increment the camera's current pitch rotation
    public void pitch(float amount) {
        //increment the pitch by the amount param
        pitch -= amount;
    }

    //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).
                put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(lPosition.x += xOffset).put(lPosition.y).
                put(lPosition.z -= zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        position.x -= xOffset;
        position.z += zOffset;
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).
                put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
  
    //creates a day/night cycle
    public void dayNight(){
        lPosition.z = 50;
        if(lPosition.x <-275)
            lPosition.x =275;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(lPosition.x -= 1).put(lPosition.y).
                put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
    
    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        position.x -= xOffset;
        position.z += zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).
                put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //moves the camera up
    public void moveUp(float distance) {
        position.y -= distance;
    }
    
    //moves the camera down
    public void moveDown(float distance) {
        position.y += distance;
    }

    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    public void lookThrough() {
        //rotate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //rotate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        
        //stops light from moving
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(lPosition.x).put(lPosition.y).
                put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    public void gameLoop() throws InterruptedException {
        FPCameraController camera = new FPCameraController(0, 0, 0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        //hide the mouse
        Mouse.setGrabbed(true);

        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //control camera yaw from x movement fromt the mouse 
            camera.yaw(dx * mouseSensitivity);
            //control camera pitch from y movement fromt the mouse 
            camera.pitch(dy * mouseSensitivity);
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
            {
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
            {
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left
            {
                camera.strafeLeft(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right
            {
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//move up
            {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            /*
            if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
                textureFile = "terrain_Alien";
            }*/

            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //Draw the scene
            //ADDON: Changes texture
            if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
                chunk.deleteTextures(tex);
                tex++;
                if (tex > 3) {
                    tex = tex % 4;
                }
                // chunk = new Chunk(-30, 1, -75, tex);
                System.out.println(tex);

                TimeUnit.MILLISECONDS.sleep(200);
            }
            //ADDON: Regenerates Terrain
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                chunk = new Chunk(-30,1,-75, tex);
                TimeUnit.MILLISECONDS.sleep(200);
            }
            if (firstGen) {
                chunk = new Chunk(-30,1,-75, tex);
                firstGen = false;
            }
            
            //ADDON: Demonstrates Day/Night cycle
            if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
                dayNight();
            }
          
            chunk.render();
            
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
}

/*OBSOLETE RENDER METHOD
    //Render simple 3D cube w/ six different colors
    private void render() {
        try {
            //Top
            glBegin(GL_QUADS);
            glColor3f(0.0f, 0.0f, 1.0f); //Color
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);

            //Bottom
            glColor3f(0.0f, 1.0f, 0.0f); //Color
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);

            //Front
            glColor3f(1.0f, 0.0f, 1.0f); //Color
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);

            //Back
            glColor3f(1.0f, 0.0f, 0.0f); //Color
            glVertex3f(1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, -1.0f);

            //Left
            glColor3f(1.0f, 1.0f, 0.0f); //Color
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);

            //Right
            glColor3f(0.0f, 1.0f, 1.0f); //Color
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();

            //Top
            glBegin(GL_LINE_LOOP);
            glColor3f(0.0f, 0.0f, 0.0f);
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glEnd();

            //Bottom
            glBegin(GL_LINE_LOOP);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();

            //Front
            glBegin(GL_LINE_LOOP);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glEnd();

            //Back
            glBegin(GL_LINE_LOOP);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, -1.0f);
            glEnd();

            //Left
            glBegin(GL_LINE_LOOP);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glEnd();

            //Right
            glBegin(GL_LINE_LOOP);
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();
            
        } catch (Exception e) {

        }
    }
*/

