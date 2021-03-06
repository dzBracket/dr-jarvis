package Controller;

import DataClass.Patient;
import DataClass.prescriptionsHistory;
import com.jfoenix.controls.JFXButton;
import dr.FinalsVal;
import dr.Main;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import libs.cellController;
import libs.requestFormer;
import model.components.amazingDialog;
import model.components.animations;
import model.components.spawnButton;
import model.popUpWindow;
import model.showButton;
import model.stageLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static dr.FinalsVal.*;
import static dr.Main.mainStage;
import static libs.helper.movableFalse;

public class PatientList implements Initializable {
    public TableView  <Patient> patient_table;
    public TableColumn<Patient,String> first_C;
    public TableColumn<Patient,String> lastName_C;
    public TableColumn<Patient, Integer> age_C;
    public TableColumn<Patient,String> lastVisite_C;
    public TableColumn<Patient,String> id_C;
    public TableColumn<Patient,String > menu_C;
    public TableColumn<Patient,String> diagnostic_C;
public cellController<Patient> cellController=new cellController<>();
    static ObservableList<Patient> data = FXCollections.observableArrayList();
    public static popUpWindow  showField ;
    public static Stage Table_quick_stage ;
    public  static  quick_panelC control ;

    static public  Stage patientFormStage;

    public Spinner<Integer> show_spinner;
    public TextField write_TXF;
    public JFXButton add_patient_btn;
    public Label info2_label;
    private final requestFormer<Patient> req=formerP;
    private final requestFormer<prescriptionsHistory> formerH=new requestFormer<>();
    private final requestFormer<prescriptionsHistory> req2= FinalsVal.formerH;
    Parent root ;
    Patient selectedPatient=null;
    private stageLoader sl;
    patientDetailsC detailController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        eventTrigger();
        initEvent();
        initCol();
        loadData();

       movableFalse(patient_table);

    }

    public void initCol(){
        id_C.getStyleClass().add("start");
        menu_C.getStyleClass().add("end");
        first_C.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName_C.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        age_C.setCellValueFactory(new PropertyValueFactory<>("age"));
        lastVisite_C.setCellValueFactory(new PropertyValueFactory<>("lastVisit"));
        id_C.setCellValueFactory(new PropertyValueFactory<>("PatientId"));
        diagnostic_C.setCellFactory(cellController.BCellFactory(new showButton("show")));
        menu_C.setCellFactory(cellController.MCellFactory(new String[]{"dr/image/trash_24px.png", "dr/image/ball_point_pen_24px.png", "dr/image/add_32px.png"},new  String[]{"Delete...","Edit...","new prescription..."}));
    }

    public void  loadData(){

            req.onReceive(v->{
                patient_table.setItems(req.respond);
                System.out.println("patient "+(patient_table.getItems().size()-req.respond.size()));
            });

            requestP.offer(req.get());
        stageLoader op=new stageLoader("Patient info","/dr/FXML/POPUP/patientDetails.fxml");
         detailController = (patientDetailsC) op.getController();
    }


    public void eventTrigger(){

        cellController.clicked.addListener(v->{
          /*  FXMLLoader loader =new FXMLLoader(getClass().getResource("/dr/FXML/POPUP/show_window.fxml"));
            try {
                Parent root = loader.load();
                show_winC control=loader.getController();
                showField = new popUpWindow(root.getChildrenUnmodifiable());
                showField.show(Main.mainStage);
                control.value_area.setText(patient_table.getItems().get(cellController.index).getLastDiagnostic());

            } catch (IOException e) {
                e.printStackTrace();
            }*/
            initPatDetailsStage(patient_table.getItems().get(cellController.index));

        });
        cellController.MenuDispatcher.addListener(e-> {
                    IntegerProperty prop= (IntegerProperty) e;
                    if(prop.getValue()==0){
                        System.out.println("delete");
                        amazingDialog check=new amazingDialog();
                        check.setTitle("confirm you choice.");
                        check.setContent("you are going to delete :\n "+patient_table.getItems().get(cellController.index).getFullName()+" and all the "+patient_table.getItems().get(cellController.index).getPrescriptionsId().size()+" prescriptions");
                         check.setImage("warning");
                        JFXButton ok = spawnButton.red("Delete");
                        JFXButton cancel = spawnButton.gray("Cancel");
                        check.setPosition(300,300);
                        check.show(mainStage);
                        check.getButtonList().setAll(ok,cancel);
                       ok.setOnAction(v->{
                           cleanDelete(patient_table.getItems().get(cellController.index));
                           cancel.fire();
                       });
                           cancel.setOnAction(v->check.close());

                        }
                        write_TXF.clear();

                    if(prop.getValue()==1){
                    loadAddStage(patient_table.getItems().get(cellController.index));
                    }
                    if(prop.getValue()==2){
                        open_quick_pane(patient_table.getItems().get(cellController.index));


                    }
                }
        );
        write_TXF.textProperty().addListener(v->{
           String value=((StringProperty)v).getValue();
            if(value.length()>1)
            requestP.offer(req.callBack("querySearch","SELECT *","WHERE firstName $LIKE '"+value+"%' OR lastName $LIKE '"+value+"%'",String.class));
            else
                requestP.offer(req.get());
        });
    }
    public void initPatDetailsStage(Patient patient){


        detailController.pops();
        detailController.loadFrom(patient);
        animations easeIn=new animations(detailController.quick_pane,animations.EASE_IN);
        easeIn.playAnimation();
      //  op.getStage().close();
        detailController.getPops().addCloseAnimation(new animations(detailController.quick_pane,animations.EASE_OUT));

        detailController.add_button.setOnAction(v->{
            open_quick_pane(patient);
        });
        detailController.delete_btn.setOnAction(v->{
            cellController.MenuDispatcher.setValue(-1);
            cellController.MenuDispatcher.setValue(0);
        });

    }

private void initEvent(){
        req2.onReceive(v->{

            requestP.offer(req.remove(selectedPatient));
            selectedPatient=null;
        });
        formerH.onReceive(v->{
            prescriptionsHistory[] blue = formerH.respond.toArray(prescriptionsHistory[]::new);
            System.out.println(blue.length);
            Platform.runLater(()->requestH.offer(req2.removeBunch(blue)));
        });
}
    private void cleanDelete(Patient patient) {
        selectedPatient = patient;

        System.out.println(patient.getPrescriptionsId().toArray(String[]::new).length);
        requestH.offer(formerH.mojoJojo("getPresId", patient.getPrescriptionsId().toArray(String[]::new)));

    }

    static public void closePopuUp(){
        patientFormStage.close();
    }

void loadAddStage(Patient options){
    stageLoader patientLoader = new stageLoader("Add new patient","/dr/FXML/POPUP/New_patient.fxml");
    patientFormStage =patientLoader.getStage();
    if(options!=null) ((NewPatient)patientLoader.getController()).preFilled(options);
    patientLoader.show();


}
    public void add_patient_table() {

loadAddStage(null);
    }
    public void initializePane() {
         sl = new stageLoader("New prescription","/dr/FXML/POPUP/quick_panel.fxml");
        root = sl.getRoot();
        control= (quick_panelC) sl.getController();

    }
    public void open_quick_pane(Patient selectedPatient)  {
        initializePane();
        Scene   quick_scene =sl.getScene();
        quick_scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        Table_quick_stage =sl.getStage();
          control.setInfoLabelValues(selectedPatient);
        sl.show();
        MainPanelC.effect.setRadius(3.25);
        Table_quick_stage .setOnCloseRequest(event -> {
            Table_quick_stage .close();
            MainPanelC.effect.setRadius(0);
        });



    }
}
