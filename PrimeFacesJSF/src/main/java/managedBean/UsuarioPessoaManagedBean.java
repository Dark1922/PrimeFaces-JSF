package managedBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import br.com.dao.DaoEmail;
import br.com.dao.DaoUsuario;
import br.com.datatablelazy.LazyDataTableModelUserPessoa;
import br.com.model.EmailUser;
import br.com.model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped // vai carregar o usuario na tela segurar os dados
public class UsuarioPessoaManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	
	//vai carregar paginando
	private LazyDataTableModelUserPessoa<UsuarioPessoa> list = new LazyDataTableModelUserPessoa<UsuarioPessoa>();

	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	// como ele está exetendo ao dao generic e dao usuario pode fazer isso

	// elemento do primefaces pra fazer gráfico de tabela
	private BarChartModel barChatModel = new BarChartModel();
	private EmailUser emailUser = new EmailUser();
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>(); // entidade com os método do daogeneric
	private String campoPesquisa;
	

	@PostConstruct //inicia os valores da tela
	public void init() { // qnd iniciar a tela vai executar esse método de mostrar a tabela
		
		list.load(0, 5, null, null, null);//inicia os objetos de 0 a 5 no começo registros

		montarGrafico(); // mostra o grafico pela lista de pessoas cadastradas com seus salarios
	}

	private void montarGrafico() {
		barChatModel = new BarChartModel();

		ChartSeries userSalario = new ChartSeries();

		for (UsuarioPessoa usuarioPessoa : list.list) {

			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());
		}
		barChatModel.addSeries(userSalario);
		barChatModel.setTitle("Gráfico de Salários");
	}

	public String salvar() {
		
    //dentro da nossa lista tem outra list com o adicionar lista do usuario dentro do lazy
		daoGeneric.updat(usuarioPessoa);
		list.list.add(usuarioPessoa); // adiciona pra lista o novo user
		init(); // dps de salvar atualiza o gráfico
		usuarioPessoa = new UsuarioPessoa();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com Sucesso!"));
		
		return "";
	}

	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}

	public void pesquisar() {
		list.pesquisar(campoPesquisa);
		montarGrafico();

	}

	public void upload(FileUploadEvent image) { // primefaces

		// base 64 pra jogar na tela e salvar no banco tb
		String imagem = "data:imagem/png;base64," + DatatypeConverter.printBase64Binary(image.getFile().getContents());
		usuarioPessoa.setImagem(imagem); //seta a imagem

	}

	public String remove() {

		try {
			daoGeneric.removerUsuario(usuarioPessoa);// método se usuario tiver telefone vai deletar ele dps a pessoa
			list.list.remove(usuarioPessoa); // remove da lista esse objeto
			usuarioPessoa = new UsuarioPessoa();
			init();
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

			String cep = ""; // váriavel auxiliar
			StringBuilder jsonCep = new StringBuilder();
			while ((cep = br.readLine()) != null) { // vai varrer as linha eqnt for diferente de null
				jsonCep.append(cep); // vai adicionando oq tiver no cep logadouro etcq
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
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Cep Inválido"));
		}
	}

	// get da nossa lista
	public LazyDataTableModelUserPessoa<UsuarioPessoa> getList() {
		montarGrafico();
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

	public void setList(LazyDataTableModelUserPessoa<UsuarioPessoa> list) {
		this.list = list;
	}

	public DaoUsuario<UsuarioPessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}

	public EmailUser getEmailUser() {
		return emailUser;
	}

	public void addEmail() {
		emailUser.setUsuarioPessoa(usuarioPessoa); // seta o email pra pessoa
		emailUser = daoEmail.updat(emailUser); // salva o email
		usuarioPessoa.getEmails().add(emailUser); // adiciono na lista
		emailUser = new EmailUser();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado", "Salvo com Sucesso!"));
	}

	public void removeEmail() throws Exception {
		String codigoemail = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigoemail");
		EmailUser remover = new EmailUser();
		remover.setId(Long.parseLong(codigoemail)); // codigoemail = id ent vai setar o id pra ser removido abaixo
		daoEmail.deletePorId(remover);
		usuarioPessoa.getEmails().remove(remover);// remover tb da lista pega busca de lista de emails
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado", "Removido com Sucesso!"));
	}
	
	public void dowload() throws IOException {
		//vai ter todos atributos que foram enviados da nossa requisição
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		//vai ter o id da pessoa passado pelo value ná página xhtml pq pega qlq dado da requisição que ja foi passado
		String fileDowloadID = params.get("fileDowloadId");
		
		//nossa id ta em string ai faz um parse long e a classe da consulta do banco de dados da pessoa
		UsuarioPessoa pessoa = daoGeneric.pesquisar(Long.parseLong(fileDowloadID), UsuarioPessoa.class);
		
		//imagem tá dentro do objeto pessoa que acabou de pesquisar
		byte[] imagem = new org.apache.tomcat.util.codec.binary.Base64().decodeBase64(pessoa.getImagem().split("\\,")[1]);
		
		//como tá generico tem que fazer a conversão  (HttpServletResponse)
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		//attchament baixa direto n vai pra outra tela e o nome que quer
		response.addHeader("Content-Disposition", "attachment; filename=dowload.png");
		
		response.setContentType("application/octet-stream");
		response.setContentLength(imagem.length);
		response.getOutputStream().write(imagem);
		response.getOutputStream().flush(); //manda tudo pra resposta
		FacesContext.getCurrentInstance().responseComplete(); //resposta completa
		
	}

	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}

	public String getCampoPesquisa() {
		return campoPesquisa;
	}

	public void setDaoEmail(DaoEmail<EmailUser> daoEmail) {
		this.daoEmail = daoEmail;
	}

	public DaoEmail<EmailUser> getDaoEmail() {
		return daoEmail;
	}
}
