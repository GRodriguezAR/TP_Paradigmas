package sistema_crafteo.logica;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;

import sistema_crafteo.objeto.IngredienteBasico;
import sistema_crafteo.objeto.Item;

class HistorialCrafteoTest {
    @Test
    void crearRegistro_guardaDatosCorrectamente() {
        Item madera = new IngredienteBasico("madera"," ");
        LocalDateTime ahora = LocalDateTime.now();
        HistorialCrafteo h = new HistorialCrafteo(madera, Map.of(madera,1), ahora);
        assertEquals(madera, h.getItem());
        assertTrue(h.getIngredientes().containsKey(madera));
        assertEquals(ahora, h.getFecha());
    }
}
