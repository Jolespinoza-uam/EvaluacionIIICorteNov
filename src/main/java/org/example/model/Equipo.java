package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "equipo", schema = "lab1")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false)
    private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo")
    private TipoEquipo tipo;

    public Equipo() {}

    public void actualizar(String codigo, String marca, String estado, int cantidad, TipoEquipo tipo) {
        this.codigo = codigo;
        this.marca = marca;
        this.estado = estado;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public Integer getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getMarca() { return marca; }
    public String getEstado() { return estado; }
    public int getCantidad() { return cantidad; }
    public TipoEquipo getTipo() { return tipo; }
}
