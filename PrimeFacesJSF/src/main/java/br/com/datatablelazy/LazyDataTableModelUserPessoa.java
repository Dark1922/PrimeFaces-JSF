package br.com.datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.dao.DaoUsuario;
import br.com.model.UsuarioPessoa;

public class LazyDataTableModelUserPessoa<T> extends LazyDataModel<UsuarioPessoa> {

	private DaoUsuario<UsuarioPessoa> dao = new DaoUsuario<UsuarioPessoa>();

	public List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	
	private String sql = " from UsuarioPessoa ";

	@Override
	public List<UsuarioPessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		
		list = dao.getEntityManager().createQuery(getSql()).
				setFirstResult(first).
				setMaxResults(pageSize).getResultList();
		sql = " from UsuarioPessoa ";
		
		setPageSize(pageSize);
		
		Integer qtdRegistro = Integer.parseInt(dao.getEntityManager().createQuery("select count(1) " + getSql()).getSingleResult().toString());
		setRowCount(qtdRegistro);
		return list;
	}
	
	public String getSql() {
		return sql;
	}
	
	public List<UsuarioPessoa> getList() {
		return list;
	}
	
	public void pesquisar(String campoPesquisa) {
		sql += " where nome like '%"+campoPesquisa+"%'"; 
	}
}
