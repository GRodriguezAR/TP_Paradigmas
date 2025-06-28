package sistema_crafteo.logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collections;
import java.util.Map;

import sistema_crafteo.objeto.Item;

/**

 * Registra de forma acumulativa cada crafteo que se realiza en el sistema. El
 * objetivo es facilitar la exportación del historial completo cuando el programa
 * finaliza.
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

    /** Representa un crafteo individual. */
    public static class Registro {
        private final Item item;
        private final Map<Item, Integer> ingredientes;
        private final LocalDateTime fecha;

        public Registro(Item item, Map<Item, Integer> ingredientes, LocalDateTime fecha) {
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

    private final List<Registro> registros = new ArrayList<>();

    /**
     * Agrega un nuevo registro al historial usando la fecha actual.
     */
    public void registrar(Item item, Map<Item, Integer> ingredientes) {
        registrar(item, ingredientes, LocalDateTime.now());
    }

    /**
     * Agrega un nuevo registro al historial indicando una fecha específica.
     */
    public void registrar(Item item, Map<Item, Integer> ingredientes, LocalDateTime fecha) {
        registros.add(new Registro(item, ingredientes, fecha));
    }

    /** Devuelve la lista completa de registros en orden de inserción. */
    public List<Registro> getRegistros() {
        return Collections.unmodifiableList(registros);
    public LocalDateTime getFecha() {
        return fecha;
    }
}
