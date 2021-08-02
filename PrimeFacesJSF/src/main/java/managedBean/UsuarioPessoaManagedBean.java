package managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.dao.DaoUsuario;
import br.com.model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped // vai carregar o usuario na tela segurar os dados
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	// como ele está exetendo ao dao generic e dao usuario pode fazer isso

	@PostConstruct
	public void init() { // qnd iniciar a tela vai executar esse método de mostrar a tabela
		// vai consultar no banco apenas uma vez
		list = daoGeneric.getListEntity(UsuarioPessoa.class);
	}

	public String salvar() {

		daoGeneric.updat(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		list.add(usuarioPessoa); // adiciona pra lista o novo user
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com Sucesso!"));
		return "usuario-salvo";
	}

	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}

	public String remove() {

		try {
			daoGeneric.removerUsuario(usuarioPessoa);// método se usuario tiver telefone vai deletar ele dps a pessoa
			list.remove(usuarioPessoa); // remove da lista esse objeto
			usuarioPessoa = new UsuarioPessoa();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com Sucesso!"));

		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Informação: ", "Existem! telefones para o usuario"));
			} else {
				e.printStackTrace();
			}
		}
		return "";

	}

	// get da nossa lista
	public List<UsuarioPessoa> getList() {
		return list;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

}
