package model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import database.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AlunoModel {

	// Salva um aluno no banco de dados
	public static void save(Aluno aluno) {
		EntityManager em = DbConnection.getEntityManager();
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(aluno);
			et.commit();
		} catch (Exception e) {
			et.rollback();
		} finally {
			em.close();
		}
	}

	// Retorna uma lista Com todos os alunos
	public static ObservableList<Aluno> ListAlunoAll() {
		ObservableList<Aluno> alunoList = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Aluno");
		List<Aluno> alunoslist = q.getResultList();
		for (Aluno alunos : alunoslist) {
			alunoList.add(alunos);
		}
		return alunoList;
	}

	// Retorna uma lista dos Alunos cadastrados de acordo com parte do nome
	public static ObservableList<Aluno> FindAlunoName(String namePart) {
		ObservableList<Aluno> obsAlunosList = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Aluno A WHERE nome LIKE :namePart").setParameter("namePart",
				"%" + namePart + "%");
		List<Aluno> alunoslist = q.getResultList();
		for (Aluno alunos : alunoslist) {
			obsAlunosList.add(alunos);
		}
		return obsAlunosList;
	}

	// Retorna uma lista dos Alunos cadastrados de acordo com CPF
	public static ObservableList<Aluno> FindAlunoCPF(String cpfPart) {
		ObservableList<Aluno> obsAlunosListcpf = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Aluno WHERE cpf LIKE :cpfPart").setParameter("cpfPart", "%" + cpfPart + "%");
		List<Aluno> alunoslist = q.getResultList();
		for (Aluno alunos : alunoslist) {
			obsAlunosListcpf.add(alunos);
		}
		return obsAlunosListcpf;
	}

	public static void deleteAluno(Aluno del) {
		EntityManager em = DbConnection.getEntityManager();
		em.getTransaction().begin();
		Aluno aluno = em.find(Aluno.class, del.getCpf());
		em.remove(aluno);
		em.getTransaction().commit();

	}
	
	public static void Update(Aluno up) {
		EntityManager em = DbConnection.getEntityManager();
		em.getTransaction().begin();
		Aluno aluno = em.find(Aluno.class, up.getCpf());
		aluno.setNome(up.getNome());
		aluno.setDataNascimento(up.getDataNascimento());
		aluno.setFoto(up.getFoto());
		em.getTransaction().commit();

	}

}
