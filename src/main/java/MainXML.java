import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainXML {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        //Имя считываемого файла:
        String fileXML = "src/main/resources/data.xml";

        // Парсим XML:
        List<Employee> list = parseXML(fileXML);
        String json = listToJson(list);
        String jsonFile = "data2.json";
        writeString(json, jsonFile);

    }

    // Список объектов класса Employee в json:
    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        Type type = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, type);
    }

    public static List<Employee> parseXML(String file) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> employees = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(file));
        //Вытаскиваем в список всё что внутри <employee>
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName("employee");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element employee = (Element) node;
                // Вытаскиваем значения для конструктора:
                int id = Integer.parseInt(employee.getElementsByTagName("id").item(0).getTextContent());
                String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                String country = employee.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());

                employees.add(new Employee(id, firstName, lastName, country, age));
            }
        }
        return employees;
    }

    //Пишем в файл
    public static void writeString(String jsonText, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(jsonText);
            writer.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

}


