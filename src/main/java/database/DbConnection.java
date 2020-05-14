package database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Carliete
 */
public final class DbConnection {

	private static EntityManagerFactory emf = null;
	private static EntityManager em = null;
				
	public static EntityManager getEntityManager() {
		
		if (emf == null){
			emf = Persistence.createEntityManagerFactory("carteirinhasPU");
		}
		
		if (em == null) {
			em = emf.createEntityManager();
		}
		
		if (!emf.isOpen()){
			emf = Persistence.createEntityManagerFactory("carteirinhasPU");
		}
		
		if (!em.isOpen()){
			em = emf.createEntityManager();
		}
		
		return em;
	}
	
	public static void close() {
		emf.close();
	}
}
