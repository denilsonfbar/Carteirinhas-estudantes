package Controller;

import java.io.IOException;
import java.text.ParseException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Curso;
import model.CursoModel;

public class CadCurso {

	@FXML
	private TextField cod_curso;
	@FXML
	private TextField NomeCurso;

	// Botão salvar o curso
	public void SalvarCurso() throws ParseException, IOException {
		try {
			Curso curso = new Curso();
			curso.setCodigo(cod_curso.getText());
			curso.setNome(NomeCurso.getText());
			CursoModel.save(curso);
			
			cod_curso.setText("");
			cod_curso.setText("");

			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Curso Salvo");
			dialogoInfo.setHeaderText("Curso salvo com sucesso!");
			dialogoInfo.showAndWait();
		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação.");
			dialogoInfo.showAndWait();
		}
	}

	@FXML
	private void Voltar() throws IOException {
		App.setRoot("principal");
	}
}
