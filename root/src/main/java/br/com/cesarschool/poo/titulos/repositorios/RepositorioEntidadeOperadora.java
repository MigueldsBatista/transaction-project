package br.com.cesarschool.poo.titulos.repositorios;

import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/*
 * Deve gravar em e ler de um arquivo texto chamado entidadeOperadora.txt os dados dos objetos do tipo
 * entidadeOperadora. Seguem abaixo exemplos de linhas.
 *
    1;PETROBRAS;2024-12-12;30.33
    2;BANCO DO BRASIL;2026-01-01;21.21
    3;CORREIOS;2027-11-11;6.12 
 * 
 * A inclus�o deve adicionar uma nova linha ao arquivo. N�o � permitido incluir 
 * identificador repetido. Neste caso, o m�todo deve retornar false. Inclus�o com 
 * sucesso, retorno true.
 * 
 * A altera��o deve substituir a linha atual por uma nova linha. A linha deve ser 
 * localizada por identificador que, quando n�o encontrado, enseja retorno false. 
 * Altera��o com sucesso, retorno true.  
 *   
 * A exclus�o deve apagar a linha atual do arquivo. A linha deve ser 
 * localizada por identificador que, quando n�o encontrado, enseja retorno false. 
 * Exclus�o com sucesso, retorno true.
 * 
 * A busca deve localizar uma linha por identificador, materializar e retornar um 
 * objeto. Caso o identificador n�o seja encontrado no arquivo, retornar null.   
 */
import java.nio.file.Path;
import java.nio.file.Paths;

public class RepositorioEntidadeOperadora {
    private Path path;
    private final Path BASE_PATH = Paths.get("").toAbsolutePath(); // Caminho para a pasta pai

    public RepositorioEntidadeOperadora() {
        // Inicializa o caminho do arquivo baseado no diretório de trabalho atual
        this.path = BASE_PATH.resolve("root").resolve("database").resolve("EntidadeOperadora.txt");
        criarArquivoSeNaoExistir(); // Verifica e cria o arquivo se necessário
    }

    // Método para verificar e criar o arquivo, se não existir
    private boolean criarArquivoSeNaoExistir() {
        try {
            // Verifica se o arquivo existe; se não, cria um novo
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent()); // Cria os diretórios pai, se necessário
                Files.createFile(path); // Cria o arquivo
                System.out.println("Arquivo criado em: " + path.toAbsolutePath()); // Imprime o caminho absoluto
                return true; // Arquivo criado
            }
            System.out.println("Arquivo já existe em: " + path.toAbsolutePath()); // Imprime se o arquivo já existe
            return false; // Arquivo já existe
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Retorna false em caso de erro
        }
    }   public boolean incluir(EntidadeOperadora entidadeOperadora) {
      try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))){
          String line;
          while ((line = reader.readLine()) != null){
              String[] parts = line.split(";");
              int id = Integer.parseInt(parts[0]);
              if(id == entidadeOperadora.getIdentificador()){
                  return false;
              }
          }
      } catch (IOException e){
          e.printStackTrace();
          return false;
      }
  
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true))) {
        writer.write(entidadeOperadora.getIdentificador() + ";" + entidadeOperadora.getNome() + ";" + entidadeOperadora.getAutorizacao() + ";" 
                     + entidadeOperadora.getSaldoAcao() + ";" + entidadeOperadora.getSaldoTituloDivida());
        writer.newLine();
        } catch (IOException e) {
        e.printStackTrace();
        return false;
        }
  
      return true;
  }
	public boolean alterar(EntidadeOperadora entidadeOperadora) {
		List<String> lines = new ArrayList<>();
		boolean found = false;

		try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))){
			String line;
			while((line = reader.readLine()) != null){
				String[] parts = line.split(";");
				int id = Integer.parseInt(parts[0]);
				if(id == entidadeOperadora.getIdentificador()){
					line = entidadeOperadora.getIdentificador() + ";" + entidadeOperadora.getAutorizacao() + ";" + entidadeOperadora.getSaldoAcao() + ";" + entidadeOperadora.getSaldoTituloDivida();
					found = true;
				}
				lines.add(line);
			}
			}catch(IOException e){
				e.printStackTrace();
				return false;
			}

			if(!found){
				return false;
			}

			try(BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))){
				for(String line : lines){
					writer.write(line);
					writer.newLine();
				}
			}catch(IOException e){
				e.printStackTrace();
				return false;
			}
			return true;
	}
	public boolean excluir(long identificador) {
		List<String> lines = new ArrayList<>();
		boolean found = false;

		try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))){
			String line;
			while((line = reader.readLine()) != null){
				String[] parts = line.split(";");
				int id = Integer.parseInt(parts[0]);
				if(id == identificador){
					found = true;
					continue;
				}
				lines.add(line);
			}

		} catch (IOException e) {
            e.printStackTrace();
            return false;
        }

		if (!found) {
			return false;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

	}
	public EntidadeOperadora buscar(long identificador) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                long id = Long.parseLong(parts[0]);
                if (id == identificador) {
                    String nome = parts[1];
                    double autorizadoAcao = Double.parseDouble(parts[2]);
                    double saldoAcao = Double.parseDouble(parts[3]);
                    double saldoTituloDivida = Double.parseDouble(parts[4]);
                    
                    EntidadeOperadora entidade = new EntidadeOperadora(identificador, nome, autorizadoAcao);
                    entidade.creditarSaldoAcao(saldoAcao);
                    entidade.creditarSaldoTituloDivida(saldoTituloDivida);
                    
                    return entidade;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; 
    }    
}
