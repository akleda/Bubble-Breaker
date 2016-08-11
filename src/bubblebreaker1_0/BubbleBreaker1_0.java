/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bubblebreaker1_0;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Adelka
 */
public class BubbleBreaker1_0 extends Application {

    int rows = 10, cols = 15;
    Bubbles b = new Bubbles(cols, rows);
    Bubbles bBack;
    Group circles = new Group();
    Group gCircles = new Group();
    final Stage stage = new Stage(StageStyle.TRANSPARENT);
    final static int RAD = 30;
    private int score = 0;
    ArrayList<Line> lines;
    int scHeight = 400;
    int scWidth = 500;

    @Override
    public void start(Stage primaryStage) {
        BorderPane bp = new BorderPane();
        Group gCircleLines = new Group();
        Text tScore = new Text("Score: 0");
        Button bNewGame = new Button("New Game");
        Button bExit = new Button("Exit");
        Image iBack = new Image(getClass().getResourceAsStream("back.png"));
        ImageView iv = new ImageView(iBack);
        Button bBack = new Button("Back", iv);
        Group gRoot = new Group();
        Rectangle rEOG = new Rectangle(5 * RAD, 5 * RAD, 5 * RAD, 2 * RAD);
        Text tEOG = new Text(6 * RAD, 6 * RAD, "END OF GAME");
        Group gEOG = new Group(rEOG, tEOG);
        Group gLines = new Group();
        Group gScore = new Group();
        Text tRunScore = new Text("0");
        HBox hBox = new HBox(bBack, bNewGame, bExit);
        Scene scene = new Scene(bp, scWidth, scHeight);
        Circle cRunScore = new Circle(RAD / 2, Color.TRANSPARENT);

        int iRRad1 = rows * RAD + 1, iCRad1 = cols * RAD + 1;

        gCircleLines.getChildren().addAll(
                new Line(0, 0, 0, iRRad1 - 1),
                new Line(0, 0, iCRad1 - 1, 0),
                new Line(0, iRRad1, iCRad1 - 1, iRRad1 - 1),
                new Line(iCRad1, 0, iCRad1 - 1, iRRad1 - 1)
        );
        gCircleLines.getChildren().stream().forEach((Node l) -> {
            ((Line) l).setStroke(Color.WHITESMOKE);
        });

        iv.setFitHeight(16);
        iv.setFitWidth(16);

        rEOG.setFill(Color.ANTIQUEWHITE);
        rEOG.setStroke(Color.BLACK);

        gRoot.getChildren().add(gEOG);
        gEOG.setVisible(false);

        circles = createCircles(b);

        tRunScore.setMouseTransparent(true);

        cRunScore.setMouseTransparent(true);

        gCircles.getChildren().add(gCircleLines);
        gCircles.getChildren().add(circles);

        gRoot.getChildren().add(gCircles);
        gRoot.getChildren().add(gLines);

        gScore.getChildren().add(cRunScore);
        gScore.getChildren().add(tRunScore);
        gRoot.getChildren().add(gScore);
        gScore.setVisible(false);

        bp.setTop(tScore);
        BorderPane.setAlignment(tScore, Pos.TOP_RIGHT);
        bp.setBottom(hBox);

        hBox.setAlignment(Pos.CENTER);

        bp.setCenter(gRoot);

        primaryStage.setScene(scene);
        primaryStage.show();

        //actions
        bNewGame.setOnAction((ActionEvent event) -> {
            b = new Bubbles();
            circles = createCircles(b);
            gCircles.getChildren().clear();
            gLines.getChildren().clear();
            gScore.setVisible(false);
            gCircles.getChildren().add(circles);
            gEOG.setVisible(false);
        });
        bExit.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        gCircles.setOnMouseClicked((MouseEvent event) -> {
            int x = (int) event.getX() / RAD;
            int y = (int) event.getY() / RAD;
            int actualScore = b.getValueOfSelected(x, y);
            if (actualScore < 2) {
                gLines.setVisible(false);
                gScore.setVisible(false);
                return;
            }
            lines = b.linesToDraw(x, y);
            gLines.getChildren().clear();
            for (Line l : lines) {
                Line l1;
                l1 = new Line(l.getStartX() * RAD, l.getStartY() * RAD, l.getEndX() * RAD, l.getEndY() * RAD);
                l1.setStrokeWidth(1);
                l1.setFill(Color.BLACK);
                gLines.getChildren().add(l1);
                gLines.setVisible(true);
            }
            gScore.relocate(x * RAD-RAD/4, y * RAD);
            tRunScore.setText(String.valueOf(actualScore));
            gScore.setVisible(true);
            if (event.getClickCount() == 2) {
                b.getSameColorAround(x, y);
                score += b.getValueOfSelected(x, y);
                tScore.setText("Score: " + score);
                b.setColorToNull();
                b.moveBubblesDown();
                b.moveBubblesRight();
                gCircles.getChildren().clear();
                circles = createCircles(b);
                gCircles.getChildren().add(circles);
                gLines.setVisible(false);
                gScore.setVisible(false);
                if (b.endOfGame()) {
                    gEOG.setVisible(true);
                }
            }
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //System.out.println(b.endOfGame());
        launch(args);

    }

    private Group createCircles(Bubbles b) {
        if (circles != null) {
            circles = new Group();
        }
        for (int i = 0; i < b.getCol(); i++) {
            for (int j = 0; j < b.getRow(); j++) {
                int c = b.getColorAt(i, j);
                Circle circle = new Circle();
                int rad2 = RAD / 2 - 1;
                switch (c) {
                    case 1:
                        circle = new Circle(rad2, Color.RED);
                        break;
                    case 2:
                        circle = new Circle(rad2, Color.YELLOW);
                        break;
                    case 3:
                        circle = new Circle(rad2, Color.BLUE);
                        break;
                    case 4:
                        circle = new Circle(rad2, Color.GREEN);
                        break;
                    default:
                        circle = new Circle(rad2, Color.TRANSPARENT);
                        break;
                }
                circle.setCenterX((i * RAD + RAD / 2));
                circle.setCenterY((j * RAD + RAD / 2));
                circles.getChildren().add(circle);
            }
        }
        return circles;
    }

}
