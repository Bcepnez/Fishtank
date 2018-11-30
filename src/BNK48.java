import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BNK48 extends Application {

    public Pane canvas;
    int count = 0;
    int max = -1;
    int MyID = -1;
    Label lb1, lb2;
    ArrayList<Kero> listOfFish = new ArrayList<Kero>();

    @Override
    public void start(Stage primaryStage) {
        canvas = new Pane();
        Scene scene = new Scene(canvas, 500, 500);
        setBackground(canvas);
        initDetail(canvas);
        
        Coonector tank = new Coonector("localhost", 4343, this);
        tank.start();
        
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("mouse click detected! " + event.getSource());
                System.out.println(event.getButton());
                if (event.getButton() == MouseButton.PRIMARY) {
                    Random rand = new Random();
                    int delX = (int) (rand.nextFloat() * 5) + 1;
                    int delY = (int) (rand.nextFloat() * 5) + 1;
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    int originX = (int) (rand.nextFloat() * 499);
                    int originY = (int) (rand.nextFloat() * 299);
                    originY += 100;
                    createFish(originX, originY, delX, delY, r, g, b);

                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (count > 0) {
                        canvas.getChildren().remove(3);
                        listOfFish.remove(0);
                        count--;
                        lb2.setText("Total Fish #" + count);
                    }
                }
            }
        });

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent t) {
                int index;
                for (int i = 0; i < canvas.getChildren().size() - 3; i++) {
                    //canvas.getChildren().get(i).setTranslateX(listDelta.get(i).deltaX);
                    index = i + 3;
                    canvas.getChildren().get(index).setLayoutX(canvas.getChildren().get(index).getLayoutX() + listOfFish.get(i).deltaX);
                    canvas.getChildren().get(index).setLayoutY(canvas.getChildren().get(index).getLayoutY() + listOfFish.get(i).deltaY);
                    final double pos_X = canvas.getChildren().get(index).getLayoutX();
                    final double pos_Y = canvas.getChildren().get(index).getLayoutY();

                    final boolean activeRight = MyID < max;
                    final boolean activeLeft = MyID > 1;

                    if (!listOfFish.get(i).dying) {
                        final boolean atRightBorder = pos_X >= 500;
                        final boolean atLeftBorder = pos_X <= 0;
                        final boolean atBottomBorder = pos_Y >= 485;
                        final boolean atTopBorder = pos_Y <= 130;
                        if (atRightBorder || atLeftBorder) {
                            if (atRightBorder && !activeRight) {
                                listOfFish.get(i).ChangeDeltaX(false);
                                canvas.getChildren().remove(index);
                                canvas.getChildren().add(index, Kero.createFish(pos_X - 40, pos_Y, listOfFish.get(i).r, listOfFish.get(i).g, listOfFish.get(i).b, false));
                            } else if (atLeftBorder && !activeLeft) {
                                listOfFish.get(i).ChangeDeltaX(true);
                                canvas.getChildren().remove(index);
                                canvas.getChildren().add(index, Kero.createFish(pos_X + 40, pos_Y, listOfFish.get(i).r, listOfFish.get(i).g, listOfFish.get(i).b, true));
                            }
                            if (atRightBorder && activeRight) {
                                tank.sent((MyID-1+1)+"_" + pos_X + "|" + pos_Y + "|" + listOfFish.get(i).deltaX + "|" + listOfFish.get(i).deltaY + "|" + listOfFish.get(i).r + "|" + listOfFish.get(i).g + "|" + listOfFish.get(i).b + "");
                                listOfFish.get(i).dying = true;
                            } else if (atLeftBorder && activeLeft) {
                                tank.sent((MyID-1-1)+"_" + pos_X + "|" + pos_Y + "|" + listOfFish.get(i).deltaX + "|" + listOfFish.get(i).deltaY + "|" + listOfFish.get(i).r + "|" + listOfFish.get(i).g + "|" + listOfFish.get(i).b + "");
                                listOfFish.get(i).dying = true;
                            }

                        }
                        int Integer = 5;
                        if (atBottomBorder || atTopBorder) {
                            listOfFish.get(i).ChangeDeltaY(atTopBorder);
                        }
                    } else {
                        final boolean atDeathLineRightBorder = pos_X >= 540;
                        final boolean atDeathLineLeftBorder = pos_X <= -40;
                        if (atDeathLineLeftBorder || atDeathLineRightBorder) {
                            if (count > 0) {
                                canvas.getChildren().remove(index);
                                listOfFish.remove(i);
                                count--;
                                lb2.setText("Total Fish #" + listOfFish.size());
                            }
                        }

                    }
                    update();
                }
            }
        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

        primaryStage.setTitle("Fish Tank");
        primaryStage.setScene(scene);
        primaryStage.show();
                update();

    }

    /**
     * @param args the command line arguments
     */
    private void setBackground(Pane canvas) {
        ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/bgFT.jpg")));
        iv.setFitHeight(400);
        iv.setFitWidth(500);
        /*Rectangle rect1 = new Rectangle(0, 100, 500, 400);
        rect1.setFill(Color.rgb(0, 153, 230));*/
        iv.setLayoutY(100);
        iv.setLayoutX(0);
        canvas.getChildren().add(iv);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initDetail(Pane canvas) {
        lb1 = new Label("Tank #");
        lb2 = new Label("Total Fish #" + count);
        lb1.setLayoutX(20);
        lb1.setLayoutY(20);
        lb2.setLayoutX(20);
        lb2.setLayoutY(40);
        canvas.getChildren().addAll(lb1, lb2);
        lb1.setText("Tank #"+(0)+"/"+(0));

    }

    public void createFish(double originX, double originY, double dX, double dY, double r, double g, double b) {
        boolean right = true;
        if (dX < 0) {
            right = false;
        }
        Kero tmp_fish = new Kero(dX, dY, r, g, b);
        canvas.getChildren().add(Kero.createFish(originX, originY, r, g, b, right));
        listOfFish.add(tmp_fish);
        count++;
        lb2.setText("Total Fish #" + count);
    }
    
    public void update()
    {
        lb1.setText("Tank #"+MyID+"/"+max);
    }

}
