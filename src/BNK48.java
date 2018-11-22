import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BNK48 extends Application{
//	public static Circle circle; 
	public Pane canvas;
	int count = 0;
    int max = -1;
    int MyID = -1;
    Label lb1, lb2;
    int width = 500;
    int height = 500;
	ArrayList<Kero> list = new ArrayList<Kero>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage)  {
		// TODO Auto-generated method stub
		canvas = new Pane(); 
		Scene scene = new Scene(canvas, width, height); 
		setBackground(canvas);
		initDetail(canvas);
		
		Coonector tank = new Coonector("localhost", 4343, this);
		tank.start();
//		
//		LOOP Animation
//		
		final Timeline loop = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent t) {
                int index;
                for (int i = 0; i < canvas.getChildren().size() - 3; i++) {
                    //canvas.getChildren().get(i).setTranslateX(listDelta.get(i).deltaX);
                    index = i + 3;
                    canvas.getChildren().get(index).setLayoutX(canvas.getChildren().get(index).getLayoutX() + list.get(i).deltaX);
                    canvas.getChildren().get(index).setLayoutY(canvas.getChildren().get(index).getLayoutY() + list.get(i).deltaY);
                    final double pos_X = canvas.getChildren().get(index).getLayoutX();
                    final double pos_Y = canvas.getChildren().get(index).getLayoutY();

                    final boolean activeRight = MyID < max;
                    final boolean activeLeft = MyID > 1;

                    if (!list.get(i).dying) {
                        final boolean atRightBorder = pos_X >= 500;
                        final boolean atLeftBorder = pos_X <= 0;
                        final boolean atBottomBorder = pos_Y >= 485;
                        final boolean atTopBorder = pos_Y <= 130;
                        if (atRightBorder || atLeftBorder) {
                            if (atRightBorder && !activeRight) {
                                list.get(i).ChangeDeltaX(false);
                                canvas.getChildren().remove(index);
                                canvas.getChildren().add(index, Kero.createFish(pos_X - 77, pos_Y, list.get(i).r, list.get(i).g, list.get(i).b, false));
                            } 
                            else if (atLeftBorder && !activeLeft) {
                                list.get(i).ChangeDeltaX(true);
                                canvas.getChildren().remove(index);
                                canvas.getChildren().add(index, Kero.createFish(pos_X + 77, pos_Y, list.get(i).r, list.get(i).g, list.get(i).b, true));
                            }
                            if (atRightBorder && activeRight && !list.get(i).dying) {
                                tank.sent((MyID)+"_" + pos_X + "|" + pos_Y + "|" + list.get(i).deltaX + "|" + list.get(i).deltaY + "|" + list.get(i).r + "|" + list.get(i).g + "|" + list.get(i).b + "");
                                list.get(i).dying = true;
                            } else if (atLeftBorder && activeLeft && !list.get(i).dying) {
                                tank.sent((MyID-2)+"_" + pos_X + "|" + pos_Y + "|" + list.get(i).deltaX + "|" + list.get(i).deltaY + "|" + list.get(i).r + "|" + list.get(i).g + "|" + list.get(i).b + "");
                                list.get(i).dying = true;
                            }

                        }
                        if (atBottomBorder || atTopBorder) {
                            list.get(i).ChangeDeltaY(atTopBorder);
                        }
                    } else {
                        final boolean atDeathLineRightBorder = pos_X >= 577;
                        final boolean atDeathLineLeftBorder = pos_X <= -77;
                        if (atDeathLineLeftBorder || atDeathLineRightBorder) {
                            if (count > 0) {
                                canvas.getChildren().remove(index);
                                list.remove(i);
                                count--;
                                lb2.setText("Total Fish #" + count);
                            }
                        }
                    }
                    update();
                }
            }
        }));
		loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
//		
//		Mouse click Event
//		
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("mouse click detected! " + event.getSource());
                System.out.println(event.getButton());
                if (event.getButton()==MouseButton.PRIMARY){
                    Random rand = new Random();
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    int delX = (int) (rand.nextFloat()*10);
                    int delY = (int) (rand.nextFloat()*10);
                    int originX = (int) (rand.nextFloat()*799);
                    int originY = (int) (rand.nextFloat()*399);
                    originY += 100;
                    frogCreate(originX, originY, delX, delY, r, g, b);
                }
                else if(event.getButton()==MouseButton.SECONDARY){
                	if (count > 0) {
                        canvas.getChildren().remove(3);
                        list.remove(0);
                        count--;
                        lb2.setText("Total Fish #" + count);
                    }
                	else{
                		lb2.setText("Total Fish #0!");
                	}
                }
            }
        });
		
		primaryStage.setTitle("Digital Live Studio"); 
		primaryStage.setScene(scene); 
		primaryStage.show();
		update();
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
	private void setBackground(Pane canvas) {
        Rectangle rect1 = new Rectangle(0, 100, width, 400);
        rect1.setFill(Color.rgb(0, 153, 230));
        canvas.getChildren().add(rect1);
    }
	public void frogCreate(double originX, double originY, double dX, double dY, double r, double g, double b) {
		boolean right = true;
        if (dX < 0) {
            right = false;
        }
        Kero tmp_fish = new Kero(dX, dY, r, g, b);
        canvas.getChildren().add(Kero.createFish(originX, originY, r, g, b, right));
        list.add(tmp_fish);
        count++;
        lb2.setText("Total Fish #" + count);
	}
	public void update()
    {
        lb1.setText("Tank #"+MyID+"/"+max);
    }
}
