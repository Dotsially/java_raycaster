import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


public class Eindopdracht extends Application {
    Canvas canvas = new Canvas(1280,720);
    Map map;
    Player player;
    Raycaster raycaster;


    boolean firstBoot = false;
    boolean shot = false;
    int timer = 0;

    public static void main(String[] args){
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        init();
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Raycaster");
        primaryStage.show();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> keyPressed(e));
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));

        new AnimationTimer(){
            long last = -1;
            @Override
            public void handle(long now) {
                if(last == -1){
                    last = now;
                }

                update((now-last)/1000000000.0);
                last = now;
                draw(g2d);
            }

        }.start();


        draw(g2d);
    }

    public void draw(FXGraphics2D graphics){
        if(!firstBoot){
            graphics.translate(25,100);
            firstBoot = true;
        }

        graphics.setBackground(new Color(50, 0, 50));
        graphics.clearRect(-25,-100,(int)canvas.getWidth(),(int)canvas.getHeight());


        map.drawMap(graphics);
        raycaster.drawRaycast(graphics, player, map);
        player.draw(graphics, shot);

    }



    public void init(){
        map = new Map();
        raycaster = new Raycaster();
        player = new Player(map.getOffsetX()+80,map.getOffsetY()+80,16,"/player_sprite.png");
    }

    public void update(double deltaTime){
        raycaster.update(player, map);

        if(shot){
            timer++;
            if(timer > 11){
                shot = false;
            }
        }
    }

    public void keyPressed(KeyEvent key){
        if(key.getCode() == KeyCode.W){
            player.setPosition(new Point2D.Double(
                    player.getPosition().getX() + player.getxDirection(),
                    player.getPosition().getY() + player.getyDirection()
                    ));
        }

        if(key.getCode() == KeyCode.S){
            player.setPosition(new Point2D.Double(
                    player.getPosition().getX() - player.getxDirection(),
                    player.getPosition().getY() - player.getyDirection()
            ));
        }

        if(key.getCode() == KeyCode.A){
            player.setPlayerAngle(player.getPlayerAngle()-0.1);

            if(player.getPlayerAngle() < 0){
                player.setPlayerAngle(2*Math.PI);
            }

            player.setxDirection(player.getPlayerAngle());
            player.setyDirection(player.getPlayerAngle());
        }

        if(key.getCode() == KeyCode.D){
            player.setPlayerAngle(player.getPlayerAngle()+0.1);

            if(player.getPlayerAngle() > 2*Math.PI){
                player.setPlayerAngle(0);
            }

            player.setxDirection(player.getPlayerAngle());
            player.setyDirection(player.getPlayerAngle());
        }


    }

    public void mousePressed(MouseEvent e){
        if(shot == false){
            shot = true;
        }

    }

    public void mouseReleased(MouseEvent e){
        timer = 0;
        shot = false;
    }
}
