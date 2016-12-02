/**********************************************************************
 * file: Block.java
 * author: Kyle Turchik, Vu Dao, Marco Roman
 * class: CS 445 - Computer Graphics
 *
 * assignment: Quarter Project CP#2
 * date last modified: 11/15/2016
 *
 * purpose: This class is a component of the Chunk object.  Each block
 *          has one of six texture types types, an active or inactive
 *          status, and a vector location.
 *
 **********************************************************************/

public class Block {

    private boolean IsActive;
    private BlockType Type;
    private float x, y, z;

    //Define the block types
    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5);
        private int BlockID;

        BlockType(int i) {
            BlockID = i;
        }

        public int GetID() {
            return BlockID;
        }

        public void SetID(int i) {
            BlockID = i;
        }

    }
    
    //Constructor
    public Block(BlockType type) {
        Type = type;
    }

    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public boolean isActive() {
        return IsActive;
    }
    
    public void setActive(boolean active) {
        IsActive = active;
    }

    public int getID() {
        return Type.GetID();
    }
}
