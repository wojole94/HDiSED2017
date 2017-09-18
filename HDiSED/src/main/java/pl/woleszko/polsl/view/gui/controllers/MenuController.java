package pl.woleszko.polsl.view.gui.controllers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import pl.woleszko.polsl.interpret.AnomaliesDetector;
import pl.woleszko.polsl.model.entities.NozzleMeasuresEntity;
import pl.woleszko.polsl.model.entities.TankMeasuresEntity;
import pl.woleszko.polsl.model.impl.FileAccessorCSV;

public class MenuController {
	private MainController mainController;
	private File csvDirectoryPath = null;

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
	void about(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("Wykrywanie wycieków ze zbiorników paliw płynnych");
		alert.setContentText("v1.0, 20.09.2017r.\n"
				+ "Informatyka sem.6, AEiI \n"
				+ "\nwykonali: \n"
				+ "Oleszko Wojciech \n"
				+ "Janocha Sebastian \n");

		alert.showAndWait();
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
		csvDirectoryPath = directoryChooser.showDialog(null);

		if (csvDirectoryPath != null) {
			boolean check1 = new File(csvDirectoryPath, "nozzleMeasures.csv").exists();
			boolean check2 = new File(csvDirectoryPath, "refuel.csv").exists();
			boolean check3 = new File(csvDirectoryPath, "tankMeasures.csv").exists();
			if (check1 && check2 && check3) {
				textFieldPathDirectory.setText(csvDirectoryPath.getAbsolutePath());
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Selected directory does not contain required files.");
				alert.setContentText("You must select another directory with the following files: \n" + "-refuel.csv \n"
						+ "-tankMeasures.csv \n" + "-nozzleMeasures.csv \n");
				csvDirectoryPath = null;
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Directory path is invalid.");
			alert.setContentText("Directory path is empty.");
			textFieldPathDirectory.clear();
			alert.showAndWait();
		}
	}

	@FXML
	void start(ActionEvent event) {
		if (csvDirectoryPath != null) {

			((Logger) LoggerFactory.getLogger("pl.woleszko")).setLevel(Level.DEBUG);
			((Logger) LoggerFactory.getLogger("pl.woleszko.polsl.model.utils")).setLevel(Level.INFO);

			AnomaliesDetector analise = new AnomaliesDetector(csvDirectoryPath);
			analise.checkPipes();
			analise.checkNozzles();

			buttonStart.setVisible(false);
			buttonDetails.setVisible(true);
		}
	}

	@FXML
	void details(ActionEvent event) {
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
}
