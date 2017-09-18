package pl.woleszko.polsl.view.gui.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import pl.woleszko.polsl.interpret.AnomaliesDetector;

public class MenuController {
	private MainController mainController;

	@FXML
	private Button buttonStart;
	@FXML
	private Button buttonDetails;
	@FXML
	private TextField textFieldPath1;
	@FXML
	private TextField textFieldPath2;
	@FXML
	private TextField textFieldPath3;
	@FXML
	private TextField textFieldPathDirectory;

	@FXML
	void exit(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void openApplication(ActionEvent event) {

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/AppScreen.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		AppController appController = loader.getController();
		appController.setMainController(mainController);
		mainController.setScreen(pane);
	}

	@FXML
	void openOptions(ActionEvent event) {

	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	@FXML
	void buttonBrowse1(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv"));
		File selectedFile = fileChooser.showOpenDialog(null);
	}

	@FXML
	void buttonBrowse2(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			textFieldPath2.setText(selectedFile.getAbsolutePath());
		} else {
			System.out.println("file is not valid");
		}
	}

	@FXML
	void buttonBrowse3(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			textFieldPath3.setText(selectedFile.getAbsolutePath());
		} else {
			System.out.println("file is not valid");
		}
	}
	
	@FXML
	void buttonBrowseDirectory(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(null);

		if (selectedDirectory.exists() && selectedDirectory.isDirectory()) {
			textFieldPathDirectory.setText(selectedDirectory.getAbsolutePath());
		} else {
			System.out.println("directory is not valid");
		}
	}

	@FXML
	void start(ActionEvent event) {
		// !!! to do
		// start obliczen
		// zabezpieczenie przed kliknieciem start bez sciezki
/*		if (!(textFieldPath1.getText().equals("") || textFieldPath2.getText().equals("")
				|| textFieldPath3.getText().equals(""))) {
			buttonStart.setVisible(false);
			buttonDetails.setVisible(true);
		}*/
		if (!(textFieldPathDirectory.getText().equals(""))) {
			
	        ((Logger) LoggerFactory.getLogger("pl.woleszko")).setLevel(Level.DEBUG);
	        ((Logger) LoggerFactory.getLogger("pl.woleszko.polsl.model.utils")).setLevel(Level.INFO);
			
    		AnomaliesDetector analise = new AnomaliesDetector(textFieldPathDirectory.getText());
    		analise.checkPipes();
    		analise.checkNozzles();

	        
			buttonStart.setVisible(false);
			buttonDetails.setVisible(true);
		}
	}

	@FXML
	void details(ActionEvent event) {
		// !!! to do
		// szczegoly wykonanych obliczen
	}
}
