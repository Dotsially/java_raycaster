import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.sql.BatchUpdateException;

//Player
public class Player {
    private BufferedImage[] playerSprite;
    private int animFrame;

    Point2D position;
    private double xDirection;
    private double yDirection;

    private double playerAngle;
    private int playerSize;
    public Player(int x, int y, int size, String playerSpritePath){
        playerSprite = new BufferedImage[2];
        position = new Point2D.Double(x,y);

        this.playerSize = size;
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(playerSpritePath));
            playerSprite[0] = image.getSubimage(0, 0, 640, 512);
            playerSprite[1] = image.getSubimage(0,512,640,512);
        }catch(Exception e){
            e.printStackTrace();
        }

        playerAngle = 0;
        setxDirection(playerAngle);
        setyDirection(playerAngle);
    }

    public void draw(FXGraphics2D graphics, boolean shot){
        AffineTransform tx = new AffineTransform();
        tx.translate(578,0);
        if(shot) {
            graphics.drawImage(this.playerSprite[1], tx, null);
        }
        else {
            graphics.drawImage(this.playerSprite[0], tx, null);
        }
        graphics.setColor(Color.yellow);
        graphics.fillRect((int)position.getX()-(playerSize/2), (int)position.getY()-(playerSize/2),playerSize,playerSize);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public double getxDirection() {
        return xDirection;
    }

    public double getyDirection() {
        return yDirection;
    }

    //Will automatically calculate with cos
    public void setxDirection(double value) {
        this.xDirection = Math.cos(value)*5;
    }

    //Will automatically calculate with sin
    public void setyDirection(double value) {
        this.yDirection = Math.sin(value)*5;
    }

    public double getPlayerAngle() {
        return playerAngle;
    }

    public void setPlayerAngle(double playerAngle) {
        this.playerAngle = playerAngle;
    }

    public void setAnimFrame(int animFrame) {
        this.animFrame = animFrame;
    }
}
