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
        HistorialCrafteo h = new HistorialCrafteo();
        h.registrar(madera, Map.of(madera,1), ahora);
        assertEquals(1, h.getRegistros().size());
        HistorialCrafteo.Registro reg = h.getRegistros().get(0);
        assertEquals(madera, reg.getItem());
        assertTrue(reg.getIngredientes().containsKey(madera));
        assertEquals(ahora, reg.getFecha());
    }
}
