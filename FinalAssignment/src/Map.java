import org.jfree.fx.FXGraphics2D;

import java.awt.*;

//2D map and map data for raycaster.
public class Map {
    private int mapX = 8;
    private int mapY = 8;
    private int tileSize = 64;
    private int offsetX = 0;
    private int offsetY = 0;

    //1 -> Walls; 2 -> Open space.
    private byte[] map = {
            1,1,1,1,1,1,1,1,
            1,0,0,0,0,0,0,1,
            1,0,1,0,0,1,0,1,
            1,0,0,0,0,0,0,1,
            1,1,0,0,0,0,1,1,
            1,0,1,0,1,1,0,1,
            1,0,0,0,0,0,0,1,
            1,1,1,1,1,1,1,1,
    };

    public Map(){

    }

    public byte[] getMap() {
        return map;
    }

    public int getMapX() {
        return mapX;
    }

    public int getMapY() {
        return mapY;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void drawMap(FXGraphics2D graphics){
        for(int x = 0; x < mapX; x++){
            for(int y = 0; y < mapY; y++){
                if(map[y*mapY+x]==1){
                    graphics.setColor(Color.white);
                    graphics.fillRect(offsetX + 1 + x*tileSize, offsetY + 1 + y*tileSize, tileSize-1,tileSize-1);
                }else{
                    graphics.setColor(Color.black);
                    graphics.fillRect( offsetX + 1 + x*tileSize,offsetY + 1 + y*tileSize, tileSize-1,tileSize-1);
                }
            }
        }
    }

}
