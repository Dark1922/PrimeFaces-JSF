package br.com.dao;

import java.io.Serializable;

import br.com.model.UsuarioPessoa;

public class DaoUsuario<E> extends  DaoGeneric<UsuarioPessoa> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//método de deletar em cascata usuario que tenha telefone
	//deletando primeiro o filhi dps o pai
    public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
    	String sqlDeleteFone = "delete from TelefoneUser where usuariopessoa_id = " + pessoa.getId();
    	
        //método do DaoGeneric da conexão com o banco de dados  
    	getEntityManager().getTransaction().begin();
    	getEntityManager().createNativeQuery(sqlDeleteFone).executeUpdate();//faz atualização ou delete
    	getEntityManager().getTransaction().commit();
    	
    	super.deletePorId(pessoa);
    	
    }
}
