package processamentoFromTabela;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;

public class MyRotas extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("stream:in")		
			.process( ex -> {
				for(int i = 0; i < 100; i++){
					ProducerTemplate tp = this.getContext().createProducerTemplate();
					tp.sendBody("direct:gerarMsg", i);
				}
			});
		
		from("direct:gerarMsg")
			.process( ex -> {				
				int i = ex.getIn().getBody(Integer.class);
				Mensagem m = new Mensagem();
				m.setTexto("algo aqui: " + i);
				m.setId(i);
				ex.getIn().setBody(m);
			})
		.to("jpa:processamentoFromTabela.Mensagem");
			
		
		from("jpa:processamentoFromTabela.Mensagem?consumer.delay=5000&maximumResults=10")
			.wireTap("direct:processJPA");

		from("direct:processJPA")
			.log("Iniciando: ${body.id} - ${body.texto}")
			.process( ex -> {
				try{
					Thread.sleep(10000);
				} catch (Exception ex1){					
				}
			})
			.log("Fim: ${body.id} - ${body.texto}");

	}

}
