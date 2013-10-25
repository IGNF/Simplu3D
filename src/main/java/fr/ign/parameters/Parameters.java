package fr.ign.parameters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "parameter-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Parameters {
  @XmlJavaTypeAdapter(MapAdapter.class)
  @XmlElement(name = "parameters")
  Map<String, String> map;

  public Parameters() {
    this.map = new HashMap<String, String>();
  }

  public String get(String key) {
    return this.map.get(key);
  }

  public void set(String key, String value) {
    this.map.put(key, value);
  }

  public static Parameters unmarshall(String file) {
    try {
      JAXBContext context = JAXBContext.newInstance(Parameters.class);
      Unmarshaller m = context.createUnmarshaller();
      Parameters root = (Parameters) m.unmarshal(new File(file));
      return root;
    } catch (JAXBException e1) {
      e1.printStackTrace();
    }
    return null;
  }

  public void marshall(String file) {
    try {
      JAXBContext context = JAXBContext.newInstance(Parameters.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      m.marshal(this, new File(file));
    } catch (JAXBException e1) {
      e1.printStackTrace();
    }
  }

  public static class MapAdapter extends XmlAdapter<MyHashMapType, Map<String, String>> {
    public MapAdapter() {
      super();
    }

    @Override
    public MyHashMapType marshal(Map<String, String> v) throws Exception {
      MyHashMapType result = new MyHashMapType(v);
      return result;
    }

    @Override
    public Map<String, String> unmarshal(MyHashMapType v) throws Exception {
      Map<String, String> result = new HashMap<String, String>();
      for (MyHashMapEntryType entry : v.entry) {
        result.put(entry.key, entry.value);
      }
      return result;
    }
  };

  public static class MyHashMapType {
    @XmlElement(name = "param")
    public List<MyHashMapEntryType> entry = new ArrayList<MyHashMapEntryType>();

    public MyHashMapType(Map<String, String> map) {
      for (Map.Entry<String, String> e : map.entrySet())
        entry.add(new MyHashMapEntryType(e));
    }

    public MyHashMapType() {
    }
  }

  public static class MyHashMapEntryType {
    @XmlAttribute
    public String key;

    @XmlAttribute
    public String value;

    public MyHashMapEntryType() {
    }

    public MyHashMapEntryType(Map.Entry<String, String> e) {
      key = e.getKey();
      value = e.getValue();
    }
  };
}
