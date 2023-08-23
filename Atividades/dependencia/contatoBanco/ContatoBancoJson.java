package Atividades.dependencia.contatoBanco;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

import Atividades.dependencia.Contato;

public class ContatoBancoJson implements ContatoBanco {

    private final String JSON_FILE_PATH = "src/Atividades/dependencia/db/agenda.json";

    private JSONArray getContatosFromFile() throws IOException, ParseException {
        try (FileReader reader = new FileReader(JSON_FILE_PATH)) {
            JSONParser parser = new JSONParser();
            return (JSONArray) parser.parse(reader);
        }
    }

    private int generateNewId() {
        ArrayList<Contato> contatos = getTodosContatos();
        if (contatos.size() == 0) {
            return 0;
        }
        return contatos.get(contatos.size() - 1).getId() + 1;
    }

    @Override
    public Contato getContatoId(int id_contato) {
        try {
            JSONArray jsonArray = getContatosFromFile();

            // pesquisa no array até achar o id certo
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                if (jsonObject.containsKey("id")) {
                    int id = ((Number) jsonObject.get("id")).intValue();
                    if (id == id_contato) {
                        String name = (String) jsonObject.get("name");
                        String birthDate = (String) jsonObject.get("birthDate");
                        String phone = (String) jsonObject.get("phone");
                        String email = (String) jsonObject.get("email");

                        return new Contato(id, name, birthDate, phone, email);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Contato getContatoEmail(String email) {
        try {
            JSONArray jsonArray = getContatosFromFile();

            // pesquisa no array até achar o email certo
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                if (jsonObject.containsKey("email")) {
                    String contactEmail = (String) jsonObject.get("email");
                    if (contactEmail.equals(email)) {
                        int id = ((Number) jsonObject.get("id")).intValue();
                        String name = (String) jsonObject.get("name");
                        String birthDate = (String) jsonObject.get("birthDate");
                        String phone = (String) jsonObject.get("phone");

                        return new Contato(id, name, birthDate, phone, email);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Contato> getTodosContatos() {
        ArrayList<Contato> contatos = new ArrayList<>();

        try (FileReader reader = new FileReader(JSON_FILE_PATH)) {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;

                int id = ((Number) jsonObject.get("id")).intValue();
                String name = (String) jsonObject.get("name");
                String birthDate = (String) jsonObject.get("birthDate");
                String phone = (String) jsonObject.get("phone");
                String email = (String) jsonObject.get("email");

                Contato contato = new Contato(id, name, birthDate, phone, email);
                contatos.add(contato);
            }

            return contatos;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return contatos;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean inserir(Contato contato) {
        try {
            JSONArray jsonArray = getContatosFromFile();

            JSONObject novoContato = new JSONObject();
            novoContato.put("id", generateNewId()); // criar novo id
            novoContato.put("name", contato.getName());
            novoContato.put("birthDate", contato.getBirthDate());
            novoContato.put("phone", contato.getPhone());
            novoContato.put("email", contato.getEmail());

            jsonArray.add(novoContato);

            try (FileWriter file = new FileWriter(JSON_FILE_PATH)) {
                file.write(jsonArray.toJSONString());
                return true;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean alterar(Contato contato) {
        try {
            JSONArray jsonArray = getContatosFromFile();

            int contatoId = contato.getId();

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject existingContato = (JSONObject) jsonArray.get(i);
                if (existingContato.containsKey("id")) {
                    int id = ((Number) existingContato.get("id")).intValue();
                    if (id == contatoId) {
                        existingContato.put("name", contato.getName());
                        existingContato.put("birthDate", contato.getBirthDate());
                        existingContato.put("phone", contato.getPhone());
                        existingContato.put("email", contato.getEmail());

                        try (FileWriter file = new FileWriter(JSON_FILE_PATH)) {
                            file.write(jsonArray.toJSONString());
                            return true;
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean excluir(int id_contato) {
        try {
            JSONArray jsonArray = getContatosFromFile();

            // pesquisa até achar o id certo e exclui ele
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject existingContato = (JSONObject) jsonArray.get(i);
                if (existingContato.containsKey("id")) {
                    int id = ((Number) existingContato.get("id")).intValue();
                    if (id == id_contato) {
                        jsonArray.remove(i);

                        // escreve denovo o json mas sem aquele que nós tiramos
                        try (FileWriter file = new FileWriter(JSON_FILE_PATH)) {
                            file.write(jsonArray.toJSONString());
                            return true;
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
