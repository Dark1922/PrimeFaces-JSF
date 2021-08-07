package br.com.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.com.jpautil.JPAUtil;
import br.com.model.UsuarioPessoa;

public class DaoUsuario<E> extends  DaoGeneric<UsuarioPessoa> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//método de deletar em cascata usuario que tenha telefone
	//deletando primeiro o filhi dps o pai
	@Transactional
    public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
		EntityManager entityManager = JPAUtil.getEntityManager();
		entityManager.getTransaction().begin();
    	String sqlDeleteFone = "delete from telefoneuser where usuariopessoa_id = " + pessoa.getId();
        //método do DaoGeneric da conexão com o banco de dados  
    	entityManager.createNativeQuery(sqlDeleteFone).executeUpdate();//faz atualização ou delete
    	
    	String sqlDeleteEmail = "delete from emailuser where usuariopessoa_id = " + pessoa.getId();
    	entityManager.createNativeQuery(sqlDeleteEmail).executeUpdate();
    	
    	entityManager.getTransaction().commit();
    	
    	super.deletePorId(pessoa);
    	
    }
}
