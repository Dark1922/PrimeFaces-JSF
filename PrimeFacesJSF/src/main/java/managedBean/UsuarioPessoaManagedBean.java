package managedBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import br.com.dao.DaoUsuario;
import br.com.model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped // vai carregar o usuario na tela segurar os dados
public class UsuarioPessoaManagedBean { 

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	// como ele está exetendo ao dao generic e dao usuario pode fazer isso
	
	//elemento do primefaces pra fazer gráfico de tabela
	private BarChartModel barChatModel = new BarChartModel();

	@PostConstruct
	public void init() { // qnd iniciar a tela vai executar esse método de mostrar a tabela
		// vai consultar no banco apenas uma vez
		list = daoGeneric.getListEntity(UsuarioPessoa.class);
		
		for (UsuarioPessoa usuarioPessoa : list) {
			//motando a tabela do usuario pelo nome e selario com chartseries passando pro barchatmodel
			ChartSeries userSalario = new ChartSeries("Sálario dos Usuarios");
			userSalario.setLabel("Users");
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());
			barChatModel.addSeries(userSalario);
		}
	}

	public String salvar() {

		daoGeneric.updat(usuarioPessoa);
		list.add(usuarioPessoa); // adiciona pra lista o novo user
		usuarioPessoa = new UsuarioPessoa();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com Sucesso!"));
		return ""; 
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
	
	
	public void pesquisaCep(AjaxBehaviorEvent event) {// tem que tar declarado aqui pro jsf entender o listener

		try {
			// oque tiver sendo enviado pela parte do html do cep vai está sendo enviado
			// para o inputText e pegamos ele dai
			// e passa ele pra buscar na url de acordo com oque o usuário escreveu
			HtmlInputText inputText = (HtmlInputText) event.getSource(); // vai converter pra um input text html

			// linkg do ibg que retorna um json pelo cep passado se ele for válido passando
			// o cep que foi passado por parametro
			URL url = new URL("https://viacep.com.br/ws/" + inputText.getValue() + "/json/");// monta a url
			URLConnection connectionDaUrl = url.openConnection(); // abre a conexão
			InputStream is = connectionDaUrl.getInputStream(); // vai executar ao lado do servidor e vai retornar os
																// dados / retorno
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); // faz a leitura desse retorno

			String cep = ""; //váriavel auxiliar
			StringBuilder jsonCep = new StringBuilder();
			while ((cep = br.readLine()) != null) { // vai varrer as linha eqnt for diferente de null
				jsonCep.append(cep); //vai adicionando oq tiver no cep logadouro etcq
			}
			// vai jogar os dados pro objeto logadouro etc os dados que vem com cep pelos
			// objetos criado em Pessoa
			UsuarioPessoa gsonAux = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);

			// setando os dados pelo gson auxiliar
			usuarioPessoa.setCep(gsonAux.getCep());
			usuarioPessoa.setLogradouro(gsonAux.getLogradouro());
			usuarioPessoa.setComplemento(gsonAux.getComplemento());
			usuarioPessoa.setBairro(gsonAux.getBairro());
			usuarioPessoa.setLocalidade(gsonAux.getLocalidade());
			usuarioPessoa.setUf(gsonAux.getUf());
			usuarioPessoa.setUnidade(gsonAux.getUnidade());
			usuarioPessoa.setIbge(gsonAux.getIbge());
			usuarioPessoa.setGia(gsonAux.getGia());

			// System.out.println(jsonCep);

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Informação: ", "Cep Inválido"));
		}
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
	
	public void setBarChatModel(BarChartModel barChatModel) {
		this.barChatModel = barChatModel;
	}
	
	public BarChartModel getBarChatModel() {
		return barChatModel;
	}
	
	public void setDaoGeneric(DaoUsuario<UsuarioPessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	
	public void setList(List<UsuarioPessoa> list) {
		this.list = list;
	}
	
	public DaoUsuario<UsuarioPessoa> getDaoGeneric() {
		return daoGeneric;
	}

}
