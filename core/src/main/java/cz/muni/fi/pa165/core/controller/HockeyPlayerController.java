package cz.muni.fi.pa165.core.controller;

import cz.muni.fi.pa165.model.dto.HockeyPlayerDto;
import cz.muni.fi.pa165.core.service.HockeyPlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hockey-players")
public class HockeyPlayerController {

    private final HockeyPlayerService hockeyPlayerService;

    @Autowired
    public HockeyPlayerController(HockeyPlayerService hockeyPlayerService) {
        this.hockeyPlayerService = hockeyPlayerService;
    }

    @GetMapping
    public ResponseEntity<List<HockeyPlayerDto>> getAll() {
        return ResponseEntity.ok(hockeyPlayerService.findAll());
    }

    @GetMapping("/get-all-without-team")
    public ResponseEntity<List<HockeyPlayerDto>> getAllWithoutTeam() {
        return ResponseEntity.ok(hockeyPlayerService.getAllWithoutTeam());
    }

    @PostMapping
    public ResponseEntity<HockeyPlayerDto> create(@Valid @RequestBody HockeyPlayerDto hockeyPlayerDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(hockeyPlayerService.create(hockeyPlayerDto));
    }

    @PutMapping
    public ResponseEntity<HockeyPlayerDto> update(@Valid @RequestBody HockeyPlayerDto hockeyPlayerDto) {
        try {
            return ResponseEntity.ok(hockeyPlayerService.update(hockeyPlayerDto));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HockeyPlayerDto> findById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(hockeyPlayerService.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable long id) {
        try {
            hockeyPlayerService.deleteById(id);
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
