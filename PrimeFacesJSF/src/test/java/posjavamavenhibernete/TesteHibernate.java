package posjavamavenhibernete;

import java.util.List;

import org.junit.Test;

import br.com.dao.DaoGeneric;
import br.com.model.TelefoneUser;
import br.com.model.UsuarioPessoa;

public class TesteHibernate {

	
	@Test
	public void salvarUsuario() {
         //passa a nossa model pro generic
         DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();
         
         UsuarioPessoa pessoa = new UsuarioPessoa();

         //seta os atributo id gera automaticamente
          
         pessoa.setNome("Beatriz");
         pessoa.setSobrenome("rei123");
         pessoa.setEmail("ardmir123@gmail.com");
         pessoa.setLogin("imitan123");
         pessoa.setSenha("qweqwqwe");
         pessoa.setIdade(22);
         
         daoGeneric.salvar(pessoa);
	}
	
	@Test
	public void testeBuscar() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();
		 
		 UsuarioPessoa pessoa = new UsuarioPessoa();
		 
		 pessoa.setId(2L);
		 
		 //método pesquisar retorna pessoa ent pode atribuir pessoa = é o retorno da pesquisa
		 pessoa = daoGeneric.pesquisar(pessoa);
		 
		 System.out.println(pessoa); //vai vir a os dados com o to string na model com informação do id
	}
	
	@Test
	public void testeBuscar2() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();
		 //método pesquisar retorna pessoa ent pode atribuir pessoa = é o retorno da pesquisa
		 UsuarioPessoa pessoa = daoGeneric.pesquisar2(2L , UsuarioPessoa.class);
		 System.out.println(pessoa); //vai vir a os dados com o to string na model com informação do id
	}
	
	@Test
	public void update() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();
		 UsuarioPessoa pessoa = daoGeneric.pesquisar2(4L , UsuarioPessoa.class);
		 
		 pessoa.setIdade(22);
		 pessoa.setNome("Minha Gata Preciosa");
		 pessoa.setSenha("admin");
		 pessoa.setLogin("admin");
		 pessoa.setEmail("admin@admin.com");
		 pessoa.setSobrenome("ardmir");
		 
		pessoa = daoGeneric.updateMerge(pessoa);
		 
		 System.out.println(pessoa); 
	}
	
	@Test
	public void testeDelete() {
		
		//para deletar precisa consultar o id
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();
		 UsuarioPessoa pessoa = daoGeneric.pesquisar2(7L , UsuarioPessoa.class);
		
		 daoGeneric.deletarPorId(pessoa); //deletando o id passando a pessoa aqui
		 
		 
		 System.out.println(pessoa); 
	}
	
	@Test
	public void testeConsultar() {
		
		//para deletar precisa consultar o id
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();

		 //passa uma lista com o generic nossa entidade com a classe que ele vai carregar
		 //que é a classe UsuarioPessoa
		 List<UsuarioPessoa> list = daoGeneric.listar(UsuarioPessoa.class);
		  
		 for (UsuarioPessoa usuarioPessoa : list) {
			
			 //lista dos usuarios pessoas
			 System.out.println(usuarioPessoa);
			 System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
			
		}
		 
		 
	}
	
	@Test
	public void testeQueryList() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();
		 
		 List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery(" from UsuarioPessoa").getResultList();
		 
		 for (UsuarioPessoa usuarioPessoa : list) {
			 
			 System.out.println(usuarioPessoa);
			 System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
			
		}
	}
	
	@Test
	public void testeQueryListMaxResult() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();
		 
		 List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery
				 (" from UsuarioPessoa order by nome")//vão estar ordenados por nome
				 .setMaxResults(6)
				 .getResultList();
		 
		 for (UsuarioPessoa usuarioPessoa : list) {
			 
			 System.out.println(usuarioPessoa);
			 System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
			
		}
	}
	
	@Test
	public void testeQueryListParameter() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa> ();
		 
		 List<UsuarioPessoa> list = daoGeneric.getEntityManager()
		 .createQuery("from  UsuarioPessoa where nome = :nome")
		 .setParameter("nome", "João Victor").getResultList();
		 
		 for (UsuarioPessoa usuarioPessoa : list) {
			
			 System.out.println(usuarioPessoa);
			 System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
		}
		
	}
	
	@Test
	public void testQuerySomaIdade() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		//sum soma todos sql tem (usuario.idade vai somar todas idade / select selecionando 
		Long somaIdade = (Long) daoGeneric.getEntityManager()                  //resultado único
				.createQuery("select sum(u.idade) from UsuarioPessoa u").getSingleResult();
		
		System.out.println("Soma de todas as idades é --> " + somaIdade);
	}
	
	@Test
	public void testNamedQuery() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		 
		 List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				 .createNamedQuery("UsuarioPessoa.findAll").getResultList();
		 
		 for (UsuarioPessoa usuarioPessoa : list) {
			
			 System.out.println(usuarioPessoa);
			 System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
		}
		
	}
	
	@Test
	public void testNamedQuer2y() {
		
		 DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		 
		 List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				 .createNamedQuery("UsuarioPessoa.buscaPorNome")
				 .setParameter("nome", "ana")
				 .getResultList();
		 
		 for (UsuarioPessoa usuarioPessoa : list) {
			
			 System.out.println(usuarioPessoa);
			 System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
		}
		
	}
	
	@Test
	public void testGravarTelefone() {
		
		 DaoGeneric daoGeneric = new DaoGeneric ();
		 
		 UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar2(2L, UsuarioPessoa.class);
		 
		 TelefoneUser telefoneUser = new TelefoneUser();
		 
		 telefoneUser.setDdd("52");
		 telefoneUser.setNumero("963263964");
		 telefoneUser.setUsuarioPessoa(pessoa); //passa a pesso que vai ser dono desse celular
		 
		 daoGeneric.salvar(telefoneUser);
		 
		 
	}
	
	@Test
	public void testeConsultarTelefone() {
		
		 DaoGeneric daoGeneric = new DaoGeneric ();

		 UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar2(2L, UsuarioPessoa.class);
 		  
		 for (TelefoneUser fone : pessoa.getTelefoneUsers()) {
			
			 //lista dos usuarios pessoas
		 System.out.println("Nome User: " + fone.getUsuarioPessoa().getNome() + " DDD: " + fone.getDdd() + " Número: " + fone.getNumero());
			 System.out.println("--------------------------------------------------------");
			
		}
	}
}
