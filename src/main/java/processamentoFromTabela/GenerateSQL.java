package processamentoFromTabela;

import java.io.File;
import java.io.FileInputStream;

public class GenerateSQL {

	public static void main(String[] args) throws Exception {
		
		File f = new File("C:\\Users\\Hugo\\Documents\\PROJS\\fromJPA\\processamentoFromTabela\\pokemons.txt");
		byte[] b = new byte[ new Long(f.length()).intValue() ];
		
		FileInputStream fis = new FileInputStream(f);
		fis.read(b);
		fis.close();
		
		String[] pokes = new String(b).split("\r\n");
		
		String template = "insert into testes (id, nome) values(%s, '%s');\r\n";
		int ini = 20;
		
		
		for(String p : pokes) {
			
			ini += 10;
			
			String stat = String.format(template, ini, p);
			
			System.out.println(stat);
			
		}
	


	}

}
