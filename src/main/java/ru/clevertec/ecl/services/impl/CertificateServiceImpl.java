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
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final CertificateMapper certificateMapper;
    private final TagMapper tagMapper;

    @Transactional
    @Override
    public CertificateDTO save(CertificateDTO certificateDTO) {

        Certificate certificate = certificateMapper.toGiftCertificate(certificateDTO);
        if (!certificateRepository.existsByName(certificateDTO.getName())) {
            certificate.setCreateDate(LocalDateTime.now());
            certificate.setLastUpdateDate(certificate.getCreateDate());
            certificate.setTags(tagMapper.toTagSet(tagService.saveAll(certificateDTO.getTags())));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return certificateMapper.toGiftCertificateDTO(certificateRepository.save(certificate));
    }

    @Transactional
    @Override
    public CertificateDTO update(Long id, CertificateDTO certificateDTO) {
        return certificateMapper.toGiftCertificateDTO(certificateRepository
                .findById(id)
                .map(certificate -> certificateRepository.save(certificationToUpdate(certificateDTO, certificate)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id=" + id)));
    }

    @Override
    public CertificateDTO findById(Long id) {
        return certificateRepository.findById(id)
                .map(certificateMapper::toGiftCertificateDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<CertificateDTO> findAllB(Pageable pageable) {
        return null;
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

    private Sort getSort(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            for (String s : sort) {
                String[] _sort = s.split(",");
                orders.add(new Sort.Order(getSort(_sort[1]), sort[0]));

            }
        } else {
            orders.add(new Sort.Order(getSort(sort[1]), sort[0]));
        }

        return Sort.by(orders);
    }

    private Sort.Direction getSort(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else {
            if (direction.equals("desc")) {
                return Sort.Direction.DESC;
            }
        }
        return Sort.Direction.ASC;
    }
//    @Override
//    public List<CertificateDTO> findByTagOrDescription(String tagName, String description) {
//        return certificateMapper.toCertificateDTOList(certificateRepository
//                .findAll(certificateToExample(tagName, description)));
//
//    }

//    private Example<Certificate> certificateToExample(String tagName, String description){
//        Set<Tag> tagSet = new HashSet<>();
//        tagSet.add(Tag.builder().name(tagName).build());
//        ExampleMatcher employeeMatcher = ExampleMatcher.matchingAny()
//                .withIgnoreNullValues()
//                .withMatcher("tags.name", ExampleMatcher.GenericPropertyMatchers.exact());
//                ;
//        return Example.of(Certificate.builder().tags(tagSet).description(description).build(), employeeMatcher);

//    }

//    @Override
//    public List<CertificateDTO> findAllB(Pageable pageable) {
//        return null;
//    }

    @Override
    public boolean delete(Long id) {
        certificateRepository.findById(id)
                .map(certificate ->
                {
                    certificateRepository.delete(certificate);
                    return true;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return false;
    }

    @Override
    public CertificateDTO findByName(String name) {
        return null;
    }

    private Certificate certificationToUpdate(CertificateDTO certificateDTO, Certificate certificate) {
        certificate.setName(certificateDTO.getName());
        certificate.setDuration(certificateDTO.getDuration());
        certificate.setPrice(certificateDTO.getPrice());
        certificate.setDescription(certificateDTO.getDescription());
        certificate.setTags(tagMapper.toTagSet(tagService.saveAll(certificateDTO.getTags())));
        return certificate;
    }

}
