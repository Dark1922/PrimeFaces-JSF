package br.com.primefaces;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
	// deixar istanciado a conexão com o banco de dados

	// static pq só pode ler o arquivo apenas uma vez para evitar erros
	public static EntityManagerFactory factory = null;

	
	static {
		init(); //sempre que chama o nosso método HibernateUtil não precisa instaciar o objeto
		//o init sera invocado
	}
	
	private static void init() {

		try {
			
            if(factory == null) {
            	//se ele for null vamos ler esse arquivo
            	factory = Persistence.createEntityManagerFactory("pos-java-maven-hibernate");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static EntityManager geEntityManager() {
		
		return factory.createEntityManager();//prove a parte de  persistencia
	}
	
	//método para identificar o ID
	public static Object getPrimaryKey(Object entity) {//Retorna a primray Key
		 
		return factory.getPersistenceUnitUtil().getIdentifier(entity);
		
	}

}
