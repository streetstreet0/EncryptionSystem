module CloudSecurityAssignment {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.base;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
