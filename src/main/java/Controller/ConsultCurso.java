package Controller;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Curso;
import model.CursoModel;

public class ConsultCurso {

	@FXML
	private TextField Encontrar;
	@FXML
	private TextField ConsultCodCurso;

	// table view De Consultar curso
	@FXML
	private TableView<Curso> TVCadastroCurso = new TableView<Curso>();
	@FXML
	private TableColumn<Curso, String> TCCursoId = new TableColumn<Curso, String>("Id Curso");
	@FXML
	private TableColumn<Curso, String> TCCursoNome = new TableColumn<Curso, String>("Nome");

	@FXML
	private void BuscarCurso() {
		TVCadastroCurso.getItems().removeAll(TVCadastroCurso.getItems());
		try {
			ObservableList<Curso> listCurso = CursoModel.findCursosByNamePart(Encontrar.getText());
			if (listCurso.isEmpty()) {
				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				dialogoInfo.setTitle("Curso não encontrado");
				dialogoInfo.setHeaderText("Curso não encontrado na base de dados");
				dialogoInfo.showAndWait();
			} else {
				TCCursoId.setCellValueFactory(new PropertyValueFactory<Curso, String>("codigo"));
				TCCursoNome.setCellValueFactory(new PropertyValueFactory<Curso, String>("nome"));
				TVCadastroCurso.getItems().addAll(listCurso);
			}
		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação");
			dialogoInfo.showAndWait();
		}
	}

	@FXML
	private void BuscarCursoCod() {
		TVCadastroCurso.getItems().removeAll(TVCadastroCurso.getItems());
		try {
			ObservableList<Curso> list = CursoModel.EncontrarCursoCod(ConsultCodCurso.getText());
			if (list.isEmpty()) {
				System.out.println("Oi");
				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				dialogoInfo.setTitle("Curso não encontrado");
				dialogoInfo.setHeaderText("Curso não encontrado na base de dados");
				dialogoInfo.showAndWait();
			} else {
				TCCursoId.setCellValueFactory(new PropertyValueFactory<Curso, String>("codigo"));
				TCCursoNome.setCellValueFactory(new PropertyValueFactory<Curso, String>("nome"));
				TVCadastroCurso.getItems().addAll(list);
			}
		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação");
			dialogoInfo.showAndWait();
		}
	}

	@FXML
	public void DeletarCurso() {
		try {
			String del = TVCadastroCurso.getSelectionModel().getSelectedItem().getCodigo();
			Curso curso = new Curso(del);
			CursoModel.DeleteCurso(curso);

			TVCadastroCurso.getItems().removeAll(TVCadastroCurso.getItems());

			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Operação realizada com sucesso");
			dialogoInfo.setHeaderText("Informações excluidas com sucesso");
			dialogoInfo.showAndWait();

		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação");
			dialogoInfo.showAndWait();
		}

	}

	@FXML
	private void Voltar() throws IOException {
		App.setRoot("principal");
	}

}
