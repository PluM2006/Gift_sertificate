package ru.clevertec.ecl.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.clevertec.ecl.dmain.dto.CertificateDTO;
import ru.clevertec.ecl.dmain.entity.Certificate;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.CertificateRepository;
import ru.clevertec.ecl.services.CertificateService;
import ru.clevertec.ecl.services.TagService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificateServiceImpl implements CertificateService {

    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String SPLITERATOR = ",";
    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final CertificateMapper certificateMapper;
    private final TagMapper tagMapper;

    @Transactional
    @Override
    public CertificateDTO save(CertificateDTO certificateDTO) {
        Certificate certificate = certificateMapper.toCertificate(certificateDTO);
        LocalDateTime now = LocalDateTime.now();
        if (!certificateRepository.existsByName(certificateDTO.getName())) {
            certificate.setCreateDate(now);
            certificate.setLastUpdateDate(now);
            certificate.setTags(tagMapper.toTagSet(tagService.saveAll(certificateDTO.getTags())));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);// TODO: 23.09.22  
        }
        return certificateMapper.toCertificateDTO(certificateRepository.save(certificate));
    }

    @Transactional
    @Override
    public CertificateDTO update(Long id, CertificateDTO certificateDTO) {
        return certificateMapper.toCertificateDTO(certificateRepository
                .findById(id)
                .map(certificate -> certificateRepository.save(certificationToUpdate(certificateDTO, certificate)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id=" + id)));
    }

    @Override
    public CertificateDTO findById(Long id) {
        return certificateRepository.findById(id)
                .map(certificateMapper::toCertificateDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<CertificateDTO> findAll(Pageable pageable) {
        return certificateMapper.toCertificateDTOList(certificateRepository
                .findAll(pageable).getContent());
    }

    @Override
    public List<CertificateDTO> findByTagOrDescription(Pageable pageable, String tagName, String description, String[] sort) {
        return certificateMapper
                .toCertificateDTOList(certificateRepository.findByTagNameDescription(
                        tagName,
                        description,
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), getSort(sort))));
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        return certificateRepository.findById(id)
                .map(certificate ->
                {
                    certificateRepository.delete(certificate);
                    return true;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public CertificateDTO findByName(String name) {
        return certificateRepository.findByName(name)
                .map(certificateMapper::toCertificateDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Certificate certificationToUpdate(CertificateDTO certificateDTO, Certificate certificate) {
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setName(certificateDTO.getName());
        certificate.setDuration(certificateDTO.getDuration());
        certificate.setPrice(certificateDTO.getPrice());
        certificate.setDescription(certificateDTO.getDescription());
        certificate.setTags(tagMapper.toTagSet(tagService.saveAll(certificateDTO.getTags())));
        return certificate;
    }

    private Sort getSort(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        try {
            if (sort[0].contains(SPLITERATOR)) {
                for (String s : sort) {
                    String[] _sort = s.split(SPLITERATOR);
                    orders.add(new Sort.Order(getSort(_sort[1]), sort[0]));
                }
            } else {
                orders.add(new Sort.Order(getSort(sort[1]), sort[0]));
            }
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sort parameter is bad");
        }
        return Sort.by(orders);
    }

    private Sort.Direction getSort(String direction) {
        if (direction.equalsIgnoreCase(ASC)) {
            return Sort.Direction.ASC;
        } else {
            if (direction.equalsIgnoreCase(DESC)) {
                return Sort.Direction.DESC;
            }
        }
        return Sort.Direction.ASC;
    }

}
