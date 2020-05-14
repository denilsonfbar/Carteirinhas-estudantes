package Controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Aluno;
import model.AlunoModel;
import model.Matricula;
import model.MatriculaModel;

public class CarteirinhaFull {
	
	private String numeroMatricula;
	private String cpfAluno;
	private String nomeAluno;
	
	public String getNumeroMatricula() {
		return numeroMatricula;
	}
	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}
	
	public String getCpfAluno() {
		return cpfAluno;
	}
	public void setCpfAluno(String cpfAluno) {
		this.cpfAluno = cpfAluno;
	}
	
	public String getNomeAluno() {
		return nomeAluno;
	}
	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
	
	public static ObservableList<CarteirinhaFull> findCarteirinhasByMatricula(String matriculaPart) {
		ObservableList<Matricula> matriculasObsList = MatriculaModel.FindAlunoMat(matriculaPart);
		return createCarteirinhasFullObsListFromMatriculaObsList(matriculasObsList);		
	}
	
	public static ObservableList<CarteirinhaFull> findCarteirinhasByCpfAluno(String cpfAlunoPart) {
		ObservableList<Aluno> alunosObsList = AlunoModel.FindAlunoCPF(cpfAlunoPart);
		return createCarteirinhasFullObsListFromAlunosObsList(alunosObsList);		
	}
	
	public static ObservableList<CarteirinhaFull> findCarteirinhasByNameAluno(String nameAlunoPart) {
		ObservableList<Aluno> alunosObsList = AlunoModel.FindAlunoName(nameAlunoPart);
		return createCarteirinhasFullObsListFromAlunosObsList(alunosObsList);		
	}
	
	private static ObservableList<CarteirinhaFull> createCarteirinhasFullObsListFromAlunosObsList(ObservableList<Aluno> alunosObsList) {
		ObservableList<CarteirinhaFull> carteirinhasFullObsList = FXCollections.observableArrayList();
		ObservableList<Matricula> matriculasAlunoObsList = FXCollections.observableArrayList();
		for (Aluno aluno : alunosObsList) {
			matriculasAlunoObsList.clear();
			List<Matricula> matriculasAluno = aluno.getMatriculas();
			for (Matricula matricula : matriculasAluno) {
				matriculasAlunoObsList.add(matricula);
			}
			carteirinhasFullObsList.addAll(createCarteirinhasFullObsListFromMatriculaObsList(matriculasAlunoObsList));
		}
		return carteirinhasFullObsList;		
	}
	
	private static ObservableList<CarteirinhaFull> createCarteirinhasFullObsListFromMatriculaObsList(ObservableList<Matricula> matriculasObsList) {
		ObservableList<CarteirinhaFull> carteirinhasFullObsList = FXCollections.observableArrayList();
		for(Matricula matricula : matriculasObsList) {
			CarteirinhaFull carteirinhaFull = new CarteirinhaFull();
			carteirinhaFull.setNumeroMatricula(matricula.getNumero());
			carteirinhaFull.setCpfAluno(matricula.getAluno().getCpf());
			carteirinhaFull.setNomeAluno(matricula.getAluno().getNome());
			carteirinhasFullObsList.add(carteirinhaFull);		
		}
		return carteirinhasFullObsList;
	}

}
