package org.retrotime.service;

import java.util.List;
import java.util.Optional;

import org.retrotime.dto.RetroDTO;
import org.retrotime.model.Retro;

/**
 * Created by vzhemevko on 5/23/2015.
 */
public interface RetroService {

    Retro getRetroById(int retroId);

    void saveRetro(Retro retro);

    Optional<Retro> createRetroOrUpdateName(String name, String teamId, String retroId);

    void deleteByIdAndTeamId(String retroId, String teamId);

    RetroDTO getRetroDTOById(int retroId);

    List<RetroDTO> getRetroListByTeamId(String teamId);
}
