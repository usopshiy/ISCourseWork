package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usopshiy.is.dto.ColonyDto;
import usopshiy.is.dto.DecorationDto;
import usopshiy.is.entity.Colony;
import usopshiy.is.exception.NotFoundException;
import usopshiy.is.repository.ColonyRepository;
import usopshiy.is.repository.DecorationRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColonyService {

    private final ColonyRepository colonyRepository;
    private final DecorationRepository decorationRepository;

    @Transactional(readOnly = true)
    public List<Colony> getAllColonies() {
        try {
            return colonyRepository.findAll();
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public Colony getColonyById(Long id) {
        Colony colony = colonyRepository.findById(id).orElse(null);
        if (colony == null) {
            throw new NotFoundException("Colony not found");
        }
        else return colony;
    }

    public void updateColony(ColonyDto colonyDto) {
        Colony colony = colonyRepository.findById(colonyDto.getId()).orElse(null);
        if (colony == null) {
            throw new NotFoundException("Colony not found");
        }
        colonyRepository.save(colony.updateByDto(colonyDto));
    }

    public void addDecoration(Long colonyId, DecorationDto dto) {
        try {
            decorationRepository.createByValues(colonyId, dto.getItemName(), dto.getAmount());
        }
        catch (JpaSystemException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
