package Atividades.dependencia.contatoBanco;

import java.util.ArrayList;

import Atividades.dependencia.Contato;
import java.io.*;

public class ContatoBancoCSV implements ContatoBanco {

    final String CSV_FILE_PATH = "src/Atividades/dependencia/db/agenda.csv";

    public int generateNewId() throws Exception {
        ArrayList<Contato> contatos = getTodosContatos();
        if (contatos.size() == 0) {
            return 0;
        }
        return contatos.get(contatos.size() - 1).getId() + 1;
    }

    private void escreverContatosNoArquivo(ArrayList<Contato> contatos) {
        try (FileWriter fileWriter = new FileWriter(CSV_FILE_PATH);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            for (Contato c : contatos) {
                printWriter.println(c.getId() + "," + c.getName() + "," + c.getBirthDate() + "," + c.getPhone() + ","
                        + c.getEmail());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Contato getContatoId(int id) {
        try {
            for (Contato contato : getTodosContatos()) {
                if (contato.getId() == id) {
                    return contato;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Contato getContatoEmail(String email) {
        try {
            for (Contato contato : getTodosContatos()) {
                if (contato.getEmail().equals(email)) {
                    return contato;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<Contato> getTodosContatos() throws IOException {
        ArrayList<Contato> contatos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                Contato contato = new Contato(id, values[1], values[2], values[3], values[4]);
                contatos.add(contato);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contatos;
    }

    @Override
    public boolean inserir(Contato contato) throws IOException {
        try (FileWriter fileWriter = new FileWriter(CSV_FILE_PATH, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(generateNewId() + "," + contato.getName() + "," + contato.getBirthDate() + ","
                    + contato.getPhone() + "," + contato.getEmail());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean alterar(Contato contato) throws IOException {
        ArrayList<Contato> contatos = getTodosContatos();
        for (Contato c : contatos) {
            if (c.getId() == contato.getId()) {
                c.setName(contato.getName());
                c.setBirthDate(contato.getBirthDate());
                c.setPhone(contato.getPhone());
                c.setEmail(contato.getEmail());
                escreverContatosNoArquivo(contatos);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean excluir(int id) throws IOException {
        ArrayList<Contato> contatos = getTodosContatos();
        if (contatos.removeIf(c -> c.getId() == id)) {
            escreverContatosNoArquivo(contatos);
            return true;
        }
        return false;
    }

}
