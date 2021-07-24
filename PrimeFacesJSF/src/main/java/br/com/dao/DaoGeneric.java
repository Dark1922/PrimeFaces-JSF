package br.com.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.primefaces.HibernateUtil;

public class DaoGeneric<E> {// e de entidade

	private EntityManager entityManager = HibernateUtil.geEntityManager();

	public void salvar(E entidade) {
 
		EntityTransaction transaction = entityManager.getTransaction(); // transação

		transaction.begin(); // inica transação

		entityManager.persist(entidade); // vai persistir a transação

		transaction.commit();// vai salvar

	}

	public E updateMerge(E entidade) {// salva ou atualiza

		EntityTransaction transaction = entityManager.getTransaction(); // transação

		transaction.begin(); // inica transação começa

		E entidadeSalva = entityManager.merge(entidade); // recebe um objeto salva se n existir , se existir atualiza

		transaction.commit();// vai salvar

		return entidadeSalva;
	}

	public E pesquisar(E entidade) {

		Object id = HibernateUtil.getPrimaryKey(entidade);

		E e = (E) entityManager.find(entidade.getClass(), id); // find buscar

		return e; // pesquiso
	}

	public E pesquisar2(Long id, Class<E> entidade) {
		E e = (E) entityManager.find(entidade, id); // find buscar
		return e; // pesquiso
	}

	public void deletarPorId(E entidade) {

		// qual é a primaryKey dela para deletar
		Object id = HibernateUtil.getPrimaryKey(entidade);

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		// vai falar tipo dele from usuario where id = ao id passado , tá genérico
		entityManager
				.createNativeQuery(
						"delete from " + entidade.getClass().getSimpleName().toLowerCase() + 
						" where id =" + id).executeUpdate(); // faz delete

				
		transaction.commit(); // comita pro banco as alteração grava
	}

	// lista e a classe que vai listar entidade
	public List<E> listar(Class<E> entidade) {

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		// from o nome da nossa entidade e retorna o resultado em lista
		//mesma coisa que from * usuariopessoa só que de forma genérica
		List<E> lista = entityManager.createQuery("from  " + entidade.getName()).getResultList();

		transaction.commit();

		return lista;

	}
	
	//acessar nosso entityManager em outras parte do projeto
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
