package model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import database.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CursoModel {

	// Salva um curso no banco de dados
	public static void save(Curso curso) {
		EntityManager em = DbConnection.getEntityManager();
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(curso);
			et.commit();
		} catch (Exception e) {
			et.rollback();
		} finally {
			em.close();
		}
	}

	// Retorna uma lista com todos os cursos cadastrados
	public static ObservableList<Curso> getCursos() {
		ObservableList<Curso> obsCursosList = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Curso");
		List<Curso> cursosList = q.getResultList();
		for (Curso curso : cursosList) {
			obsCursosList.add(curso);
		}
		em.close();
		return obsCursosList;
	}

	// Retorna uma lista dos cursos cadastrados de acordo com parte do nome do curso
	public static ObservableList<Curso> findCursosByNamePart(String namePart) {
		ObservableList<Curso> obsCursosList = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Curso WHERE nome LIKE :namePart").setParameter("namePart", "%" + namePart + "%");
		List<Curso> cursosList = q.getResultList();
		for (Curso curso : cursosList) {
			obsCursosList.add(curso);
		}
		em.close();
		return obsCursosList;
	}

	// Retorna uma lista dos cursos cadastrados de acordo com parte do Codigo
	public static ObservableList<Curso> EncontrarCursoCod(String Cod) {
		ObservableList<Curso> obsCursosList = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Curso WHERE codigo LIKE :Cod").setParameter("Cod", "%" + Cod + "%");
		List<Curso> cursosList = q.getResultList();
		for (Curso curso : cursosList) {
			obsCursosList.add(curso);
		}
		em.close();
		return obsCursosList;
	}

	public static void DeleteCurso(Curso del) {
		EntityManager em = DbConnection.getEntityManager();
		em.getTransaction().begin();
		Curso curso = em.find(Curso.class, del.getCodigo());
		em.remove(curso);
		em.getTransaction().commit();

	}
}
