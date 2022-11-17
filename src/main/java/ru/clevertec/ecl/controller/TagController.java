package ru.clevertec.ecl.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.services.TagService;

@Slf4j
@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  @PostMapping
  public ResponseEntity<TagDTO> saveTage(@Valid @RequestBody TagDTO tagDTO) {
    log.info("REQUEST: method = POST, path = /v1/tags, body = {}", tagDTO);
    TagDTO tag = tagService.save(tagDTO);
    log.info("RESPONSE: responseBody = {}", tag);
    return ResponseEntity.ok(tag);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @Valid @RequestBody TagDTO tagDTO) {
    log.info("REQUEST: method = PUT, path = /v1/tags/{}, body = {}", id, tagDTO);
    TagDTO tag = tagService.update(id, tagDTO);
    log.info("RESPONSE: responseBody = {}", tag);
    return ResponseEntity.ok(tag);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTag(@PathVariable Long id) {
    log.info("REQUEST: method = DELETE, path = /v1/tags/{}", id);
    tagService.delete(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
    log.info("REQUEST: method = GET, path = /v1/tags/{}", id);
    TagDTO tagById = tagService.getById(id);
    log.info("RESPONSE: responseBody = {}", tagById);
    return ResponseEntity.ok(tagById);
  }

  @GetMapping("/popularTag")
  public ResponseEntity<TagDTO> getPopularTagUser() {
    log.info("REQUEST: method = GET, path = /v1/tags/popularTag");
    TagDTO popularTagUser = tagService.getPopularTagUser();
    log.info("RESPONSE: responseBody = {}", popularTagUser);
    return ResponseEntity.ok(popularTagUser);
  }

  @GetMapping
  public ResponseEntity<List<TagDTO>> getTags(@PageableDefault Pageable pageable) {
    log.info("REQUEST: method = GET, path = /v1/tags");
    List<TagDTO> allTags = tagService.getAllTags(pageable);
    log.info("RESPONSE: responseBody = {}", allTags);
    return ResponseEntity.ok(allTags);
  }

}
