import java.awt.*;

public class Ray {
    private double x;
    private double y;
    private Color color;
    private double distance;

    public Ray(){

    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public double getDistance() {
        return distance;
    }
}
