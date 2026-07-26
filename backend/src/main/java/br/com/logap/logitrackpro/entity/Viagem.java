package br.com.logap.logitrackpro.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "viagens")
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @Column(name = "data_saida", nullable = false)
    private LocalDateTime dataSaida;

    @Column(name = "data_chegada")
    private LocalDateTime dataChegada;

    private String origem;

    private String destino;

    @Column(name = "km_percorrida")
    private BigDecimal kmPercorrida;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Viagem() {
    }

    public Viagem(Veiculo veiculo, LocalDateTime dataSaida, LocalDateTime dataChegada,
                  String origem, String destino, BigDecimal kmPercorrida) {
        this.veiculo = veiculo;
        this.dataSaida = dataSaida;
        this.dataChegada = dataChegada;
        this.origem = origem;
        this.destino = destino;
        this.kmPercorrida = kmPercorrida;
    }

    public void atualizar(Veiculo veiculo, LocalDateTime dataSaida, LocalDateTime dataChegada,
                           String origem, String destino, BigDecimal kmPercorrida) {
        this.veiculo = veiculo;
        this.dataSaida = dataSaida;
        this.dataChegada = dataChegada;
        this.origem = origem;
        this.destino = destino;
        this.kmPercorrida = kmPercorrida;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public LocalDateTime getDataChegada() {
        return dataChegada;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public BigDecimal getKmPercorrida() {
        return kmPercorrida;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
