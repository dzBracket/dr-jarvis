package dr;

import Controller.alertBox;
import DataClass.*;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import libs.stackTraceViewer;
import libs.templateCracker.Holder;
import libs.templateCracker.templateController;
import libs.templateCracker.templateDeserializer;
import libs.templateCracker.templateSerializer;
import model.components.amazingDialog;
import model.components.spawnButton;
import model.stageLoader;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static dr.FinalsVal.*;

public class Main extends Application {
    public static dataWorker<Drug> drugThread=null;
    public static dataWorker<Patient> patientThread=null;
    public static dataWorker<prescriptionsHistory> hisPerThread=null;
    public static dataWorker<userData> userDataWorker =null;
    public static dataWorker<customizable> costumeThread=null;
   static public stageLoader sl=null;
    public static Stage mainStage =null;
    public static async wait=new async();
    private boolean Error=false;
    public void init() {


        System.setProperty("prism.text", "t2k");
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("file.encoding", "UTF-8");

        try {
            drugThread=new dataWorker<>("drug",Drug.class,requestD);
            drugThread.start();
            hisPerThread=new dataWorker<>("prescriptions",prescriptionsHistory.class,requestH);
            hisPerThread.start();

            patientThread=new dataWorker<>("patient",Patient.class,requestP);
            patientThread.start();

            userDataWorker =new dataWorker<>("data",userData.class,requestU);
            userDataWorker.start();

            costumeThread=new dataWorker<>("tempCostume",customizable.class,requestT);
            costumeThread.start();

            Platform.runLater(()->sl=new stageLoader("Dr.jarvis - Dashboard","/dr/FXML/PAGES/main_pane.fxml"));
            System.out.println("wait");

        } catch (Exception e) {
           Error=true;
            Platform.runLater(()->{
                sl =new stageLoader("DrJarvis -ERROR "+e.getMessage(),"/dr/FXML/POPUP/alertBox.fxml");
                amazingDialog alr=new amazingDialog((alertBox) sl.getController());
                alr.setTitle("Ohh ohh!!!");
                alr.setContent("Something wrong happen. try to run the app as administrator");
                alr.setImage(amazingDialog.WARNING);
                //sl.getStage().setScene(new Scene(alr.self.container));

                JFXButton exit = spawnButton.red("Exit");
                JFXButton show = spawnButton.blue("show...");
            alr.getButtonList().setAll(show,exit);

                exit.setOnAction(v->{
                    Platform.exit();
                    System.exit(0);
                });
                show.setOnAction(v->{
                    stackTraceViewer tracer = new stackTraceViewer(e);
                    tracer.showAndWait();
                });
            });


        }









    }

    @Override
    public void start(Stage primaryStage) {
/*
        ArrayList<Holder> holders=new ArrayList<Holder>();
        Holder holder = new Holder();
        holder.setType(Holder.LABEL);
        holder.setValue("hello doc");
        holder.setYPos(7.0);
        holder.setWidth(222.0);
        holder.setHeight(37.0);
        holder.setStyle("-fx-font-weight: bold;-fx-text-fill:Green;-fx-background-color: black");
        holder.setName(Holder.PATIENT_FNAME);
        holders.add(holder);
        holder = new Holder();
        holder.setType(Holder.LABEL);
        holder.setValue("hello doc");
        holder.setYPos(45.0);
        holder.setWidth(222.0);
        holder.setHeight(37.0);
        holder.setStyle("-fx-font-weight: bold;-fx-text-fill:Green;");
        holder.setName("tmp");
        holders.add(holder);
        holder = new Holder();
        holder.setType(Holder.IMAGE);
        holder.setValue("C:\\Users\\forgiven\\Desktop\\dr-jarvis\\src\\main\\resources\\dr\\image\\appIcon.png");
        holder.setYPos(100.0);
        holder.setXPos(100.0);
        holder.setWidth(10.0);
        holder.setHeight(10.0);
        //holder.setStyle("-fx-font-weight: bold;-fx-text-fill:Green;");
        holder.setName("img");
        holders.add(holder);
        System.out.println(holders);
        templateController controller = new templateController(holders);
        stageLoader sc=new stageLoader("temp",controller.container);
controller.setTemplateInfo(new Patient().Patient("dd","moh","boh","",1, LocalDate.now(),""));
        sc.show();

        List<Holder> v = controller.serialize();
        System.out.println(v);
*/
        stageLoader sc=new stageLoader("temp","/dr/FXML/POPUP/templateEditor.fxml");
sc.show();
               if (!Error) {
            primaryStage = sl.getStage();
            mainStage = primaryStage;
            wait.dispatchEvent();
            mainStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
        }
       // sl.show();

    }
}
