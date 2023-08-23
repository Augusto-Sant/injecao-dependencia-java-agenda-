package Atividades.dependencia.contatoBanco;

import Atividades.dependencia.Contato;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

public class ContatoBancoXML implements ContatoBanco {

    private final String XML_FILE_PATH = "src/Atividades/dependencia/db/agenda.xml";

    private int generateNewId() {
        ArrayList<Contato> contatos = getTodosContatos();
        if (contatos.size() == 0) {
            return 0;
        }
        return contatos.get(contatos.size() - 1).getId() + 1;
    }

    private Document getDocument() throws ParserConfigurationException, SAXException, IOException {
        File xmlFile = new File(XML_FILE_PATH);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(xmlFile);
    }

    private void saveDocument(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(XML_FILE_PATH));
        transformer.transform(source, result);
    }

    @Override
    public Contato getContatoId(int id_contato) {
        try {
            Document doc = getDocument();
            NodeList contatos = doc.getElementsByTagName("contato");
            for (int i = 0; i < contatos.getLength(); i++) {
                Element contato = (Element) contatos.item(i);
                int id = Integer.parseInt(contato.getElementsByTagName("id").item(0).getTextContent());
                if (id == id_contato) {
                    String name = contato.getElementsByTagName("name").item(0).getTextContent();
                    String birthDate = contato.getElementsByTagName("birthDate").item(0).getTextContent();
                    String phone = contato.getElementsByTagName("phone").item(0).getTextContent();
                    String email = contato.getElementsByTagName("email").item(0).getTextContent();
                    return new Contato(id, name, birthDate, phone, email);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Contato getContatoEmail(String email) {
        try {
            Document doc = getDocument();
            NodeList contatos = doc.getElementsByTagName("contato");
            for (int i = 0; i < contatos.getLength(); i++) {
                Element contato = (Element) contatos.item(i);
                String contactEmail = contato.getElementsByTagName("email").item(0).getTextContent();
                if (contactEmail.equals(email)) {
                    int id = Integer.parseInt(contato.getElementsByTagName("id").item(0).getTextContent());
                    String name = contato.getElementsByTagName("name").item(0).getTextContent();
                    String birthDate = contato.getElementsByTagName("birthDate").item(0).getTextContent();
                    String phone = contato.getElementsByTagName("phone").item(0).getTextContent();
                    return new Contato(id, name, birthDate, phone, email);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Contato> getTodosContatos() {
        ArrayList<Contato> contatos = new ArrayList<>();
        try {
            Document doc = getDocument();
            if (doc != null) {
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("contato");
                if (nList.getLength() == 0) {
                    return contatos;
                }

                for (int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element elem = (Element) nNode;

                        int id = Integer.parseInt(elem.getElementsByTagName("id").item(0).getTextContent());
                        String name = elem.getElementsByTagName("name").item(0).getTextContent();
                        String birthDate = elem.getElementsByTagName("birthDate").item(0).getTextContent();
                        String phone = elem.getElementsByTagName("phone").item(0).getTextContent();
                        String email = elem.getElementsByTagName("email").item(0).getTextContent();

                        Contato contato = new Contato(id, name, birthDate, phone, email);
                        contatos.add(contato);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contatos;
    }

    @Override
    public boolean inserir(Contato contato) {
        try {
            Document doc = getDocument();

            Element rootElement = doc.getDocumentElement();

            Element newContato = doc.createElement("contato");
            rootElement.appendChild(newContato);

            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(String.valueOf(generateNewId())));
            newContato.appendChild(id);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(contato.getName()));
            newContato.appendChild(name);

            Element birthDate = doc.createElement("birthDate");
            birthDate.appendChild(doc.createTextNode(contato.getBirthDate()));
            newContato.appendChild(birthDate);

            Element phone = doc.createElement("phone");
            phone.appendChild(doc.createTextNode(contato.getPhone()));
            newContato.appendChild(phone);

            Element email = doc.createElement("email");
            email.appendChild(doc.createTextNode(contato.getEmail()));
            newContato.appendChild(email);

            saveDocument(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean alterar(Contato contato) {
        try {
            Document doc = getDocument();
            NodeList contatos = doc.getElementsByTagName("contato");
            for (int i = 0; i < contatos.getLength(); i++) {
                Element contatoElem = (Element) contatos.item(i);
                int id = Integer.parseInt(contatoElem.getElementsByTagName("id").item(0).getTextContent());
                if (id == contato.getId()) {
                    contatoElem.getElementsByTagName("name").item(0).setTextContent(contato.getName());
                    contatoElem.getElementsByTagName("birthDate").item(0).setTextContent(contato.getBirthDate());
                    contatoElem.getElementsByTagName("phone").item(0).setTextContent(contato.getPhone());
                    contatoElem.getElementsByTagName("email").item(0).setTextContent(contato.getEmail());
                    saveDocument(doc);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean excluir(int id_contato) {
        try {
            Document doc = getDocument();
            NodeList contatos = doc.getElementsByTagName("contato");
            Element root = doc.getDocumentElement();
            for (int i = 0; i < contatos.getLength(); i++) {
                Element contato = (Element) contatos.item(i);
                int id = Integer.parseInt(contato.getElementsByTagName("id").item(0).getTextContent());
                if (id == id_contato) {
                    root.removeChild(contato);
                    saveDocument(doc);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
