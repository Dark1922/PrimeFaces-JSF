package managedBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.dao.DaoTelefones;
import br.com.dao.DaoUsuario;
import br.com.model.TelefoneUser;
import br.com.model.UsuarioPessoa;

@ManagedBean(name = "telefoneManegedBean")
@ViewScoped
public class TelefoneManegedBean {

	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUser = new DaoUsuario<UsuarioPessoa>();
	private DaoTelefones<TelefoneUser> daoTelefone = new DaoTelefones<TelefoneUser>();

	private TelefoneUser telefone = new TelefoneUser();

	@PostConstruct
	public void init() {
		// vai trazer o código do usuario qnd clcia no telefone do id dele e trazer os
		// tel dele massa
		String codUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigouser");
		user = daoUser.pesquisar(Long.parseLong(codUser), UsuarioPessoa.class);
	}

	public String salvar() {
		telefone.setUsuarioPessoa(user); // pega o user que ta carregado
		daoTelefone.updat(telefone);
		telefone = new TelefoneUser();
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com Sucesso!"));
		return "";
	}

	public String novo() {
		telefone = new TelefoneUser();
		return "";
	}

	public String remove() throws Exception {

		daoTelefone.deletePorId(telefone);
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);
		telefone = new TelefoneUser();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Telefone Removido!"));

		return "";
	}

	public UsuarioPessoa getUser() {
		return user;
	}

	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}

	public void setDaoTelefone(DaoTelefones<TelefoneUser> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}

	public DaoTelefones<TelefoneUser> getDaoTelefone() {
		return daoTelefone;
	}

	public void setDaoUser(DaoUsuario<UsuarioPessoa> daoUser) {
		this.daoUser = daoUser;
	}

	public DaoUsuario<UsuarioPessoa> getDaoUser() {
		return daoUser;
	}

	public void setTelefone(TelefoneUser telefone) {
		this.telefone = telefone;
	}

	public TelefoneUser getTelefone() {
		return telefone;
	}
}
