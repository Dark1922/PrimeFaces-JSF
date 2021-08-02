package managedBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.dao.DaoUsuario;
import br.com.model.UsuarioPessoa;

@ManagedBean(name = "telefoneManegedBean")
@ViewScoped
public class TelefoneManegedBean {
	
	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUser = new DaoUsuario<UsuarioPessoa>();

	@PostConstruct
	public void init() {
		//vai trazer o código do usuario qnd clcia no telefone do id dele e trazer os tel dele massa
		String codUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigouser");
		user = daoUser.pesquisar(Long.parseLong(codUser), UsuarioPessoa.class);
	}
	
	public UsuarioPessoa getUser() {
		return user;
	}

	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}
}
