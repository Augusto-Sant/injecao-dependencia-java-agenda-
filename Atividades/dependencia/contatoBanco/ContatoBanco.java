package Atividades.dependencia.contatoBanco;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Atividades.dependencia.Contato;

/**
 * ContatoBanco
 */
public interface ContatoBanco {
    public Contato getContatoId(int id) throws Exception; // Buscar contato por ID

    public Contato getContatoEmail(String email) throws Exception; // Buscar contato por e-mail

    public ArrayList<Contato> getTodosContatos() throws FileNotFoundException, IOException, Exception; // Obter todos

    public boolean inserir(Contato contato) throws Exception; // Inserir um contato

    public boolean alterar(Contato contato) throws IOException; // Alterar um contato existente

    public boolean excluir(int id) throws Exception, IOException; // Excluir um contato por ID
}