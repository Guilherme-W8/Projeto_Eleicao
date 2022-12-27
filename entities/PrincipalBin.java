package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalBin implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nrSecao;
	private String nrVotavel;
	private String nm_Votavel;
	private int qtVotos;

	public PrincipalBin() {
		super();
	}

	public PrincipalBin(String nrSecao, String nrVotavel, String nm_Votavel, int qtVotos) {
		this.nrSecao = nrSecao;
		this.nrVotavel = nrVotavel;
		this.nm_Votavel = nm_Votavel;
		this.qtVotos = qtVotos;
	}

	public void setNrSecao(String nrSecao) {
		this.nrSecao = nrSecao;
	}

	public String getNrVotavel() {
		return nrVotavel;
	}

	public void setNrVotavel(String nrVotavel) {
		this.nrVotavel = nrVotavel;
	}

	public void setNm_Votavel(String nm_Votavel) {
		this.nm_Votavel = nm_Votavel;
	}

	public int getQtVotos() {
		return qtVotos;
	}

	public void setQtVotos(int qtVotos) {
		this.qtVotos = qtVotos;
	}

	@Override
	public String toString() {
		return "PrincipalBin [nrSecao=" + nrSecao + ", nrVotavel=" + nrVotavel + ", nm_Votavel=" + nm_Votavel
				+ ", qtVotos=" + qtVotos + "]";
	}

	/*
	 * Metodo 'atributosMunicipioPG': Extracao dos atributos importantes para
	 * facilitar a movimetacao de dados; e auxliar na criacao do arquivo binario.
	 * 
	 * Retorno: Lista de Objetos a partir do arquivo .csv
	 */

	public List<PrincipalBin> atributosMunicipioPG(String filename) {
		List<PrincipalBin> dados = new ArrayList<PrincipalBin>();
		try {
			FileInputStream entradaArquivo = new FileInputStream(new File(filename));
			try (Scanner lerArquivo = new Scanner(entradaArquivo, "UTF-8")) {
				lerArquivo.nextLine();
				String line = lerArquivo.nextLine();
				while (lerArquivo.hasNextLine()) {
					line = lerArquivo.nextLine();
					if (line != null && !line.isEmpty()) {
						String[] vect = line.split("\";\"");
						if (vect[5].equals("2") && vect[13].equals("69213")) {
							PrincipalBin auxiliar = new PrincipalBin();
							auxiliar.setNrSecao(vect[16]);
							auxiliar.setNrVotavel(vect[19]);
							auxiliar.setNm_Votavel(vect[20]);
							auxiliar.setQtVotos(Integer.parseInt(vect[21]));
							dados.add(auxiliar);
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dados;
	}

	/*
	 * Salva os arquivos em forma binaria, utiliza do metodo 'atributosMunicipioPG'
	 * fazer o arquivo apenas com os atributos importantes pedidos no trabalho
	 * prático.
	 * 
	 * Não retorna nada, apenas cria o arquivo binario para extração de dados.
	 */

	public void saveToBinary(String arquivoCSV) {
		try {
			ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream("Binario.dat"));
			for (PrincipalBin objeto : atributosMunicipioPG(arquivoCSV)) {
				if (objeto != null) {
					file.writeObject(objeto);
				}
			}
			file.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * Faz a extração, a leitura a partir do arquivo binario(.dat) gerado,
	 * facilitando assim, a criação de metodos que o trabalho pratico exige.
	 * 
	 * Retorno: Lista de dados binarios lidos.
	 */

	public List<PrincipalBin> extrairDadosBinario() {
		List<PrincipalBin> retornoDados = new ArrayList<PrincipalBin>();
		try {
			PrincipalBin objetoLido;
			ObjectInputStream file = new ObjectInputStream(new FileInputStream("Binario.dat"));
			do {
				objetoLido = (PrincipalBin) file.readObject();
				if (objetoLido != null) {
					retornoDados.add(objetoLido);
				}
			} while (objetoLido != null);
			file.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return retornoDados;
	}

	public void votacaoDetalhada() {
		int totalVotos = 0;
		int totalVotosHaddad = 0;
		int totalVotosTarcisio = 0;
		int totalVotosBrancos = 0;
		int totalVotosNulo = 0;
		int subtrairVotosParaPercentual = 0;

		for (PrincipalBin p : extrairDadosBinario()) {

			totalVotos += p.getQtVotos();

			if (p.getNrVotavel().equals("10")) {
				totalVotosTarcisio += p.getQtVotos();
			} else if (p.getNrVotavel().equals("13")) {
				totalVotosHaddad += p.getQtVotos();
			} else if (p.getNrVotavel().equals("95")) {
				totalVotosBrancos += p.getQtVotos();
			} else {
				totalVotosNulo += p.getQtVotos();
			}
		}

		subtrairVotosParaPercentual = totalVotos - totalVotosNulo - totalVotosBrancos;

		double percentualTarcisio = (double) (totalVotosTarcisio * 100) / subtrairVotosParaPercentual;
		double percentualHaddad = (double) (totalVotosHaddad * 100) / subtrairVotosParaPercentual;
		double percentualVotosBrancos = (double) (totalVotosBrancos * 100) / totalVotos;
		double percentualVotosNulos = (double) (totalVotosNulo * 100) / totalVotos;

		System.out.printf("%nTOTAL DE VOTOS DO MUNICIPIO DE PRAIA GRANDE: %d%n", totalVotos);
		System.out.printf("TOTAL DE VOTOS TARCISIO: %d. Percentual de votos do candidato: %.2f%%%n", totalVotosTarcisio,
				percentualTarcisio);
		System.out.printf("TOTAL DE VOTOS HADDAD: %d. Percentual de votos do candidato: %.2f%%%n", totalVotosHaddad,
				percentualHaddad);
		System.out.printf("TOTAL DE VOTOS BRANCOS: %d. Percentual de votos em branco: %.3f%%%n", totalVotosBrancos,
				percentualVotosBrancos);
		System.out.printf("TOTAL DE VOTOS NULOS: %d. Percentual de votos nulos: %.2f%%%n%n", totalVotosNulo,
				percentualVotosNulos);
	}

	public void resultadoPorSecao() {

		Scanner sc = new Scanner(System.in);

		do {
			int votosDaSecao = 0;
			int votosHaddad = 0;
			int votosTarcisio = 0;
			int votosBranco = 0;
			int votosNulo = 0;
			boolean isSecao = false;
			String secao = null;

			System.out.println("Digite - 0 - Consultar secao.");
			System.out.println("Digite - Qualquer numero - Encerrar consulta por secao.");
			System.out.printf("Aguardando decisao: ");
			int decisaoMenu = sc.nextInt();

			System.out.println(" ");

			if (decisaoMenu == 0) {
				System.out.print("Numero da secao a qual deseja visualizar: ");
				secao = sc.next();
			} else {
				System.out.println(" <-- Encerrando --> ");
				break;
			}

			for (PrincipalBin varredura : extrairDadosBinario()) {
				if (varredura.nrSecao.equals(secao)) {
					isSecao = true;
					votosDaSecao += varredura.qtVotos;
					if (varredura.nrVotavel.equals("13")) {
						votosHaddad += varredura.qtVotos;
					} else if (varredura.nrVotavel.equals("10")) {
						votosTarcisio += varredura.qtVotos;
					} else if (varredura.nrVotavel.equals("95")) {
						votosBranco += varredura.qtVotos;
					} else if (varredura.nrVotavel.equals("96")) {
						votosNulo += varredura.qtVotos;
					}
				}
			}

			if (isSecao) {
				int subtrairVotosParaPercentual = votosDaSecao - votosNulo - votosBranco;
				double percentualTarcisio = (double) (votosTarcisio * 100) / subtrairVotosParaPercentual;
				double percentualHaddad = (double) (votosHaddad * 100) / subtrairVotosParaPercentual;
				double percentualVotosBranco = (double) (votosBranco * 100) / votosDaSecao;
				double percentualVotosNulo = (double) (votosNulo * 100) / votosDaSecao;

				System.out.printf("%nVOTOS TOTAIS NA %s SECAO : %d%n", secao, votosDaSecao);

				System.out.printf("Votos do candidato: %d. Percentual do Tarcisio: %.2f%n", votosTarcisio,
						percentualTarcisio);
				System.out.printf("Votos do candidato: %d. Percentual do Haddad: %.2f%n", votosHaddad,
						percentualHaddad);
				System.out.printf("Votos em branco: %d. Percentual dos votos em branco: %.2f%n", votosBranco,
						percentualVotosBranco);
				System.out.printf("Votos Nulos: %d. Percentual dos votos Nulo: %.2f%n%n", votosNulo,
						percentualVotosNulo);
			} else {

				System.out.printf("===================================%n");
				System.out.printf("ERRO: <-- SECAO NAO ENCONTRADA -->%n");
				System.out.printf("===================================%n%n");

				System.out.println("Deseja tentar visualizar uma nova secao?");
				System.out.println("Digite <1> encerrar.");
				System.out.println("Digite <0> retornar ao menu.");
				System.out.print("Digite o comando: ");
				int simOuNao = sc.nextInt();

				if (simOuNao == 1) {
					System.out.printf("Secao inexistente: <-- encerrando -->%n");
					break;
				}
			}
		} while (true);
	}
}
