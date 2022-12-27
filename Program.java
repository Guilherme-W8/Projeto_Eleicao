
/* 
 * 	Autor: Guilherme Correia 
 * 	Trabalho prático - Fatec PG
 * 	Professor: Ciro Cirne Trindade.
 */

import java.util.Scanner;
import entities.PrincipalBin;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PrincipalBin binario = new PrincipalBin();

		System.out.print("Passe o caminho do arquivo CSV: ");
		String arquivoCSV = sc.nextLine();
		binario.saveToBinary(arquivoCSV);

		int comando = -1;

		while (true) {
			System.out.println(
					"1 - Resultados em Praia Grande: Calcula e exibe o numero e percentual de votos dos 2 candidatos a governador, votos nulos e votos em branco.");
			System.out.println(
					"2 - Resultados por seção: Calcula e exibe o numero e percentual de votos dos 2 candidatos a governador, votos nulos e votos em branco. (Especifico da seção)");
			System.out.println("0 - Encerrar programa.");
			System.out.print("Digite o comando: ");

			comando = sc.nextInt();

			System.out.println(" ");

			if (comando == 1) {
				binario.votacaoDetalhada();
			} else if (comando == 2) {
				binario.resultadoPorSecao();
			} else if (comando == 0) {
				break;
			} else {
				System.out.println("Comando invalido.");
			}
		}

		System.out.println(" -- Programa encerrado -- ");
		sc.close();
	}
}
