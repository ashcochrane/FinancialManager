package application.view;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	
	//Our primary stage that contains everything
	private Stage primaryStage;
	
	//BorderPane for RootLayout
	private BorderPane rootLayout;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Ashton's Financial Tracker");
		
		initRootLayout();
		
		initTransactionView();
			
	}
	
	//Initialize rootLayout
	public void initRootLayout() {
		try {
			//Load RootLayout  from RootLayout.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("RootLayout.fxml"));

			rootLayout = (BorderPane) loader.load();
			
			//Show the scene containing RootLayout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//
	public void initTransactionView() {
		try {
			//Load TransactionView from TransactionView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("TransactionView.fxml"));
			AnchorPane transactionView = (AnchorPane) loader.load();
			
			
			//Set transactionView to center of rootLayout
			rootLayout.setCenter(transactionView);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
