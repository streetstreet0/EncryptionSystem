module CloudSecurityAssignmentSaved {
	requires javafx.controls;
	requires java.sql;
	requires java.desktop;
	requires javafx.graphics;
	requires json.simple;
	
	opens application to javafx.graphics, javafx.fxml;
}
