package Controller;

import java.io.IOException;

import javafx.fxml.FXML;

public class PrincipalController {

	@FXML
	private void CadasAluno() throws IOException {
		App.setRoot("CadasAluno");
	}
	@FXML
	private void ConsultAluno() throws IOException {
		App.setRoot("ConsultaAluno");
	}
	@FXML
	private void CadasCurso() throws IOException {
		App.setRoot("CadasCurso");
	}
	@FXML
	private void ConsultCurso() throws IOException {
		App.setRoot("ConsultaCurso");
	}
	@FXML
	private void CarteiraIndividual() throws IOException {
		App.setRoot("GeraIndividual");
	}
	@FXML
	private void GeraPdf() throws IOException {
		App.setRoot("GerarCarteirinha");
	}

}
