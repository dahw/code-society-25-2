package com.codedifferently.lesson23.web;

import com.codedifferently.lesson23.library.Librarian;
import com.codedifferently.lesson23.library.Library;
import com.codedifferently.lesson23.library.MediaItem;
import com.codedifferently.lesson23.library.search.SearchCriteria;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/items")
public class MediaItemsController {

  private final Library library;
  private final Librarian librarian;

  public MediaItemsController(Library library) throws IOException {
    this.library = library;
    this.librarian = library.getLibrarians().stream().findFirst().orElseThrow();
  }

  @GetMapping
  public ResponseEntity<GetMediaItemsResponse> getItems() {
    Set<MediaItem> items = library.search(SearchCriteria.builder().build());
    List<MediaItemResponse> responseItems = items.stream().map(MediaItemResponse::from).toList();
    var response = GetMediaItemsResponse.builder().items(responseItems).build();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<MediaItemResponse> findById(@PathVariable("id") UUID id) {
    Set<MediaItem> items = library.search(SearchCriteria.builder().id(id.toString()).build());
    if (items.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    MediaItem item = items.iterator().next();
    MediaItemResponse response = MediaItemResponse.from(item);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CreateMediaItemResponse> create(
      @Valid @RequestBody CreateMediaItemRequest request) {
    MediaItem item = MediaItemRequest.asMediaItem(request.getItem());
    library.addMediaItem(item, librarian);
    MediaItemResponse response = MediaItemResponse.from(item);
    var createResponse = CreateMediaItemResponse.builder().item(response).build();
    return ResponseEntity.ok(createResponse);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    Set<MediaItem> items = library.search(SearchCriteria.builder().id(id.toString()).build());
    if (items.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    MediaItem item = items.iterator().next();
    try {
      library.removeMediaItem(item, librarian);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
