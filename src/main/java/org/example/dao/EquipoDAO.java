package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.model.Equipo;
import org.example.model.TipoEquipo;

import java.util.List;
import java.util.function.Consumer;

public class EquipoDAO {
    private final EntityManagerFactory factory;

    public EquipoDAO(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public List<TipoEquipo> listarTipos() {
        try (EntityManager em = factory.createEntityManager()) {
            return em.createQuery("from TipoEquipo t order by t.nombre", TipoEquipo.class)
                .getResultList();
        }
    }

    public List<Equipo> listar(TipoEquipo tipo, String estado) {
        String jpql = "select e from Equipo e left join fetch e.tipo where 1 = 1";
        if (tipo != null) {
            jpql += " and e.tipo.id = :idTipo";
        }
        if (estado != null) {
            jpql += " and e.estado = :estado";
        }
        jpql += " order by e.codigo";

        try (EntityManager em = factory.createEntityManager()) {
            TypedQuery<Equipo> consulta = em.createQuery(jpql, Equipo.class);
            if (tipo != null) {
                consulta.setParameter("idTipo", tipo.getId());
            }
            if (estado != null) {
                consulta.setParameter("estado", estado);
            }
            return consulta.getResultList();
        }
    }

    public void guardar(Integer id, String codigo, String marca, String estado, int cantidad, int idTipo) {
        escribir(em -> {
            Equipo equipo = id == null ? new Equipo() : buscar(em, id);
            TipoEquipo tipo = em.find(TipoEquipo.class, idTipo);
            if (tipo == null) {
                throw new IllegalArgumentException("El tipo seleccionado ya no existe.");
            }
            equipo.actualizar(codigo, marca, estado, cantidad, tipo);
            if (id == null) {
                em.persist(equipo);
            }
        });
    }

    public void eliminar(int id) {
        escribir(em -> em.remove(buscar(em, id)));
    }

    private Equipo buscar(EntityManager em, int id) {
        Equipo equipo = em.find(Equipo.class, id);
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo seleccionado ya no existe.");
        }
        return equipo;
    }

    private void escribir(Consumer<EntityManager> operacion) {
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                operacion.accept(em);
                tx.commit();
            } catch (RuntimeException error) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw error;
            }
        }
    }
}
