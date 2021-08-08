package br.com.datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.dao.DaoUsuario;
import br.com.model.UsuarioPessoa;

public class LazyDataTableModelUserPessoa<T> extends LazyDataModel<UsuarioPessoa> {


	public void setList(List<UsuarioPessoa> list) {
		this.list = list;
	}
	private static final long serialVersionUID = 1L;

	//instaciações
	private DaoUsuario<UsuarioPessoa> dao = new DaoUsuario<UsuarioPessoa>();

	public List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	
	private String sql = " from UsuarioPessoa "; //sql padrão

	@SuppressWarnings("unchecked")
	@Override           //ordenar os dados , pagina 1 a 5 
	public List<UsuarioPessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		//buscar apartir do resultado que o primefaces der vai vir do 0 ao 5, 5 ao 10 .. paginando os resultados 
		//empilhando até o máximo de resultado pageSize e retorna a lista de dados
		list = dao.getEntityManager().createQuery(getSql()).
				setFirstResult(first).
				setMaxResults(pageSize).getResultList();
		
		sql = " from UsuarioPessoa "; //dps q faz a consulta reinicia o sql pra ficar ele msm original
		
		setPageSize(pageSize); //seta o tamanho da página
		
		//qnts linhas de resultados tem e seta, select count da posição 1 so ultilizando 1 coluna q fica mais facil	
		Integer qtdRegistro = Integer.parseInt(dao.getEntityManager().createQuery("select count(1) " + getSql()).getSingleResult().toString());
		setRowCount(qtdRegistro); 
		
		return list; //retorna a lista
	}
	
	public String getSql() {
		return sql;
	}
	
	public List<UsuarioPessoa> getList() {
		return list;
	}
	
	public void pesquisar(String campoPesquisa) {
		sql += " where nome like '%" + campoPesquisa + "%'"; 
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public DaoUsuario<UsuarioPessoa> getDao() {
		return dao;
	}

	public void setDao(DaoUsuario<UsuarioPessoa> dao) {
		this.dao = dao;
	}

}
