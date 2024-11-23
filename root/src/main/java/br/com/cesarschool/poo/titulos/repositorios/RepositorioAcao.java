package br.com.cesarschool.poo.titulos.repositorios;

import br.com.cesarschool.poo.titulos.entidades.Acao;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RepositorioAcao {
    private Path path;
    private final Path BASE_PATH = Paths.get("").toAbsolutePath();

    public RepositorioAcao() {
        this.path = BASE_PATH.resolve("root").resolve("database").resolve("Acao.txt");
        criarArquivoSeNaoExistir();
    }

    private boolean criarArquivoSeNaoExistir() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Retorna false em caso de erro
        }
    }

	/*public boolean incluir(Acao acao) {
			// Verifica se a ação já existe
			try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(";");
					int id = Integer.parseInt(parts[0]);
					if (id == acao.getIdentificador()) {
						return false; // Ação já existe, não incluir
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true))){
			writer.write(acao.getIdentificador() + ";" + acao.getNome() + ";" + acao.getDataValidade() + ";" + acao.getValorUnitario());
			writer.newLine();

		} catch(IOException e){
			e.printStackTrace();
			return false;
		}

		return true;
		
	}
	*/

	/*public boolean alterar(Acao acao) {
		List<String> lines = new ArrayList<>();
		boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).split(";")[0].equals(String.valueOf(acao.getIdentificador()))) {
                    lines.set(i, serialize(acao)); // Atualiza a ação
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false; // Não encontrou a ação
            }

            escreverArquivo(lines); // Escreve de volta para o arquivo
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(int identificador) {
        try {
            List<String> lines = lerArquivo();
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                if (Integer.parseInt(lines.get(i).split(";")[0]) == identificador) {
                    lines.remove(i); // Remove a linha
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false; // Não encontrou a ação
            }

            escreverArquivo(lines); // Escreve de volta para o arquivo
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Acao buscar(int identificador) {
        try {
            List<String> lines = lerArquivo();
            for (String line : lines) {
                if (Integer.parseInt(line.split(";")[0]) == identificador) {
                    return deserialize(line); // Retorna a ação desserializada
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

	}
	public Acao buscar(int identificador) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))){
			String line;
			while((line = reader.readLine()) != null){
				String[] parts = line.split(";");
				int id = Integer.parseInt(parts[0]);
				if(id == identificador){
					String nome = parts[1];
					LocalDate dataValidade = LocalDate.parse(parts[2], formatter);
					double valorUnitario = Double.parseDouble(parts[3]);
					return new Acao(id, nome, dataValidade, valorUnitario);
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	*/
	public List<Acao> listar() {
		List<Acao> acoes = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))){
			String line;
			while((line = reader.readLine()) != null){
				String[] parts = line.split(";");
				int id = Integer.parseInt(parts[0]);
				String nome = parts[1];
				LocalDate dataValidade = LocalDate.parse(parts[2], formatter);
				double valorUnitario = Double.parseDouble(parts[3]);
				acoes.add(new Acao(id, nome, dataValidade, valorUnitario));
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return acoes;
	}
}
