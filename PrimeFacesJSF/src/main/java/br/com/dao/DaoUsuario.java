package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.com.jpautil.JPAUtil;
import br.com.model.UsuarioPessoa;

public class DaoUsuario<E> extends  DaoGeneric<UsuarioPessoa> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	//método de deletar em cascata usuario que tenha telefone
	//deletando primeiro o filhi dps o pai
	@Transactional
    public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
		
		getEntityManager().getTransaction().begin();
		
		String sqlDeleteFone = "delete from telefoneuser where usuariopessoa_id = " + pessoa.getId();
        //método do DaoGeneric da conexão com o banco de dados  
		getEntityManager().createNativeQuery(sqlDeleteFone).executeUpdate();//faz atualização ou delete
    	
    	String sqlDeleteEmail = "delete from emailuser where usuariopessoa_id = " + pessoa.getId();
    	getEntityManager().createNativeQuery(sqlDeleteEmail).executeUpdate();
    	
    	getEntityManager().getTransaction().commit();
    	
    	super.deletePorId(pessoa);
    	
		
    	
    }

	public List<UsuarioPessoa> pesquisar(String campoPesquisa) {
		
		//trabalha com os objetos e classes / like pesquisar por parte
	Query query  = super.getEntityManager().createQuery("from UsuarioPessoa where nome like '%"+campoPesquisa+"%' ");
		
		return query.getResultList(); //retorna o resultado da lista
	}
}
