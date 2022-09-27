package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dmain.dto.TagDTO;
import ru.clevertec.ecl.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/tags")
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
    public ResponseEntity<String> deleteTag(@PathVariable Long id) {
        return tagService.delete(id)
                ? ResponseEntity.ok("delete tag with id = " + id)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getTags(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(tagService.getAllTags(pageable));
    }
}
