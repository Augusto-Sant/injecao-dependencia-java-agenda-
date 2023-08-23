package Atividades.dependencia;

import java.util.ArrayList;
import java.util.Random;

import Atividades.dependencia.contatoBanco.ContatoBanco;
import Atividades.dependencia.contatoBanco.ContatoBancoCSV;
import Atividades.dependencia.contatoBanco.ContatoBancoJson;
import Atividades.dependencia.contatoBanco.ContatoBancoMySQL;
import Atividades.dependencia.contatoBanco.ContatoBancoPostgreSQL;
import Atividades.dependencia.contatoBanco.ContatoBancoXML;

public class Main {
    public static void main(String[] args) throws Exception {
        // String bancoSelecionado = "JSON";
        // String bancoSelecionado = "XML";
        // String bancoSelecionado = "MySQL";
        String bancoSelecionado = "CSV";
        // String bancoSelecionado = "PostgreSQL";
        testarBanco(bancoSelecionado);
    }

    public static Contato fakeContato() {
        String[] lista_nomes = { "Jose", "Anderson", "Carlos", "Rodrigo", "John" };
        String[] lista_dataNascimento = { "11/01/1998", "31/12/2003", "15/09/2019", "02/05/2010", "05/02/2022" };
        String[] lista_telefone = { "(11) 9 90158596", "(47) 9 90838596", "(47) 9 90158580", "(11) 9 83948596",
                "(11) 9 90145596" };
        String[] lista_email = { "lolzeiro@uol.com", "ninja@hotmail.com", "paodequeijo@mail.com",
                "celta@mail.com", "celta@mail.com" };
        Random random = new Random();
        Contato contato = new Contato(null, null, null, null);
        contato.setBirthDate(lista_dataNascimento[random.nextInt(5)]);
        contato.setEmail(lista_email[random.nextInt(5)]);
        contato.setName(lista_nomes[random.nextInt(5)]);
        contato.setPhone(lista_telefone[random.nextInt(5)]);
        return contato;
    }

    public static void mostrarContatos(ArrayList<Contato> contatos) {
        for (Contato contato_lido : contatos) {
            System.out.println(contato_lido.toString());
        }
    }

    public static void testarBanco(String bancoSelecionado) throws Exception {
        ContatoBanco banco;
        switch (bancoSelecionado) {
            case "JSON":
                banco = new ContatoBancoJson();
                break;
            case "XML":
                banco = new ContatoBancoXML();
                break;
            case "CSV":
                banco = new ContatoBancoCSV();
                break;
            case "MySQL":
                banco = new ContatoBancoMySQL();
                break;
            case "PostgreSQL":
                banco = new ContatoBancoPostgreSQL();
                break;

            default:
                banco = new ContatoBancoJson();
                break;
        }

        System.out.format("---------------------- %s ---------------------\n", bancoSelecionado);
        // insere no banco
        for (int i = 0; i < 5; i++) {
            banco.inserir(fakeContato());
        }
        System.out.format("-- %s: Inserir --\n", bancoSelecionado);
        ArrayList<Contato> contatos = banco.getTodosContatos();
        mostrarContatos(contatos);

        // alterar o primeiro
        System.out.format("-- %s: Alterar --\n", bancoSelecionado);
        int idAtual = contatos.get(1).getId();
        Contato novo_primeiro = new Contato(idAtual, "Raimundo", "10/02/1998", "(11) 9 19048596",
                "what@ifc.com");
        banco.alterar(novo_primeiro);

        contatos = banco.getTodosContatos();
        mostrarContatos(contatos);

        // excluir
        System.out.format("-- %s: Excluir --\n", bancoSelecionado);
        idAtual = contatos.get(3).getId();
        banco.excluir(idAtual);
        contatos = banco.getTodosContatos();
        mostrarContatos(contatos);

        // limpar
        for (int i = 0; i < contatos.size(); i++) {
            idAtual = contatos.get(i).getId();
            banco.excluir(idAtual);
        }
    }

}
