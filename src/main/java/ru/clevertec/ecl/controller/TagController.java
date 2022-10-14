package ru.clevertec.ecl.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  @PostMapping
  public ResponseEntity<TagDTO> saveTage(@RequestBody TagDTO tagDTO) {
    return ResponseEntity.ok(tagService.save(tagDTO));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
    return ResponseEntity.ok(tagService.update(id, tagDTO));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTag(@PathVariable Long id) {
    tagService.delete(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
    return ResponseEntity.ok(tagService.getById(id));
  }

  @GetMapping("/popularTag")
  public ResponseEntity<TagDTO> getPopularTagUser() {
    return ResponseEntity.ok(tagService.getPopularTagUser());
  }

  @GetMapping
  public ResponseEntity<List<TagDTO>> getTags(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(tagService.getAllTags(pageable));
  }
}
