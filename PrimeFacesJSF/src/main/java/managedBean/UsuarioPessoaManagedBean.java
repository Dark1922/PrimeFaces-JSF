package managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped // vai carregar o usuario na tela segurar os dados
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();

	
	public String salvar() {
		
		daoGeneric.updat(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com Sucesso!"));
		return "";
	}
	
	public String novo() {
		
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}
		
	public String remove() {

		daoGeneric.deletePorId(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com Sucesso!"));
		return "";
	}
	

	
	//get da nossa lista
	public List<UsuarioPessoa> getList() {
		list = daoGeneric.getListEntity(UsuarioPessoa.class);
		return list;
	}
	
	
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

}
