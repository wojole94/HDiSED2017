package pl.woleszko.polsl.view.gui.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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

		if (selectedFile != null) {
			textFieldPath1.setText(selectedFile.getAbsolutePath());
		} else {
			System.out.println("file is not valid");
		}
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
	void start(ActionEvent event) {
		// !!! to do
		// start obliczen
		// zabezpieczenie przed kliknieciem start bez sciezki
		if (!(textFieldPath1.getText().equals("") || textFieldPath2.getText().equals("")
				|| textFieldPath3.getText().equals(""))) {
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
