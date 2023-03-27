import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

//@Mat
//Draws vertical lines with the length of the distance between the player and a wall.
public class Raycaster {
    private Ray[] rays;
    private final double degreeRadian = 0.0174533;
    private final int FOV = 60;
    private double rayAngle;
    private double rayY;
    private double rayX;

    private double horizontalRayX;
    private double horizontalRayY;
    private double verticalRayX;
    private double verticalRayY;

    double distanceVertical;
    double distanceHorizontal;

    public Raycaster(){
        rays = new Ray[FOV];

        for(int i = 0; i < rays.length; i++){
            rays[i] = new Ray();
        }

        rayAngle = 0;
        rayX = 0;
        rayY = 0;
        horizontalRayX = 0;
        horizontalRayY = 0;
        verticalRayX = 0;
        verticalRayY = 0;
        distanceVertical = 100000;
        distanceHorizontal = 100000;
    }

    public void drawRaycast(FXGraphics2D graphics, Player player, Map map){

        graphics.setColor(new Color (100, 150, 255));
        graphics.fillRect(578,0,640,256);

        graphics.setColor(new Color(0, 50, 150));
        graphics.fillRect(578,256,640,256);

        for(int i = 0; i < rays.length; i++) {
            graphics.setColor(rays[i].getColor());
            graphics.draw(new Line2D.Double(player.getPosition().getX(), player.getPosition().getY(), rays[i].getX(), rays[i].getY()));

            if(rays[i].getDistance() > 0) {
                int lineHeight = (map.getTileSize() * 512) / (int) rays[i].getDistance();
                if(lineHeight > 512){
                    lineHeight = 512;
                }
                graphics.fill(new Rectangle2D.Double(578 + i * 640 / FOV, (int) (256 - lineHeight / 2), 640 / FOV+1, lineHeight));
            }
        }
    }

    public void update(Player player, Map map){
        rayAngle = player.getPlayerAngle() - degreeRadian*30;
        if(rayAngle < 0){
            rayAngle+=2*Math.PI;
        }
        if(rayAngle > 2*Math.PI){
            rayAngle-= 2*Math.PI;
        }



        for(int i = 0; i < FOV; i++) {
            calculateVerticalDistance(player, map);
            calculateHorizontalDistance(player, map);

            if (distanceHorizontal < distanceVertical) {
                rayX = horizontalRayX;
                rayY = horizontalRayY;
                rays[i].setDistance(distanceHorizontal);
                rays[i].setColor(new Color(0,150,0));
            }

            if (distanceVertical < distanceHorizontal) {
                rayX = verticalRayX;
                rayY = verticalRayY;
                rays[i].setDistance(distanceVertical);
                rays[i].setColor(Color.green);
            }

            rays[i].setX(rayX);
            rays[i].setY(rayY);

            rayAngle+=degreeRadian;
            if(rayAngle < 0){
                rayAngle+= 2*Math.PI;
            }
            if(rayAngle > 2*Math.PI){
                rayAngle-= 2*Math.PI;
            }
        }
    }


    //Checks for collision with vertical lines.
    public void calculateVerticalDistance(Player player, Map map){
        int depthOfField = 0;
        double xOffset = 0;
        double yOffset = 0;
        double nTan = -Math.tan(rayAngle);

        verticalRayX = player.getPosition().getX();
        verticalRayY = player.getPosition().getY();
        distanceVertical = 100000;

        if(rayAngle > Math.PI/2 && rayAngle < 3*Math.PI/2){
            rayX = Math.floor(player.getPosition().getX()/map.getTileSize()) * map.getTileSize() -0.001;
            rayY = (player.getPosition().getX() - rayX) * nTan + player.getPosition().getY();
            xOffset = -64;
            yOffset = -xOffset*nTan;
        }
        if(rayAngle < Math.PI/2 || rayAngle > 3*Math.PI/2){
            rayX = Math.floor(player.getPosition().getX()/map.getTileSize())* map.getTileSize() + map.getTileSize();
            rayY = (player.getPosition().getX() - rayX) * nTan + player.getPosition().getY();
            xOffset = 64;
            yOffset = -xOffset*nTan;
        }
        if (rayAngle == 0 || rayAngle == Math.PI){
            rayX = player.getPosition().getX();
            rayY = player.getPosition().getY();
            depthOfField = 8;
        }

        while(depthOfField < 8){
            int mapX = (int)(rayX)/map.getTileSize();
            int mapY = (int)(rayY)/map.getTileSize();
            int mapIndex = mapY*map.getMapX()+mapX;
            if(mapIndex < map.getMapX()*map.getMapY() && mapIndex > 0 && map.getMap()[mapIndex] == 1){
                depthOfField = 8;
                verticalRayX = rayX;
                verticalRayY = rayY;
                distanceVertical = calculateDistance(player.getPosition().getX(), player.getPosition().getY(), verticalRayX, verticalRayY);
            }
            else{
                rayX += xOffset;
                rayY += yOffset;
                depthOfField++;
            }
        }

    }

    //Checks for collision with horizontal lines.
    public void calculateHorizontalDistance(Player player, Map map){
        int depthOfField = 0;
        double xOffset = 0;
        double yOffset = 0;
        double aTan = -1.0/Math.tan(rayAngle);

        horizontalRayX = player.getPosition().getX();
        horizontalRayY = player.getPosition().getY();
        distanceHorizontal = 100000;

        if(rayAngle > Math.PI){
            rayY = Math.floor(player.getPosition().getY()/map.getTileSize()) * map.getTileSize() -0.001;
            rayX = (player.getPosition().getY() - rayY) * aTan + player.getPosition().getX();
            yOffset = -64;
            xOffset = -yOffset*aTan;
        }
        if(rayAngle < Math.PI){
            rayY = Math.floor(player.getPosition().getY()/map.getTileSize())* map.getTileSize() + map.getTileSize();
            rayX = (player.getPosition().getY() - rayY) * aTan + player.getPosition().getX();
            yOffset = 64;
            xOffset = -yOffset*aTan;
        }
        if (rayAngle == 0 || rayAngle == Math.PI){
            rayX = player.getPosition().getX();
            rayY = player.getPosition().getY();
            depthOfField = 8;
        }

        while(depthOfField < 8){
            int mapX = (int)(rayX)/map.getTileSize();
            int mapY = (int)(rayY)/map.getTileSize();
            int mapIndex = mapY*map.getMapX()+mapX;
            if(mapIndex < map.getMapX()*map.getMapY() && mapIndex > 0 && map.getMap()[mapIndex] == 1){
                depthOfField = 8;
                horizontalRayX = rayX;
                horizontalRayY = rayY;
                distanceHorizontal = calculateDistance(player.getPosition().getX(), player.getPosition().getY(), horizontalRayX, horizontalRayY);
            }
            else{
                rayX += xOffset;
                rayY += yOffset;
                depthOfField++;
            }
        }
    }

    //Get the length between 2 points using Pythagoras.
    public double calculateDistance(double aX, double aY, double bX, double bY){
        return Math.sqrt((bX - aX) * (bX - aX) + (bY - aY) * (bY - aY));
    }
}
