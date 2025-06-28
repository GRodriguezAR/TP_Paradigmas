package sistema_crafteo.integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema_crafteo.inventario.Inventario;
import sistema_crafteo.logica.HistorialCrafteo;
import sistema_crafteo.objeto.Item;

class GestorArchivoTest {

    Path invPath = Path.of("sistema_crafteo/test/resources/inventario.json");
    Path recPath = Path.of("sistema_crafteo/test/resources/recetas.json");
    GestorArchivo gestor;

    @BeforeEach
    void setup() {
        gestor = new GestorArchivo(invPath, recPath);
    }

    @Test
    void cargarItems_yInventario_funcionaCorrectamente() throws IOException {
        Map<String, Item> items = gestor.cargarItems();
        assertTrue(items.containsKey("espada"));
        assertTrue(items.get("espada").esCrafteable());

        Inventario inv = gestor.cargarInventario(items);
        assertEquals(4, inv.getCantidad(items.get("madera")));
    }

    @Test
    void guardarInventario_yHistorial_generaArchivos() throws IOException {
        Map<String, Item> items = gestor.cargarItems();
        Inventario inv = gestor.cargarInventario(items);
        Path invOut = Files.createTempFile("inv", ".json");
        Path histOut = Files.createTempFile("hist", ".json");
        gestor.guardarInventario(invOut, inv);
        HistorialCrafteo hist = new HistorialCrafteo();
        hist.registrar(items.get("palo"), Map.of());
        gestor.guardarHistorial(histOut, hist);
        gestor.guardarHistorial(histOut, List.of(new HistorialCrafteo(items.get("palo"), Map.of(), null)));
        assertTrue(Files.size(invOut) > 0);
        assertTrue(Files.size(histOut) > 0);
        Files.delete(invOut);
        Files.delete(histOut);
    }
}
