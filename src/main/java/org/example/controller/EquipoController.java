package org.example.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.dao.EquipoDAO;
import org.example.model.Equipo;
import org.example.model.TipoEquipo;

import java.sql.SQLException;
import java.util.List;

public class EquipoController {
    private static final List<String> ESTADOS = List.of(
        "Disponible", "En mantenimiento", "Fuera de servicio"
    );

    private final EquipoDAO dao;

    @FXML private TableView<Equipo> tabla;
    @FXML private TableColumn<Equipo, String> colCodigo;
    @FXML private TableColumn<Equipo, String> colMarca;
    @FXML private TableColumn<Equipo, String> colEstado;
    @FXML private TableColumn<Equipo, Integer> colCantidad;
    @FXML private TableColumn<Equipo, TipoEquipo> colTipo;
    @FXML private TextField codigo;
    @FXML private TextField marca;
    @FXML private TextField cantidad;
    @FXML private ComboBox<String> estado;
    @FXML private ComboBox<TipoEquipo> tipo;
    @FXML private ComboBox<String> filtroEstado;
    @FXML private ComboBox<TipoEquipo> filtroTipo;
    @FXML private Label mensaje;
    @FXML private Button botonEliminar;

    public EquipoController(EquipoDAO dao) {
        this.dao = dao;
    }

    @FXML
    private void initialize() {
        colCodigo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getCodigo()));
        colMarca.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMarca()));
        colEstado.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getEstado()));
        colCantidad.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getCantidad()));
        colTipo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTipo()));
        botonEliminar.disableProperty().bind(tabla.getSelectionModel().selectedItemProperty().isNull());
        estado.getItems().setAll(ESTADOS);
        filtroEstado.getItems().setAll(ESTADOS);
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, equipo) -> {
            if (equipo != null) {
                codigo.setText(equipo.getCodigo());
                marca.setText(equipo.getMarca());
                cantidad.setText(Integer.toString(equipo.getCantidad()));
                estado.setValue(equipo.getEstado());
                TipoEquipo tipoEquipo = equipo.getTipo();
                Integer idTipo = tipoEquipo == null ? null : tipoEquipo.getId();
                tipo.setValue(buscarTipo(tipo.getItems(), idTipo));
            }
        });
        recargar();
    }

    @FXML
    private void recargar() {
        try {
            Integer idFiltro = filtroTipo.getValue() == null ? null : filtroTipo.getValue().getId();
            List<TipoEquipo> tipos = dao.listarTipos();
            TipoEquipo tipoActual = buscarTipo(tipos, idFiltro);
            List<Equipo> equipos = dao.listar(tipoActual, filtroEstado.getValue());
            tipo.getItems().setAll(tipos);
            filtroTipo.getItems().setAll(tipos);
            filtroTipo.setValue(tipoActual);
            nuevo();
            tabla.getItems().setAll(equipos);
            mensaje.setText(equipos.size() + " equipo(s)");
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    @FXML
    private void limpiarFiltros() {
        filtroTipo.setValue(null);
        filtroEstado.setValue(null);
        recargar();
    }

    @FXML
    private void nuevo() {
        tabla.getSelectionModel().clearSelection();
        codigo.clear();
        marca.clear();
        cantidad.clear();
        estado.setValue(null);
        tipo.setValue(null);
    }

    @FXML
    private void guardar() {
        Equipo seleccionado = tabla.getSelectionModel().getSelectedItem();
        String codigoNuevo = codigo.getText().strip();
        String marcaNueva = marca.getText().strip();
        if (codigoNuevo.isEmpty() || codigoNuevo.length() > 50
            || marcaNueva.isEmpty() || marcaNueva.length() > 50
            || estado.getValue() == null || tipo.getValue() == null) {
            mensaje.setText("Completa código y marca (hasta 50 caracteres), estado y tipo.");
            return;
        }

        try {
            int unidades = Integer.parseInt(cantidad.getText().strip());
            if (unidades < 0) {
                throw new NumberFormatException();
            }
            dao.guardar(seleccionado == null ? null : seleccionado.getId(), codigoNuevo,
                marcaNueva, estado.getValue(), unidades, tipo.getValue().getId());
            nuevo();
            recargar();
        } catch (NumberFormatException error) {
            mensaje.setText("La cantidad debe ser un entero entre 0 y 2147483647.");
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    @FXML
    private void eliminar() {
        Equipo equipo = tabla.getSelectionModel().getSelectedItem();
        if (equipo == null) {
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar el equipo " + equipo.getCodigo() + "?", ButtonType.YES, ButtonType.NO);
        if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                dao.eliminar(equipo.getId());
                nuevo();
                recargar();
            } catch (RuntimeException error) {
                mostrarError(error);
            }
        }
    }

    private TipoEquipo buscarTipo(List<TipoEquipo> tipos, Integer idTipo) {
        for (TipoEquipo candidato : tipos) {
            if (candidato.getId().equals(idTipo)) {
                return candidato;
            }
        }
        return null;
    }

    private void mostrarError(RuntimeException error) {
        if (error instanceof IllegalArgumentException || error instanceof IllegalStateException) {
            mensaje.setText(error.getMessage());
            return;
        }
        mensaje.setText("No se pudo completar la operación. Revisa la conexión y el esquema lab1.");
        error.printStackTrace();
    }
}
