package sistema_crafteo.logica;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import sistema_crafteo.objeto.Item;

/**
 * Registra un crafteo realizado en el sistema. Guarda el objeto fabricado,
 * los ingredientes consumidos y la fecha del evento.
 */
public class HistorialCrafteo {
    private final Item item;
    private final Map<Item, Integer> ingredientes;
    private final LocalDateTime fecha;

    public HistorialCrafteo(Item item, Map<Item, Integer> ingredientes, LocalDateTime fecha) {
        this.item = item;
        this.ingredientes = ingredientes == null ? Collections.emptyMap() : ingredientes;
        this.fecha = fecha == null ? LocalDateTime.now() : fecha;
    }

    public Item getItem() {
        return item;
    }

    public Map<Item, Integer> getIngredientes() {
        return Collections.unmodifiableMap(ingredientes);
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
