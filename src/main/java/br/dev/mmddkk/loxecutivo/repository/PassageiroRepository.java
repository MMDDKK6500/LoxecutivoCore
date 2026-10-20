package br.dev.mmddkk.loxecutivo.repository;

import br.dev.mmddkk.loxecutivo.model.Passageiro;
import br.dev.mmddkk.loxecutivo.model.Viagens;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PassageiroRepository extends CrudRepository<Passageiro, Integer> {

    List<Passageiro> findByIdViagem(Viagens viagem);

    @Query("SELECT p FROM Passageiro p WHERE p.idViagem.id = :viagemId")
    List<Passageiro> findAllByViagemId(@Param("viagemId") Integer viagemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Passageiro p WHERE p.idViagem.id = :viagemId")
    void deleteAllByIdViagem(@Param("viagemId") Integer viagemId);

}