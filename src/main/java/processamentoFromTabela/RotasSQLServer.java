package processamentoFromTabela;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.camel.Handler;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class RotasSQLServer extends RouteBuilder {
	
	@Handler
	public static SQLServerDataSource dataSource() {
		
		SQLServerDataSource dataSource = new SQLServerDataSource();
		
	    dataSource.setUser("hugo");
	    dataSource.setPassword("teste");

	    dataSource.setServerName("localhost");
	    dataSource.setDatabaseName("master");
	    
	    return dataSource;
		
	}

	@Override
	public void configure() throws Exception {
		
//		SQLServerDataSource dataSource = new SQLServerDataSource();
//		
//	    dataSource.setUser("hugo");
//	    dataSource.setPassword("teste");
//
//	    dataSource.setServerName("localhost");
//	    dataSource.setDatabaseName("master");
//	    
//	    //Object o = this.getContext().getRegistry();
	    
	    this.getContext().getRegistry();
	    
	    //registry.bind("datasource", dataSource);
        
//        from("stream:in")
//        	.to("sql:select * from testes?dataSource=dataSourceMSSQL")
//        	.log("${body}");
	    
	    //from("stream:in")

	    from("quartz2://procPooling?fireNow=false&trigger.repeatInterval=3000")
	    	//.to("sql://exec ObterRegistro?dataSource=dataSourceMSSQL")
	    	.to("sql-stored://ObterRegistro()?dataSource=dataSourceMSSQL")
	    	.process(ex -> {
	    		
	    		System.out.println(ex);
	    		
	    		LinkedHashMap map = ex.getIn().getBody(LinkedHashMap.class);
	    		Object o = map.values();
	    		Collection c = (Collection) o;
	    		System.out.println( o + " - " + c.size() );
	    		if (c.size() >= 2) {
	    			Object o1 = c.toArray()[1];
	    			System.out.println(o1 + " - " + o1.getClass());
	    			ArrayList a = (ArrayList) o1;
	    			for (Object o2 : a) {
	    				System.out.println(o2 + " - " + o2.getClass());
	    				LinkedCaseInsensitiveMap lis = (LinkedCaseInsensitiveMap) o2;
	    				for( Object o3 : lis.values()) {
	    					System.out.println(o3 + " - " + o3.getClass());
	    					ex.getIn().setHeader("ID-RETORNADO", o3);
	    				}
	    				
	    			}
	    		}    		
	    		
	    	} )
	    	.log("${body}")
	    		.filter(simple("${header.ID-RETORNADO} != null"))
	    			.transform( body().append("\r\n") )
	    			.setBody( simple("${header.ID-RETORNADO}") )
	    			.toD("sql:insert into retorno(id) values(#)?dataSource=dataSourceMSSQL")
	    			.log("gravado: ${body}");

	    	//.to("file://C:/Users/Hugo/Documents/PROJS/fromJPA/processamentoFromTabela?fileName=lidos.txt&fileExist=Append");

	}

}
