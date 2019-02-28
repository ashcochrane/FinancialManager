package application.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class RootLayoutController {

	
	//Exit the program
	public void handleExit(ActionEvent actionEvent) {
		System.exit(0);
	}
	
	//Help Menu button behaviour
	public void handleHelp(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Ashton Cochrane's Financial Tracker...");
		alert.setHeaderText("This is a financial tracker designed to monitor your expenditure each month.");
		alert.setContentText("All you need to do is upload your monthly Bank Statements in pdf form.");
		alert.show();
	}
}
