package me.williandrade;

import java.util.Scanner;

import me.williandrade.converter.BasicConverter;
import me.williandrade.converter.Converter;

public class ProtocolWorkMain {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("----------------------------");
			System.out.println("0 - Compactar");
			System.out.println("1 - Descompactar");
			System.out.println("----------------------------");

			Integer response = sc.nextInt();

			if (response == 0) {
				System.out.println("Informe o diretório completo do arquivo: \n ex: /Users/Downloads/toCompact.sql");
				String path = sc.next();
				String dir = new BasicConverter(path).pack();
				System.out.println("Compactado em: " + dir);

				break;
			} else if (response == 1) {
				System.out.println(
						"Informe o diretório completo do arquivo: \n ex: /Users/Downloads/descompact.sql.patronun");
				String path = sc.next();
				String dir = new BasicConverter(path).unpack();
				System.out.println("Descompactado em: " + dir);
				break;
			}
		}

	}

}
