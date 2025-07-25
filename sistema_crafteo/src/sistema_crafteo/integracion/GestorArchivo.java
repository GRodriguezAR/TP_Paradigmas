package sistema_crafteo.integracion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sistema_crafteo.inventario.Inventario;
import sistema_crafteo.logica.HistorialCrafteo;
import sistema_crafteo.objeto.IngredienteBasico;
import sistema_crafteo.objeto.Item;
import sistema_crafteo.objeto.ObjetoCrafteable;
import sistema_crafteo.objeto.Receta;

/**
 * Gestiona la lectura y escritura de los archivos JSON necesarios para el
 * sistema de crafteo. Es una implementación simple basada en Jackson pensada
 * para facilitar las pruebas.
 */
public class GestorArchivo {
    private final Path pathInventario;
    private final Path pathRecetas;
    private final ObjectMapper mapper = new ObjectMapper();

    public GestorArchivo(Path pathInventario, Path pathRecetas) {
        this.pathInventario = pathInventario;
        this.pathRecetas = pathRecetas;
    }

    /**
     * Carga todos los items definidos en el archivo de recetas. La estructura
     * del JSON es un diccionario donde cada clave es el nombre del item y su
     * valor describe el objeto.
     */
    public Map<String, Item> cargarItems() throws IOException {
        Map<String, ItemData> data = mapper.readValue(pathRecetas.toFile(), new TypeReference<>() {});
        Map<String, Item> items = new HashMap<>();
        for (String nombre : data.keySet()) {
            construirItem(nombre, data, items);
        }
        return items;
    }

    /**
     * Construye recursivamente un item a partir de la información deserializada.
     */
    private Item construirItem(String nombre, Map<String, ItemData> data, Map<String, Item> creados) {
        if (creados.containsKey(nombre)) {
            return creados.get(nombre);
        }
        ItemData d = data.get(nombre);
        if (d == null) {
            throw new IllegalArgumentException("Item no definido: " + nombre);
        }
        Item item;
        if ("basico".equalsIgnoreCase(d.tipo)) {
            item = new IngredienteBasico(nombre, d.descripcion);
        } else {
            List<Receta> recetas = new ArrayList<>();
            if (d.recetas != null) {
                for (RecetaData rd : d.recetas) {
                    Map<Item, Integer> ingredientes = new HashMap<>();
                    if (rd.ingredientes != null) {
                        for (Map.Entry<String, Integer> e : rd.ingredientes.entrySet()) {
                            Item ing = construirItem(e.getKey(), data, creados);
                            ingredientes.put(ing, e.getValue());
                        }
                    }
                    Receta rec = new Receta(ingredientes, null, rd.cantidadGenerada);
                    recetas.add(rec);
                }
            }
            item = new ObjetoCrafteable(nombre, d.descripcion, d.tiempo, recetas.toArray(new Receta[0]));
        }
        creados.put(nombre, item);
        return item;
    }

    /**
     * Carga el inventario inicial desde el archivo indicado.
     */
    public Inventario cargarInventario(Map<String, Item> itemsRegistrados) throws IOException {
        InventarioData data = mapper.readValue(pathInventario.toFile(), InventarioData.class);
        Inventario inventario = new Inventario();
        if (data.items != null) {
            Map<Item, Integer> mapa = new HashMap<>();
            for (Map.Entry<String, Integer> e : data.items.entrySet()) {
                Item it = itemsRegistrados.get(e.getKey());
                if (it == null) {
                    throw new IllegalArgumentException("Item desconocido en inventario: " + e.getKey());
                }
                mapa.put(it, e.getValue());
            }
            inventario.setItems(mapa);
        }
        return inventario;
    }

    /** Escribe el historial de crafteo en formato JSON. */

    public void guardarHistorial(Path destino, HistorialCrafteo historial) throws IOException {
        mapper.writeValue(destino.toFile(), historial.getRegistros());
    }
    
    public void guardarHistorial(Path destino, List<HistorialCrafteo> historial) throws IOException {
        mapper.writeValue(destino.toFile(), historial);

    }

    /** Escribe el inventario actual en formato JSON. */
    public void guardarInventario(Path destino, Inventario inventario) throws IOException {
        mapper.writeValue(destino.toFile(), inventario);
    }

    /* Classes auxiliares para mapear el JSON */
    private static class ItemData {
        public String tipo;
        public String descripcion;
        public int tiempo;
        public List<RecetaData> recetas;
    }

    private static class RecetaData {
        public Map<String, Integer> ingredientes;
        public int cantidadGenerada = 1;
    }

    private static class InventarioData {
        public Map<String, Integer> items;
    }
}
