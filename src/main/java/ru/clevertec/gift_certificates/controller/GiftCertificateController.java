package ru.clevertec.gift_certificates.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.gift_certificates.repository.GiftCertificateRepository;

@RestController
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateRepository giftCertificateRepository;

}
