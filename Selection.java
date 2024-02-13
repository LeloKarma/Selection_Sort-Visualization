package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class SelectionSort extends Application {
    private int[] nos;
    private SelectionPanel panCenter;
    private Button btnStart, btnClose;
    private Thread t;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        nos = new int[10];

        btnStart = new Button("Start");
        btnClose = new Button("Close");

        panCenter = new SelectionPanel(nos);

        //Layout container
        HBox hbox = new HBox(10, btnStart, btnClose);

        BorderPane root = new BorderPane();
        root.setCenter(panCenter);
        root.setBottom(hbox);

        //action event when the start button is clicked it generates random numbers and started the sorting process
        btnStart.setOnAction(e -> {
            generateNos();
            panCenter.setFlag(true);
            t = new Thread(new MyRunnable());
            t.start();
        });

        btnClose.setOnAction(e -> {
            Platform.exit();
        });

        Scene scene = new Scene(root, 450, 200);
        primaryStage.setTitle("Selection Sort");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //generate random numbers
    private void generateNos() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            nos[i] = random.nextInt(100) + 1;
        }
    }

    class SelectionPanel extends Canvas {
        private int[] nos;
        private int pos1, pos2, pass;
        private boolean flag;

        public SelectionPanel(int[] nos) {
            this.nos = nos;
            setWidth(400);
            setHeight(100);
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void setPos(int pos1, int pos2) {
            this.pos1 = pos1;
            this.pos2 = pos2;
        }

        public void setPass(int pass) {
            this.pass = pass;
        }

        public void repaint() {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            if (flag) {
                int cw = (int) (getWidth() / 10) - 4;
                gc.setFill(Color.PINK);
                gc.fillRect(pos1 * cw + 4, 20, cw, cw);
                gc.setFill(Color.CYAN);
                gc.fillRect(pos2 * cw + 4, 20, cw, cw);
                gc.setStroke(Color.BLACK);
                for (int i = 0; i < 10; i++) {
                    gc.strokeRect(i * cw + 4, 20, cw, cw);
                    gc.strokeText(Integer.toString(nos[i]), i * cw + 10, 20 + cw / 2);
                }
                gc.strokeText("Pass: " + pass, 4, 10);
            }
        }
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            int n = 10;
            for (int i = 0; i < n - 1; i++) {
                panCenter.setPass(i + 1);
                int jMin = i;
                for (int j = i + 1; j < n; j++) {
                    if (nos[j] < nos[jMin]) {
                        jMin = j;
                    }
                }
                panCenter.setPos(i, jMin);
                int temp = nos[i];
                nos[i] = nos[jMin];
                nos[jMin] = temp;
                panCenter.repaint();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                panCenter.setPos(-1, -1);
                panCenter.repaint();
                btnStart.setDisable(false);
                btnClose.setDisable(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sorting Complete");
                alert.showAndWait();
            });
        }
    }
}
