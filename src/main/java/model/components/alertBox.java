package model.components;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;

import java.net.URL;
import java.util.ResourceBundle;

public class alertBox implements Initializable {

    public AnchorPane general;
    public Pane container;
    public Polyline ptr;
    public Polyline pbr;
    public Polyline pbl;
    public Polyline ptl;
    public Label title;
    public Label content;
    public HBox buttonList;

    alertBox self;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
