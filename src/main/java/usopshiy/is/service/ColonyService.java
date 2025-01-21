package usopshiy.is.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usopshiy.is.dto.ColonyDto;
import usopshiy.is.dto.DecorationDto;
import usopshiy.is.entity.Colony;
import usopshiy.is.repository.ColonyRepository;
import usopshiy.is.repository.DecorationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColonyService {

    private final ColonyRepository colonyRepository;
    private final DecorationRepository decorationRepository;
    private final UserService userService;

    public List<Colony> getAllColonies() {
        return colonyRepository.findAll();
    }

    public Colony getColonyById(Long id) {
        Colony colony = colonyRepository.findById(id).orElse(null);
        if (colony == null) {
            throw new RuntimeException("Colony not found");
        }
        else return colony;
    }

    public void updateColony(ColonyDto colonyDto) {
        Colony colony = colonyRepository.findById(colonyDto.getId()).orElse(null);
        if (colony == null) {
            throw new RuntimeException("Colony not found");
        }
        colonyRepository.save(colony.updateByDto(colonyDto));
    }

    public void addDecoration(Long colonyId, DecorationDto dto) {
        decorationRepository.createByValues(colonyId, dto.getItemName(), dto.getAmount());
    }
}
