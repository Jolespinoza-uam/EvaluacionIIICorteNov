package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_equipo", schema = "lab1")
public class TipoEquipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(length = 150)
    private String descripcion;

    protected TipoEquipo() {}

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() { return nombre; }
}
